<h1>更新日志</h1>

# developer

### ⭐ New Features

- 放宽Lists和Sets中部分方法对null的限制
- 为集合工具类添加chunkedLimit方法
- 新增重试工具 Retryer 和AsyncRetryer
- 出现InvalidDataAccessApiUsageException时打印整个异常栈
- 为rabbitmq事件代理添加zstd数据压缩功能, 节省内存以及带宽的使用
- 分布式缓存支持通过配置来控制是否缓存null值
- 完善基于Redis的snowflake工厂
- 新增IndiscriminateEventListener, 可以从事件代理中监听系统中产生的所有事件

### 🐞 Bug Fixes

- 修复未启用事件消费幂等时服务启动失败的问题
- 修复joiner传入的集合为空时抛出异常的问题

### 🔨 Dependency Upgrades

- spring.boot.version -> 2.6.5
- spring.cloud.version -> 2021.0.1
- vertx.version -> 4.2.6
- zstd-jni.version -> 1.5.2-2
- redisson.version -> 3.17.0

### ⚠️ Compatibility

- 修改Reactors类中部分方法的名称, 避免编译时因歧义而导致的编译失败

# 2.6.2.211227

### ⭐ New Features

- trace log默认不记录响应结果
- Result类返回失败将会反映在http状态码上
- `BasicResult`添加`onFailureThrow`方法, 当返回失败时可调用此方法抛出`ResultException`以在服务间传递异常.
- 为`Result`和`PageResult`添加`getOrThrow`方法
- 添加`EventSuppliers`类简化事件发布
- Lists新增ofArray方法
- 统计时间维度枚举类中新增枚举值HOUR

### 🐞 Bug Fixes

- 修复部分手机号码无法通过正则校验的问题
- 修复Joiner中连接符长度大于1时返回结果与预期不一致的问题
- 修复未引入ideal-event模块时可能导致服务启动失败的问题
- 修复事件消息结构错误情况下无限抛异常的问题

### 🔨 Dependency Upgrades

- 添加easypoi版本管理
- 添加commons-net依赖管理
- spring-boot升级到2.6.2
- spring-cloud升级到2021.0.0
- kotlin升级到1.6.10
- vertx升级到4.2.3
- redisson升级到3.16.6
- zstd-jni升级到1.5.0-5

### ⚠️ Compatibility

- `PageResult`中的page方法名称修改为of
- 移除authentication模块, 因为ideal-cloud-iam已经以无侵入的方式实现该模块的功能了

# 2.5.6.2021.11.05

### ⭐ New Features

- 通过Reactors创建webClient支持链路日志埋点
- 集合类新增chunked方法, 用于集合分块
- Result新增create方法
- Maps工具类添加更多的of方法
- 新增Pair类
- 添加jpa converter

### 🐞 Bug Fixes

- 修复RabbitMQ event broker启动时无法自动创建exchange的问题

### 🔨 Dependency Upgrades

- vertx -> 4.2.1
- redisson -> 3.16.4

# 2.5.6.2021.10.25

### ⭐ New Features

- 为RabbitMQ事件代理添加本地开发模式. 通过`ideal.event.broker.rabbit.enable-local-model`进行配置, 默认`false`不开启.
  本地开发模式的Listener会在RabbitMQ创建一个随机名称的自动删除队列, 以支持多个客户端消费相同的消息.
- `ArrayUtils`、`StringUtils`、`RandomUtils`、`RandomStringUtils`添加一些新方法.

### 🐞 Bug Fixes

- 修复Joiner.joinSkipNull()首个元素为null时不能正确跳过的问题
- Joiner.join()出现null元素时,以空字符串拼接而非字符串`null`

### 🔨 Dependency Upgrades

- spring boot升级到 2.5.6

# 2.5.5.2021.10.21

### ⚠️ Compatibility

- `EventHeaders`中的`value`类型修改为`String`

### ⭐ New Features

- 添加cron工具类`Cron`和`CronExpression`

### 🐞 Bug Fixes

### 🔨 Dependency Upgrades

# 2.5.5.20211020

### ⚠️ Compatibility

- `EventMessageTuple`更名为`EventTuple`，事件类型修改为`EventSupplier`。
- `NullableEventMessageTuple`更名为`NullableEventTuple`，事件类型修改为`EventSupplier`。
- 移除`EventPublisher`原有方法，新增`void publish(@Nonnull Collection<EventSupplier> suppliers);`。

### ⭐ New Features

- `EventPublisher`发布事件现不区分`EventMessage`和`EventMessageBuilder`，统一发布他们的父接口`EventSupplier`。
- `DomainEvent`继承接口`EventSupplier`，现可直接发布`DomainEvent`的实现类。因为`DomainEvent`
  中不包含事件头，因此在需要携带事件头的场景仍需通过`EventMessage`或`EventMessageBuilder`实现。

### 🐞 Bug Fixes

### 🔨 Dependency Upgrades

# 2.5.5.20211012

First release

