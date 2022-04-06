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
package net.ymate.module.schedule.impl;

import net.ymate.module.schedule.*;
import net.ymate.module.schedule.support.QuartzScheduleHelper;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/05/11 11:34
 */
public class DefaultScheduleProvider implements IScheduleProvider {

    private static final Log LOG = LogFactory.getLog(DefaultScheduleProvider.class);

    private QuartzScheduleHelper quartzScheduleHelper;

    private IScheduler owner;

    private boolean initialized;

    public DefaultScheduleProvider() throws Exception {
    }

    @Override
    public void initialize(IScheduler owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            this.quartzScheduleHelper = QuartzScheduleHelper.bind(owner, StdSchedulerFactory.getDefaultScheduler());
            this.initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public IScheduler getOwner() {
        return owner;
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    @Override
    public void start() throws Exception {
        if (!quartzScheduleHelper.getScheduler().isStarted()) {
            quartzScheduleHelper.getScheduler().start();
        }
    }

    @Override
    public void shutdown() throws Exception {
        try {
            if (!quartzScheduleHelper.isShutdown()) {
                quartzScheduleHelper.shutdown(true);
            }
        } catch (org.quartz.SchedulerException e) {
            try {
                if (!quartzScheduleHelper.isShutdown()) {
                    quartzScheduleHelper.shutdown();
                }
            } catch (SchedulerException ex) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("An exception occurred while shutdown scheduler: ", RuntimeUtils.unwrapThrow(ex));
                }
            }
        }
    }

    @Override
    public boolean addTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        try {
            return quartzScheduleHelper.addTask(config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(String id, String cron) throws SchedulerException {
        try {
            quartzScheduleHelper.updateTask(id, cron);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(ITaskConfig config) throws SchedulerException {
        try {
            quartzScheduleHelper.updateTask(config);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id) throws SchedulerException {
        try {
            return quartzScheduleHelper.hasTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id, String group) throws SchedulerException {
        try {
            return quartzScheduleHelper.hasTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public List<String> getGroupNames() throws SchedulerException {
        try {
            return quartzScheduleHelper.getJobGroupNames();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void addOrUpdateTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        try {
            quartzScheduleHelper.addOrUpdateTask(config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id) throws SchedulerException {
        try {
            quartzScheduleHelper.pauseTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id, String group) throws SchedulerException {
        try {
            quartzScheduleHelper.pauseTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTaskGroup(String group) throws SchedulerException {
        try {
            quartzScheduleHelper.pauseTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id) throws SchedulerException {
        try {
            quartzScheduleHelper.resumeTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id, String group) throws SchedulerException {
        try {
            quartzScheduleHelper.resumeTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTaskGroup(String group) throws SchedulerException {
        try {
            quartzScheduleHelper.resumeTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id) throws SchedulerException {
        try {
            quartzScheduleHelper.deleteTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id, String group) throws SchedulerException {
        try {
            quartzScheduleHelper.deleteTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTaskGroup(String group) throws SchedulerException {
        try {
            quartzScheduleHelper.deleteTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id) throws SchedulerException {
        try {
            quartzScheduleHelper.triggerTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id, String group) throws SchedulerException {
        try {
            quartzScheduleHelper.triggerTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseAll() throws SchedulerException {
        try {
            quartzScheduleHelper.pauseAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        try {
            quartzScheduleHelper.resumeAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }
}
