package de.flocki;

import de.hsmainz.iiwa.AsyncService.executor.context.EventLoopContext;

import de.hsmainz.iiwa.AsyncService.executor.context.EventLoopContextSingleThread;
import de.hsmainz.iiwa.AsyncService.executor.context.ExecutorWorkGuard;
import netP5.NetAddress;
import oscP5.*;

import java.util.ArrayList;
import java.util.Comparator;

public class Networking implements OscEventListener {

    EventLoopContextSingleThread ctx;
    ExecutorWorkGuard guard;
    Thread th;

    OscP5 oscp5;
    Flock f;

    Networking(Flock f) {

        ctx = new EventLoopContextSingleThread();
        guard = new ExecutorWorkGuard(ctx);

        th = new Thread(() -> {
            System.out.println("Network thread running");
            ctx.run();
        });

        th.start();

        oscp5 = new OscP5(this, 12000);
        this.f = f;
    }

    @Override
    public void oscEvent(OscMessage oscMessage) {
        if(oscMessage.checkAddrPattern("/maxspeed"))
            f.props.maxspeed = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/maxforce"))
            f.props.maxforce = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/separation"))
            f.props.separation = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/alignment"))
            f.props.alignment = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/cohesion"))
            f.props.cohesion = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/randomisation"))
            f.props.randomisation = oscMessage.get(0).floatValue();
        else if(oscMessage.checkAddrPattern("/wind"))
            f.props.wind.setXYZ(oscMessage.get(0).floatValue(), oscMessage.get(1).floatValue(), oscMessage.get(2).floatValue());
    }

    @Override
    public void oscStatus(OscStatus oscStatus) {
        System.out.println("OSC Status: " + oscStatus.id());
    }

    public void sendClusterMessage(ArrayList<Cluster> clusters) {

        ctx.post(() -> {

            clusters.sort(Comparator.comparingInt(Cluster::getID));

            NetAddress tgt = new NetAddress("127.0.0.1", 9001);

            OscBundle b = new OscBundle();

            OscMessage alive = new OscMessage("/alive");

            clusters.forEach(c -> {
                alive.add(c.id);
            });

            oscp5.send(alive, tgt);

            clusters.forEach(cl -> {

                OscMessage msg = new OscMessage("/clPos");

                msg.add(cl.id);

                msg.add(cl.direction.x);
                msg.add(cl.direction.y);
                msg.add(cl.direction.z);

                b.add(msg);

            });

            oscp5.send(b, tgt);

        });
    }
}
