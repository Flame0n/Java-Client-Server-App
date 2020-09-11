package model.threading;

import static java.lang.System.out;

public class TickTaskThread implements RunnableTask {
    private class ForeverThread implements RunnableTask {
        private final DeltaTimeManager deltaManager = new DeltaTimeManager();
        private final Thread _thread;

        private int currentTick = 0;
        private boolean isNeedToBeSuspended = false;
        private boolean isNeedToBeAborted = false;

        public ForeverThread(Function function, int tickRate) {
            _thread = new Thread(() -> {
                while (true) {
                    if (!isNeedToBeAborted && !isNeedToBeSuspended) function.Invoke();
                    try {
                        synchronized (this) {
                            while (isNeedToBeSuspended) wait();
                            if (isNeedToBeAborted) return;
                        }
                        deltaManager.refreshOldTime();
                        Thread.sleep(1000 / tickRate);
                    } catch (InterruptedException ex) {
                        out.println(ex);
                    }
                    currentTick++;
                }
            });
        }

        public int getTick() {
            return currentTick;
        }

        public void refreshTick() {
            currentTick = 0;
        }

        public double getDelta() {
            return deltaManager.getDelta();
        }

        public void start() {
            _thread.start();
        }

        public void suspend() {
            isNeedToBeSuspended = true;
        }

        public synchronized void resume() {
            isNeedToBeSuspended = false;
            notify();
        }

        public void abort() {
            isNeedToBeAborted = true;
            if (isNeedToBeSuspended) resume();
        }
    }

    private ForeverThread thread;
    private final Function function;
    private final int tickRate;

    public TickTaskThread(Function function, int tickRate) {
        this.function = function;
        this.tickRate = tickRate;
    }

    public double getDelta() {
        return thread.getDelta();
    }
    public int getTick() {
        return thread.getTick();
    }
    public void resetTick() {
        thread.refreshTick();
    }

    public void start() {
        if(thread != null) {
            //For cases when thread isn't aborted
            thread.abort();
        }
        thread = new ForeverThread(function, tickRate);
        thread.start();
    }
    public void abort() {
        thread.abort();
    }
    public void suspend() {
        thread.suspend();
    }
    public void resume() {
        thread.resume();
    }
}
