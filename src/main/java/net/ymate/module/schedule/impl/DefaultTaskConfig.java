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
import net.ymate.module.schedule.ITaskConfig;
import net.ymate.module.schedule.annotation.TaskConfig;
import net.ymate.platform.commons.util.ParamUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/12/06 01:14
 */
public final class DefaultTaskConfig implements ITaskConfig {

    private String id;

    private String name;

    private String group;

    private String cron;

    private final Map<String, String> params = new HashMap<>();

    public static Builder builder() {
        return new Builder();
    }

    public static Builder duplicate(ITaskConfig taskConfig) {
        return builder()
                .id(taskConfig.getId())
                .name(taskConfig.getName())
                .cron(taskConfig.getCron())
                .group(taskConfig.getGroup())
                .addParams(taskConfig.getParams());
    }

    public static DefaultTaskConfig create(IScheduler owner, TaskConfig taskConfig) {
        return create(owner, null, taskConfig);
    }

    public static DefaultTaskConfig create(IScheduler owner, String group, TaskConfig taskConfig) {
        Builder configBuilder = builder()
                .id(taskConfig.id())
                .name(taskConfig.name())
                .cron(taskConfig.cron());
        if (StringUtils.isNotBlank(group)) {
            configBuilder.group(group);
        }
        for (String param : taskConfig.params()) {
            String[] kValue = StringUtils.split(param, "=");
            if (kValue != null && kValue.length > 0) {
                String key = null;
                String value = null;
                if (kValue.length == 2) {
                    key = StringUtils.trimToNull(kValue[0]);
                    value = StringUtils.trimToNull(kValue[1]);
                    if (value != null && value.length() > 1 && value.charAt(0) == '$') {
                        value = StringUtils.trimToNull(owner.getOwner().getParam(value.substring(1)));
                    }
                } else if (kValue.length == 1) {
                    boolean flag = kValue[0].length() > 1 && kValue[0].charAt(0) == '$';
                    if (flag) {
                        key = StringUtils.trimToNull(kValue[0].substring(1));
                        value = StringUtils.trimToNull(owner.getOwner().getParam(key));
                    }
                }
                if (key != null && value != null) {
                    configBuilder.addParam(key, value);
                }
            }
        }
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
                // 根据当前任务名称及参数集合进行签名计算
                config.setId(ParamUtils.createSignature(config.params, false, config.getName()));
            }
            return config;
        }
    }
}
