package de.flocki;

import oscP5.OscBundle;
import oscP5.OscMessage;

public class ClusterDataMessage extends ClusterMessage {

    OscMessage _msg = new OscMessage("/cdat");
    Cluster _cl;

    ClusterDataMessage(Cluster cl) {
        _cl = cl;
    }

    @Override
    int clusterID() {
        return _cl.id;
    }

    @Override
    void setSlotId(int slot) {
        _msg.add(slot);
        _msg.add((_cl.direction.x - 250) / 500);
        _msg.add((_cl.direction.y - 250) / 500);
        _msg.add((_cl.direction.z - 250) / 500);
        _msg.add(_cl.boidcount);
    }

    @Override
    boolean isBundle() {
        return false;
    }

    @Override
    OscBundle bundle() {
        return null;
    }

    @Override
    OscMessage msg() {
        return _msg;
    }
}
