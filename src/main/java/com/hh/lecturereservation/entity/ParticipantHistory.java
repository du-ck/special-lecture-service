package com.hh.lecturereservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "participant_history")
public class ParticipantHistory {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "participant_id", nullable = false)
    private int participantId;

    @Column(name = "lecture_id", nullable = false)
    private int lectureId;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    @Column(name = "action_type", length = 6, nullable = false)
    private String actionType;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;
}
