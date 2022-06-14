# YMATE-MODULE-SCHEDULE

[![Maven Central status](https://img.shields.io/maven-central/v/net.ymate.module/ymate-module-schedule.svg)](https://search.maven.org/artifact/net.ymate.module/ymate-module-schedule)
[![LICENSE](https://img.shields.io/github/license/suninformation/ymate-module-schedule.svg)](https://gitee.com/suninformation/ymate-module-schedule/blob/master/LICENSE)

为 YMP 框架提供基于 Quartz 技术的任务调度服务集成与模块封装。




## Maven包依赖

```xml
<dependency>
    <groupId>net.ymate.module</groupId>
    <artifactId>ymate-module-schedule</artifactId>
    <version>1.0.2</version>
</dependency>
```



## 模块配置

### 配置文件参数说明

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



### 配置注解参数说明

#### @ScheduleConf 

| 配置项                | 描述                                                       |
| --------------------- | ---------------------------------------------------------- |
| enabled               | 模块是否已启用, 默认值：`true`                             |
| lockerFactoryClass    | 调度锁工厂接口实例对象, 若未提供则使用默认实现             |
| providerClass         | 调度服务提供者接口实例对象, 若未提供则使用默认实现         |
| taskConfigLoaderClass | 计划任务规则配置加载器接口实例对象, 若未提供则使用默认实现 |



#### @ScheduleTask

声明一个类为计划任务类，框架启动时将扫描该注解并向模块注册。

| 配置项      | 描述                   |
| ----------- | ---------------------- |
| name        | 任务名称, 默认为类名称 |
| description | 描述                   |



### 默认任务规则配置加载器相关注解

以下注解仅作用于 `taskConfigLoaderClass` 配置项采用默认值时有效。



#### @TaskConfigGroups

计划任务规则配置组集合，是任务规则配置的根级注解，作用于类或包。

| 配置项 | 描述         |
| ------ | ------------ |
| value  | 任务配置集合 |



#### @TaskConfigs

用于定义计划任务规则配置分组。

| 配置项 | 描述             |
| ------ | ---------------- |
| group  | 任务分组名称     |
| value  | 任务规则配置集合 |



#### @TaskConfig 

用于描述计划任务的具体规则配置。

| 配置项 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| id     | 任务唯一标识, 若未提供则采用UUID自动生成<br />*注：若任务唯标识重复则后者将覆盖前者* |
| name   | 计划任务名称或类名称                                         |
| cron   | 任务执行规则表达式                                           |
| params | 任务扩展参数, 采用 `k=v` 格式并支持以 `$xxx` 格式从框架全局参数中获取 `xxx` 的值<br />例：`key1=$value1`  或 `$key1` |



#### @TaskConfigLoader

声明一个类为自定义计划任务规则配置加载器，该类必须是 `ITaskConfigLoader` 接口的实现类，该加载器执行结果将被自动注册到模块的默认任务规则配置加载器中，该注解无参数。



## 模块事件

事件枚举对象 `ScheduleEvent` 包括以下事件类型：

| 事务类型              | 说明               |
| --------------------- | ------------------ |
| SCHEDULE_INITIALIZED  | 调度服务初始化事件 |
| SCHEDULE_STARTED      | 调度服务已启动事件 |
| SCHEDULE_SHUTDOWN     | 调度服务停止事件   |
| TASK_TO_BE_EXECUTED   | 即将执行任务事件   |
| TASK_EXECUTION_VETOED | 任务执行被否决事件 |
| TASK_WAS_EXECUTED     | 任务被执行事件     |



## 示例：

本示例代码将实现从数据库中读取位置记录并通过百度地图 Web 服务接口将坐标点转换为门牌地址后更新记录，该任务设置为每分钟执行一次，代码如下：

```java
@ScheduleTask(name = BaiduReverseGeocodingTask.NAME, 
              description = "基于百度地图WEB服务接口将坐标点转换为门牌地址（每间隔一分钟执行一次）")
@TaskConfig(id = "baidu",
            name = BaiduReverseGeocodingTask.NAME, 
            cron = "0 0/1 * * * ?", 
            params = {"$" + BaiduReverseGeocodingTask.BAIDU_AK})
public class BaiduReverseGeocodingTask extends AbstractScheduleTask {

    private static final Log LOG = LogFactory.getLog(BaiduReverseGeocodingTask.class);

    public static final String NAME = "BaiduReverseGeocodingTask";

    public static final String BAIDU_AK = "baidu_ak";

    public static final String BAIDU_REVERSE_GEOCODING_URL = "https://api.map.baidu.com/reverse_geocoding/v3/?ak=${ak}&output=json&coordtype=wgs84ll&location=${lat},${lon}";

    public BaiduReverseGeocodingTask() {
        super(true);
    }

    @Override
    public void execute(ITaskExecutionContext context) {
        try {
            StopWatcher<Integer> stopWatcher = StopWatcher.watch(() -> {
                String ak = context.getParamStr(BAIDU_AK);
                if (StringUtils.isBlank(ak)) {
                    throw new NullArgumentException(BAIDU_AK);
                }
                IResultSet<PositionLastEntity> resultSet = PositionLastEntity.builder()
                    .errorStatus(0)
                    .build()
                    .find(Page.create().pageSize(50).count(false));
                if (resultSet != null && resultSet.isResultsAvailable()) {
                    List<ReverseGeocodingResult> results = ThreadUtils.executeOnce(resultSet.getResultData()
                            .stream()
                            .map(positionLastEntity -> new ReverseGeocodingRequester(ak, positionLastEntity))
                            .collect(Collectors.toList()));
                    if (results != null && !results.isEmpty()) {
                        BatchSQL batchSQL = BatchSQL.create(Update.create(PositionLastEntity.class)
                                .field(PositionLastEntity.FIELDS.DESCRIPTION)
                                .field(PositionLastEntity.FIELDS.ERROR_STATUS)
                                .field(PositionLastEntity.FIELDS.ERROR_REASON)
                                .field(PositionLastEntity.FIELDS.LAST_MODIFY_TIME)
                                .where(Cond.create().eqWrap(PositionLastEntity.FIELDS.ID)));
                        results.stream()
                                .map(result -> Params.create(result.getDescription(), result.getErrorStatus(), result.getErrorReason(), result.getLastModifyTime(), result.getTid()))
                                .forEach(batchSQL::addParameter);
                        return BatchUpdateOperator.parseEffectCounts(batchSQL.execute());
                    }
                }
                return 0;
            });
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("%s - Effect count: %d, total elapsed time: %d", NAME, stopWatcher.getValue(), stopWatcher.getStopWatch().getTime()));
            }
        } catch (Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(String.format("%s - Exception: %s", NAME, e.getMessage()), RuntimeUtils.unwrapThrow(e));
            }
        }
    }

    public static class ReverseGeocodingResult {

        private final String tid;

        private int errorStatus = 2;

        private String errorReason;

        private String description;

        private long lastModifyTime = System.currentTimeMillis();

        public ReverseGeocodingResult(String tid) {
            this.tid = tid;
        }

        public String getTid() {
            return tid;
        }

        public int getErrorStatus() {
            return errorStatus;
        }

        public void setErrorStatus(int errorStatus) {
            this.errorStatus = errorStatus;
        }

        public String getErrorReason() {
            return errorReason;
        }

        public void setErrorReason(String errorReason) {
            this.errorReason = errorReason;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(long lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }
    }

    public static class ReverseGeocodingRequester implements Callable<ReverseGeocodingResult> {

        private final String ak;

        private final String tid;

        private final GeoPoint point;

        public ReverseGeocodingRequester(String ak, PositionLastEntity positionLast) {
            this.ak = ak;
            this.tid = positionLast.getId();
            this.point = new GeoPoint(parsePoint(positionLast.getLatitude()), parsePoint(positionLast.getLongitude()));
        }

        private float parsePoint(Long origin) {
            if (origin == null) {
                return 0;
            }
            return MathCalcHelper.bind(origin.toString()).scale(6).divide(Math.pow(10, 6)).value().floatValue();
        }

        @Override
        public ReverseGeocodingResult call() throws Exception {
            ReverseGeocodingResult result = new ReverseGeocodingResult(tid);
            if (point.isValidCoordinate() && !point.notInChina()) {
                String serviceUrl = ExpressionUtils.bind(BAIDU_REVERSE_GEOCODING_URL)
                        .set("ak", ak)
                        .set("lat", String.valueOf(point.getLatitude()))
                        .set("lon", String.valueOf(point.getLongitude())).getResult();
                IHttpResponse response = HttpRequestBuilder.create(serviceUrl)
                        .requestTimeout(30000)
                        .charset(StandardCharsets.UTF_8).build().get();
                if (response != null && response.getStatusCode() == HttpClientHelper.HTTP_STATUS_CODE_SUCCESS) {
                    JsonWrapper jsonWrapper = JsonWrapper.fromJson(response.getContent());
                    if (jsonWrapper != null && jsonWrapper.isJsonObject()) {
                        IJsonObjectWrapper objectWrapper = jsonWrapper.getAsJsonObject();
                        if (objectWrapper != null) {
                            Integer status = objectWrapper.getAsInteger("status");
                            if (status != null) {
                                if (status == 0) {
                                    IJsonObjectWrapper resultObjWrapper = objectWrapper.getJsonObject("result");
                                    if (resultObjWrapper != null) {
                                        result.setErrorStatus(1);
                                        result.setDescription(resultObjWrapper.getString("formatted_address"));
                                    }
                                } else {
                                    result.setErrorStatus(3);
                                    result.setErrorReason("BAIDU_ERR_" + status);
                                }
                            }
                        } else {
                            result.setErrorStatus(3);
                        }
                    }
                }
            } else if (LOG.isDebugEnabled()) {
                LOG.debug(String.format("%s - Invalid coordinate %s of Terminal[tid=%S], ignored.", NAME, point, tid));
            }
            return result;
        }
    }
}
```

配置文件中添加如下内容：

```properties
ymp.params.module.schedule.task_cron_baidu=0 0/1 * * * ?
ymp.params.baidu_ak=xxxxxxxx
```



## One More Thing

YMP 不仅提供便捷的 Web 及其它 Java 项目的快速开发体验，也将不断提供更多丰富的项目实践经验。

感兴趣的小伙伴儿们可以加入官方 QQ 群：[480374360](https://qm.qq.com/cgi-bin/qm/qr?k=3KSXbRoridGeFxTVA8HZzyhwU_btZQJ2)，一起交流学习，帮助 YMP 成长！

如果喜欢 YMP，希望得到你的支持和鼓励！

![Donation Code](https://ymate.net/img/donation_code.png)

了解更多有关 YMP 框架的内容，请访问官网：[https://ymate.net](https://ymate.net)