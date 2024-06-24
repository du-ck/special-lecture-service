package com.hh.lecturereservation.repository;

import com.hh.lecturereservation.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Optional<List<Lecture>> findAllByActive(char active);
}
