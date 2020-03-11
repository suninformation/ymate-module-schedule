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
package net.ymate.module.schedule.annotation;

import net.ymate.module.schedule.IScheduleLockerFactory;
import net.ymate.module.schedule.IScheduleProvider;
import net.ymate.module.schedule.ITaskConfigLoader;

import java.lang.annotation.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/03/11 21:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduleConf {

    /**
     * @return 模块是否已启用, 默认值: true
     */
    boolean enabled();

    /**
     * @return 调度锁工厂接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleLockerFactory
     */
    Class<? extends IScheduleLockerFactory> lockerFactoryClass() default IScheduleLockerFactory.class;

    /**
     * @return 调度服务提供者接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleProvider
     */
    Class<? extends IScheduleProvider> providerClass() default IScheduleProvider.class;

    /**
     * @return 计划任务规则配置加载器接口实例对象, 默认值: net.ymate.module.schedule.impl.DefaultTaskConfigLoader
     */
    Class<? extends ITaskConfigLoader> taskConfigLoaderClass() default ITaskConfigLoader.class;
}
