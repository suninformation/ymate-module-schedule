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
import org.quartz.JobExecutionContext;

import java.io.Serializable;

/**
 * 任务执行上下文环境对象
 *
 * @author 刘镇 (suninformation@163.com) on 2018/05/11 04:31
 */
@Ignored
public interface ITaskExecutionContext extends Serializable {

    /**
     * 获取当前计划任务ID
     *
     * @return 返回当前计划任务ID
     */
    String getId();

    /**
     * 获取当前计划任务名称
     *
     * @return 返回当前计划任务名称
     */
    String getName();

    /**
     * 获取当前计划任务分组名称
     *
     * @return 返回当前计划任务分组名称
     */
    String getGroup();

    /**
     * 获取所属模块实例
     *
     * @return 返回模块实例对象
     */
    IScheduler getOwner();

    /**
     * 获取指定参数key的字符串值
     *
     * @param key 键名
     * @return 返回参数字符串值
     */
    String getParamStr(String key);

    /**
     * 获取指定参数key的对象值
     *
     * @param key 键名
     * @return 返回参数对象值
     */
    Object getParamObject(String key);

    /**
     * 获取Quartz任务执行上下文环境对象
     *
     * @return 返回任务执行上下文环境对象
     */
    JobExecutionContext getJobExecutionContext();
}
