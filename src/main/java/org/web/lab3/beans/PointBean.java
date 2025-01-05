package org.web.lab3.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.web.lab3.models.Point;
import org.web.lab3.models.Result;
import org.web.lab3.utils.DatabaseManager;

import java.io.Serializable;

@Named
@SessionScoped
public class PointBean implements Serializable {

    private double x;
    private double y;
    private double radius;
    private boolean hit;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius should be more than 0");
        }
        this.radius = radius;
    }

    public boolean isHit() {
        return hit;
    }

    public void checkHit() {
        hit = isWithinArea(x, y, radius);
        saveResult();
    }

    private boolean isWithinArea(double x, double y, double r) {
        if (x <= 0 && y >= 0){
            return x >= -r && y <= r;
        } else if (x >= 0 && y <= 0){
            return x <= r && y >= -r/2 && x + r >= -y;
        } else if (x >= 0 && y >= 0){
            return x * x + y * y <= r * r / 4;
        }
        return false;
    }

    private void saveResult() {
        DatabaseManager.saveResult(new Result(new Point(x, y, radius), hit));
    }
}