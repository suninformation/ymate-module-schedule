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

import net.ymate.platform.core.beans.annotation.Ignored;
import org.quartz.Job;

/**
 * 计划任务接口
 *
 * @author 刘镇 (suninformation@163.com) on 2016/05/06 03:09
 */
@Ignored
public interface IScheduleTask extends Job {

    /**
     * 是否采用同步执行(相同的计划任务同一时间仅能有一个实例正在执行)
     *
     * @return 返回true表示同步执行
     */
    boolean isSync();

    /**
     * 执行计划任务
     *
     * @param context 任务执行上下文环境对象
     */
    void execute(ITaskExecutionContext context);
}
