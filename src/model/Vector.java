package model;

public class Vector {
    public double x;
    public double y;

    public Vector() {
        x = 0;
        y = 0;
    }
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector Normalize() {
        double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        x /= length;
        y /= length;
        return this;
    }
    public Vector Randomize(double x, double y) {
        this.x = (Math.random() - 0.5) * 2 * x;
        this.y = (Math.random() - 0.5) * 2 * y;
        return this;
    }
    public Vector Multiply(double value) {
        x *= value;
        y *= value;
        return this;
    }
}
