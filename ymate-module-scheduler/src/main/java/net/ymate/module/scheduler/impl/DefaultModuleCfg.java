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
package net.ymate.module.scheduler.impl;

import net.ymate.module.scheduler.IScheduleLockerFactory;
import net.ymate.module.scheduler.IScheduler;
import net.ymate.module.scheduler.ISchedulerModuleCfg;
import net.ymate.module.scheduler.ITaskConfigLoader;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.util.ClassUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/25 上午 02:33
 * @version 1.0
 */
public class DefaultModuleCfg implements ISchedulerModuleCfg {

    private IScheduleLockerFactory __scheduleLockerFactory;

    private ITaskConfigLoader __taskConfigLoader;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IScheduler.MODULE_NAME);
        //
        __scheduleLockerFactory = ClassUtils.impl(_moduleCfgs.get("schedule_locker_factory_class"), IScheduleLockerFactory.class, getClass());
        if (__scheduleLockerFactory == null) {
            __scheduleLockerFactory = new DefaultScheduleLockerFactory();
        }
        //
        __taskConfigLoader = ClassUtils.impl(_moduleCfgs.get("task_config_loader_class"), ITaskConfigLoader.class, getClass());
    }

    @Override
    public IScheduleLockerFactory getScheduleLockerFactory() {
        return __scheduleLockerFactory;
    }

    @Override
    public ITaskConfigLoader getTaskConfigLoader() {
        return __taskConfigLoader;
    }
}