package cn.idealframework.cache;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/1/25
 */
public class CacheUtils {
  public static final String NULL_VALUE = "$$ideal$$cache$$null$$value$$";

  public static boolean isNullValue(@Nullable Object value) {
    return value == null || NULL_VALUE.equals(value);
  }

  public static boolean isNotNulValue(@Nullable Object value) {
    return !isNullValue(value);
  }
}
