package com.hh.lecturereservation.infra.entity.impl;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.Student;
import com.hh.lecturereservation.infra.LectureJpaRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.StudentJpaRepository;
import com.hh.lecturereservation.infra.StudentRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import com.hh.lecturereservation.infra.entity.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository jpaRepository;

    @Override
    public Optional<Student> findById(Long studentId) {
        Optional<Student> lecture = Optional.of(StudentEntity.toDto(jpaRepository.findById(studentId).get()));
        return lecture;
    }
}
