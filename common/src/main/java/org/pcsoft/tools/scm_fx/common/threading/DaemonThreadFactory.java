package org.pcsoft.tools.scm_fx.common.threading;

import java.util.concurrent.ThreadFactory;

/**
 * Created by pfeifchr on 24.10.2014.
 */
public final class DaemonThreadFactory implements ThreadFactory {

    private final String name;

    public DaemonThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r, name);
        thread.setDaemon(true);

        return thread;
    }
}
