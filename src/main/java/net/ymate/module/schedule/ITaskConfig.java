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

import java.util.Map;

/**
 * 计划任务规则配置接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/05 23:56
 */
@Ignored
public interface ITaskConfig {

    String DEFAULT_GROUP = "DEFAULT";

    String DISABLED = "disabled";

    /**
     * 获取任务唯一标识, 若未提供则根据名称及参数自动生成
     *
     * @return 返回任务唯一标识
     */
    String getId();

    /**
     * 获取计划任务名称或类名称
     *
     * @return 返回计划任务名称或类名称
     */
    String getName();

    /**
     * 获取分组名称
     *
     * @return 返回分组名称
     */
    String getGroup();

    /**
     * 获取任务执行规则表达式
     *
     * @return 返回任务执行规则表达式
     */
    String getCron();

    /**
     * 获取任务扩展参数
     *
     * @return 返回任务扩展参数
     */
    Map<String, String> getParams();
}
