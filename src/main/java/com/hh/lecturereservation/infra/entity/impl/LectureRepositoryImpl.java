package com.hh.lecturereservation.infra.entity.impl;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.infra.LectureJpaRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public List<Lecture> findByLectureDateAfter(LocalDateTime localDateTime) {
        return LectureEntity.toDtoList(lectureJpaRepository.findByLectureDateAfter(localDateTime));
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        Optional<Lecture> lecture = Optional.of(LectureEntity.toDto(lectureJpaRepository.findById(lectureId).get()));
        return lecture;
    }

    @Override
    public Optional<Lecture> save(Lecture lecture) {
        Optional<LectureEntity> lectureEntity = Optional.of(lectureJpaRepository.save(LectureEntity.toEntity(lecture)));
        if (lectureEntity.isPresent()) {
            return Optional.of(LectureEntity.toDto(lectureEntity.get()));
        }
        return Optional.empty();
    }
}
