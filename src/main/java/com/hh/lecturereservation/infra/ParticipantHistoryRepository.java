package com.hh.lecturereservation.infra;

import com.hh.lecturereservation.infra.entity.ParticipantHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantHistoryRepository extends JpaRepository<ParticipantHistoryEntity, Long> {
    
}
