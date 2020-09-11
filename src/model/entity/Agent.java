package model.entity;

import model.Vector;
import model.threading.RunnableTask;
import model.threading.TickTaskThread;

public class Agent extends GameEntity implements RunnableTask {
    private final static double START_ENERGY = 100;
    private final static double ENERGY_LOSS_PER_TICK = 0.4;
    private final static double ENERGY_ADDED_ON_EAT = 50;
    private final static int TICKS_PER_SECOND = 32;
    private final static double SPEED_MULTIPLIER = 0.01;

    private final Vector speed = new Vector().Randomize(1, 1).Normalize().Multiply(SPEED_MULTIPLIER);

    private TickTaskThread task;
    private double energy = START_ENERGY;

    public Agent(int id, Vector position, Color color) {
        super(id, position, color);
        task = new TickTaskThread(() -> {
            double delta = task.getDelta() * 1000;
            position.x += speed.x * delta;
            position.y += speed.y * delta;
            energy -= ENERGY_LOSS_PER_TICK;
        }, TICKS_PER_SECOND);
        start();
    }

    public double getEnergy() {
        return energy;
    }
    public Vector getSpeed() {
        return speed;
    }

    public void eat() {
        energy += ENERGY_ADDED_ON_EAT;
        if(energy > START_ENERGY) energy = START_ENERGY;
    }
    public void verticalFlip() {
        speed.y = -speed.y;
    }
    public void horizontalFlip() {
        speed.x = -speed.x;
    }

    public void start() {
        task.start();
    }
    public void abort() {
        task.abort();
    }
    public void suspend() {
        task.suspend();
    }
    public synchronized void resume() {
        task.resume();
    }
}