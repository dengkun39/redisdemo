package com.ken.util;

import java.util.concurrent.locks.LockSupport;

public class ThreadUtil {

    public static void sleepSeconds(int second) {
        LockSupport.parkNanos(second * 1000L * 1000L * 1000L);
    }
}
