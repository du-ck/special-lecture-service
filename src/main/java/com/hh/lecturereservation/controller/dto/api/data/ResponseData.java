package com.hh.lecturereservation.controller.dto.api.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData<T> {
    private T data;
}
