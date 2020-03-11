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
import net.ymate.module.schedule.annotation.ScheduleConf;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/11 13:11
 */
public final class DefaultSchedulerConfig implements ISchedulerConfig {

    private boolean enabled = true;

    private IScheduleLockerFactory scheduleLockerFactory;

    private IScheduleProvider scheduleProvider;

    private ITaskConfigLoader taskConfigLoader;

    private boolean initialized;

    public static DefaultSchedulerConfig defaultConfig() {
        return builder().build();
    }

    public static DefaultSchedulerConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultSchedulerConfig(null, moduleConfigurer);
    }

    public static DefaultSchedulerConfig create(Class<?> mainClass, IModuleConfigurer moduleConfigurer) {
        return new DefaultSchedulerConfig(mainClass, moduleConfigurer);
    }

    public static Builder builder() {
        return new Builder();
    }

    private DefaultSchedulerConfig() {
    }

    private DefaultSchedulerConfig(Class<?> mainClass, IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        //
        ScheduleConf confAnn = mainClass == null ? null : mainClass.getAnnotation(ScheduleConf.class);
        //
        enabled = configReader.getBoolean(ENABLED, confAnn != null && confAnn.enabled());
        //
        if (enabled) {
            scheduleLockerFactory = configReader.getClassImpl(LOCKER_FACTORY_CLASS, confAnn == null || confAnn.lockerFactoryClass().equals(IScheduleLockerFactory.class) ? null : confAnn.lockerFactoryClass().getName(), IScheduleLockerFactory.class);
            scheduleProvider = configReader.getClassImpl(PROVIDER_CLASS, confAnn == null || confAnn.providerClass().equals(IScheduleProvider.class) ? null : confAnn.providerClass().getName(), IScheduleProvider.class);
            taskConfigLoader = configReader.getClassImpl(TASK_CONFIG_LOADER_CLASS, confAnn == null || confAnn.taskConfigLoaderClass().equals(ITaskConfigLoader.class) ? null : confAnn.taskConfigLoaderClass().getName(), ITaskConfigLoader.class);
        }
    }

    @Override
    public void initialize(IScheduler owner) throws Exception {
        if (!initialized) {
            if (enabled) {
                if (scheduleLockerFactory == null) {
                    scheduleLockerFactory = new DefaultScheduleLockerFactory();
                }
                if (scheduleProvider == null) {
                    scheduleProvider = new DefaultScheduleProvider();
                }
                if (taskConfigLoader == null) {
                    taskConfigLoader = new DefaultTaskConfigLoader(owner);
                }
            }
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (!initialized) {
            this.enabled = enabled;
        }
    }

    @Override
    public IScheduleLockerFactory getScheduleLockerFactory() {
        return scheduleLockerFactory;
    }

    public void setScheduleLockerFactory(IScheduleLockerFactory scheduleLockerFactory) {
        if (!initialized) {
            this.scheduleLockerFactory = scheduleLockerFactory;
        }
    }

    @Override
    public IScheduleProvider getScheduleProvider() {
        return scheduleProvider;
    }

    public void setScheduleProvider(IScheduleProvider scheduleProvider) {
        if (!initialized) {
            this.scheduleProvider = scheduleProvider;
        }
    }

    @Override
    public ITaskConfigLoader getTaskConfigLoader() {
        return taskConfigLoader;
    }

    public void setTaskConfigLoader(ITaskConfigLoader taskConfigLoader) {
        if (!initialized) {
            this.taskConfigLoader = taskConfigLoader;
        }
    }

    public static final class Builder {

        private final DefaultSchedulerConfig config = new DefaultSchedulerConfig();

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            config.setEnabled(enabled);
            return this;
        }

        public Builder scheduleLockerFactory(IScheduleLockerFactory scheduleLockerFactory) {
            config.setScheduleLockerFactory(scheduleLockerFactory);
            return this;
        }

        public Builder scheduleProvider(IScheduleProvider scheduleProvider) {
            config.setScheduleProvider(scheduleProvider);
            return this;
        }

        public Builder taskConfigLoader(ITaskConfigLoader taskConfigLoader) {
            config.setTaskConfigLoader(taskConfigLoader);
            return this;
        }

        public DefaultSchedulerConfig build() {
            return config;
        }
    }
}