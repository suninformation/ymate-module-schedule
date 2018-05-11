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

import net.ymate.platform.core.YMP;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/25 上午 02:33
 * @version 1.0
 */
public interface IScheduler {

    String MODULE_NAME = "module.scheduler";

    /**
     * @return 返回所属YMP框架管理器实例
     */
    YMP getOwner();

    /**
     * @return 返回模块配置对象
     */
    ISchedulerModuleCfg getModuleCfg();

    /**
     * @return 返回模块是否已初始化
     */
    boolean isInited();

    /**
     * 注册计划任务
     *
     * @param targetClass 目标类型
     * @throws Exception 可能产生的异常
     */
    void registerTask(Class<IScheduleTask> targetClass) throws Exception;

    /**
     * @return 返回已注册计划任务映射
     */
    Map<String, ScheduleTaskMeta> getTaskMetas();

    /**
     * @return 返回计划任务配置映射
     */
    Map<String, ITaskConfig> getTaskConfigs();
}