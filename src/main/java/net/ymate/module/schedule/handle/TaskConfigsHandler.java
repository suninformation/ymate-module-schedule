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
package net.ymate.module.schedule.handle;

import net.ymate.module.schedule.IScheduler;
import net.ymate.module.schedule.annotation.TaskConfig;
import net.ymate.module.schedule.annotation.TaskConfigGroups;
import net.ymate.module.schedule.annotation.TaskConfigs;
import net.ymate.module.schedule.impl.DefaultTaskConfig;
import net.ymate.module.schedule.impl.DefaultTaskConfigLoader;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.beans.IBeanHandler;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/12 18:36
 */
public class TaskConfigsHandler implements IBeanHandler {

    private final IScheduler owner;

    public TaskConfigsHandler(IScheduler owner) {
        this.owner = owner;
    }

    private void doRegisterTaskConfig(String group, TaskConfig[] taskConfigs) throws InstantiationException, IllegalAccessException {
        for (TaskConfig taskConfig : taskConfigs) {
            ((DefaultTaskConfigLoader) owner.getConfig().getTaskConfigLoader()).addTaskConfig(DefaultTaskConfig.create(owner, group, taskConfig));
        }
    }

    @Override
    public Object handle(Class<?> targetClass) throws Exception {
        if (owner.getConfig().getTaskConfigLoader() instanceof DefaultTaskConfigLoader && ClassUtils.isNormalClass(targetClass)) {
            TaskConfigGroups taskConfigGroups = targetClass.getAnnotation(TaskConfigGroups.class);
            if (taskConfigGroups != null) {
                for (TaskConfigs taskConfigs : taskConfigGroups.value()) {
                    doRegisterTaskConfig(taskConfigs.group(), taskConfigs.value());
                }
            } else {
                TaskConfigs taskConfigs = targetClass.getAnnotation(TaskConfigs.class);
                if (taskConfigs != null) {
                    doRegisterTaskConfig(taskConfigs.group(), taskConfigs.value());
                } else {
                    TaskConfig taskConfig = targetClass.getAnnotation(TaskConfig.class);
                    if (taskConfig != null) {
                        doRegisterTaskConfig(null, new TaskConfig[]{taskConfig});
                    }
                }
            }
        }
        return null;
    }
}
