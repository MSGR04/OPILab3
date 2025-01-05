package org.web.lab3.models;

public class Point {
    private final double x;
    private final double y;
    private final double r;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getR() {
        return r;
    }
}


