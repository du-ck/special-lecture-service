package com.hh.lecturereservation.domain.dto;

import com.hh.lecturereservation.domain.dto.types.HistoryActionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ParticipantHistory {
    private Long historyId;
    private Long participantId;
    private Long lectureId;
    private Long studentId;
    private HistoryActionType actionType;
    private LocalDateTime actionDate;
}
