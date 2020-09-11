package model.threading;

public class DeltaTimeManager {
    private long oldTime = System.currentTimeMillis();

    public double getDelta() {
        return (double)(System.currentTimeMillis() - oldTime) / 1000;
    }
    public void refreshOldTime() {
        oldTime = System.currentTimeMillis();
    }
}
