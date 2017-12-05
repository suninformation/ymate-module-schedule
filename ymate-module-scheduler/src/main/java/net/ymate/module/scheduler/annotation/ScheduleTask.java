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
package net.ymate.module.scheduler.annotation;

import java.lang.annotation.*;

/**
 * 声明一个类为计划任务
 *
 * @author 刘镇 (suninformation@163.com) on 16/5/7 上午2:17
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduleTask {

    /**
     * @return 任务名称, 默认为类名称
     */
    String name() default "";

    /**
     * @return 描述
     */
    String description() default "";
}
