package com.hh.lecturereservation.dto.detail;

import com.hh.lecturereservation.entity.Lecture;
import com.hh.lecturereservation.utils.DateUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class LectureDetail {
    private String title;
    private String description;
    private int capacity;
    private String lectureDate;
    private int currentEnrollment;

    public static LectureDetail toDTO(Lecture lectureEntity) {
        return LectureDetail.builder()
                .title(lectureEntity.getTitle())
                .description(lectureEntity.getDescription())
                .capacity(lectureEntity.getCapacity())
                .lectureDate(DateUtil.removeTAndUntilMinute(lectureEntity.getLectureDate()))
                .currentEnrollment(lectureEntity.getCurrentEnrollment())
                .build();
    }
}
