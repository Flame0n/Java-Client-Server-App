package model;

import model.entity.*;
import model.threading.RunnableTask;
import model.threading.TickTaskThread;
import java.util.ArrayList;
import static java.lang.System.out;

public class GameEngine implements RunnableTask {
    private final static int TICKS_PER_SECOND = 32;
    private final static double AGENT_RADIUS = 0.5;
    private final static double FOOD_RADIUS = 0.1;
    private final static int AGENTS_COUNT_IN_GROUP = 8;
    private final static int TICKS_TO_SPAWN_FOOD = 10;

    private final ArrayList<Agent> agents = new ArrayList<>();
    private final ArrayList<Food> food = new ArrayList<>();
    private final DifferenceManager<Food> foodDifferenceManager = new DifferenceManager<>();
    private final DifferenceManager<Agent> agentDifferenceManager = new DifferenceManager<>();

    private TickTaskThread task;
    private Vector halfScreenSize;

    public GameEngine() {
        task = new TickTaskThread(() -> {
            //Add food
            if(task.getTick() > TICKS_TO_SPAWN_FOOD) {
                Food foodInstance = new Food(IdGenerator.Get(), new Vector().Randomize(halfScreenSize.x, halfScreenSize.y), Color.Dark);
                food.add(foodInstance);
                foodDifferenceManager.pushToAdded(foodInstance);
                task.resetTick();
            }
            //Check agents collisions
            //TODO: reduce get position count
            for(Agent a : agents) {
                //Screen collision check
                if((a.getPosition().x + AGENT_RADIUS > halfScreenSize.x && a.getSpeed().x > 0) || (a.getPosition().x - AGENT_RADIUS  < -halfScreenSize.x && a.getSpeed().x < 0)) a.horizontalFlip();
                if((a.getPosition().y + AGENT_RADIUS > halfScreenSize.y && a.getSpeed().y > 0) || (a.getPosition().y - AGENT_RADIUS  < -halfScreenSize.y && a.getSpeed().y < 0)) a.verticalFlip();
                //Food collision check
                try {
                    food.removeIf(f -> {
                        if(Math.sqrt(Math.pow(a.getPosition().x - f.getPosition().x, 2) + Math.pow(a.getPosition().y - f.getPosition().y, 2)) < FOOD_RADIUS + AGENT_RADIUS) {
                            foodDifferenceManager.pushToRemoved(f);
                            a.eat();
                            return true;
                        } else {
                            return false;
                        }
                    });
                } catch (Exception ex) {
                    out.println("Error on food remove: " + ex);
                }
            }
            //Check for dead
            agents.removeIf(a -> {
                if(a.getEnergy() <= 0) {
                    agentDifferenceManager.pushToRemoved(a);
                    return true;
                } else {
                    return false;
                }
            });
        }, TICKS_PER_SECOND);
    }

    public void createAgents() {
        for(int i = 0; i < AGENTS_COUNT_IN_GROUP; i++) {
            agents.add(new Agent(IdGenerator.Get(), new Vector().Randomize(halfScreenSize.x, halfScreenSize.y), Color.Blue));
            agents.add(new Agent(IdGenerator.Get(), new Vector().Randomize(halfScreenSize.x, halfScreenSize.y), Color.Red));
            agents.add(new Agent(IdGenerator.Get(), new Vector().Randomize(halfScreenSize.x, halfScreenSize.y), Color.Dark));
        }
    }
    public void setScreenSize(Vector size) {
        halfScreenSize = size.Multiply(0.5);
    }
    public ArrayList<Agent> getAgents() {
        return agents;
    }
    public ArrayList<Food> getAddedFood() {
        return foodDifferenceManager.getAdded();
    }
    public ArrayList<Food> getRemovedFood() {
        return foodDifferenceManager.getRemoved();
    }
    public ArrayList<Agent> getRemovedAgents() {
        return agentDifferenceManager.getRemoved();
    }
    public void refreshDifferenceManager() {
        agentDifferenceManager.clear();
        foodDifferenceManager.clear();
    }
    public static double getAgentRadius() {
        return AGENT_RADIUS;
    }
    public static double getFoodRadius() {
        return FOOD_RADIUS;
    }

    public void start() {
        createAgents();
        task.start();
    }
    public void abort() {
        task.abort();
        for(Agent a : agents) a.abort();
        agents.clear();
    }
    public void suspend() {
        for(Agent a : agents) a.suspend();
        task.suspend();
    }
    public synchronized void resume() {
        for(Agent a : agents) a.resume();
        task.resume();
    }
}
