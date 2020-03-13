package de.flocki;

import java.util.ArrayList;

public class Cluster {

    Cluster(int id) {
        this.id = id;
    }

    boolean build(ArrayList<Boid> boids) {

        if(age < 10)
            return false;

        direction = new Vector3D(0,0,0);

        boids.forEach(boid -> {
            direction.add(boid.loc);
        });

        direction.div(boids.size());

        boidcount = boids.size();

        // System.out.println("ID: " + id + " x: " + direction.x + " y: "+ direction.y + " z: " + direction.z);

        return true;
    }

    int getID() {
        return id;
    }

    float r = (float) Math.random() * 255;
    float g = (float) Math.random() * 255;
    float b = (float) Math.random() * 255;

    int boidcount = 0;
    float clustersize = 0;
    Vector3D direction;

    int id = 0;

    int age = 0;

    boolean used = false;
}
