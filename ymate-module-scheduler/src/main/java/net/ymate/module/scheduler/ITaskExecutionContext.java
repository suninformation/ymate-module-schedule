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
package net.ymate.module.scheduler;

import java.io.Serializable;

/**
 * 任务执行上下文环境对象
 *
 * @author 刘镇 (suninformation@163.com) on 2018/5/11 下午4:31
 * @version 1.0
 */
public interface ITaskExecutionContext extends Serializable {

    /**
     * @return 返回当前计划任务ID
     */
    String getId();

    /**
     * @return 返回当前计划任务名称
     */
    String getName();
}
