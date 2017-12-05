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

import java.util.List;

/**
 * 计划任务规则配置加载器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/6 上午12:19
 * @version 1.0
 */
public interface ITaskConfigLoader {

    /**
     * @return 加载计划任务规则配置
     */
    List<ITaskConfig> load();
}
