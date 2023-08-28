package com.lvboaa.utils.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    private volatile static ExecutorService SERVICE = null;

    private ThreadPoolUtil() {
    }

    public static ExecutorService getExecutor() {
        if (SERVICE == null) {
            synchronized (ThreadPoolUtil.class){
                if (SERVICE == null){
                    SERVICE = new ThreadPoolExecutor(0, 10,
                            60L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>());
                }
            }
        }
        return SERVICE;
    }

    public static void execute(Runnable runnable) {
        SERVICE.execute(runnable);
    }

}
