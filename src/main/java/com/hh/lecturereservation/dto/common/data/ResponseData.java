package com.hh.lecturereservation.dto.common.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData<T> {
    private T data;
}
