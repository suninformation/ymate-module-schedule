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

import net.ymate.module.schedule.IScheduler;
import net.ymate.module.schedule.ISchedulerConfig;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurable;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/02/07 21:02
 */
public class DefaultSchedulerConfigurable extends DefaultModuleConfigurable {

    public static Builder builder() {
        return new Builder();
    }

    private DefaultSchedulerConfigurable() {
        super(IScheduler.MODULE_NAME);
    }

    public static final class Builder {

        private final DefaultSchedulerConfigurable configurable = new DefaultSchedulerConfigurable();

        private Builder() {
        }

        public Builder enabled(boolean enabled) {
            configurable.addConfig(ISchedulerConfig.ENABLED, String.valueOf(enabled));
            return this;
        }

        public Builder lockerFactoryClass(String lockerFactoryClass) {
            configurable.addConfig(ISchedulerConfig.LOCKER_FACTORY_CLASS, lockerFactoryClass);
            return this;
        }

        public Builder providerClass(String providerClass) {
            configurable.addConfig(ISchedulerConfig.PROVIDER_CLASS, providerClass);
            return this;
        }

        public Builder taskConfigLoaderClass(String taskConfigLoaderClass) {
            configurable.addConfig(ISchedulerConfig.TASK_CONFIG_LOADER_CLASS, taskConfigLoaderClass);
            return this;
        }

        public IModuleConfigurer build() {
            return configurable.toModuleConfigurer();
        }
    }
}