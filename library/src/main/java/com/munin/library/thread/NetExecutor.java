package com.munin.library.thread;


import com.munin.library.log.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author M
 * 针对网络请求
 */
public class NetExecutor {
    private static final String TAG = "NetExecutor";
    private static final int CORE_POOL_SIZE = 30;
    private static final int MAX_POOL_SIZE = 60;
    private static final int TIME = 5;

    private static volatile NetExecutor mInstance = new NetExecutor();
    private ThreadPoolExecutor mExecutor;

    private NetExecutor() {
        mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(30),
                new NetThreadFactory(),
                new RejectedHandler()
        );
    }

    public static NetExecutor newInstance() {
        return mInstance;
    }

    public ThreadPoolExecutor getExecutor() {
        return mExecutor;
    }

    public void execute(Runnable task) {
        if (task == null) {
            Logger.e(TAG, "execute: task is null!");
            return;
        }
        if (mExecutor != null) {
            mExecutor.execute(task);
        }
    }

    public Future submit(Runnable task) {
        if (task == null) {
            return null;
        }
        if (mExecutor == null) {
            return null;
        }
        return mExecutor.submit(task);
    }

    public <V> Future<V> submit(Runnable task, V data) {
        if (task == null) {
            return null;
        }
        if (mExecutor == null) {
            return null;
        }
        return mExecutor.submit(task, data);
    }

    public <V> Future<V> submit(Callable<V> task) {
        if (task == null) {
            return null;
        }
        if (mExecutor == null) {
            return null;
        }
        return mExecutor.submit(task);
    }


    public static class NetThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        private NetThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "Net-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-Thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


    public class RejectedHandler implements RejectedExecutionHandler {

        private static final String TAG = "RejectedHandler";

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Logger.e(TAG, "rejectedExecution!!!");
        }

    }
}
