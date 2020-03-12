package de.flocki;

public class Cluster {

    Cluster(int id) {
        this.id = id;
    }

    float r = (float) Math.random() * 255;
    float g = (float) Math.random() * 255;
    float b = (float) Math.random() * 255;
    int id = 0;
    boolean used = false;
}
