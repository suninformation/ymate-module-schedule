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

import net.ymate.module.scheduler.annotation.ScheduleTask;
import net.ymate.module.scheduler.handle.ScheduleTaskHandler;
import net.ymate.module.scheduler.impl.DefaultModuleCfg;
import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.Version;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.annotation.Module;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/02/25 上午 02:33
 * @version 1.0
 */
@Module
public class Scheduler implements IModule, IScheduler {

    private static final Log _LOG = LogFactory.getLog(Scheduler.class);

    public static final Version VERSION = new Version(1, 0, 0, Scheduler.class.getPackage().getImplementationVersion(), Version.VersionType.Alpha);

    private static volatile IScheduler __instance;

    private YMP __owner;

    private ISchedulerModuleCfg __moduleCfg;

    private boolean __inited;

    private Map<String, ScheduleTaskMeta> __tasksMetaMap;

    private Map<String, ITaskConfig> __tasksConfigMap;

    public static IScheduler get() {
        if (__instance == null) {
            synchronized (VERSION) {
                if (__instance == null) {
                    __instance = YMP.get().getModule(Scheduler.class);
                }
            }
        }
        return __instance;
    }

    @Override
    public String getName() {
        return IScheduler.MODULE_NAME;
    }

    @Override
    public void init(YMP owner) throws Exception {
        if (!__inited) {
            //
            _LOG.info("Initializing ymate-module-scheduler-" + VERSION);
            //
            __owner = owner;
            __owner.registerHandler(ScheduleTask.class, new ScheduleTaskHandler(this));
            __owner.getEvents().registerListener(Events.MODE.ASYNC, ApplicationEvent.class, new IEventListener<ApplicationEvent>() {
                @Override
                public boolean handle(ApplicationEvent context) {
                    switch (context.getEventName()) {
                        case APPLICATION_INITED:
                            __doStartupTasks();
                            break;
                        default:
                    }
                    return false;
                }
            });
            __tasksMetaMap = new ConcurrentHashMap<String, ScheduleTaskMeta>();
            __tasksConfigMap = new ConcurrentHashMap<String, ITaskConfig>();
            //
            __moduleCfg = new DefaultModuleCfg(owner);
            __moduleCfg.getScheduleProvider().start();
            //
            __inited = true;
        }
    }

    @SuppressWarnings("unchecked")
    private void __doStartupTasks() {
        if (__moduleCfg.getTaskConfigLoader() != null) {
            List<ITaskConfig> _configs = __moduleCfg.getTaskConfigLoader().load();
            if (_configs != null) {
                for (ITaskConfig _item : _configs) {
                    Class<IScheduleTask> _targetClass = null;
                    ScheduleTaskMeta _meta = __tasksMetaMap.get(_item.getName());
                    if (_meta != null) {
                        _targetClass = _meta.getTaskClass();
                    } else {
                        try {
                            _targetClass = (Class<IScheduleTask>) ClassUtils.loadClass(_item.getName(), getClass());
                        } catch (ClassNotFoundException e) {
                            _LOG.warn("Load task class error: " + _item.getName());
                        }
                    }
                    if (_targetClass != null) {
                        try {
                            String _taskId = UUIDUtils.UUID().toUpperCase();
                            __moduleCfg.getScheduleProvider().addTask(_taskId, _item, _targetClass);
                            __tasksConfigMap.put(_taskId, _item);
                            //
                            _LOG.info("Add task: " + _item.getName() + " - " + _targetClass.getName());
                        } catch (SchedulerException e) {
                            _LOG.warn("Add task error: " + _item.getName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInited() {
        return __inited;
    }

    @Override
    public void registerTask(Class<IScheduleTask> targetClass) throws Exception {
        ScheduleTask _taskAnno = targetClass.getAnnotation(ScheduleTask.class);
        if (_taskAnno != null) {
            ScheduleTaskMeta _meta = new ScheduleTaskMeta(_taskAnno.name(), _taskAnno.description(), targetClass);
            __tasksMetaMap.put(_meta.getName(), _meta);
        }
    }

    @Override
    public Map<String, ScheduleTaskMeta> getTaskMetas() {
        return Collections.unmodifiableMap(__tasksMetaMap);
    }

    @Override
    public Map<String, ITaskConfig> getTaskConfigs() {
        return Collections.unmodifiableMap(__tasksConfigMap);
    }

    @Override
    public void destroy() throws Exception {
        if (__inited) {
            __inited = false;
            //
            __moduleCfg.getScheduleProvider().shutdown();
            //
            __tasksMetaMap = null;
            //
            __moduleCfg = null;
            __owner = null;
        }
    }

    @Override
    public YMP getOwner() {
        return __owner;
    }

    @Override
    public ISchedulerModuleCfg getModuleCfg() {
        return __moduleCfg;
    }
}
