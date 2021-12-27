/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework.util.cron;

import cn.idealframework.date.Dates;
import cn.idealframework.lang.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 宋志宗 on 2021/12/22
 */
@SuppressWarnings("unused")
@Getter
@Setter
@CommonsLog
public class CronBuilder {
  private static final String CONNECTOR_CONTINUOUS = "-";
  private static final String CONNECTOR_INTERVAL = "/";
  private static final String CONNECTOR_COMMA = ",";
  private static final int TWO = 2;
  private String year = "*";
  private String week = "?";
  private String month = "*";
  private String day = "*";
  private String hour = "*";
  private String minute = "*";
  private String second = "*";

  protected CronBuilder() {
  }

  @Nonnull
  private static String create(@Nonnull String... strings) {
    if (strings.length == 1) {
      strings = strings[0].split(",");
    }
    StringBuilder sb = new StringBuilder();
    int length = strings.length;
    for (int i = 0; i < length; i++) {
      sb.append(Integer.parseInt(strings[i]));
      if (i < length - 1) {
        sb.append(CONNECTOR_COMMA);
      }
    }
    return sb.toString();
  }

  /**
   * 星期转换
   *
   * @param week 星期对应的数字,如星期一 -> 1; 星期天 -> 7
   * @return QuartzCron 格式的星期数字
   */
  @Nonnull
  private static String weekOf(int week) {
    final int simpleSunday = 7;
    if (week == simpleSunday) {
      return "1";
    } else if (0 < week && week < simpleSunday) {
      return String.valueOf(week + 1);
    } else {
      throw new RuntimeException("星期值必须在1-7之间");
    }
  }

  /**
   * 星期转换
   *
   * @param weekStr 星期对应的字符串,如星期一 -> 1; 星期天 -> 7
   * @return QuartzCron 格式的星期数字
   */
  @Nonnull
  private static String weekOf(String weekStr) {
    int week = Integer.parseInt(weekStr);
    return weekOf(week);
  }

  /**
   * <pre>
   * 根据date获取一个最近有效的QuartzCronBuilder
   * 如果date是一个已经过去的时间,则顺延到下一个有效的时间
   * </pre>
   *
   * @param date Date
   * @return QuartzCronBuilder
   */
  public CronBuilder date(Date date) {
    date = calculateRecentDate(date);
    return recentDate(date);
  }

  /**
   * 根据date获取一个确定时间的QuartzCronBuilder
   *
   * @param date Date
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder determineDate(@Nonnull Date date) {
    if (date.getTime() < System.currentTimeMillis()) {
      log.debug("时间已过时！");
      throw new RuntimeException("时间已过时！");
    }
    return recentDate(date);
  }

  /**
   * 根据时分秒获取一个最近有效的QuartzCronBuilder
   *
   * @param hour   时
   * @param minute 分
   * @param second 秒
   * @return QuartzCronBuilder
   */
  public CronBuilder date(int hour, int minute, int second) {
    Date date = calculateRecentDate(hour, minute, second);
    return recentDate(date);
  }

  /**
   * 根据时分秒获取一个最近有效的QuartzCronBuilder
   *
   * @param hour   时
   * @param minute 分
   * @param second 秒
   * @return QuartzCronBuilder
   */
  public CronBuilder date(String hour, String minute, String second) {
    return date(Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
  }

  /**
   * 根据最近有效时间生成QuartzCronBuilder
   */
  private CronBuilder recentDate(Date date) {
    String format = Dates.format(date, "yyyy,MM,dd,HH,mm,ss");
    log.debug("format = " + format);
    String[] split = StringUtils.split(format, CONNECTOR_COMMA);
    assert split != null;
    return new CronBuilder()
      .year(split[0])
      .month(split[1])
      .day(split[2])
      .hours(split[3])
      .minutes(split[4])
      .seconds(split[5]);
  }

  /**
   * 计算最近的一个有效时间
   */
  @Nonnull
  private Date calculateRecentDate(Date date) {
    String format = Dates.format(date, "HH,mm,ss");
    String[] split = StringUtils.split(format, CONNECTOR_COMMA);
    assert split != null;
    int hour = Integer.parseInt(split[0]);
    int minute = Integer.parseInt(split[1]);
    int second = Integer.parseInt(split[2]);
    return calculateRecentDate(hour, minute, second);
  }

  /**
   * 计算最近的一个有效时间
   */
  @Nonnull
  private Date calculateRecentDate(int hour, int minute, int second) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    Date executeDate = calendar.getTime();
    // 如果启动时间在当前时间之前,则顺延一天
    if (executeDate.before(new Date())) {
      Calendar startDT = Calendar.getInstance();
      startDT.setTime(executeDate);
      startDT.add(Calendar.DAY_OF_MONTH, 1);
      return startDT.getTime();
    }
    return executeDate;
  }

