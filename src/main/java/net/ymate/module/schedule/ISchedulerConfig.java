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

import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/11 13:11
 */
@Ignored
public interface ISchedulerConfig extends IInitialization<IScheduler> {

    String ENABLED = "enabled";

    String LOCKER_FACTORY_CLASS = "locker_factory_class";

    String PROVIDER_CLASS = "provider_class";

    String TASK_CONFIG_LOADER_CLASS = "task_config_loader_class";

    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 调度锁工厂接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleLockerFactory
     *
     * @return 返回调度锁工厂接口实例对象
     */
    IScheduleLockerFactory getScheduleLockerFactory();

    /**
     * 调度服务提供者接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleProvider
     *
     * @return 返回任务调度服务提供者接口实例对象
     */
    IScheduleProvider getScheduleProvider();

    /**
     * 计划任务规则配置加载器接口实例对象, 默认值: net.ymate.module.schedule.impl.DefaultTaskConfigLoader
     *
     * @return 返回计划任务规则配置加载器接口实例对象
     */
    ITaskConfigLoader getTaskConfigLoader();
}