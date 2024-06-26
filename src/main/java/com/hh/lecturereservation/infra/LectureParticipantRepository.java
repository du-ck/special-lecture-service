package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface LectureParticipantRepository {
    Optional<List<LectureParticipant>> checkLectureParticipant(Long studentId, Lecture lecture);
    Optional<List<LectureParticipant>> getLectureParticipant(Long studentId);
    Optional<LectureParticipant> save(LectureParticipant lectureParticipant);
}
