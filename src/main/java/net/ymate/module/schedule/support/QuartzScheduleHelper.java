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
package net.ymate.module.schedule.support;

import net.ymate.module.schedule.IScheduleTask;
import net.ymate.module.schedule.ITaskConfig;
import net.ymate.module.schedule.impl.DefaultTaskConfig;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author 刘镇 (suninformation@163.com) on 2016/05/06 03:00
 */
public final class QuartzScheduleHelper {

    private static final Log LOG = LogFactory.getLog(QuartzScheduleHelper.class);

    public static final String TASK_NAME = "__TASK_NAME";

    private final Scheduler scheduler;

    public static QuartzScheduleHelper bind(Scheduler scheduler) {
        return new QuartzScheduleHelper(scheduler);
    }

    private QuartzScheduleHelper(Scheduler scheduler) {
        if (scheduler == null) {
            throw new NullArgumentException("scheduler");
        }
        this.scheduler = scheduler;
    }

    private JobDataMap doBuildDataMap(ITaskConfig config) {
        JobDataMap dataMap = new JobDataMap();
        if (!config.getParams().isEmpty()) {
            dataMap.putAll(config.getParams());
        }
        dataMap.put(TASK_NAME, config.getName());
        //
        return dataMap;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * 添加计划任务
     *
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @return 是否完成添加动作
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public boolean addTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        CronTrigger trigger = getCronTrigger(config);
        if (trigger == null) {
            TriggerKey triggerKey = TriggerKey.triggerKey(config.getId(), config.getGroup());
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();
            //
            scheduler.scheduleJob(JobBuilder.newJob(taskClass)
                    .withIdentity(triggerKey.getName(), triggerKey.getGroup())
                    .setJobData(doBuildDataMap(config)).build(), trigger);
            //
            if (config.getJobListeners() != null) {
                for (JobListener listener : config.getJobListeners()) {
                    scheduler.getListenerManager().addJobListener(listener);
                }
            }
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Add task %s.%s (%s) - %s with cron: %s", triggerKey.getGroup(), triggerKey.getName(), config.getName(), taskClass.getName(), config.getCron()));
            }
            return true;
        }
        return false;
    }

    /**
     * 更新任务规则
     *
     * @param id   任务ID
     * @param cron 任务规则
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void updateTask(String id, String cron) throws SchedulerException {
        updateTask(DefaultTaskConfig.builder().id(id).cron(cron).build());
    }

    public void updateTask(ITaskConfig config) throws SchedulerException {
        CronTrigger trigger = getCronTrigger(config);
        if (trigger != null) {
            TriggerKey triggerKey = trigger.getKey();
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.getCron())).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Update task %s.%s with cron: %s", triggerKey.getGroup(), triggerKey.getName(), config.getCron()));
            }
        }
    }

    public CronTrigger getCronTrigger(ITaskConfig config) throws SchedulerException {
        return getCronTrigger(config.getId(), config.getGroup());
    }

    public CronTrigger getCronTrigger(String id, String group) throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(StringUtils.isBlank(group) ? GroupMatcher.anyGroup() : GroupMatcher.groupEquals(group));
        for (TriggerKey triggerKey : triggerKeys) {
            if (triggerKey.getName().equals(id)) {
                return (CronTrigger) scheduler.getTrigger(triggerKey);
            }
        }
        return null;
    }

    public JobKey getJobKey(String id, String group) throws SchedulerException {
        return getJobKeys(group).stream().filter(jobKey -> jobKey.getName().equals(id)).findFirst().orElse(null);
    }

    public Set<JobKey> getJobKeys(String group) throws SchedulerException {
        return scheduler.getJobKeys(StringUtils.isBlank(group) ? GroupMatcher.anyGroup() : GroupMatcher.groupEquals(group));
    }

    public List<String> getJobGroupNames() throws SchedulerException {
        return Collections.unmodifiableList(scheduler.getJobGroupNames());
    }

    public List<String> getTriggerGroupNames() throws SchedulerException {
        return Collections.unmodifiableList(scheduler.getTriggerGroupNames());
    }

    /**
     * @param id 任务ID
     * @return 判断指定id的计划任务是否存在
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public boolean hasTask(String id) throws SchedulerException {
        return getCronTrigger(id, null) != null;
    }

    public boolean hasTask(String id, String group) throws SchedulerException {
        return getCronTrigger(id, group) != null;
    }

    /**
     * 添加或更新Job
     *
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void addOrUpdateTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        if (!addTask(config, taskClass)) {
            updateTask(config);
        }
    }

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void pauseTask(String id) throws SchedulerException {
        pauseTask(id, null);
    }

    public void pauseTask(String id, String group) throws SchedulerException {
        JobKey jobKey = getJobKey(id, group);
        if (jobKey != null) {
            this.scheduler.pauseJob(jobKey);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Pause task %s.%s", jobKey.getGroup(), jobKey.getName()));
            }
        }
    }

    public void pauseTaskGroup(String group) throws SchedulerException {
        if (StringUtils.isNotBlank(group)) {
            this.scheduler.pauseJobs(GroupMatcher.groupEquals(group));
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Pause task group %s", group));
            }
        }
    }

    /**
     * 恢复任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void resumeTask(String id) throws SchedulerException {
        resumeTask(id, null);
    }

    public void resumeTask(String id, String group) throws SchedulerException {
        JobKey jobKey = getJobKey(id, group);
        if (jobKey != null) {
            this.scheduler.resumeJob(jobKey);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Resume task %s.%s", jobKey.getGroup(), jobKey.getName()));
            }
        }
    }

    public void resumeTaskGroup(String group) throws SchedulerException {
        if (StringUtils.isNotBlank(group)) {
            this.scheduler.resumeJobs(GroupMatcher.groupEquals(group));
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Resume task group %s", group));
            }
        }
    }

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void deleteTask(String id) throws SchedulerException {
        deleteTask(id, null);
    }

    public void deleteTask(String id, String group) throws SchedulerException {
        JobKey jobKey = getJobKey(id, group);
        if (jobKey != null) {
            this.scheduler.deleteJob(jobKey);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Delete task %s.%s", jobKey.getGroup(), jobKey.getName()));
            }
        }
    }

    public void deleteTaskGroup(String group) throws SchedulerException {
        if (StringUtils.isNotBlank(group)) {
            List<JobKey> jobKeys = new ArrayList<>(getJobKeys(group));
            this.scheduler.deleteJobs(jobKeys);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Delete task group %s - keys: %s", group, jobKeys));
            }
        }
    }

    /**
     * 执行任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    public void triggerTask(String id) throws SchedulerException {
        triggerTask(id, null);
    }

    public void triggerTask(String id, String group) throws SchedulerException {
        JobKey jobKey = getJobKey(id, group);
        if (jobKey != null) {
            this.scheduler.triggerJob(jobKey);
            //
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Trigger task %s.%s", jobKey.getGroup(), jobKey.getName()));
            }
        }
    }

    /**
     * 暂停所以的计划执行的任务
     */
    public void pauseAll() throws SchedulerException {
        this.scheduler.pauseAll();
        //
        if (LOG.isInfoEnabled()) {
            LOG.info("Pause all tasks");
        }
    }

    /**
     * 恢复所有被暂停的计划任务
     */
    public void resumeAll() throws SchedulerException {
        this.scheduler.resumeAll();
        //
        if (LOG.isInfoEnabled()) {
            LOG.info("Resume all tasks");
        }
    }

    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
        this.scheduler.shutdown(waitForJobsToComplete);
    }

    public boolean isShutdown() throws SchedulerException {
        return this.scheduler.isShutdown();
    }

    public void shutdown() throws SchedulerException {
        this.scheduler.shutdown();
    }
}
