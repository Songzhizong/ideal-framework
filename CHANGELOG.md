<h1>更新日志</h1>

# developer

### ⭐ New Features

- 使用RabbitMQ作为事件代理时, 支持自动删除队列. 通过`ideal.event.broker.rabbit.auto-delete-queue`进行配置, 默认`false`不开启.

### 🐞 Bug Fixes

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
- `DomainEvent`继承接口`EventSupplier`，现可直接发布`DomainEvent`的实现类。因为`DomainEvent`中不包含事件头，因此在需要携带事件头的场景仍需通过`EventMessage`或`EventMessageBuilder`实现。

### 🐞 Bug Fixes

### 🔨 Dependency Upgrades



# 2.5.5.20211012

First release

