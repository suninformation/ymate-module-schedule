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
package net.ymate.module.schedule;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/03/12 23:55
 */
public class ScheduleTaskMeta implements Serializable {

    private final String name;

    private final String description;

    private final Class<? extends IScheduleTask> taskClass;

    public ScheduleTaskMeta(String name, String description, Class<? extends IScheduleTask> taskClass) {
        if (taskClass == null) {
            throw new NullArgumentException("taskClass");
        }
        this.name = StringUtils.defaultIfBlank(name, taskClass.getName());
        this.description = description;
        this.taskClass = taskClass;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Class<? extends IScheduleTask> getTaskClass() {
        return taskClass;
    }
}
