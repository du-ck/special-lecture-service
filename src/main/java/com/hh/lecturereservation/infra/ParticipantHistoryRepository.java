package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.domain.dto.ParticipantHistory;

import java.util.List;
import java.util.Optional;


public interface ParticipantHistoryRepository  {
    boolean save(ParticipantHistory history);
    void deleteAll();

    Optional<List<ParticipantHistory>> findByLectureId(Long lectureId);
}
