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

import net.ymate.module.schedule.annotation.*;
import net.ymate.module.schedule.handle.ScheduleTaskHandler;
import net.ymate.module.schedule.handle.TaskConfigLoaderHandler;
import net.ymate.module.schedule.handle.TaskConfigsHandler;
import net.ymate.module.schedule.impl.DefaultSchedulerConfig;
import net.ymate.module.schedule.impl.DefaultTaskConfigLoader;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.core.*;
import net.ymate.platform.core.beans.IBeanLoadFactory;
import net.ymate.platform.core.beans.IBeanLoader;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/11 13:11
 */
public final class Scheduler implements IModule, IScheduler {

    private static final Log LOG = LogFactory.getLog(Scheduler.class);

    private static volatile IScheduler instance;

    private IApplication owner;

    private ISchedulerConfig config;

    private Map<String, ScheduleTaskMeta> scheduleTaskMetas;

    private boolean initialized;

    public static IScheduler get() {
        IScheduler inst = instance;
        if (inst == null) {
            synchronized (Scheduler.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Scheduler.class);
                }
            }
        }
        return inst;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize(IApplication owner) throws Exception {
        if (!initialized) {
            //
            YMP.showVersion("Initializing ymate-module-schedule-${version}", new Version(1, 0, 0, Scheduler.class, Version.VersionType.Release));
            //
            this.owner = owner;
            IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
            IApplicationConfigurer configurer = null;
            if (configureFactory != null) {
                configurer = configureFactory.getConfigurer();
                IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                if (moduleConfigurer != null) {
                    config = DefaultSchedulerConfig.create(configureFactory.getMainClass(), moduleConfigurer);
                } else {
                    config = DefaultSchedulerConfig.create(configureFactory.getMainClass(), DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                }
            }
            if (config == null) {
                config = DefaultSchedulerConfig.defaultConfig();
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }
            if (config.isEnabled()) {
                scheduleTaskMetas = new ConcurrentHashMap<>(16);
                if (configurer != null) {
                    IBeanLoadFactory beanLoaderFactory = configurer.getBeanLoadFactory();
                    if (beanLoaderFactory != null) {
                        IBeanLoader beanLoader = beanLoaderFactory.getBeanLoader();
                        if (beanLoader != null) {
                            beanLoader.registerHandler(ScheduleTask.class, new ScheduleTaskHandler(this));
                            if (config.getTaskConfigLoader() instanceof DefaultTaskConfigLoader) {
                                beanLoader.registerHandler(TaskConfigLoader.class, new TaskConfigLoaderHandler(this));
                                //
                                TaskConfigsHandler taskConfigsHandler = new TaskConfigsHandler(this);
                                beanLoader.registerHandler(TaskConfigGroups.class, taskConfigsHandler);
                                beanLoader.registerHandler(TaskConfigs.class, taskConfigsHandler);
                                beanLoader.registerHandler(TaskConfig.class, taskConfigsHandler);
                            }
                        }
                    }
                }
                owner.getEvents().registerEvent(ScheduleEvent.class)
                        .registerListener(Events.MODE.ASYNC, ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
                            if (context.getEventName() == ApplicationEvent.EVENT.APPLICATION_INITIALIZED) {
                                doTasksStartup();
                            }
                            return false;
                        });
            }
            initialized = true;
        }
    }

    @SuppressWarnings("unchecked")
    private void doTasksStartup() {
        Collection<ITaskConfig> taskConfigs = config.getTaskConfigLoader().loadConfigs();
        if (!taskConfigs.isEmpty()) {
            try {
                for (ITaskConfig taskConfig : taskConfigs) {
                    Class<? extends IScheduleTask> taskClass = null;
                    ScheduleTaskMeta taskMeta = scheduleTaskMetas.get(taskConfig.getName());
                    if (taskMeta != null) {
                        taskClass = taskMeta.getTaskClass();
                    } else {
                        try {
                            taskClass = (Class<? extends IScheduleTask>) ClassUtils.loadClass(taskConfig.getName(), getClass());
                        } catch (ClassNotFoundException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn(String.format("Task class '%s' could not be found, load failed.", taskConfig.getName()));
                            }
                        }
                    }
                    if (taskClass != null) {
                        try {
                            config.getScheduleProvider().addTask(taskConfig, taskClass);
                        } catch (SchedulerException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn(String.format("An exception occurred when adding task '%s.%s (%s) - %s':", StringUtils.defaultIfBlank(taskConfig.getGroup(), ITaskConfig.DEFAULT_GROUP), taskConfig.getId(), taskConfig.getName(), taskClass.getName()), RuntimeUtils.unwrapThrow(e));
                            }
                        }
                    }
                }
                owner.getEvents().fireEvent(new ScheduleEvent(this, ScheduleEvent.EVENT.SCHEDULE_INITIALIZED));
                config.getScheduleProvider().start();
                owner.getEvents().fireEvent(new ScheduleEvent(this, ScheduleEvent.EVENT.SCHEDULE_STARTED));
            } catch (Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(String.format("An exception occurred while starting the scheduled task service '%s': ", config.getScheduleProvider().getClass().getName()), RuntimeUtils.unwrapThrow(e));
                }
            }
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void close() throws Exception {
        if (initialized) {
            owner.getEvents().fireEvent(new ScheduleEvent(this, ScheduleEvent.EVENT.SCHEDULE_SHUTDOWN));
            //
            initialized = false;
            //
            if (config.isEnabled()) {
                config.getScheduleProvider().shutdown();
                scheduleTaskMetas = null;
            }
            //
            config = null;
            owner = null;
        }
    }

    @Override
    public IApplication getOwner() {
        return owner;
    }

    @Override
    public ISchedulerConfig getConfig() {
        return config;
    }

    @Override
    public void registerTask(Class<? extends IScheduleTask> targetClass) throws Exception {
        ScheduleTask scheduleTaskAnn = targetClass.getAnnotation(ScheduleTask.class);
        if (scheduleTaskAnn != null) {
            ScheduleTaskMeta taskMeta = new ScheduleTaskMeta(scheduleTaskAnn.name(), scheduleTaskAnn.description(), targetClass);
            scheduleTaskMetas.put(taskMeta.getName(), taskMeta);
        }
    }
}
