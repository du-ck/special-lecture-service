package com.hh.lecturereservation.repository;

import com.hh.lecturereservation.entity.ParticipantHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantHistoryRepository extends JpaRepository<ParticipantHistory, Long> {
    
}
