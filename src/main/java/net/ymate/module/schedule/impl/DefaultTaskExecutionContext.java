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

import net.ymate.module.schedule.ITaskExecutionContext;
import net.ymate.module.schedule.support.QuartzScheduleHelper;
import org.quartz.JobExecutionContext;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/05/11 16:58
 */
public class DefaultTaskExecutionContext implements ITaskExecutionContext {

    private final String id;

    private final String name;

    private final String group;

    private final JobExecutionContext context;

    public DefaultTaskExecutionContext(JobExecutionContext context) {
        this.id = context.getTrigger().getKey().getName();
        this.group = context.getTrigger().getKey().getGroup();
        this.name = context.getJobDetail().getJobDataMap().getString(QuartzScheduleHelper.TASK_NAME);
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
    public String getParamStr(String key) {
        return context.getJobDetail().getJobDataMap().getString(key);
    }

    @Override
    public Object getParamObject(String key) {
        return context.getJobDetail().getJobDataMap().get(key);
    }

    @Override
    public JobExecutionContext getJobExecutionContext() {
        return context;
    }
}
