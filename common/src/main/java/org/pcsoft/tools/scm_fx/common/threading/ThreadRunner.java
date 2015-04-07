package org.pcsoft.tools.scm_fx.common.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by pfeifchr on 24.10.2014.
 */
public final class ThreadRunner {

    public static void submit(String threadName, Runnable runnable) {
        Executors.newCachedThreadPool(new DaemonThreadFactory(threadName)).submit(runnable);
    }

    public static <V>Future<V> submit(String threadName, Callable<V> callable) {
        return Executors.newCachedThreadPool(new DaemonThreadFactory(threadName)).submit(callable);
    }

    private ThreadRunner() {
    }
}
