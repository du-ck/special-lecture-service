package com.hh.lecturereservation.infra.jpa;

import com.hh.lecturereservation.infra.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {
    
}
