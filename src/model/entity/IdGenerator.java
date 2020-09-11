package model.entity;

public class IdGenerator {
    private static int value = 0;
    public static int Get() {
        return value++;
    }
    public static void Refresh() {
        value = 0;
    }
}
