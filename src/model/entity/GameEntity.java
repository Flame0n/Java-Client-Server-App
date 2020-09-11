package model.entity;

import model.Vector;

public abstract class GameEntity {
    protected final Vector position;
    protected final int id;
    protected final Color color;

    public GameEntity(int id, Vector position, Color color) {
        this.position = position;
        this.id = id;
        this.color = color;
    }

    public Vector getPosition() {
        return position;
    }
    public int getId() {
        return id;
    }
    public Color getColor() {
        return color;
    }
}
