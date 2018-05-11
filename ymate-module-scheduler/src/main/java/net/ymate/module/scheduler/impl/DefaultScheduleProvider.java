/*
 * Copyright 2007-2018 the original author or authors.
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
package net.ymate.module.scheduler.impl;

import net.ymate.module.scheduler.IScheduleProvider;
import net.ymate.module.scheduler.IScheduleTask;
import net.ymate.module.scheduler.ITaskConfig;
import net.ymate.module.scheduler.SchedulerException;
import net.ymate.module.scheduler.support.QuartzScheduleHelper;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/5/11 上午11:34
 * @version 1.0
 */
public class DefaultScheduleProvider implements IScheduleProvider {

    private static final Log _LOG = LogFactory.getLog(DefaultScheduleProvider.class);

    private QuartzScheduleHelper __scheduleHelper;

    @Override
    public void start() throws Exception {
        __scheduleHelper = QuartzScheduleHelper.bind(StdSchedulerFactory.getDefaultScheduler());
        __scheduleHelper.getScheduler().start();
    }

    @Override
    public void shutdown() throws Exception {
        try {
            if (!__scheduleHelper.isShutdown()) {
                __scheduleHelper.shutdown(true);
            }
        } catch (org.quartz.SchedulerException e) {
            try {
                if (!__scheduleHelper.isShutdown()) {
                    __scheduleHelper.shutdown();
                }
            } catch (SchedulerException ex) {
                _LOG.warn("An exception occurred while shutdown scheduler", RuntimeUtils.unwrapThrow(ex));
            }
        }
    }

    @Override
    public boolean addTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        try {
            return __scheduleHelper.addTask(id, config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(String id, String cron) throws SchedulerException {
        try {
            __scheduleHelper.updateTask(id, cron);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(String id, ITaskConfig config) throws SchedulerException {
        try {
            __scheduleHelper.updateTask(id, config);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id) throws SchedulerException {
        try {
            return __scheduleHelper.hasTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id, String name, String group) throws SchedulerException {
        try {
            return __scheduleHelper.hasTask(id, name, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void addOrUpdateTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        try {
            __scheduleHelper.addOrUpdateTask(id, config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id) throws SchedulerException {
        try {
            __scheduleHelper.pauseTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id, String name, String group) throws SchedulerException {
        try {
            __scheduleHelper.pauseTask(id, name, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id) throws SchedulerException {
        try {
            __scheduleHelper.resumeTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id, String name, String group) throws SchedulerException {
        try {
            __scheduleHelper.resumeTask(id, name, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id) throws SchedulerException {
        try {
            __scheduleHelper.deleteTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id, String name, String group) throws SchedulerException {
        try {
            __scheduleHelper.deleteTask(id, name, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id) throws SchedulerException {
        try {
            __scheduleHelper.triggerTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id, String name, String group) throws SchedulerException {
        try {
            __scheduleHelper.triggerTask(id, name, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseAll() throws SchedulerException {
        try {
            __scheduleHelper.pauseAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        try {
            __scheduleHelper.resumeAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }
}
