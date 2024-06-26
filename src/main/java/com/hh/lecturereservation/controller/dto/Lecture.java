package com.hh.lecturereservation.controller.dto;

import com.hh.lecturereservation.domain.entity.LectureEntity;
import com.hh.lecturereservation.utils.DateUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Lecture {
    private Long lectureId;
    private String title;
    private String lecturer;
    private String description;
    private Long capacity;
    private String lectureDate;
    private Long currentEnrollment;

    public static Lecture toDto(LectureEntity lectureEntity) {
        return Lecture.builder()
                .lectureId(lectureEntity.getLectureId())
                .title(lectureEntity.getTitle())
                .description(lectureEntity.getDescription())
                .lecturer(lectureEntity.getLecturer())
                .capacity(lectureEntity.getCapacity())
                .lectureDate(DateUtils.removeTAndUntilMinute(lectureEntity.getLectureDate()))
                .currentEnrollment(lectureEntity.getCurrentEnrollment())
                .build();
    }

    public static List<Lecture> toDtoList(List<LectureEntity> lectureEntityList) {
        return lectureEntityList.stream().map(m -> Lecture.toDto(m)).toList();
    }
}
