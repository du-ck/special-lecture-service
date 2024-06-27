package com.hh.lecturereservation.infra.impl;

import com.hh.lecturereservation.domain.dto.Student;
import com.hh.lecturereservation.infra.jpa.StudentJpaRepository;
import com.hh.lecturereservation.infra.StudentRepository;
import com.hh.lecturereservation.infra.entity.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository jpaRepository;

    @Override
    public Optional<Student> findById(Long studentId) {
        Optional<StudentEntity> findStudentEntity = jpaRepository.findById(studentId);
        if (findStudentEntity.isPresent()) {
            return Optional.of(StudentEntity.toDto(findStudentEntity.get()));
        }
        return Optional.empty();
    }
}
