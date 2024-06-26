package com.hh.lecturereservation.controller.dto.api;

import lombok.*;

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
        private String userId;
    }
}
