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
    private final LectureJpaRepository jpaRepository;

    @Override
    public List<Lecture> findByLectureDateAfter(LocalDateTime localDateTime) {
        return LectureEntity.toDtoList(jpaRepository.findByLectureDateAfter(localDateTime));
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        Optional<LectureEntity> findLectureEntity = jpaRepository.findById(lectureId);
        if (findLectureEntity.isPresent()) {
            return Optional.of(LectureEntity.toDto(findLectureEntity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Lecture> save(Lecture lecture) {
        Optional<LectureEntity> lectureEntity = Optional.of(jpaRepository.save(LectureEntity.toEntity(lecture)));
        if (lectureEntity.isPresent()) {
            return Optional.of(LectureEntity.toDto(lectureEntity.get()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
