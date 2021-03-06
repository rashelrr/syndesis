/*
 * Copyright (C) 2016 Red Hat, Inc.
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
package io.syndesis.common.util.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Threads {

    static class Logging implements UncaughtExceptionHandler {

        final Logger log;

        Logging(final Logger log) {
            this.log = log;
        }

        @Override
        public void uncaughtException(final Thread thread, final Throwable e) {
            log.error("Failure on thread: {}", thread.getName(), e);
        }

    }

    private Threads() {
        // utility class
    }

    public static ThreadFactory newThreadFactory(final String groupName) {
        return new ThreadFactory() {
            @SuppressWarnings("PMD.AvoidThreadGroup")
            final ThreadGroup initialization = new ThreadGroup(groupName);

            final AtomicInteger threadNumber = new AtomicInteger(-1);

            @Override
            public Thread newThread(final Runnable task) {

                final Thread thread = new Thread(initialization, task);
                thread.setName(groupName + "-" + threadNumber.incrementAndGet());

                final Logger log = LoggerFactory.getLogger(task.getClass());
                thread.setUncaughtExceptionHandler(new Logging(log));

                return thread;
            }
        };
    }
}
