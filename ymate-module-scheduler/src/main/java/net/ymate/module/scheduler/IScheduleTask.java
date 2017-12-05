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
package net.ymate.module.scheduler;

import org.quartz.Job;

/**
 * 计划任务接口
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/6 上午3:09
 * @version 1.0
 */
public interface IScheduleTask extends Job {

    /**
     * @return 是否采用同步执行(表示相同的计划任务仅能有一个正在执行)
     */
    boolean isSync();
}
