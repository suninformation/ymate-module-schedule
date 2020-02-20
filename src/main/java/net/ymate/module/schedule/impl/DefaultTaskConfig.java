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

import net.ymate.module.schedule.ITaskConfig;
import net.ymate.module.schedule.annotation.TaskConfig;
import net.ymate.platform.commons.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobListener;

import java.util.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/06 01:14
 */
public class DefaultTaskConfig implements ITaskConfig {

    private String id;

    private String name;

    private String group;

    private String cron;

    private List<JobListener> listeners = new ArrayList<>();

    private Map<String, String> params = new HashMap<>();

    public static Builder builder() {
        return new Builder();
    }

    public static Builder duplicate(ITaskConfig taskConfig) {
        Builder configBuilder = builder().id(taskConfig.getId())
                .name(taskConfig.getName())
                .cron(taskConfig.getCron())
                .group(taskConfig.getGroup())
                .addParams(taskConfig.getParams());
        taskConfig.getJobListeners().forEach(configBuilder::addListener);
        return configBuilder;
    }

    public static DefaultTaskConfig create(TaskConfig taskConfig) throws IllegalAccessException, InstantiationException {
        return create(null, taskConfig);
    }

    public static DefaultTaskConfig create(String group, TaskConfig taskConfig) throws IllegalAccessException, InstantiationException {
        Builder configBuilder = builder()
                .id(taskConfig.id())
                .name(taskConfig.name())
                .cron(taskConfig.cron());
        if (StringUtils.isNotBlank(group)) {
            configBuilder.group(group);
        }
        for (Class<? extends JobListener> listenerClass : taskConfig.listeners()) {
            configBuilder.addListener(listenerClass.newInstance());
        }
        Arrays.stream(taskConfig.params())
                .map(param -> StringUtils.split(param, "="))
                .filter(kValue -> kValue != null && kValue.length == 2)
                .forEachOrdered(kValue -> configBuilder.addParam(kValue[0], kValue[1]));
        return configBuilder.build();
    }

    private DefaultTaskConfig() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return Collections.unmodifiableList(listeners);
    }

    public void addListener(JobListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public static final class Builder {

        private final DefaultTaskConfig config = new DefaultTaskConfig();

        private Builder() {
        }

        public Builder id(String id) {
            config.setId(id);
            return this;
        }

        public Builder name(String name) {
            config.setName(name);
            return this;
        }

        public Builder group(String group) {
            config.setGroup(group);
            return this;
        }

        public Builder cron(String cron) {
            config.setCron(cron);
            return this;
        }

        public Builder addListener(JobListener... listeners) {
            if (listeners != null && listeners.length > 0) {
                Arrays.stream(listeners).forEach(config::addListener);
            }
            return this;
        }

        public Builder addParam(String key, String value) {
            config.addParam(key, value);
            return this;
        }

        public Builder addParams(Map<String, String> params) {
            if (params != null && !params.isEmpty()) {
                params.forEach((config::addParam));
            }
            return this;
        }

        public DefaultTaskConfig build() {
            if (StringUtils.isBlank(config.getId())) {
                config.setId(UUIDUtils.UUID());
            }
            return config;
        }
    }
}
