package cn.idealframework.operation;

import lombok.Setter;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/3/30
 */
@Setter
public class OperatorContextImpl implements OperatorContext {
  private String platform;
  private long userId;
  private String username;
  private Long tenantId;


  @Nullable
  @Override
  public String platform() {
    return platform;
  }

  @Override
  public long getUserId() {
    return userId;
  }

  @Nullable
  @Override
  public String getUsername() {
    return username;
  }

  @Nullable
  @Override
  public Long getTenantId() {
    return tenantId;
  }
}