  /**
   * <pre>
   * Joint.CONTINUOUS
   * 此时years只允许传入两个值,第一个代表起始年,第二个代表结束年.例如:
   * ["2020","2025"] 从2020到2025的每年
   * Joint.INTERVAL
   * 此时years只允许传入两个值,第一个代表起始年,第二个代表间隔年份.例如:
   * ["2020","3"] 从2020年开始每隔3年 -> 2020、2023、2026...
   * Joint.EVERY
   * 此时years传值不限
   * 个数为0则代表每一年
   * 个数不为0,则其中对应的每年都将执行
   * </pre>
   *
   * @param joint {@link Joint}
   * @param years 年份数组
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder year(Joint joint, String... years) {
    if (years == null || years.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (years.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.year = Integer.parseInt(years[0]) + CONNECTOR_CONTINUOUS + years[1];
      } else if (joint == Joint.INTERVAL) {
        if (years.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.year = Integer.parseInt(years[0])
          + CONNECTOR_INTERVAL
          + Integer.parseInt(years[1]);
      } else {
        this.year = create(years);

      }
    }
    return this;
  }

  /**
   * <pre>
   * 如果传入的是年份数组,则其中对应的每年都将执行
   * 如果传入表达式则只支持一个,例如:
   *   * 每年
   *   2018-2020 2018到2020每年
   *   2020/3 从2020年开始每隔3年 -> 2020、2023、2026...
   * </pre>
   *
   * @param years 年份
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder year(String... years) {
    return year(Joint.EVERY, years);
  }

  /**
   * <pre>
   * Joint.CONTINUOUS
   * 此时weeks只允许传入两个值,第一个代表起始日,第二个代表结束日.例如:
   * ["1","5"] 从周一到周五的每天
   * Joint.INTERVAL
   * 此时weeks只允许传入两个值,第一个代表起始日,第二个代表间隔天数.例如:
   * ["1","1"] 从周一开始每隔1天 -> 周一/周三/周五/周日
   * Joint.EVERY
   * 此时weeks传值为 1~7
   * 个数为0则代表一周中的每天
   * 个数不为0,则其中对应的每天都将执行
   * </pre>
   *
   * @param joint {@link Joint}
   * @param weeks 星期数组
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder week(Joint joint, String... weeks) {
    if (weeks == null || weeks.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (weeks.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.week = weekOf(weeks[0]) + CONNECTOR_CONTINUOUS + weekOf(weeks[1]);
        this.day = "?";
      } else if (joint == Joint.INTERVAL) {
        if (weeks.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.week = weekOf(weeks[0]) + CONNECTOR_INTERVAL + weeks[1];
        this.day = "?";
      } else {
        if (weeks.length == 1) {
          String week = weeks[0];
          if (StringUtils.contains(week, CONNECTOR_COMMA)) {
            String[] split = StringUtils.split(week, CONNECTOR_COMMA);
            if (split.length > 1) {
              return week(split);
            }
          } else if (StringUtils.contains(week, CONNECTOR_CONTINUOUS)) {
            String[] split = StringUtils.split(week, CONNECTOR_CONTINUOUS);
            if (split.length == TWO) {
              return week(Joint.CONTINUOUS, split[0], split[1]);
            }
          } else if (StringUtils.contains(week, CONNECTOR_INTERVAL)) {
            String[] split = StringUtils.split(week, CONNECTOR_INTERVAL);
            if (split.length == TWO) {
              return week(Joint.INTERVAL, split[0], split[1]);
            }
          }
        }
        String[] strings = new String[weeks.length];
        for (int i = 0; i < weeks.length; i++) {
          strings[i] = weekOf(weeks[i]);
        }
        this.week = create(strings);
        this.day = "?";
      }
    }
    return this;
  }

  /**
   * <pre>
   * 如果传入的是星期数组,则其中对应的每天都将执行
   * 如果传入表达式则只支持一个,例如:
   *  * 周一~周日
   *  1-3 每周一到周三
   *  1/1 从周一开始每隔1天 -> 周一/周三/周五/周日
   * </pre>
   *
   * @param weeks 1~7
   * @return QuartzCronBuilder
   */
  public CronBuilder week(String... weeks) {
    return week(Joint.EVERY, weeks);
  }

