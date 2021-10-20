# 兼容性变更

## 2.5.5.20211020

### ⚠️ Compatibility

- `EventMessageTuple`更名为`EventTuple`，事件类型修改为`EventSupplier`。
- `NullableEventMessageTuple`更名为`NullableEventTuple`，事件类型修改为`EventSupplier`。
- 移除`EventPublisher`原有方法，新增`void publish(@Nonnull Collection<EventSupplier> suppliers);`。

