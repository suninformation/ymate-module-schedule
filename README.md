# YMATE-MODULE-SCHEDULE

[![Maven Central status](https://img.shields.io/maven-central/v/net.ymate.module/ymate-module-schedule.svg)](https://search.maven.org/artifact/net.ymate.module/ymate-module-schedule)
[![LICENSE](https://img.shields.io/github/license/suninformation/ymate-module-schedule.svg)](https://gitee.com/suninformation/ymate-module-schedule/blob/master/LICENSE)

为 YMP 框架提供基于 Quartz 技术的任务调度服务集成与模块封装。




## Maven包依赖

```xml
<dependency>
    <groupId>net.ymate.module</groupId>
    <artifactId>ymate-module-schedule</artifactId>
    <version>1.0.1</version>
</dependency>
```



## 模块配置参数说明

```properties
#-------------------------------------
# module.schedule 调度模块初始化参数
#-------------------------------------

# 调度模块是否已启用, 默认值: true
ymp.configs.module.schedule.enabled=

# 调度锁工厂接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleLockerFactory
ymp.configs.module.schedule.locker_factory_class=

# 调度服务提供者接口实例对象, 若未提供则使用默认实现: net.ymate.module.schedule.impl.DefaultScheduleProvider
ymp.configs.module.schedule.provider_class=

# 计划任务规则配置加载器接口实例对象, 默认值: net.ymate.module.schedule.impl.DefaultTaskConfigLoader
ymp.configs.module.schedule.task_config_loader_class=

# 当使用默认规则配置加载器时, 用于为指定任务设置调度规则(当值为disabled时将禁用该任务), 此配置优先于注解配置
ymp.params.module.schedule.task_cron_<TASK_ID>=<TASK_CRON|disabled>
```



## One More Thing

YMP 不仅提供便捷的 Web 及其它 Java 项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入官方 QQ 群：[480374360](https://qm.qq.com/cgi-bin/qm/qr?k=3KSXbRoridGeFxTVA8HZzyhwU_btZQJ2)，一起交流学习，帮助 YMP 成长！

如果喜欢 YMP，希望得到你的支持和鼓励！

![Donation Code](https://ymate.net/img/donation_code.png)

了解更多有关 YMP 框架的内容，请访问官网：[https://ymate.net](https://ymate.net)