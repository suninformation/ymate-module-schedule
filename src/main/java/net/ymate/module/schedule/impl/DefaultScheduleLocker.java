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

import net.ymate.module.schedule.IScheduleLocker;
import net.ymate.module.schedule.SchedulerException;
import net.ymate.platform.commons.ReentrantLockHelper;
import net.ymate.platform.commons.util.RuntimeUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/03 16:50
 */
public class DefaultScheduleLocker implements IScheduleLocker {

    private final ReentrantLock reentrantLock;

    public DefaultScheduleLocker(String lockerName) {
        if (StringUtils.isBlank(lockerName)) {
            throw new NullArgumentException("lockerName");
        }
        try {
            reentrantLock = ReentrantLockHelper.DEFAULT.getLocker(lockerName);
        } catch (Exception e) {
            throw new SchedulerException(e.getMessage(), RuntimeUtils.unwrapThrow(e));
        }
    }

    @Override
    public void lock() {
        reentrantLock.lock();
    }

    @Override
    public boolean tryLock() {
        return reentrantLock.tryLock();
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return reentrantLock.tryLock(timeout, unit);
    }

    @Override
    public boolean isLocked() {
        return reentrantLock.isLocked();
    }

    @Override
    public void unlock() {
        reentrantLock.unlock();
    }
}