  /**
   * 某一个月中的最后一个周几,例如6代表这个月的最后一个星期六
   *
   * @param week 1~7
   * @return QuartzCronBuilder
   */
  public CronBuilder lastWeekDayOfMonth(String week) {
    this.week = weekOf(week) + "L";
    return this;
  }

  /**
   * <pre>
   * 允许值 1-12
   * Joint.CONTINUOUS
   * 此时months只允许传入两个值,第一个代表起始月,第二个代表结束月.例如:
   * ["1","8"] 从一月到八月的每月
   * Joint.INTERVAL
   * 此时years只允许传入两个值,第一个代表起始月,第二个代表间隔月份.例如:
   * ["1","3"] 从1月开始每隔3月 -> 1、4、7、10
   * Joint.EVERY
   * 此时months传值不限
   * 个数为0则代表每一月
   * 个数不为0,则其中对应的每月都将执行
   * </pre>
   *
   * @param joint  {@link Joint}
   * @param months 月份数组
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder month(Joint joint, String... months) {
    if (months == null || months.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (months.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.month = Integer.parseInt(months[0])
          + CONNECTOR_CONTINUOUS
          + Integer.parseInt(months[1]);
      } else if (joint == Joint.INTERVAL) {
        if (months.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.month = Integer.parseInt(months[0])
          + CONNECTOR_INTERVAL
          + Integer.parseInt(months[1]);
      } else {
        this.month = create(months);

      }
    }
    return this;
  }

  /**
   * <pre>
   * 允许值 1-12
   * 如果传入的是月份数组,则其中对应的每月都将执行
   * 如果传入表达式则只支持一个,例如:
   *   * 每月
   *   1-8 一月到八月每月执行
   *   1/3 从1月开始每隔3月 -> 1、4、7、10
   * </pre>
   *
   * @param months 月份
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder month(String... months) {
    return month(Joint.EVERY, months);
  }

  /**
   * <pre>
   * 允许值 1-31
   * Joint.CONTINUOUS
   * 此时days只允许传入两个值,第一个代表起始日,第二个代表结束日.例如:
   * ["1","20"] 从1号到20号的每天
   * Joint.INTERVAL
   * 此时days只允许传入两个值,第一个代表起始日,第二个代表间隔天数.例如:
   * ["1","3"] 从1号开始每隔3天 -> 1、4、7、10...28、31
   * Joint.EVERY
   * 此时days传值不限
   * 个数为0则代表每一天
   * 个数不为0,则其中对应的每天都将执行
   * </pre>
   *
   * @param joint {@link Joint}
   * @param days  日数组
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder day(Joint joint, String... days) {
    if (days == null || days.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (days.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.day = Integer.parseInt(days[0])
          + CONNECTOR_CONTINUOUS + Integer.parseInt(days[1]);
        this.week = "?";
      } else if (joint == Joint.INTERVAL) {
        if (days.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.day = Integer.parseInt(days[0])
          + CONNECTOR_INTERVAL + Integer.parseInt(days[1]);
        this.week = "?";
      } else {
        this.day = create(days);
        this.week = "?";
      }
    }
    return this;
  }

  /**
   * <pre>
   * 允许值 1-31
   * 如果传入的是日数组,则其中对应的每天都将执行
   * 如果传入表达式则只支持一个,例如:
   *   * 每月
   *   1-8 一号到八号每天执行
   *   1/3 从1号开始每隔3天 -> 1、4、7、10...28、31
   * </pre>
   *
   * @param days 日
   * @return QuartzCronBuilder
   */
  public CronBuilder day(String... days) {
    return day(Joint.EVERY, days);
  }

  /**
   * 某一个日期最近的工作日
   *
   * @param day 1~31
   * @return QuartzCronBuilder
   */
  public CronBuilder lastWorkingDay(String day) {
    this.day = Integer.parseInt(day) + "W";
    this.week = "?";
    return this;
  }

  /**
   * 某个月的最后一天
   *
   * @return QuartzCronBuilder
   */
  public CronBuilder lastDay() {
    this.day = "L";
    this.week = "?";
    return this;
  }

