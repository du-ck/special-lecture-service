package com.hh.lecturereservation.infra;


import com.hh.lecturereservation.domain.dto.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    List<Lecture> findByLectureDateAfter(LocalDateTime localDateTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Lecture> findByIdWithLock(Long lectureId);
    Optional<Lecture> save(Lecture lecture);
    void deleteAll();
}
