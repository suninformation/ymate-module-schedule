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

import net.ymate.module.scheduler.IScheduleLocker;
import net.ymate.platform.core.support.ReentrantLockHelper;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/3 下午4:50
 * @version 1.0
 */
public class DefaultScheduleLocker implements IScheduleLocker {

    private final ReentrantLock __locker;

    public DefaultScheduleLocker(String lockerName) {
        if (StringUtils.isBlank(lockerName)) {
            throw new NullArgumentException("lockerName");
        }
        __locker = ReentrantLockHelper.DEFAULT.getLocker(lockerName);
    }

    @Override
    public void lock() {
        __locker.lock();
    }

    @Override
    public boolean tryLock() {
        return __locker.tryLock();
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return __locker.tryLock(timeout, unit);
    }

    @Override
    public boolean isLocked() {
        return __locker.isLocked();
    }

    @Override
    public void unlock() {
        __locker.unlock();
    }
}
