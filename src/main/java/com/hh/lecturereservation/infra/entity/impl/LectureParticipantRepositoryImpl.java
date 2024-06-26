package com.hh.lecturereservation.infra.entity.impl;

import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.infra.LectureParticipantJpaRepository;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureParticipantRepositoryImpl implements LectureParticipantRepository {

    private final LectureParticipantJpaRepository jpaRepository;

    @Override
    public Optional<List<LectureParticipant>> checkLectureParticipant(Long studentId, Long lectureId) {
        List<LectureParticipantEntity> resultList = jpaRepository.checkLectureParticipant(studentId, lectureId);
        return Optional.of(LectureParticipantEntity.toDtoList(resultList));
    }

    @Override
    public Optional<List<LectureParticipant>> getLectureParticipant(Long studentId) {
        List<LectureParticipantEntity> resultList = jpaRepository.getLectureParticipant(studentId);
        return Optional.of(LectureParticipantEntity.toDtoList(resultList));
    }

    @Override
    public Optional<LectureParticipant> save(LectureParticipant lectureParticipant) {
        Optional<LectureParticipantEntity> lectureParticipantEntity = Optional.of(jpaRepository.save(LectureParticipantEntity.toEntity(lectureParticipant)));
        if (lectureParticipantEntity.isPresent()) {
            return Optional.of(LectureParticipantEntity.toDto(lectureParticipantEntity.get()));
        }
        return Optional.empty();
    }
}
