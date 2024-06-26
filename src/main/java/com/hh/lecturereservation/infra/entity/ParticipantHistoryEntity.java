package com.hh.lecturereservation.infra.entity;

import com.hh.lecturereservation.infra.entity.types.HistoryActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "participant_history")
public class ParticipantHistoryEntity {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "action_type", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryActionType actionType;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;
}
