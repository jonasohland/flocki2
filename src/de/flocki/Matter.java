package de.flocki;

import processing.core.PApplet;

import java.util.ArrayList;

class Matter {
    ArrayList particles;

    Matter() {
        particles = new ArrayList();
    }

    void addParticle(Particle p) {
        particles.add(p);
    }

    void render(PApplet app) {
        for (int i = 0; i < particles.size(); i++) {
            Particle p = (Particle) particles.get(i);
            p.render(app);
        }
    }
}