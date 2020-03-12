package de.flocki;

import processing.core.*;
import processing.opengl.*;

public class MainClass extends PApplet {

    boolean pause = false;
    boolean record = false;

    Flock flock = new Flock();
    Matter matter = new Matter();
    Camera cam = new Camera();
    Vector3D ghetto = new Vector3D();
    Shapes shapes;

    @Override
    public void settings() {

        size(1200,1000,P3D);

        shapes = new Shapes(this);
        ghetto = new Vector3D(500,500,500);

    }

    @Override
    public void setup() {

        colorMode(RGB, 255, 255, 255, 100);

        shapes.cylinderDetail(300);
        noStroke();
        flock.setup(this, matter, ghetto);

        matter.addParticle(new Particle(new Vector3D(250, 250, 250), 100f, 180, 180, 180));

    }

    @Override
    public void keyPressed() {
        if (key == 32) pause = !pause;
        // if (key == 110) flock.toggleNbhdRep(); // 110 is 'n' for neighborhood
        if (key == 119) flock.toggleWebRepp(); // 119 is 'w' for web-view
        if (key == 'r') record = true;

    }

    @Override
    public void draw() {

        if (pause)
            return;

        background(255);

        cam.run(this);
        rotateY(PI/2);


        matter.render(this);

        flock.run();

        // super.draw();
    }

    public static void main(String[] args) {
        PApplet.main(MainClass.class);
    }

}
