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
import net.ymate.module.schedule.ITaskConfigLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/11 07:54
 */
public final class DefaultTaskConfigLoader implements ITaskConfigLoader {

    private static final Log LOG = LogFactory.getLog(DefaultTaskConfigLoader.class);

    private IScheduler owner;

    private Map<String, ITaskConfig> taskConfigs = new ConcurrentHashMap<>();

    public DefaultTaskConfigLoader(IScheduler owner) {
        this.owner = owner;
    }

    @Override
    public Collection<ITaskConfig> loadConfigs() {
        return taskConfigs.values();
    }

    public void addTaskConfig(ITaskConfig taskConfig) {
        if (StringUtils.isNotBlank(taskConfig.getId())) {
            String taskCron = owner.getOwner().getParam(String.format("%s.task_cron_%s", IScheduler.MODULE_NAME, taskConfig.getId()));
            if (StringUtils.isNotBlank(taskCron)) {
                if (StringUtils.equalsIgnoreCase(taskCron, ITaskConfig.DISABLED)) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info(String.format("Task %s.%s_%s has been disabled.", StringUtils.defaultIfBlank(taskConfig.getGroup(), ITaskConfig.DEFAULT_GROUP), taskConfig.getName(), taskConfig.getId()));
                    }
                } else {
                    taskConfigs.put(taskConfig.getId(), DefaultTaskConfig.duplicate(taskConfig).cron(taskCron).build());
                }
            } else {
                taskConfigs.put(taskConfig.getId(), taskConfig);
            }
        } else {
            taskConfigs.put(taskConfig.getId(), taskConfig);
        }
    }

    public void addTaskConfigs(ITaskConfigLoader taskConfigLoader) {
        Collection<ITaskConfig> configs = taskConfigLoader.loadConfigs();
        if (configs != null && !configs.isEmpty()) {
            configs.forEach(this::addTaskConfig);
        }
    }
}
