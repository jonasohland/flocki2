package de.flocki;

import processing.core.PApplet;

public class Shapes {

    PApplet renderer;

    int cylinder_detail=0;
    float[] cylinderX,cylinderZ;

    float sinLUT[];
    float cosLUT[];
    float SINCOS_PRECISION = 0.5f;
    int SINCOS_LENGTH = (int) (360.0 / SINCOS_PRECISION);

    Shapes(PApplet app) {
        renderer = app;
    }

    void cylinderDetail(int res) {

        sinLUT = new float[SINCOS_LENGTH];
        cosLUT = new float[SINCOS_LENGTH];

        for (int i = 0; i < SINCOS_LENGTH; i++) {
            sinLUT[i] = (float) Math.sin(i * renderer.DEG_TO_RAD * SINCOS_PRECISION);
            cosLUT[i] = (float) Math.cos(i * renderer.DEG_TO_RAD * SINCOS_PRECISION);
        }

        if (res<3) res=3; // force a minimum res
        if (res != cylinder_detail) {
            float delta = (float)SINCOS_LENGTH/res;
            cylinderX = new float[res];
            cylinderZ = new float[res];
            // calc unit circle in current resolution in XZ plane
            for (int i = 0; i < res; i++) {
                cylinderX[i] = cosLUT[(int) (i*delta) % SINCOS_LENGTH];
                cylinderZ[i] = sinLUT[(int) (i*delta) % SINCOS_LENGTH];
            }
            cylinder_detail = res;
        }
    }

    void cone(float r, float h, boolean bottomCap) {
        if (cylinder_detail == 0) {
            cylinderDetail(30);
        }
        h*=0.5; // center along Y axis
        renderer.beginShape(renderer.TRIANGLE_STRIP);
        for (int i = 0; i < cylinder_detail; i++) {
            renderer.vertex(0,-h,0);
            renderer.vertex(cylinderX[i]*r, h, cylinderZ[i]*r);
        }
        renderer.vertex(0,-h,0);
        renderer.vertex(cylinderX[0]*r, h, cylinderZ[0]*r);
        renderer.endShape();
        if (bottomCap) {
            renderer.beginShape(renderer.TRIANGLE_STRIP);
            for (int i = 0; i < cylinder_detail; i++) {
                renderer.vertex(0,h,0);
                renderer.vertex(cylinderX[i]*r, h, cylinderZ[i]*r);
            }
            renderer.vertex(0,h,0);
            renderer.vertex(cylinderX[0]*r, h, cylinderZ[0]*r);
            renderer.endShape();
        }
    }

    void cylinder(float r1, float r2, float h, boolean topCap, boolean bottomCap) {
        if (cylinder_detail == 0) {
            cylinderDetail(30);
        }
        h*=0.5;
        if (topCap) {
            renderer.beginShape(renderer.TRIANGLE_STRIP);
            for (int i = 0; i < cylinder_detail; i++) {
                renderer.vertex(0,-h,0);
                renderer.vertex(cylinderX[i]*r1, -h, cylinderZ[i]*r1);
            }
            renderer.vertex(0,-h,0);
            renderer.vertex(cylinderX[0]*r1, -h, cylinderZ[0]*r1);
            renderer.endShape();
        }
        renderer.beginShape(renderer.TRIANGLE_STRIP);
        for (int i = 0; i < cylinder_detail; i++) {
            renderer.vertex(cylinderX[i]*r1, -h, cylinderZ[i]*r1);
            renderer.vertex(cylinderX[i]*r2, h, cylinderZ[i]*r2);
        }
        renderer.vertex(cylinderX[0]*r1, -h, cylinderZ[0]*r1);
        renderer.vertex(cylinderX[0]*r2, h, cylinderZ[0]*r2);
        renderer.endShape();
        if (bottomCap) {
            renderer.beginShape(renderer.TRIANGLE_STRIP);
            for (int i = 0; i < cylinder_detail; i++) {
                renderer.vertex(0,h,0);
                renderer.vertex(cylinderX[i]*r2, h, cylinderZ[i]*r2);
            }
            renderer.vertex(0,h,0);
            renderer.vertex(cylinderX[0]*r2, h, cylinderZ[0]*r2);
            renderer.endShape();
        }
    }
}
