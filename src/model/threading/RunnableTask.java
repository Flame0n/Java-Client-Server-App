package model.threading;

public interface RunnableTask {
    void start();
    void abort();
    void suspend();
    void resume();
}
