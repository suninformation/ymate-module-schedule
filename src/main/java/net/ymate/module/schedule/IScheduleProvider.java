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

import java.util.List;

/**
 * 任务调度服务提供者接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/07 04:01
 */
@Ignored
public interface IScheduleProvider extends IInitialization<IScheduler>, IDestroyable {

    /**
     * 启动服务
     *
     * @throws Exception 可能产生的任何异常
     */
    void start() throws Exception;

    /**
     * 停止服务
     *
     * @throws Exception 可能产生的任何异常
     */
    void shutdown() throws Exception;

    /**
     * 添加计划任务
     *
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @return 是否完成添加动作
     * @throws SchedulerException 可能产生的任务调度异常
     */
    boolean addTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException;

    /**
     * 更新任务规则
     *
     * @param id   任务ID
     * @param cron 任务规则
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void updateTask(String id, String cron) throws SchedulerException;

    /**
     * 更新任务规则
     *
     * @param config 任务规则
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void updateTask(ITaskConfig config) throws SchedulerException;

    /**
     * 判断指定计划任务是否存在
     *
     * @param id 任务ID
     * @return 返回true表示存在
     * @throws SchedulerException 可能产生的任务调度异常
     */
    boolean hasTask(String id) throws SchedulerException;

    /**
     * 判断指定计划任务是否存在
     *
     * @param id    任务ID
     * @param group 分组名称
     * @return 返回true表示存在
     * @throws SchedulerException 可能产生的任务调度异常
     */
    boolean hasTask(String id, String group) throws SchedulerException;

    /**
     * 获取分组名称
     *
     * @return 返回组名称集合
     * @throws SchedulerException 可能产生的任务调度异常
     */
    List<String> getGroupNames() throws SchedulerException;

    /**
     * 添加或更新任务
     *
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void addOrUpdateTask(ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void pauseTask(String id) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param id    任务ID
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void pauseTask(String id, String group) throws SchedulerException;

    /**
     * 暂停任务组
     *
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void pauseTaskGroup(String group) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void resumeTask(String id) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param id    任务ID
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void resumeTask(String id, String group) throws SchedulerException;

    /**
     * 恢复任务组
     *
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void resumeTaskGroup(String group) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void deleteTask(String id) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param id    任务ID
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void deleteTask(String id, String group) throws SchedulerException;

    /**
     * 删除任务组
     *
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void deleteTaskGroup(String group) throws SchedulerException;

    /**
     * 执行任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void triggerTask(String id) throws SchedulerException;

    /**
     * 执行任务
     *
     * @param id    任务ID
     * @param group 分组名称
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void triggerTask(String id, String group) throws SchedulerException;

    /**
     * 暂停所以的计划执行的任务
     *
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void pauseAll() throws SchedulerException;

    /**
     * 恢复所有被暂停的计划任务
     *
     * @throws SchedulerException 可能产生的任务调度异常
     */
    void resumeAll() throws SchedulerException;
}
