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
package net.ymate.module.schedule.annotation;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/01/12 18:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(TaskConfigGroups.class)
public @interface TaskConfigs {

    /**
     * 分组名称
     */
    String group() default StringUtils.EMPTY;

    /**
     * @return 计划任务规则配置集合
     */
    TaskConfig[] value();
}