  /**
   * <pre>
   * 允许值 0-23
   * Joint.CONTINUOUS
   * 此时hours只允许传入两个值,第一个代表起始时,第二个代表结束时.例如:
   * ["1","20"] 从1点到20点的每小时
   * Joint.INTERVAL
   * 此时hours只允许传入两个值,第一个代表起始时,第二个代表间隔小时.例如:
   * ["1","3"] 从凌晨1点开始每隔3小时 -> 1、4、7、10...19、22
   * Joint.EVERY
   * 此时days传值不限
   * 个数为0则代表每一天
   * 个数不为0,则其中对应的每天都将执行
   * </pre>
   *
   * @param joint {@link Joint}
   * @param hours 小时数组
   * @return QuartzCronBuilder
   */
  @SuppressWarnings("all")
  public CronBuilder hours(Joint joint, String... hours) {
    if (hours == null || hours.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (hours.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.hour = Integer.parseInt(hours[0])
          + CONNECTOR_CONTINUOUS + Integer.parseInt(hours[1]);
      } else if (joint == Joint.INTERVAL) {
        if (hours.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.hour = Integer.parseInt(hours[0])
          + CONNECTOR_INTERVAL + Integer.parseInt(hours[1]);
      } else {
        this.hour = create(hours);

      }
    }
    return this;
  }

  /**
   * <pre>
   * 允许值 0-23
   * 如果传入的是小时数组,则其中对应的每小时都将执行
   * 如果传入表达式则只支持一个,例如:
   *   * 每小时
   *   1-8 一点到八点每小时执行
   *   1/3 从凌晨1点开始每隔3小时 -> 1、4、7、10...19、22
   * </pre>
   *
   * @param hours 小时
   * @return QuartzCronBuilder
   */
  public CronBuilder hours(String... hours) {
    return hours(Joint.EVERY, hours);
  }

  @SuppressWarnings("all")
  public CronBuilder minutes(Joint joint, String... minutes) {
    if (minutes == null || minutes.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (minutes.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.minute = Integer.parseInt(minutes[0])
          + CONNECTOR_CONTINUOUS + Integer.parseInt(minutes[1]);
      } else if (joint == Joint.INTERVAL) {
        if (minutes.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.minute = Integer.parseInt(minutes[0])
          + CONNECTOR_INTERVAL + Integer.parseInt(minutes[1]);
      } else {
        this.minute = create(minutes);

      }
    }
    return this;
  }

  public CronBuilder minutes(String... minutes) {
    return minutes(Joint.EVERY, minutes);
  }

  public CronBuilder seconds(Joint joint, String... seconds) {
    if (seconds == null || seconds.length == 0) {
      return this;
    } else {
      if (joint == Joint.CONTINUOUS) {
        if (seconds.length != TWO) {
          log.debug("使用连续符时 years参数个数必须为2");
          throw new RuntimeException("使用连续符时 years参数个数必须为2");
        }
        this.second = Integer.parseInt(seconds[0])
          + CONNECTOR_CONTINUOUS + Integer.parseInt(seconds[1]);
      } else if (joint == Joint.INTERVAL) {
        if (seconds.length != TWO) {
          log.debug("使用间隔符时 years参数个数必须为2");
          throw new RuntimeException("使用间隔符时 years参数个数必须为2");
        }
        this.second = Integer.parseInt(seconds[0])
          + CONNECTOR_INTERVAL + Integer.parseInt(seconds[1]);
      } else {
        this.second = create(seconds);

      }
    }
    return this;
  }

  public CronBuilder seconds(String... seconds) {
    return seconds(Joint.EVERY, seconds);
  }

  @Nonnull
  public Cron build() {
    return new Cron(this.toString());
  }

  @Override
  public String toString() {
    final String failCron = "* * * * * * *";
    String cron = this.second + " "
      + this.minute + " "
      + this.hour + " "
      + this.day + " "
      + this.month + " "
      + this.week + " "
      + this.year;
    if (failCron.equals(cron)) {
      throw new RuntimeException("不合法的表达式 -> * * * * * * *");
    }
    return cron;
  }

  /**
   * 连接符
   */
  public enum Joint {
    /**
     * 连续
     */
    CONTINUOUS,
    /**
     * 间隔
     */
    INTERVAL,
    /**
     * 全部使用
     */
    EVERY,
  }
}
