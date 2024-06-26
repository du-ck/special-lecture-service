package com.hh.lecturereservation.controller.dto.api;

import com.hh.lecturereservation.controller.dto.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class Lectures {
    @Builder
    @Getter
    public static class Request {

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private List<Lecture> lectures;
    }
}
