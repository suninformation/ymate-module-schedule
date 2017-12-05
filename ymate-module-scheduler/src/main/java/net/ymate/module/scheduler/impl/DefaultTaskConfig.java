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

import net.ymate.module.scheduler.ITaskConfig;
import org.quartz.JobListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/6 上午1:14
 * @version 1.0
 */
public class DefaultTaskConfig implements ITaskConfig {

    private String name;

    private String group;

    private String cron;

    private List<JobListener> listeners = new ArrayList<JobListener>();

    private Map<String, String> params = new HashMap<String, String>();

    public DefaultTaskConfig() {
    }

    public DefaultTaskConfig(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public DefaultTaskConfig(String name, String group, String cron) {
        this.name = name;
        this.group = group;
        this.cron = cron;
    }

    public DefaultTaskConfig(String name, String group, String cron, List<JobListener> listeners, Map<String, String> params) {
        this.name = name;
        this.group = group;
        this.cron = cron;
        this.listeners = listeners;
        this.params = params;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public List<JobListener> getJobListeners() {
        return listeners;
    }

    public void setJobListeners(List<JobListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
