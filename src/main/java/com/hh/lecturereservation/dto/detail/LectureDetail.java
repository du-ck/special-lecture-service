package com.hh.lecturereservation.dto.detail;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class LectureDetail {
    private String title;
    private String description;
    private int capacity;
    private LocalDateTime lectureDate;
    private int current_enrollment;
}
