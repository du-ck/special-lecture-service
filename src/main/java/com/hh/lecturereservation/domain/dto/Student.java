package com.hh.lecturereservation.domain.dto;

import com.hh.lecturereservation.domain.dto.types.LectureType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class Student {
    private Long studentId;
    private String id;
    private String name;
    private String phone;
    private String email;
}
