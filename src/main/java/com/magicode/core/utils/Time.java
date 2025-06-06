package main.java.com.magicode.core.utils;

public class Time { // Вспомогательный класс для передачи времени

    public static final long SECOND = 1000000000l;

    public static long get() {
        return System.nanoTime();
    }
}
