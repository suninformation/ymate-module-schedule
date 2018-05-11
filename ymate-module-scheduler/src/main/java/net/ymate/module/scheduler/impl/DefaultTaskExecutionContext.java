/*
 * Copyright 2007-2018 the original author or authors.
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

import net.ymate.module.scheduler.ITaskExecutionContext;
import org.quartz.JobExecutionContext;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/5/11 下午4:58
 * @version 1.0
 */
public class DefaultTaskExecutionContext implements ITaskExecutionContext {

    private String __id;

    private String __name;

    private JobExecutionContext __context;

    public DefaultTaskExecutionContext(String id, String name, JobExecutionContext context) {
        this.__id = id;
        this.__name = name;
        this.__context = context;
    }

    @Override
    public String getId() {
        return __id;
    }

    @Override
    public String getName() {
        return __name;
    }

    public JobExecutionContext getContext() {
        return __context;
    }
}
