/*
 * Copyright 2007-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.module.schedule;

import net.ymate.module.schedule.impl.DefaultTaskExecutionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/04 15:23
 */
public abstract class AbstractScheduleTask implements IScheduleTask {

    private static final Log LOG = LogFactory.getLog(AbstractScheduleTask.class);

    private final IScheduleLocker scheduleLocker;

    private final boolean sync;

    public AbstractScheduleTask() {
        this(false);
    }

    public AbstractScheduleTask(boolean sync) {
        this.sync = sync;
        if (this.sync) {
            scheduleLocker = Scheduler.get().getConfig().getScheduleLockerFactory().getScheduleLocker(getClass().getName());
            if (scheduleLocker == null) {
                throw new IllegalStateException(String.format("Can not create schedule locker for %s", getClass().getName()));
            }
        } else {
            scheduleLocker = null;
        }
    }

    @Override
    public boolean isSync() {
        return sync;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ITaskExecutionContext executionContext = new DefaultTaskExecutionContext(context);
        //
        if (sync) {
            if (scheduleLocker.tryLock()) {
                try {
                    execute(executionContext);
                } finally {
                    if (scheduleLocker.isLocked()) {
                        scheduleLocker.unlock();
                    }
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("Task %s.%s (%s) - %s has been running, Skipped.", executionContext.getGroup(), executionContext.getId(), executionContext.getName(), this.getClass().getName()));
            }
        } else {
            execute(executionContext);
        }
    }
}
