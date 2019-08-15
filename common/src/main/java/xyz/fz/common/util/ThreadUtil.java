package xyz.fz.common.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadUtil {
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();
    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CPU_CORES * 2,
            new BasicThreadFactory.Builder().namingPattern("util-schedule-pool-%d").daemon(true).build());

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
