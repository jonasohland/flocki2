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
        velocity = 0;

        for(int i = 0; i < boids.size(); ++i) {

            Boid b = boids.get(i);

            direction.add(b.loc);
            velocity += b.vel.magnitude();
        }

        velocity = velocity / boids.size();

        direction.div(boids.size());

        boidcount = boids.size();

        ArrayList<Vector3D> relative_positions = new ArrayList<>();

        boids.forEach(b -> {
            Vector3D rel = b.loc.copy();
            rel.sub(direction);
            relative_positions.add(rel);
        });

        float mag_sum = 0;

        for(int i = 0; i < relative_positions.size(); ++i)
            mag_sum += relative_positions.get(i).magnitude();

        clustersize = mag_sum / relative_positions.size();

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
    float velocity = 0;
    Vector3D direction;

    int id = 0;

    int age = 0;

    boolean used = false;
}
