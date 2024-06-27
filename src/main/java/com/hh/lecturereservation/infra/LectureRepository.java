package com.hh.lecturereservation.infra;


import com.hh.lecturereservation.domain.dto.Lecture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    List<Lecture> findByLectureDateAfter(LocalDateTime localDateTime);

    Optional<Lecture> findById(Long lectureId);
    Optional<Lecture> save(Lecture lecture);
    void deleteAll();
}
