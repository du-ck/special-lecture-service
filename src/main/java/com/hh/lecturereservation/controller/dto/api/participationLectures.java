package com.hh.lecturereservation.controller.dto.api;

import com.hh.lecturereservation.domain.dto.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class participationLectures {
    @Builder
    @Getter
    public static class Request {

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long userId;
        private String userName;
        private List<Lecture> lectures;
    }
}
