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

/**
 * 任务调度服务提供者接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/12/7 上午4:01
 * @version 1.0
 */
public interface IScheduleProvider {

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
     * @param id        任务ID
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @return 是否完成添加动作
     * @throws SchedulerException 可能产生的任何异常
     */
    boolean addTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException;

    /**
     * 更新任务规则
     *
     * @param id   任务ID
     * @param cron 任务规则
     * @throws SchedulerException 可能产生的任何异常
     */
    void updateTask(String id, String cron) throws SchedulerException;

    void updateTask(String id, ITaskConfig config) throws SchedulerException;

    /**
     * @param id 任务ID
     * @return 判断指定id的计划任务是否存在
     * @throws SchedulerException 可能产生的任何异常
     */
    boolean hasTask(String id) throws SchedulerException;

    boolean hasTask(String id, String name, String group) throws SchedulerException;

    /**
     * 添加或更新Job
     *
     * @param id        任务ID
     * @param config    任务规则
     * @param taskClass 添加的执行的任务类
     * @throws SchedulerException 可能产生的任何异常
     */
    void addOrUpdateTask(String id, ITaskConfig config, Class<? extends IScheduleTask> taskClass) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    void pauseTask(String id) throws SchedulerException;

    void pauseTask(String id, String name, String group) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    void resumeTask(String id) throws SchedulerException;

    void resumeTask(String id, String name, String group) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    void deleteTask(String id) throws SchedulerException;

    void deleteTask(String id, String name, String group) throws SchedulerException;

    /**
     * 执行任务
     *
     * @param id 任务ID
     * @throws SchedulerException 可能产生的任何异常
     */
    void triggerTask(String id) throws SchedulerException;

    void triggerTask(String id, String name, String group) throws SchedulerException;

    /**
     * 暂停所以的计划执行的任务
     */
    void pauseAll() throws SchedulerException;

    /**
     * 恢复所有被暂停的计划任务
     */
    void resumeAll() throws SchedulerException;
}
