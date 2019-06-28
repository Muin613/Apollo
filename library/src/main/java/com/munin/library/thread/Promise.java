package com.munin.library.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author M
 * 链式
 */
public class Promise<V> implements Future<Result<V>> {
    private static final String TAG = "Promise";

    public interface Action<T> {
        void invoke(T result);
    }

    public interface Consumer<T> {
        void invoke(Result<T> result);
    }

    private final CountDownLatch taskLock = new CountDownLatch(1);
    private List<Action<Promise<V>>> callbacks = new ArrayList<>();

    protected Result<V> result = null;

    protected boolean invoked = false;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return invoked;
    }


    @Override
    public Result<V> get() throws InterruptedException {
        taskLock.await();
        return result;
    }

    @Override
    public Result<V> get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!taskLock.await(timeout, unit)) {
            throw new TimeoutException(String.format("Promise didn't redeem in %s %s", timeout, unit));
        }
        return result;
    }

    public void invoke(Result<V> result) {
        synchronized (this) {
            if (!invoked) {
                invoked = true;
                this.result = result;
                taskLock.countDown();
            } else {
                return;
            }
            for (Action<Promise<V>> callback : callbacks) {
                callback.invoke(this);
            }
        }
    }

    public synchronized void onRedeem(Action<Promise<V>> callback) {
        if (!invoked) {
            callbacks.add(callback);
        }
        if (invoked) {
            callback.invoke(this);
        }
    }

    public static <V> Promise<V> submit(final Callable<Result<V>> callable, ExecutorService executorService) {
        final Promise<V> promise = new Promise<>();
        Callable<Result<V>> smarterCallable = new Callable<Result<V>>() {
            @Override
            public Result<V> call() throws Exception {
                try {
                    Result<V> result = callable.call();
                    promise.invoke(result);
                    return result;
                } catch (Exception e) {
                    Result<V> result = new Result<V>();
                    result.setException(e);
                    promise.invoke(result);
                    return result;
                }
            }
        };
        executorService.submit(smarterCallable);
        return promise;
    }

    public static <V> Promise<V> submit(final Callable<Result<V>> callable) {
        return submit(callable, GlobalExecutor.newInstance().getExecutor());
    }

    public synchronized Promise<V> then(final Consumer<V> action) {
        final Promise<V> promise = new Promise<>();
        promise.onRedeem(new Action<Promise<V>>() {
            @Override
            public void invoke(Promise<V> result) {
                action.invoke(result.result);

            }
        });
        onRedeem(new Action<Promise<V>>() {
            @Override
            public void invoke(Promise<V> result) {
                promise.invoke(result.result);
            }
        });
        if (isDone()) {
            promise.invoke(result);
        }
        return promise;
    }

}