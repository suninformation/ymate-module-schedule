/*
 * Copyright 2007-2017 the original author or authors.
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
package net.ymate.module.scheduler;

import net.ymate.module.scheduler.impl.DefaultTaskExecutionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/4 下午3:23
 * @version 1.0
 */
public abstract class AbstractScheduleTask implements IScheduleTask {

    private static final Log _LOG = LogFactory.getLog(AbstractScheduleTask.class);

    private final IScheduleLocker __locker;

    private boolean __sync;

    public AbstractScheduleTask() {
        this(false);
    }

    public AbstractScheduleTask(boolean sync) {
        __sync = sync;
        if (__sync) {
            __locker = Scheduler.get().getModuleCfg().getScheduleLockerFactory().getScheduleLocker(getClass().getName());
            if (__locker == null) {
                throw new IllegalStateException("Can not create schedule locker for '" + getClass().getName() + "'");
            }
        } else {
            __locker = null;
        }
    }

    @Override
    public boolean isSync() {
        return __sync;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String _id = context.getJobDetail().getJobDataMap().getString("__ID");
        String _name = context.getJobDetail().getJobDataMap().getString("__TASK");
        //
        if (__sync) {
            if (__locker.tryLock()) {
                try {
                    __doExecute(new DefaultTaskExecutionContext(_id, _name, context));
                } finally {
                    if (__locker.isLocked()) {
                        __locker.unlock();
                    }
                }
            } else {
                _LOG.warn("Task " + _name + " - [" + this.getClass().getName() + "] has been running, Skipped.");
            }
        } else {
            __doExecute(new DefaultTaskExecutionContext(_id, _name, context));
        }
    }

    /**
     * 执行计划任务
     *
     * @param context 任务执行上下文环境对象
     */
    protected abstract void __doExecute(ITaskExecutionContext context);
}
