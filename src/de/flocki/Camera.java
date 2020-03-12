package de.flocki;

import processing.core.PApplet;

public class Camera {

    int cam_x = 1000;
    int cam_y = 250;
    int cam_z = -250;

    int cam_px = 250;
    int cam_py = 250;
    int cam_pz = -250;


    void run(PApplet renderer){
        renderer.camera(cam_x, cam_y, cam_z, cam_px, cam_py, cam_pz, 0,1,0);
    }

}
