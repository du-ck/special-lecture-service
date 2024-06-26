package com.hh.lecturereservation.domain.dto;

import com.hh.lecturereservation.domain.dto.types.LectureType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class LectureParticipant {
    private Long participantId;
    private Lecture lecture;
    private Long studentId;
    private String studentName;
    private LocalDateTime participantDate;
}
