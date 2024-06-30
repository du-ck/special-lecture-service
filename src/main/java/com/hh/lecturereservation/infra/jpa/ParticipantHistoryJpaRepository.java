package com.hh.lecturereservation.infra.jpa;

import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.infra.entity.ParticipantHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantHistoryJpaRepository extends JpaRepository<ParticipantHistoryEntity, Long> {
    List<ParticipantHistoryEntity> findByLectureId(Long lectureId);
}
