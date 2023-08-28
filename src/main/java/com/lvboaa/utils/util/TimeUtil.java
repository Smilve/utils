package com.lvboaa.utils.util;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static int TYPE_YEAR = 1;

    public static int TYPE_MONTH = 2;

    public static int TYPE_DAY = 3;

    public static int TYPE_HOUR = 4;

    public static int TYPE_MINUTE = 5;

    public static int TYPE_SECOND = 6;

    //默认东八区，北京时间
    public static ZoneOffset bj_zone = ZoneOffset.of("+8");

    /**
     * 年月日
     */
    public static String DateFormat_YMD = "yyyy-MM-dd";

    /**
     * 年月日-时分秒（24小时制）
     */
    public static String DateFormat_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 年月日-时分秒（12小时制）
     */
    public static String DateFormat_YMDHM_12 = "yyyy-MM-dd hh:mm";

    /**
     * 年月日-时分秒（24小时制）
     */
    public static String DateFormat_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年/月/日-时分秒（24小时制）
     */
    public static String DateFormat_YMDHMS2 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 年月日-时分秒（12小时制）
     */
    public static String DateFormat_YMDHMS_12 = "yyyy-MM-dd hh:mm:ss";

    /**
     * 年月
     */
    public static String DateFormat_YM = "yyyy-MM";

    /**
     * @return
     * @Author recivejt
     * @Description //Instant转Date
     * @Date 15:37 2019/3/8
     * @Param
     **/
    public static Date instantToDate(Instant instant) {
        try {
            return new Date(instant.toEpochMilli());
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * @return
     * @Author recivejt
     * @Description //Date转Instant
     * @Date 15:37 2019/3/8
     * @Param
     **/
    public static Instant dateToInstant(Date date) {
        if (null == date) {
            return Instant.now();
        }
        return date.toInstant();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant, ZoneOffset defaultZone) {
        if (null != defaultZone) {
            return LocalDateTime.ofInstant(instant, defaultZone);
        }
        return LocalDateTime.ofInstant(instant, bj_zone);
    }

    public static Instant localDateTimeToInstant(LocalDateTime localDateTime, ZoneOffset defaultZone) {
        if (null != defaultZone) {
            return localDateTime.toInstant(defaultZone);
        }
        return localDateTime.toInstant(bj_zone);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneOffset defaultZone) {
        if (null != defaultZone) {
            return instantToDate(localDateTime.toInstant(defaultZone));
        }
        return instantToDate(localDateTime.toInstant(bj_zone));
    }

    public static LocalDateTime dateToLocalDateTime(Date date, ZoneOffset defaultZone) {
        return instantToLocalDateTime(dateToInstant(date), defaultZone);
    }

    public static String dateToString(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd";
        }

        if (null == date) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String sDate = sdf.format(date);
        return sDate;
    }

    public static String dateToString(Date date) {
        return dateToString(date, null);
    }


    /**
     * @return
     * @Author recivejt
     * @Description //获取目标时间
     * @Date 16:21 2019/3/19
     * @Param cValueEnd：偏移单位
     * sourceTime:原时间
     * num：偏移量
     **/
    public static LocalDateTime getTargetTime(char cValueEnd, LocalDateTime sourceTime, long num) {
        LocalDateTime targetTime = LocalDateTime.now().minusMonths(1);
        switch (cValueEnd) {
            case 'D':
                targetTime = sourceTime.minusDays(num);
                break;
            case 'M':
                targetTime = sourceTime.minusMonths(num);
                break;
            case 'Y':
                targetTime = sourceTime.minusYears(num);
                break;
            default:
                break;
        }
        return targetTime;
    }

    public static Date getStartOfDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        return day.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        return day.getTime();
    }

    public static Date getStartOfMoth(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.DAY_OF_MONTH, 1);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        return day.getTime();
    }

    public static Date getEndOfMoth(Date date) {
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.DATE, 1);
        day.roll(Calendar.DATE, -1);
        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        return day.getTime();
    }


    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个日期之间的相差月数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 相差月数
     */
    public static Integer getDifMonth(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int month = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result);
    }

    /**
     * 工龄
     *
     * @param hireDate 入职日期
     * @return 工龄
     */
    public static String getWorkYears(Date hireDate) {

        int difMonth = getDifMonth(hireDate, new Date());

        int year = difMonth / 12;
        int month = difMonth % 12;

        return (year == 0 ? "" : (year + "年")) + (month == 0 ? "" : month + "个月");
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime targetTime = getTargetTime('M', localDateTime, 11);

        System.out.println(getWorkYears(localDateTimeToDate(targetTime, null)));
    }


    /**
     * 将String转化为LocalDateTime
     *
     */
    public static LocalDateTime stringToLocalDateTime(String dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime updateTime= LocalDateTime.parse(dateTime, dateTimeFormatter);
        return updateTime;
    }


    /**
     * 计算两个string类型的时间差几个小时
     *
     */
    public static Long getDifHours(String start, String end, String format) {
        LocalDateTime startTime= stringToLocalDateTime(start, format);
        LocalDateTime endTime= stringToLocalDateTime(end, format);
        Duration duration = Duration.between(startTime,endTime);
        return duration.toHours();
    }


    /**
     * 计算两个Date类型的时间差几个小时
     *
     */
    public static Long getDifHours(Date start, Date end) {
        LocalDateTime startTime= dateToLocalDateTime(start, null);
        LocalDateTime endTime= dateToLocalDateTime(end, null);
        Duration duration = Duration.between(startTime,endTime);
        return duration.toHours();
    }

    /**
     * 计算两个LocalDateTime对象之间的小时差
     * @param start 开始时间
     * @param end 结束时间
     * @return 两个时间之间的小时差，如果开始时间在结束时间之后，返回0
     */
    public static int hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            return 0;
        }
        long hours = ChronoUnit.HOURS.between(start, end);
        return (int) hours;
    }
}
