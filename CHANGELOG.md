# developer

### ⚠️ Compatibility

- `EventMessageTuple`更名为`EventTuple`，事件类型修改为`EventSupplier`
- `NullableEventMessageTuple`更名为`NullableEventTuple`，事件类型修改为`EventSupplier`
- 移除`EventPublisher`原有方法，新增`void publish(@Nonnull Collection<EventSupplier> suppliers);`

### ⭐ New Features

- `EventPublisher`发布事件现不区分`EventMessage`和`EventMessageBuilder`，统一发布他们的父接口`EventSupplier`

### 🐞 Bug Fixes

### 🔨 Dependency Upgrades



# 2.5.5.20211012

First release

