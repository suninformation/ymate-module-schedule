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

import net.ymate.module.schedule.SchedulerException;
import net.ymate.module.schedule.*;
import net.ymate.module.schedule.support.IQuartzSchedulerFactory;
import net.ymate.module.schedule.support.QuartzScheduleHelper;
import net.ymate.module.schedule.support.impl.DefaultQuartzSchedulerFactory;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.*;

import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/05/11 11:34
 */
public class DefaultScheduleProvider implements IScheduleProvider, SchedulerListener, JobListener, TriggerListener {

    private static final Log LOG = LogFactory.getLog(DefaultScheduleProvider.class);

    private QuartzScheduleHelper scheduleHelper;

    private IScheduler owner;

    private boolean initialized;

    public DefaultScheduleProvider() throws Exception {
    }

    @Override
    public void initialize(IScheduler owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            IQuartzSchedulerFactory schedulerFactory = ClassUtils.loadClass(IQuartzSchedulerFactory.class, DefaultQuartzSchedulerFactory.class);
            schedulerFactory.initialize(owner);
            //
            Scheduler scheduler = schedulerFactory.getScheduler();
            ListenerManager listenerManager = scheduler.getListenerManager();
            listenerManager.addJobListener(this);
            listenerManager.addTriggerListener(this);
            scheduler.getContext().put(IScheduler.class.getName(), owner);
            //
            owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULE_INITIALIZED).setEventSource(scheduler));
            this.scheduleHelper = QuartzScheduleHelper.bind(scheduler);
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

    public QuartzScheduleHelper getScheduleHelper() {
        return scheduleHelper;
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    @Override
    public void start() throws Exception {
        if (!scheduleHelper.getScheduler().isStarted()) {
            scheduleHelper.getScheduler().start();
        }
    }

    @Override
    public void shutdown() throws Exception {
        try {
            if (!scheduleHelper.isShutdown()) {
                scheduleHelper.shutdown(true);
            }
        } catch (org.quartz.SchedulerException e) {
            try {
                if (!scheduleHelper.isShutdown()) {
                    scheduleHelper.shutdown();
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
            return scheduleHelper.addTask(config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(String id, String cron) throws SchedulerException {
        try {
            scheduleHelper.updateTask(id, cron);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void updateTask(ITaskConfig config) throws SchedulerException {
        try {
            scheduleHelper.updateTask(config);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id) throws SchedulerException {
        try {
            return scheduleHelper.hasTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public boolean hasTask(String id, String group) throws SchedulerException {
        try {
            return scheduleHelper.hasTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public List<String> getGroupNames() throws SchedulerException {
        try {
            return scheduleHelper.getJobGroupNames();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void addOrUpdateTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException {
        try {
            scheduleHelper.addOrUpdateTask(config, taskClass);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id) throws SchedulerException {
        try {
            scheduleHelper.pauseTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTask(String id, String group) throws SchedulerException {
        try {
            scheduleHelper.pauseTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseTaskGroup(String group) throws SchedulerException {
        try {
            scheduleHelper.pauseTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id) throws SchedulerException {
        try {
            scheduleHelper.resumeTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTask(String id, String group) throws SchedulerException {
        try {
            scheduleHelper.resumeTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeTaskGroup(String group) throws SchedulerException {
        try {
            scheduleHelper.resumeTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id) throws SchedulerException {
        try {
            scheduleHelper.deleteTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTask(String id, String group) throws SchedulerException {
        try {
            scheduleHelper.deleteTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void deleteTaskGroup(String group) throws SchedulerException {
        try {
            scheduleHelper.deleteTaskGroup(group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id) throws SchedulerException {
        try {
            scheduleHelper.triggerTask(id);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void triggerTask(String id, String group) throws SchedulerException {
        try {
            scheduleHelper.triggerTask(id, group);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void pauseAll() throws SchedulerException {
        try {
            scheduleHelper.pauseAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public void resumeAll() throws SchedulerException {
        try {
            scheduleHelper.resumeAll();
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerException(e);
        }
    }

    @Override
    public String getName() {
        return DefaultScheduleProvider.class.getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        owner.getOwner()
                .getEvents()
                .fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_TO_BE_EXECUTED)
                        .setEventSource(DefaultTaskExecutionContext.contextWrap(context)));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        owner.getOwner()
                .getEvents()
                .fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_EXECUTION_VETOED)
                        .setEventSource(DefaultTaskExecutionContext.contextWrap(context)));
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        ScheduleEvent scheduleEvent = new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_WAS_EXECUTED);
        scheduleEvent.setEventSource(DefaultTaskExecutionContext.contextWrap(context));
        if (jobException != null) {
            scheduleEvent.addParamExtend("jobException", jobException);
        }
        owner.getOwner().getEvents().fireEvent(scheduleEvent);
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_FIRED)
                .setEventSource(DefaultTaskExecutionContext.contextWrap(context))
                .addParamExtend("trigger", trigger));
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        try {
            owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_VETO_EXECUTION)
                    .setEventSource(DefaultTaskExecutionContext.contextWrap(context))
                    .addParamExtend("trigger", trigger));
        } catch (SchedulerException e) {
            return true;
        }
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_MISFIRED)
                .addParamExtend("trigger", trigger));
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_SCHEDULED)
                .setEventSource(DefaultTaskExecutionContext.contextWrap(context))
                .addParamExtend("code", triggerInstructionCode)
                .addParamExtend("trigger", trigger));
    }

    @Override
    public void jobScheduled(Trigger trigger) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_SCHEDULED)
                .addParamExtend("trigger", trigger));
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_UNSCHEDULED)
                .addParamExtend("triggerKey", triggerKey));
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_FINALIZED)
                .addParamExtend("trigger", trigger));
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_PAUSED)
                .addParamExtend("triggerKey", triggerKey));
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_GROUP_PAUSED)
                .addParamExtend("triggerGroup", triggerGroup));
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_RESUMED)
                .addParamExtend("triggerKey", triggerKey));
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TRIGGER_GROUP_RESUMED)
                .addParamExtend("triggerGroup", triggerGroup));
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_ADDED)
                .addParamExtend("jobDetail", jobDetail));
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_DELETED)
                .addParamExtend("jobKey", jobKey));
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_GROUP_PAUSED)
                .addParamExtend("jobKey", jobKey));
    }

    @Override
    public void jobsPaused(String jobGroup) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_GROUP_PAUSED)
                .addParamExtend("jobGroup", jobGroup));
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_RESUMED)
                .addParamExtend("jobKey", jobKey));
    }

    @Override
    public void jobsResumed(String jobGroup) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.TASK_GROUP_RESUMED)
                .addParamExtend("jobGroup", jobGroup));
    }

    @Override
    public void schedulerError(String msg, org.quartz.SchedulerException cause) {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULER_ERROR)
                .addParamExtend("msg", msg)
                .addParamExtend("cause", cause));
    }

    @Override
    public void schedulerInStandbyMode() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULER_IN_STANDBY_MODE));
    }

    @Override
    public void schedulerStarted() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULE_STARTED));
    }

    @Override
    public void schedulerStarting() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULE_STARTING));
    }

    @Override
    public void schedulerShutdown() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULE_SHUTDOWN));
    }

    @Override
    public void schedulerShuttingdown() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULE_SHUTTING_DOWN));
    }

    @Override
    public void schedulingDataCleared() {
        owner.getOwner().getEvents().fireEvent(new ScheduleEvent(owner, ScheduleEvent.EVENT.SCHEDULING_DATA_CLEARED));
    }
}
