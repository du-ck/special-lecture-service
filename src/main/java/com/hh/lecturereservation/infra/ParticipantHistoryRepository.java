package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.domain.dto.ParticipantHistory;

import java.util.Optional;


public interface ParticipantHistoryRepository  {
    boolean save(ParticipantHistory history);
}
