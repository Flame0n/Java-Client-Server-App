package model.entity;

import java.util.ArrayList;

public class DifferenceManager<T> {
    private final ArrayList<T> added = new ArrayList<T>();
    private final ArrayList<T> removed = new ArrayList<T>();

    public void pushToAdded(T elem) {
        added.add(elem);
    }
    public void pushToRemoved(T elem) {
        removed.add(elem);
    }
    public ArrayList<T> getAdded() {
        return added;
    }
    public ArrayList<T> getRemoved() {
        return removed;
    }
    public void clear() {
        added.clear();
        removed.clear();
    }
}
