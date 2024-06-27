package com.hh.lecturereservation.infra.jpa;

import com.hh.lecturereservation.infra.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
    List<LectureEntity> findByLectureDateAfter(LocalDateTime localDateTime);
}
