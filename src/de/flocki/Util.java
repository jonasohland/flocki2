package de.flocki;

public class Util {
    static float random(float min, float max) {
        return (float) Math.random() * ((max - min) + 1) + min;
    }
}
