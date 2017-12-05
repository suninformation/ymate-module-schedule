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

import org.quartz.JobListener;

import java.util.List;
import java.util.Map;

/**
 * 计划任务规则配置接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/5 下午11:56
 * @version 1.0
 */
public interface ITaskConfig {

    /**
     * @return 计划任务名称或类名称
     */
    String getName();

    /**
     * @return 分组名称
     */
    String getGroup();

    /**
     * @return 任务执行规则表达式
     */
    String getCron();

    /**
     * @return 任务监听器集合
     */
    List<JobListener> getJobListeners();

    /**
     * @return 任务扩展参数
     */
    Map<String, String> getParams();
}
