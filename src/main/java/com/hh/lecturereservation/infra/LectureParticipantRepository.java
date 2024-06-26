package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureParticipantRepository extends JpaRepository<LectureParticipantEntity, Long> {
    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where lp.studentEntity.studentId = :studentId " +
            "and lp.lectureEntity.lectureId = :lectureId")
    List<LectureParticipantEntity> checkLectureParticipant(
            @Param("studentId") Long studentId,
            @Param("lectureId") Long lectureId);
}
