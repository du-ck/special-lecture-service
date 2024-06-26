package com.hh.lecturereservation.infra.impl;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.infra.jpa.LectureParticipantJpaRepository;
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
    public Optional<List<LectureParticipant>> checkLectureParticipant(Long studentId, Lecture lecture) {
        List<LectureParticipantEntity> resultList = jpaRepository.checkLectureParticipant(studentId, lecture);
        return Optional.of(LectureParticipantEntity.toDtoList(resultList));
    }

    @Override
    public Optional<List<LectureParticipant>> getLectureParticipant(Long studentId) {
        List<LectureParticipantEntity> resultList = jpaRepository.getLectureParticipant(studentId);
        return Optional.of(LectureParticipantEntity.toDtoList(resultList));
    }

    @Override
    public Optional<LectureParticipant> save(LectureParticipant lectureParticipant) {
        LectureParticipantEntity entity = LectureParticipantEntity.toEntity(lectureParticipant);
        Optional<LectureParticipantEntity> lectureParticipantEntity = Optional.of(jpaRepository.save(entity));
        if (lectureParticipantEntity.isPresent()) {
            return Optional.of(LectureParticipantEntity.toDto(lectureParticipantEntity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<LectureParticipant>> getLectureParticipantMember(Lecture lecture) {
        List<LectureParticipantEntity> resultList = jpaRepository.getLectureParticipantMember(lecture);
        return Optional.of(LectureParticipantEntity.toDtoList(resultList));
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
