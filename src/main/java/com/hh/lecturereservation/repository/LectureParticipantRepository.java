package com.hh.lecturereservation.repository;

import com.hh.lecturereservation.entity.LectureParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureParticipantRepository extends JpaRepository<LectureParticipant, Long> {

}
