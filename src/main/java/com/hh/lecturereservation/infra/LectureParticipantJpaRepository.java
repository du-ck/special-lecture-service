package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureParticipantJpaRepository extends JpaRepository<LectureParticipantEntity, Long> {
    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where lp.studentEntity.studentId = :studentId " +
            "and lp.lectureEntity.lectureId = :lectureId")
    List<LectureParticipantEntity> checkLectureParticipant(
            @Param("studentId") Long studentId,
            @Param("lectureId") Long lectureId);

    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where lp.studentEntity.studentId = :studentId")
    List<LectureParticipantEntity> getLectureParticipant(@Param("studentId") Long studentId);
}
