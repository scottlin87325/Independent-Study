package com.scott.chat.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class TimeUtils {
    
    private static final Logger logger = Logger.getLogger(TimeUtils.class.getName());
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static final ZoneId TAIPEI_ZONE = ZoneId.of("Asia/Taipei");
    
    /**
     * 獲取當前時間字符串
     */
    public String getCurrentTimeString() {
        return LocalDateTime.now(TAIPEI_ZONE).format(formatter);
    }
    
    /**
     * 格式化時間
     */
    public String formatTime(LocalDateTime time) {
        return time.format(formatter);
    }
    
    /**
     * 解析時間字符串
     */
    public LocalDateTime parseTime(String timeStr) {
        try {
            return LocalDateTime.parse(timeStr, formatter);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing time string: " + timeStr, e);
            throw new IllegalArgumentException("Invalid time format");
        }
    }
    
    /**
     * 檢查時間字符串格式是否有效
     */
    public boolean isValidTimeFormat(String timeStr) {
        try {
            LocalDateTime.parse(timeStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 獲取相對時間描述
     */
    public String getRelativeTimeDescription(String timeStr) {
        LocalDateTime time = parseTime(timeStr);
        LocalDateTime now = LocalDateTime.now(TAIPEI_ZONE);
        
        long minutes = ChronoUnit.MINUTES.between(time, now);
        
        if (minutes < 1) {
            return "剛剛";
        } else if (minutes < 60) {
            return minutes + "分鐘前";
        } else if (minutes < 24 * 60) {
            long hours = minutes / 60;
            return hours + "小時前";
        } else {
            long days = minutes / (24 * 60);
            if (days < 7) {
                return days + "天前";
            } else if (days < 30) {
                long weeks = days / 7;
                return weeks + "週前";
            } else if (days < 365) {
                long months = days / 30;
                return months + "個月前";
            } else {
                return formatTime(time);
            }
        }
    }
    
    /**
     * 比較兩個時間先後
     */
    public int compareTime(String time1, String time2) {
        LocalDateTime dt1 = parseTime(time1);
        LocalDateTime dt2 = parseTime(time2);
        return dt1.compareTo(dt2);
    }
    
    /**
     * 檢查時間是否在指定範圍內
     */
    public boolean isTimeInRange(String timeStr, int rangeMinutes) {
        LocalDateTime time = parseTime(timeStr);
        LocalDateTime now = LocalDateTime.now(TAIPEI_ZONE);
        long diffMinutes = ChronoUnit.MINUTES.between(time, now);
        return Math.abs(diffMinutes) <= rangeMinutes;
    }
    
    /**
     * 獲取未來時間
     */
    public String getFutureTime(int minutes) {
        LocalDateTime futureTime = LocalDateTime.now(TAIPEI_ZONE).plusMinutes(minutes);
        return formatTime(futureTime);
    }
    
    /**
     * 獲取過去時間
     */
    public String getPastTime(int minutes) {
        LocalDateTime pastTime = LocalDateTime.now(TAIPEI_ZONE).minusMinutes(minutes);
        return formatTime(pastTime);
    }
}