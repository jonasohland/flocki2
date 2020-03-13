package de.flocki;

import oscP5.OscBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class OutputFilter {

    Networking _net;

    OutputFilter(Networking net) {
        _net = net;

        for(int i = 0; i < slot_count; ++i)
            slots.put(100000 + i, new OutputSlot(i, 100000 + i));

    }

    int slot_count = 32;

    ConcurrentHashMap<Integer, OutputSlot> slots  = new ConcurrentHashMap<>();
    LinkedTransferQueue<ClusterMessage> requests = new LinkedTransferQueue<>();

    void dispatch(ClusterMessage msg, OutputSlotLock lock) {

        // System.out.println("dispatch " + msg.clusterID());

        if(slots.containsKey(msg.clusterID())) {
            slots.get(msg.clusterID()).use(msg, lock);
            // System.out.println("dispatch direct");
        } else {
            // System.out.println("Add msg to req q");
            requests.add(msg);
        }
    }

    void reset(OutputSlotLock lock) {

        // System.out.println("reset filter");

        Iterator<OutputSlot> empty_slot_it = slots.values().stream().filter((sl) -> !sl.holdsActiveLocks()).iterator();


        while (empty_slot_it.hasNext()) {

            // System.out.println("assign slot");

            OutputSlot sl = empty_slot_it.next();

            ClusterMessage msg = requests.poll();

            if (msg != null) {

                // System.out.println("use slot");
                sl.use(msg, lock);

                slots.remove(sl._clid);
                slots.put(msg.clusterID(), sl);

            } else
                break;
        }

        requests.clear();

        OscBundle bndl = new OscBundle();

        slots.forEach((i, sl) -> {

            if (sl.used()) {
                sl.msg.setSlotId(sl._id);
                bndl.add(sl.msg.msg());
            } else {
                // System.out.println("release lock");
                sl.releaseLock(lock);
            }

            sl.reset();

        });

        if (bndl.size() > 0)
            outputBundle(bndl);
/*
        System.out.println("------");
        slots.forEach((i, sl) -> {
            System.out.println(sl.holdsActiveLocks());
        });
*/
    }

    private void outputBundle(OscBundle bundle) {
        // System.out.println("send msg");
        _net.send(bundle);
    }

    private void outputMessage(ClusterMessage msg, int slotID) {

        msg.setSlotId(slotID);

        if(msg.isBundle())
            _net.send(msg.bundle());
        else
            _net.send(msg.msg());

    }

}


