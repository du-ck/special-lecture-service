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
@Table(name = "lecture_participant")
public class LectureParticipant {
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @Column(name = "lecture_id", nullable = false)
    private int lectureId;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    @Column(name = "participant_date", nullable = false)
    private LocalDateTime participantDate;
}
