package cn.idealframework.date;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 宋志宗 on 2022/2/21
 */
public final class DateTimeFormatters {

  private static final Locale LOCALE = Locale.SIMPLIFIED_CHINESE;
  private static final ConcurrentMap<String, ConcurrentMap<Locale, DateTimeFormatter>> FORMATTER_MAP = new ConcurrentHashMap<>();


  @Nonnull
  public static DateTimeFormatter getFormatter(@Nonnull String pattern, @Nonnull Locale locale) {
    ConcurrentMap<Locale, DateTimeFormatter> concurrentMap
      = FORMATTER_MAP.computeIfAbsent(pattern, k -> new ConcurrentHashMap<>());
    return concurrentMap.computeIfAbsent(locale, k -> DateTimeFormatter.ofPattern(pattern, locale));
  }
}
