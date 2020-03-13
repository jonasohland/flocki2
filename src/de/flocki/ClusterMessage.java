package de.flocki;

import oscP5.OscBundle;
import oscP5.OscMessage;

public abstract class ClusterMessage<P> {

    int _clusterID = 0;

    abstract int clusterID();


    abstract void setSlotId(int slot);
    abstract boolean isBundle();
    abstract OscBundle bundle();
    abstract OscMessage msg();
}
