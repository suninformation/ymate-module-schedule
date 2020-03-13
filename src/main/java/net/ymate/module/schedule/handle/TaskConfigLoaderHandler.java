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
import net.ymate.module.schedule.ITaskConfigLoader;
import net.ymate.module.schedule.impl.DefaultTaskConfigLoader;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.beans.IBeanHandler;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/11 18:32
 */
public class TaskConfigLoaderHandler implements IBeanHandler {

    private final IScheduler owner;

    public TaskConfigLoaderHandler(IScheduler owner) {
        this.owner = owner;
    }

    @Override
    public Object handle(Class<?> targetClass) throws Exception {
        if (ClassUtils.isNormalClass(targetClass) && !targetClass.isInterface() && ClassUtils.isInterfaceOf(targetClass, ITaskConfigLoader.class)) {
            if (owner.getConfig().getTaskConfigLoader() instanceof DefaultTaskConfigLoader) {
                ((DefaultTaskConfigLoader) owner.getConfig().getTaskConfigLoader()).addTaskConfigs((ITaskConfigLoader) targetClass.newInstance());
            }
        }
        return null;
    }
}
