/*
 * Copyright 2007-2025 the original author or authors.
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
package net.ymate.module.schedule.support.impl;

import net.ymate.module.schedule.IScheduler;
import net.ymate.module.schedule.support.IQuartzSchedulerFactory;
import net.ymate.module.schedule.support.QuartzSchedulerJobStore;
import net.ymate.platform.core.configuration.IConfigReader;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;

import java.util.Map;
import java.util.Properties;

/**
 * @author 刘镇 (suninformation@163.com) on 2025/1/2 18:33
 * @since 1.0.2
 */
public class DefaultQuartzSchedulerFactory implements IQuartzSchedulerFactory {

    private IScheduler owner;

    private Scheduler scheduler;

    private boolean initialized;

    @Override
    public void initialize(IScheduler owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            //
            Properties properties = new Properties();
            //
            IConfigReader configReader = owner.getOwner().getParamConfigReader();
            Map<String, String> propMap = configReader.getMap(IScheduler.MODULE_NAME + ".");
            String dataSourceName = propMap.remove(QuartzSchedulerJobStore.DATA_SOURCE_NAME);
            if (!propMap.isEmpty()) {
                for (Map.Entry<String, String> entry : propMap.entrySet()) {
                    if (StringUtils.startsWith(entry.getKey(), "org.quartz.") && StringUtils.isNotBlank(entry.getValue())) {
                        if (!StringUtils.startsWith(entry.getKey(), StdSchedulerFactory.PROP_DATASOURCE_PREFIX)) {
                            properties.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(dataSourceName)) {
                properties.put("org.quartz.jobStore.isClustered", properties.getProperty("org.quartz.jobStore.isClustered", "true"));
                properties.put("org.quartz.jobStore.clusterCheckinInterval", properties.getProperty("org.quartz.jobStore.clusterCheckinInterval", "15000"));
                properties.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", properties.getProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1"));
                properties.put("org.quartz.jobStore.misfireThreshold", properties.getProperty("org.quartz.jobStore.misfireThreshold", "12000"));
            }
            String threadPoolClass = properties.getProperty("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
            if (StringUtils.equals(threadPoolClass, SimpleThreadPool.class.getName())) {
                properties.put("org.quartz.threadPool.class", threadPoolClass);
                properties.put("org.quartz.threadPool.threadCount", properties.getProperty("org.quartz.threadPool.threadCount", "20"));
                properties.put("org.quartz.threadPool.threadPriority", properties.getProperty("org.quartz.threadPool.threadPriority", "5"));
            }
            properties.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID, StdSchedulerFactory.AUTO_GENERATE_INSTANCE_ID);
            if (StringUtils.isNotBlank(dataSourceName)) {
                properties.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, QuartzSchedulerJobStore.class.getName());
            }
            //
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
            this.scheduler = schedulerFactory.getScheduler();
            //
            this.initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public IScheduler getOwner() {
        return owner;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }
}
