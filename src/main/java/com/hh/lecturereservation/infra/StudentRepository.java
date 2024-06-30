package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.domain.dto.Student;
import com.hh.lecturereservation.infra.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository {
    Optional<Student> findById(Long studentId);
}
