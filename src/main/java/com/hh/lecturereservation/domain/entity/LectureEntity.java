package com.hh.lecturereservation.domain.entity;

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
@Table(name = "lecture")
public class LectureEntity {
    @Id
    @Column(name = "lecture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 10, nullable = false)
    private String lecturer;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long capacity;

    @Column(name = "lecture_date", nullable = false)
    private LocalDateTime lectureDate;

    @Column(name = "current_enrollment", nullable = false)
    private Long currentEnrollment;
}
