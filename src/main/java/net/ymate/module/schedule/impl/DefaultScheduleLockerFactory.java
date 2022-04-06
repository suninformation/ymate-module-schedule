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
import net.ymate.module.schedule.IScheduleLockerFactory;
import net.ymate.module.schedule.IScheduler;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/03 16:47
 */
public class DefaultScheduleLockerFactory implements IScheduleLockerFactory {

    private IScheduler owner;

    private boolean initialized;

    @Override
    public void initialize(IScheduler owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public IScheduler getOwner() {
        return owner;
    }

    @Override
    public void close() throws Exception {
        if (initialized) {
            initialized = false;
            owner = null;
        }
    }

    @Override
    public IScheduleLocker getScheduleLocker(String lockerName) {
        return new DefaultScheduleLocker(lockerName);
    }
}
