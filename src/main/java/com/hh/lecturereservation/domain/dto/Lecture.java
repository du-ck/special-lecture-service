package com.hh.lecturereservation.domain.dto;

import com.hh.lecturereservation.domain.dto.types.LectureType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class Lecture {
    private Long lectureId;
    private String title;
    private String lecturer;
    private String description;
    private LectureType lectureType;
    private Long capacity;
    private LocalDateTime lectureDate;
    private Long currentEnrollment;
}
