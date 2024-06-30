package com.hh.lecturereservation.controller.dto.api;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import lombok.*;

import java.time.LocalDateTime;

public class Apply {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long studentId;
        private Long lectureId;
    }

    @Builder
    @Getter
    public static class Response {
        private Long participantId;
        private Long studentId;
        private String studentName;
        private Lecture lecture;
    }
}
