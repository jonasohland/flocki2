package de.flocki;

import org.javatuples.Triplet;
import processing.core.*;

class Particle {
    Vector3D loc;
    float radius;
    float r;
    float g;
    float b;

    void setColor(float r_, float g_, float b_){
        r = r_;
        g = g_;
        b = b_;
    }

    Triplet<Float, Float, Float> getColor(){
        return Triplet.with(r, g, b);
    }

    // constructor
    Particle(float r) {
        loc = new Vector3D();
        this.r = 180;
        g = 180;
        b = 180;
        radius = r;
    }

    // constructor
    Particle(Vector3D l, float r, float cr, float cg, float cb) {
        loc = l;
        this.r = cr;
        g = cg;
        b = cb;
        radius = r;
    }

    void render(PApplet app) {
        // noStroke();
        app.sphereDetail(24);
        app.pushMatrix();
        app.fill(r,g,b);
        app.translate(loc.x,loc.y,loc.z);
        app.sphere(radius);
        app.popMatrix();
    }
}
