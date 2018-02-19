/*
 * Copyright 2008 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @author Kim Hansen
 */
package dk.ange.octave.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A ThreadFactory that allows to create a thread from a runnable 
 * with a specific thread name. 
 * 
 * @author Kim Hansen
 */
public final class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * The thread group either from the security manager 
     * or from the current thread. 
     */
    private final ThreadGroup group;

    /**
     * The name prefix of the form 
     * <code>&lt;threadname>-javaoctave-&lt;prefix>-&lt;POOLNUMBER>-</code>, 
     * where <code>&lt;threadname></code> is the name of the curren thread, 
     * <code>&lt;prefix></code> is the prefix given by a parameter 
     * of the constructor {@link #NamedThreadFactory(String)} 
     * and <code>&lt;POOLNUMBER></code> is a running integer starting with 1. 
     * The trailing <code>-</code> is there because a new thread 
     * defined by {@link #newThread(Runnable)} obtains a name 
     * consisting of the prefix followed by {@link #threadNumber}. 
     */
    private final String namePrefix;

    /**
     * Will create a factory that create Threads with the names: 
     * <code>[parent]-javaoctave-[prefix]-[pool#]-[thread#]</code>. 
     * 
     * @param prefix
     */
    @SuppressWarnings("PMD.AvoidThreadGroup")
    // Thread.getThreadGroup() causes warning 
    // only because threadgroup has methods which are not threadsafe. 
    // but we do not invoke method on group, 
    // use just to create new thread with that group. 
    public NamedThreadFactory(final String prefix) {
        final SecurityManager securityManager = System.getSecurityManager();
        this.group = (securityManager == null) 
	    ? Thread.currentThread().getThreadGroup()
	    : securityManager       .getThreadGroup();
        this.namePrefix = Thread.currentThread().getName() + "-javaoctave-" 
	    + prefix + "-" + POOL_NUMBER.getAndIncrement() + "-";
    }

    /**
     * Returns a new thread with standard priorith which is no daemon 
     * from <code>runnable</code> 
     * with name consisting of {@link #namePrefix} and a running number 
     * {@link #threadNumber}. 
     * 
     */
    @Override
    public Thread newThread(final Runnable runnable) {
	String name = this.namePrefix + this.threadNumber.getAndIncrement();
        final Thread thread = new Thread(this.group, runnable, name);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
	// Here, thread is no daemon 
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
	// Here, thread has NORM_PRIORITY 
        return thread;
    }

}
