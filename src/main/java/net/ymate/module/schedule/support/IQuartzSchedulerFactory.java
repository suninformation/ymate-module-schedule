/*
 * Copyright 2007-2025 the original author or authors.
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

import net.ymate.module.schedule.IScheduler;
import net.ymate.platform.core.support.IInitialization;
import org.quartz.Scheduler;

/**
 * @author 刘镇 (suninformation@163.com) on 2025/1/2 18:32
 * @since 1.0.2
 */
public interface IQuartzSchedulerFactory extends IInitialization<IScheduler> {

    IScheduler getOwner();

    Scheduler getScheduler();
}
