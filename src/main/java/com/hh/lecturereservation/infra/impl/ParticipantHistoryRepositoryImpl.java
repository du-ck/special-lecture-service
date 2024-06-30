package com.hh.lecturereservation.infra.impl;

import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.infra.jpa.ParticipantHistoryJpaRepository;
import com.hh.lecturereservation.infra.ParticipantHistoryRepository;
import com.hh.lecturereservation.infra.entity.ParticipantHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParticipantHistoryRepositoryImpl implements ParticipantHistoryRepository {
    private final ParticipantHistoryJpaRepository jpaRepository;

    @Override
    public boolean save(ParticipantHistory history) {
        Optional<ParticipantHistoryEntity> saveHistory = Optional.of(jpaRepository.save(ParticipantHistoryEntity.toEntity(history)));
        return saveHistory.isPresent();
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<List<ParticipantHistory>> findByLectureId(Long lectureId) {
        List<ParticipantHistoryEntity> resultList = jpaRepository.findByLectureId(lectureId);
        return Optional.of(ParticipantHistoryEntity.toDtoList(resultList));
    }
}
