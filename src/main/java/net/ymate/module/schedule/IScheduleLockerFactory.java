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
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;

/**
 * 调度锁工厂接口定义
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/03 01:10
 */
@Ignored
public interface IScheduleLockerFactory extends IInitialization<IScheduler>, IDestroyable {

    /**
     * 获取调度锁接口实现类对象
     *
     * @param lockerName 锁名称
     * @return 返回调度锁接口实现类对象
     */
    IScheduleLocker getScheduleLocker(String lockerName);
}
