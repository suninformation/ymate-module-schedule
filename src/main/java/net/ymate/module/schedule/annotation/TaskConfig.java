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
 * @author 刘镇 (suninformation@163.com) on 2020/01/12 18:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(TaskConfigs.class)
public @interface TaskConfig {

    /**
     * @return 任务唯一标识, 若未提供则采用UUID自动生成(注: 若任务唯标识重复则后者将覆盖前者)
     */
    String id() default StringUtils.EMPTY;

    /**
     * @return 计划任务名称或类名称
     */
    String name();

    /**
     * @return 任务执行规则表达式
     */
    String cron();

    /**
     * @return 任务扩展参数, 采用key=value格式(支持以$xxx格式从框架全局参数中获取xxx的值, 例: key1=$value1 或 $key1)
     */
    String[] params() default {};
}
