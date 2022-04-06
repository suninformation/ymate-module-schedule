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
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;

import java.util.Collection;

/**
 * 计划任务规则配置加载器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/06 00:19
 */
@Ignored
public interface ITaskConfigLoader extends IInitialization<IScheduler>, IDestroyable {

    /**
     * 加载计划任务规则配置
     *
     * @return 返回计划任务规则配置集合
     */
    Collection<ITaskConfig> loadConfigs();
}
