package com.hh.lecturereservation.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    /**
     * LocalDateTime 형태로 그대로 받으면 중간에 들어가는 T 제거
     * + 시 분까지 출력하기 위한 메서드
     * @param localDateTime
     * @return
     */
    public static String removeTAndUntilMinute(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }
}
