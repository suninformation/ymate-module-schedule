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

import net.ymate.platform.core.event.AbstractEventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/17 22:25
 */
public class ScheduleEvent extends AbstractEventContext<IScheduler, ScheduleEvent.EVENT> implements IEvent {

    private static final long serialVersionUID = 1L;

    /**
     * 调度服务事件枚举
     */
    public enum EVENT {

        /**
         * 调度服务初始化事件
         */
        SCHEDULE_INITIALIZED,

        /**
         * 调度服务已启动事件
         */
        SCHEDULE_STARTED,

        /**
         * 调度服务停止事件
         */
        SCHEDULE_SHUTDOWN,

        // --

        SCHEDULE_STARTING,

        SCHEDULE_SHUTTING_DOWN,

        SCHEDULER_ERROR,

        SCHEDULER_IN_STANDBY_MODE,

        SCHEDULING_DATA_CLEARED,

        // ---

        /**
         * 即将执行任务事件
         */
        TASK_TO_BE_EXECUTED,

        /**
         * 任务执行被否决事件
         */
        TASK_EXECUTION_VETOED,

        /**
         * 任务被执行事件
         */
        TASK_WAS_EXECUTED,

        // ---

        TASK_SCHEDULED,

        TASK_UNSCHEDULED,

        TASK_ADDED,

        TASK_DELETED,

        TASK_PAUSED,

        TASK_GROUP_PAUSED,

        TASK_RESUMED,

        TASK_GROUP_RESUMED,

        TRIGGER_FINALIZED,

        TRIGGER_PAUSED,

        TRIGGER_GROUP_PAUSED,

        TRIGGER_RESUMED,

        TRIGGER_GROUP_RESUMED,

        // ---

        TASK_VETO_EXECUTION,

        TRIGGER_FIRED,

        TRIGGER_MISFIRED,

        TRIGGER_COMPLETE
    }

    public ScheduleEvent(IScheduler owner, ScheduleEvent.EVENT eventName) {
        super(owner, ScheduleEvent.class, eventName);
    }
}
