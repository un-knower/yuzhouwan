package com.yuzhouwan.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：Thread Utils
 *
 * @author Benedict Jin
 * @since 2016/12/9
 */
public class ThreadUtils {

    private static final Logger _log = LoggerFactory.getLogger(ThreadUtils.class);

    private static long MIN_PERIOD;

    static {
        String minPeriodStr;
        if (StrUtils.isEmpty(minPeriodStr = PropUtils.getInstance().getProperty("thread.min.period.millisecond")))
            MIN_PERIOD = 0L;
        else MIN_PERIOD = Long.parseLong(minPeriodStr);
        if (MIN_PERIOD < 0) MIN_PERIOD = 0L;
    }

    /**
     * CachedThreadPool
     *
     * @param poolName
     * @return
     */
    public static ExecutorService buildExecutorService(String poolName) {
        return buildExecutorService(null, poolName, null);
    }

    /**
     * CachedThreadPool with isDaemon
     *
     * @param poolName
     * @param isDaemon
     * @return
     */
    public static ExecutorService buildExecutorService(String poolName, Boolean isDaemon) {
        return buildExecutorService(null, poolName, isDaemon);
    }

    /**
     * FixedThreadPool
     *
     * @param poolName
     * @return
     */
    public static ExecutorService buildExecutorService(Integer nThread, String poolName) {
        return buildExecutorService(nThread, poolName, null);
    }

    /**
     * FixedThreadPool with isDaemon
     *
     * @param nThread
     * @param poolName
     * @param isDaemon
     * @return
     */
    public static ExecutorService buildExecutorService(Integer nThread, String poolName, Boolean isDaemon) {
        if (nThread != null && nThread >= 0)
            return Executors.newFixedThreadPool(nThread, buildThreadFactory(poolName, isDaemon));
        else
            return buildExecutorService(null, null, null, null, poolName, isDaemon);
    }

    /**
     * CachedThreadPool / (ThreadPoolExecutor with ArrayBlockingQueue)
     *
     * @param jobThreadCorePoolSize
     * @param jobThreadMaximumPoolSize
     * @param jobThreadKeepAliveSecond
     * @param jobArrayBlockingQueueSize
     * @param poolName
     * @param isDaemon
     * @return
     */
    public static ExecutorService buildExecutorService(Integer jobThreadCorePoolSize, Integer jobThreadMaximumPoolSize,
                                                       Long jobThreadKeepAliveSecond, Integer jobArrayBlockingQueueSize,
                                                       String poolName, Boolean isDaemon) {
        ThreadFactory threadFactory = buildThreadFactory(poolName, isDaemon);
        ExecutorService executorService;
        if (jobThreadCorePoolSize == null || jobThreadMaximumPoolSize == null
                || jobThreadKeepAliveSecond == null || jobArrayBlockingQueueSize == null) {
            executorService = Executors.newCachedThreadPool(threadFactory);
        } else {
            try {
                executorService = new ThreadPoolExecutor(jobThreadCorePoolSize, jobThreadMaximumPoolSize,
                        jobThreadKeepAliveSecond, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(jobArrayBlockingQueueSize), threadFactory);    // jdk7: new ArrayBlockingQueue<Runnable>
            } catch (Exception e) {
                _log.error(ExceptionUtils.errorInfo(e));
                executorService = Executors.newCachedThreadPool(threadFactory);
            }
        }
        return executorService;
    }

    private static ThreadFactory buildThreadFactory(String poolName, Boolean isDaemon) {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        if (!StrUtils.isEmpty(poolName)) threadFactoryBuilder.setNameFormat("[".concat(poolName).concat("]-%d"));
        if (isDaemon != null) threadFactoryBuilder.setDaemon(isDaemon);
        return threadFactoryBuilder.build();
    }

    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void wait4Period(long passedPeriod, long aimPeriod) {
        try {
            long sleep;
            if (passedPeriod < aimPeriod) {
                if ((sleep = aimPeriod - passedPeriod) < MIN_PERIOD) sleep = MIN_PERIOD;
            } else {
                _log.warn("Thread:{}, Used Time: {}, Excepted Period Time: {}", Thread.currentThread().getName(),
                        passedPeriod, aimPeriod);
                sleep = MIN_PERIOD;
            }
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            _log.error(ExceptionUtils.errorInfo(e));
        }
    }
}