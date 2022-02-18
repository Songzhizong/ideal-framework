# 兼容性变更

# developer

### ⚠️ Compatibility

- 修改Reactors类中部分方法的名称, 避免编译时因歧义而导致的编译失败

# 2.6.2.211227

### ⚠️ Compatibility

- `PageResult`中的page方法名称修改为of
- 移除authentication模块, 因为ideal-cloud-iam已经以无侵入的方式实现该模块的功能了

# 2.5.5.2021.10.21

### ⚠️ Compatibility

- `EventHeaders`中的`value`类型修改为`String`

## 2.5.5.20211020

### ⚠️ Compatibility

- `EventMessageTuple`更名为`EventTuple`，事件类型修改为`EventSupplier`。
- `NullableEventMessageTuple`更名为`NullableEventTuple`，事件类型修改为`EventSupplier`。
- 移除`EventPublisher`原有方法，新增`void publish(@Nonnull Collection<EventSupplier> suppliers);`。

