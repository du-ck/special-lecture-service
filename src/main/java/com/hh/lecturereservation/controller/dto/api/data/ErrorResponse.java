package com.hh.lecturereservation.controller.dto.api.data;

public record ErrorResponse(
        String code,
        String message
) {
}
