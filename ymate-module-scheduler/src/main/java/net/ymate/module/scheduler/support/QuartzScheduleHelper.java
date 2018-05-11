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
package net.ymate.module.scheduler.support;

import net.ymate.module.scheduler.IScheduleTask;
import net.ymate.module.scheduler.ITaskConfig;
import net.ymate.module.scheduler.impl.DefaultTaskConfig;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;

/**
 * @author 刘镇 (suninformation@163.com) on 16/5/6 上午3:00
 * @version 1.0
 */
public final class QuartzScheduleHelper {

    private static final String __ID = "__id";

    private static final String __TASK = "__task";

    private static final String __PREFIX_GROUP = "GROUP_";

    private static final String __PREFIX_TASK = "TASK_";

    private final Scheduler __scheduler;

    public static QuartzScheduleHelper bind(Scheduler scheduler) {
        return new QuartzScheduleHelper(scheduler);
    }

    private QuartzScheduleHelper(Scheduler scheduler) {
        if (scheduler == null) {
            throw new NullArgumentException("scheduler");
        }
        __scheduler = scheduler;
    }

    private String __buildName(String taskId, ITaskConfig config, boolean group) {
        if (group) {
            if (config == null || StringUtils.isBlank(config.getGroup())) {
                return __PREFIX_GROUP.concat(taskId);
            }
            return config.getGroup();
        }
        if (config == null || StringUtils.isBlank(config.getName())) {
            return __PREFIX_TASK.concat(taskId);
        }
        return config.getName();
    }

    private JobDataMap __buildDataMap(String taskId, ITaskConfig config) {
        JobDataMap _dataMap = new JobDataMap();
        if (config.getParams() != null) {
            _dataMap.putAll(config.getParams());
        }
        _dataMap.put(__TASK, config.getName());
        _dataMap.put(__ID, taskId);
        //
        return _dataMap;
    }

    public Scheduler getScheduler() {
        return __scheduler;
    }

    /**
     * 添加计划任务
     *
     * @param id        任务ID
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @return 是否完成添加动作
     * @throws SchedulerException 可能产生的任何异常
     */
    public boolean addTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        CronTrigger _trigger = getCronTrigger(id, config);
        if (_trigger == null) {
            TriggerKey _triggerKey = TriggerKey.triggerKey(__buildName(id, config, false), __buildName(id, config, true));
            _trigger = TriggerBuilder.newTrigger().withIdentity(_triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();
            //
            __scheduler.scheduleJob(JobBuilder.newJob(taskClass).withIdentity(_triggerKey.getName(), _triggerKey.getGroup()).setJobData(__buildDataMap(id, config)).build(), _trigger);
            //
            if (config.getJobListeners() != null) {
                for (JobListener _listener : config.getJobListeners()) {
                    __scheduler.getListenerManager().addJobListener(_listener);
                }
            }
            //
            return true;
        }
        return false;
    }

    /**
     * 更新任务规则
     *
     * @param id   任务ID
     * @param cron 任务规则
     * @throws SchedulerException 可能产生的任何异常
     */
    public void updateTask(String id, String cron) throws SchedulerException {
        updateTask(id, new DefaultTaskConfig(null, null, cron));
    }

    public void updateTask(String id, ITaskConfig config) throws SchedulerException {
        CronTrigger _trigger = getCronTrigger(id, config);
        if (_trigger != null) {
            _trigger = _trigger.getTriggerBuilder()
                    .withIdentity(TriggerKey.triggerKey(__buildName(id, config, false), __buildName(id, config, true)))
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();
            __scheduler.rescheduleJob(_trigger.getKey(), _trigger);
        }
    }

    public CronTrigger getCronTrigger(String id, ITaskConfig config) throws SchedulerException {
        return (CronTrigger) __scheduler.getTrigger(TriggerKey.triggerKey(__buildName(id, config, false), __buildName(id, config, true)));
    }

    /**
     * @param id 任务ID
     * @return 判断指定id的计划任务是否存在
     * @throws SchedulerException 可能产生的任何异常
     */
    public boolean hasTask(String id) throws SchedulerException {
        return hasTask(id, null, null);
    }

    public boolean hasTask(String id, String name, String group) throws SchedulerException {
        return getCronTrigger(id, new DefaultTaskConfig(name, group)) != null;
    }

    /**
     * 添加或更新Job
     *
     * @param id        任务ID
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @throws SchedulerException 可能产生的任何异常
     */
    public void addOrUpdateTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        if (!addTask(id, config, taskClass)) {
            updateTask(id, config);
        }
    }

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    public void pauseTask(String id) throws SchedulerException {
        pauseTask(id, null, null);
    }

    public void pauseTask(String id, String name, String group) throws SchedulerException {
        ITaskConfig _config = new DefaultTaskConfig(name, group);
        __scheduler.pauseJob(JobKey.jobKey(__buildName(id, _config, false), __buildName(id, _config, true)));
    }

    /**
     * 恢复任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    public void resumeTask(String id) throws SchedulerException {
        resumeTask(id, null, null);
    }

    public void resumeTask(String id, String name, String group) throws SchedulerException {
        ITaskConfig _config = new DefaultTaskConfig(name, group);
        __scheduler.resumeJob(JobKey.jobKey(__buildName(id, _config, false), __buildName(id, _config, true)));
    }

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    public void deleteTask(String id) throws SchedulerException {
        deleteTask(id, null, null);
    }

    public void deleteTask(String id, String name, String group) throws SchedulerException {
        ITaskConfig _config = new DefaultTaskConfig(name, group);
        __scheduler.deleteJob(JobKey.jobKey(__buildName(id, _config, false), __buildName(id, _config, true)));
    }

    /**
     * 执行任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    public void triggerTask(String id) throws SchedulerException {
        triggerTask(id, null, null);
    }

    public void triggerTask(String id, String name, String group) throws SchedulerException {
        ITaskConfig _config = new DefaultTaskConfig(name, group);
        __scheduler.triggerJob(JobKey.jobKey(__buildName(id, _config, false), __buildName(id, _config, true)));
    }

    /**
     * 暂停所以的计划执行的任务
     */
    public void pauseAll() throws SchedulerException {
        this.__scheduler.pauseAll();
    }

    /**
     * 恢复所有被暂停的计划任务
     */
    public void resumeAll() throws SchedulerException {
        this.__scheduler.resumeAll();
    }

    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
        this.__scheduler.shutdown(waitForJobsToComplete);
    }

    public boolean isShutdown() throws SchedulerException {
        return this.__scheduler.isShutdown();
    }

    public void shutdown() throws SchedulerException {
        this.__scheduler.shutdown();
    }
}
