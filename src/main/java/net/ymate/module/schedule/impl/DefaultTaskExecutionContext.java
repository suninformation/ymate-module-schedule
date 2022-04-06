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
import net.ymate.module.schedule.ITaskExecutionContext;
import net.ymate.module.schedule.support.QuartzScheduleHelper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.TriggerKey;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/05/11 16:58
 */
public class DefaultTaskExecutionContext implements ITaskExecutionContext {

    private final String id;

    private final String name;

    private final String group;

    private final IScheduler owner;

    private final JobDataMap detailJobDataMap;

    private final JobExecutionContext context;

    public DefaultTaskExecutionContext(JobExecutionContext context) {
        TriggerKey triggerKey = context.getTrigger().getKey();
        this.id = triggerKey.getName();
        this.group = triggerKey.getGroup();
        //
        detailJobDataMap = context.getJobDetail().getJobDataMap();
        this.name = detailJobDataMap.getString(QuartzScheduleHelper.TASK_NAME);
        this.owner = (IScheduler) detailJobDataMap.get(IScheduler.class.getName());
        //
        this.context = context;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public IScheduler getOwner() {
        return owner;
    }

    @Override
    public String getParamStr(String key) {
        return detailJobDataMap.getString(key);
    }

    @Override
    public Object getParamObject(String key) {
        return detailJobDataMap.get(key);
    }

    @Override
    public JobExecutionContext getJobExecutionContext() {
        return context;
    }
}
