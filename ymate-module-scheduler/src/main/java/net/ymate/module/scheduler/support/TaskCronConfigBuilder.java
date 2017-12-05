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
package net.ymate.module.scheduler.support;

import net.ymate.module.scheduler.ITaskConfig;
import net.ymate.module.scheduler.impl.DefaultTaskConfig;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/6 上午12:16
 * @version 1.0
 */
public class TaskCronConfigBuilder {

    private String __name;

    private String __group;

    private String __cron;

    private List<JobListener> __listeners;

    private Map<String, String> __params;

    public static TaskCronConfigBuilder create() {
        return new TaskCronConfigBuilder();
    }

    public TaskCronConfigBuilder() {
        __listeners = new ArrayList<JobListener>();
        __params = new HashMap<String, String>();
    }

    public TaskCronConfigBuilder name(String name) {
        __name = name;
        return this;
    }

    public TaskCronConfigBuilder group(String group) {
        __group = group;
        return this;
    }

    public TaskCronConfigBuilder cron(String cron) {
        __cron = cron;
        return this;
    }

    public TaskCronConfigBuilder listeners(List<JobListener> listeners) {
        __listeners = listeners;
        return this;
    }

    public TaskCronConfigBuilder addListener(JobListener listener) {
        __listeners.add(listener);
        return this;
    }

    public TaskCronConfigBuilder addListener(List<JobListener> listeners) {
        if (__listeners != null && !__listeners.isEmpty()) {
            __listeners.addAll(listeners);
        }
        return this;
    }

    public TaskCronConfigBuilder params(Map<String, String> params) {
        __params = params;
        return this;
    }

    public TaskCronConfigBuilder addParam(String name, String value) {
        __params.put(name, value);
        return this;
    }

    public TaskCronConfigBuilder addParam(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            __params.putAll(params);
        }
        return this;
    }

    public ITaskConfig build() {
        if (StringUtils.isBlank(__name)) {
            throw new NullArgumentException("name");
        }
        if (StringUtils.isBlank(__cron)) {
            throw new NullArgumentException("cron");
        }
        if (__params == null) {
            __params = new HashMap<String, String>();
        }
        //
        return new DefaultTaskConfig(__name, __group, __cron, __listeners, __params);
    }
}
