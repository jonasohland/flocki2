package de.flocki;

import java.util.HashMap;
import java.util.Hashtable;

public class OutputSlot {

    private HashMap<Integer, OutputSlotLock> _locks = new HashMap<>();

    OutputSlot(int id, int clid) {
        _id = id;
        _clid = clid;
    }

    int _id = 0;
    int _clid = 0;

    ClusterMessage msg = null;

    void use(ClusterMessage msg, OutputSlotLock lock) {

        this.msg = msg;

        _clid = msg.clusterID();

        if(!isLockedBy(lock)) {
            _locks.put(lock.lock_id, lock);
        }
    }


    void releaseLock(OutputSlotLock lock) {
        if(isLockedBy(lock)) {
            _locks.remove(lock.lock_id);
        }
    }

    boolean isLockedBy(OutputSlotLock lock) {
        return _locks.containsKey(lock.lock_id);
    }

    boolean holdsActiveLocks() {
        // System.out.println(_locks.size());
        return _locks.size() > 0;
    }

    boolean used() {
        return msg != null;
    }

    int slotID() {
        return _id;
    }

    void reset() {
        msg = null;
    }
}
