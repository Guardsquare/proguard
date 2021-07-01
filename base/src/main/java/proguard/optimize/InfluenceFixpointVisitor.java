/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * This ClassPoolVisitor visits members using visitors created by a factory. When any member X is changed, all other members
 * that refer or connect to X are revisited. This is repeated until fix point.
 *
 * This class is used for side effect marking where, once a side effect is found, all methods referring to it
 * could also have side effects.
 */
public class InfluenceFixpointVisitor
implements   ClassPoolVisitor
{
    private static final boolean DEBUG = System.getProperty("ifv") != null;

    // A copy of the code in ParallelAllClassVisitor.
    private static final int THREAD_COUNT;
    static
    {
        Integer threads = null;
        try
        {
            String threadCountString = System.getProperty("parallel.threads");
            if (threadCountString != null)
            {
                threads = Integer.parseInt(threadCountString);
            }
        }
        catch (Exception ignored) {}

        threads = threads == null ?
            Runtime.getRuntime().availableProcessors() - 1 :
            Math.min(threads, Runtime.getRuntime().availableProcessors());

        THREAD_COUNT = Math.max(1, threads);
    }


    private final MemberVisitorFactory   memberVisitorFactory;
    private       ReverseDependencyStore reverseDependencyStore;
    private final ExecutorService        executorService = Executors.newFixedThreadPool(THREAD_COUNT, new MyThreadFactory());
    private final Set<MyAnalysis>        queuedAnalyses  = Collections.synchronizedSet(new HashSet<MyAnalysis>());
    private final CountLatch             countLatch      = new CountLatch();


    /**
     * Creates a mew InfluenceFixpointVisitor
     * @param memberVisitorFactory The factory of membervisitors that will be used to visit all the classes
     */
    public InfluenceFixpointVisitor(MemberVisitorFactory memberVisitorFactory)
    {
        this.memberVisitorFactory = memberVisitorFactory;
    }


    // Implementations for ClassPoolVisitor.

    @Override
    public void visitClassPool(ClassPool classPool)
    {
        // This variable represents the ReverseDependencyStore to know which classes are impacted on change
        reverseDependencyStore = new ReverseDependencyCalculator(classPool).reverseDependencyStore();

        long start = 0L;
        if (DEBUG)
        {
            start = System.currentTimeMillis();
        }

        try
        {
            // Submit analyses for all class members.
            // We're doing this on the main thread, so we're
            // sure at least some analyses will be submitted.
            classPool.classesAccept(new AllMemberVisitor(
                                    new MyAnalysisSubmitter()));

            // Wait for all analyses to finish.
            countLatch.await();

            // Clean up the executor.
            executorService.shutdown();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException("Parallel execution is taking too long", e);
        }

        if (DEBUG)
        {
            long end = System.currentTimeMillis();
            System.out.print("InfluenceFixpointVisitor........................");
            System.out.printf(" took: %6d ms%n", (end - start));
        }
    }


    // Utility classes.

    /**
     * A factory of MemberVisitor instances.
     */
    public interface MemberVisitorFactory
    {
        /**
         * Creates a MemberVisitor instance that should visit all the members
         * that have changed with the given influencedMethodCollector.
         */
        MemberVisitor createMemberVisitor(MemberVisitor influencedMethodCollector);
    }


    /**
     * This thread factory creates analysis threads.
     */
    private class MyThreadFactory
    implements    ThreadFactory
    {
        // Implementations for ThreadFactory.

        @Override
        public Thread newThread(Runnable runnable)
        {
            return new MyAnalysisThread(runnable);
        }
    }


    /**
     * This thread runs analyses.
     */
    private class MyAnalysisThread
    extends       Thread
    {
        // Create a member visitor that runnables can reuse.
        private final MemberVisitor memberVisitor =
            memberVisitorFactory.createMemberVisitor(
                reverseDependencyStore.new InfluencedMethodTraveller(
                    new MyAnalysisSubmitter()));


        public MyAnalysisThread(Runnable runnable)
        {
            super(runnable);
        }
    }


    /**
     * This MemberVisitor schedules visited class members for analysis.
     */
    private class MyAnalysisSubmitter
    implements    MemberVisitor
    {
        // Implementations for MemberVisitor.

        @Override
        public void visitAnyMember(Clazz clazz, Member member)
        {
            // Create a new analysis task.
            MyAnalysis analysis = new MyAnalysis(clazz, member);

            // Is the analysis not queued yet?
            if (queuedAnalyses.add(analysis))
            {
                // First make sure the executor waits for the analysis.
                countLatch.increment();

                // Queue the analysis.
                executorService.submit(analysis);
            }
        }
    }


    /**
     * This Runnable analyzes a given class member.
     */
    private class MyAnalysis
    implements    Runnable
    {
        private final Clazz  clazz;
        private final Member member;


        private MyAnalysis(Clazz clazz, Member member)
        {
            this.clazz  = clazz;
            this.member = member;
        }


        // Implementations for Runnable.

        @Override
        public void run()
        {
            try
            {
                // Remove ourselves from the set of queued analyses. This is a
                // conservative approach: it's possible that the same analysis
                // is queued again right away.
                queuedAnalyses.remove(this);

                // Perform the actual analysis.
                // Reuse the thread's member visitor.
                MemberVisitor memberVisitor = ((MyAnalysisThread)Thread.currentThread()).memberVisitor;
                member.accept(clazz, memberVisitor);
            }
            finally
            {
                // Allow the executor to end if we're the last analysis.
                countLatch.decrement();
            }
        }

        // Implementations for Object.

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyAnalysis that = (MyAnalysis)o;
            return Objects.equals(clazz, that.clazz) &&
                   Objects.equals(member, that.member);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(clazz, member);
        }
    }


    /**
     * This latch allows one or more threads to wait until other threads have
     * incremented and then decremented its internal counter to 0. Be careful
     * to increment it (if applicable) before waiting for it.
     */
    private static class CountLatch
    {
        private int counter;


        /**
         * Increments the internal counter.
         */
        public synchronized void increment()
        {
            counter++;
        }


        /**
         * Decrements the internal counter.
         */
        public synchronized void decrement()
        {
            if (--counter <= 0)
            {
                // Wake up all threads that are waiting for the monitor.
                // They may proceed as soon as we relinquish the
                // synchronization lock.
                notifyAll();
            }
        }


        /**
         * Waits for the internal counter to become 0, if it's larger than 0.
         */
        public synchronized void await()
        throws InterruptedException
        {
            if (counter > 0)
            {
                // Relinquish the synchronization lock and wait for the
                // monitor.
                wait();
            }
        }
    }
}
