### YMP-Scheduler:

> 计划任务调度服务模块，特性如下：
>

#### Maven包依赖

    <dependency>
        <groupId>net.ymate.module</groupId>
        <artifactId>ymate-module-scheduler</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

#### 模块配置参数说明

    #-------------------------------------
    # module.scheduler 模块初始化参数
    #-------------------------------------
    
    # 调度锁工厂接口实例类, 若未提供则使用默认实现: net.ymate.module.scheduler.impl.DefaultScheduleLockerFactory
    ymp.configs.module.scheduler.schedule_locker_factory_class=
    
    # 计划任务规则配置加载器接口实例对象, 必须参数
    ymp.configs.module.scheduler.task_config_loader_class=

#### 示例代码


#### One More Thing

YMP不仅提供便捷的Web及其它Java项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入 官方QQ群480374360，一起交流学习，帮助YMP成长！

了解更多有关YMP框架的内容，请访问官网：http://www.ymate.net/