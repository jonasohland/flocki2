package de.flocki;

import java.util.ArrayList;
import oscP5.*;
import processing.core.PApplet;

class Flock {

    ArrayList boids; // An arraylist for all the boids

    FlockingProperties props = new FlockingProperties();

    int graincount = 200;

    Flock() {
        boids = new ArrayList(); // Initialize the arraylist
    }

    void toggleNbhdRep() {
        for (int i = 0; i < boids.size(); i++) {
            Boid b = (Boid) boids.get(i);
            b.neighborhoodrepresentation = !b.neighborhoodrepresentation;
        }
    }

    void toggleWebRepp() {
        for (int i = 0; i < boids.size(); i++) {
            Boid b = (Boid) boids.get(i);
            b.connectivity = !b.connectivity;
        }
    }

    void run() {

        OscBundle bundle = new OscBundle();

        for (int i = 0; i < boids.size(); i++) {
            Boid b = (Boid) boids.get(i);
            b.run(boids);  // Passing the entire list of boids to each boid individually

            OscMessage msg = new OscMessage("/" + i);

            float tx = b.loc.x - 250;
            float ty = (b.loc.y - 250) * -1;
            float tz = (b.loc.z - 250) * -1;

            float rad = (float) Math.sqrt(tx * tx + ty * ty + tz * tz);

            msg.add(tx);
            msg.add(ty);
            msg.add(tz);
            msg.add(rad / 353.55);
            msg.add(b.vel.magnitude());

            bundle.add(msg);
        }

        // oscP5.send(bundle, remote);
    }

    void setup(PApplet renderer, Matter matter, Vector3D ghetto) {

        for (int i = 0; i < graincount; i++) {

            Boid boid = new Boid(new Vector3D(
                    (float) Math.random() * renderer.width / 2, (float) Math.random() * renderer.height / 2), i);

            boid.renderer = renderer;
            boid.props = props;
            boid.matter = matter;
            boid.ghetto = ghetto;
            boid.flock = this;

            boids.add(boid);
        }
    }
}