package com.hh.lecturereservation.infra.jpa;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureParticipantJpaRepository extends JpaRepository<LectureParticipantEntity, Long> {
    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where lp.studentEntity.studentId = :studentId " +
            "and FORMATDATETIME(lp.lectureEntity.lectureDate, 'yyyy-MM-dd') = FORMATDATETIME(:#{#lecture.lectureDate}, 'yyyy-MM-dd') " +
            "and lp.lectureEntity.lecturer = :#{#lecture.lecturer} " +
            "and lp.lectureEntity.lectureType = :#{#lecture.lectureType}")
    List<LectureParticipantEntity> checkLectureParticipant(
            @Param("studentId") Long studentId,
            @Param("lecture") Lecture lecture);

    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where lp.studentEntity.studentId = :studentId")
    List<LectureParticipantEntity> getLectureParticipant(@Param("studentId") Long studentId);

    @Query("select lp from LectureParticipantEntity lp join fetch lp.studentEntity " +
            "join fetch lp.lectureEntity " +
            "where FORMATDATETIME(lp.lectureEntity.lectureDate, 'yyyy-MM-dd') = FORMATDATETIME(:#{#lecture.lectureDate}, 'yyyy-MM-dd') " +
            "and lp.lectureEntity.lecturer = :#{#lecture.lecturer} " +
            "and lp.lectureEntity.lectureType = :#{#lecture.lectureType}")
    List<LectureParticipantEntity> getLectureParticipantMember(@Param("lecture") Lecture lecture);
}
