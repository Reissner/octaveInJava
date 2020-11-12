/*
 * Copyright 2008 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreeds to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @author Kim Hansen
 */
package eu.simuline.octave.util;

import eu.simuline.octave.exec.OctaveExec;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A ThreadFactory that allows to create a thread from a runnable 
 * with a specific thread name. 
 * 
 * @author Kim Hansen
 */
public final class NamedThreadFactory implements ThreadFactory {

    /**
     * This is initialized with 1 
     * and read and incremented only if a factory object is created. 
     * It goes into {@link #namePrefix}. 
     */
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

    /**
     * The thread group from the security manager if it exists 
     * or else from the current thread. 
     * It is the group each thread belongs to created by {@link #newThread(Runnable)}. 
     */
    private final ThreadGroup group;

    /**
     * The name prefix of the form 
     * <code>&lt;threadname&gt;-javaoctave-&lt;prefix&gt;-&lt;POOLNUMBER&gt;-</code>, 
     * where <code>&lt;threadname&gt;</code> is the name of the current thread, 
     * <code>&lt;prefix&gt;</code> is the prefix given by a parameter 
     * of the constructor {@link #NamedThreadFactory(String)} 
     * and <code>&lt;POOLNUMBER&gt;</code> is {@link #POOL_NUMBER} 
     * depending on the factory object. 
     * The trailing <code>-</code> is there because a new thread 
     * defined by {@link #newThread(Runnable)} obtains a name 
     * consisting of the prefix followed by {@link #threadNumber}. 
     */
    private final String namePrefix;

    /**
     * The number of the thread created next by this factory starting with one 
     * and being incremented by method {@link #newThread(Runnable)}. 
     * 
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * Will create a factory that create Threads with the names: 
     * <code>[parent]-javaoctave-[prefix]-[pool#]-[thread#]</code>. 
     * Here, <code>[parent]</code> is the name of the parent thread, 
     * i.e. of the current thread, 
     * <code>[prefix]</code> is given by the parameter 
     * <code>[pool#]</code> is the number of this factory 
     * and <code>[thread#]</code> refers to the number of the thread 
     * created by this factory. 
     * 
     * @param prefix
     */
    @SuppressWarnings("PMD.AvoidThreadGroup")
    // Thread.getThreadGroup() causes warning 
    // only because threadgroup has methods which are not threadsafe. 
    // but we do not invoke method on group, 
    // use just to create new thread with that group. 
    private NamedThreadFactory(final String prefix) {
        final SecurityManager securityManager = System.getSecurityManager();
        this.group = (securityManager == null) 
	    ? Thread.currentThread().getThreadGroup()
	    : securityManager       .getThreadGroup();
        this.namePrefix = Thread.currentThread().getName() + "-javaoctave-" 
	    + prefix + "-" + POOL_NUMBER.getAndIncrement() + "-";
    }

    /**
     * Creates a NamedThreadFactory via {@link #NamedThreadFactory(String)} 
     * with name given by the simple class name of {@link OctaveExec}. 
     */
    public NamedThreadFactory() {
	this(OctaveExec.class.getSimpleName());
    }

    /**
     * Returns a new thread with standard priority which is no daemon 
     * with default priority {@link Thread#NORM_PRIORITY} 
     * from <code>runnable</code> 
     * with name consisting of {@link #namePrefix} and a running number 
     * {@link #threadNumber}. 
     *
     * @param runnable
     *    the runnable to create a thread from. 
     */
    @Override
    public Thread newThread(final Runnable runnable) {
	String name = this.namePrefix + this.threadNumber.getAndIncrement();
        final Thread thread = new Thread(this.group, runnable, name);
        if (thread.isDaemon()) {
            // is a daemon iff this thread is a daemon 
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
