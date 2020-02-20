### YMP-Schedule

#### Maven包依赖

    <dependency>
        <groupId>net.ymate.module</groupId>
        <artifactId>ymate-module-schedule</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

#### 模块配置参数说明

    #-------------------------------------
    # module.schedule 调度模块初始化参数
    #-------------------------------------
    
    # 调度模块是否已启用, 默认值: true
    #ymp.configs.module.schedule.enabled=
    
    # 调度锁工厂接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleLockerFactory
    #ymp.configs.module.schedule.locker_factory_class=
    
    # 调度服务提供者接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleProvider
    #ymp.configs.module.schedule.provider_class=
    
    # 计划任务规则配置加载器接口实例对象, 默认值: net.ymate.module.schedule.impl.DefaultTaskConfigLoader
    #ymp.configs.module.schedule.task_config_loader_class=
    
    # 当使用默认规则配置加载器时, 用于为指定任务设置调度规则(当值为disabled时将禁用该任务), 此配置优先于注解配置
    #ymp.params.module.schedule.task_cron_<TASK_ID>=<TASK_CRON|disabled>

#### 示例代码


#### One More Thing

YMP不仅提供便捷的Web及其它Java项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入 官方QQ群480374360，一起交流学习，帮助YMP成长！

了解更多有关YMP框架的内容，请访问官网：http://www.ymate.net/