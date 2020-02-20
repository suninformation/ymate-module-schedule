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

import java.util.concurrent.TimeUnit;

/**
 * 调度锁接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/03 12:44
 */
@Ignored
public interface IScheduleLocker {

    /**
     * 加锁, 等待直到获得锁
     */
    void lock();

    /**
     * 加锁, 若无法获得锁就放弃
     *
     * @return 返回true表示加锁成功
     */
    boolean tryLock();

    /**
     * 加锁, 若无法获得锁就放弃
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 返回true表示加锁成功
     * @throws InterruptedException 可能产生的中断异常
     */
    boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 判断是否已加锁
     *
     * @return 返回true表示已加锁
     */
    boolean isLocked();

    /**
     * 解锁
     */
    void unlock();
}
