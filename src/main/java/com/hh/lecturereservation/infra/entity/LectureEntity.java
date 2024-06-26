package com.hh.lecturereservation.infra.entity;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "lecture_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    @Column(nullable = false)
    private Long capacity;

    @Column(name = "lecture_date", nullable = false)
    private LocalDateTime lectureDate;

    @Column(name = "current_enrollment", nullable = false)
    private Long currentEnrollment;

    public static Lecture toDto(LectureEntity lectureEntity) {
        return Lecture.builder()
                .lectureId(lectureEntity.getLectureId())
                .title(lectureEntity.getTitle())
                .description(lectureEntity.getDescription())
                .lecturer(lectureEntity.getLecturer())
                .capacity(lectureEntity.getCapacity())
                .lectureDate(lectureEntity.getLectureDate())
                .currentEnrollment(lectureEntity.getCurrentEnrollment())
                .build();
    }

    public static List<Lecture> toDtoList(List<LectureEntity> lectureEntityList) {
        return lectureEntityList.stream().map(m -> LectureEntity.toDto(m)).toList();
    }

    public static LectureEntity toEntity(Lecture lecture) {
        return LectureEntity.builder()
                .lectureId(lecture.getLectureId())
                .title(lecture.getTitle())
                .lecturer(lecture.getLecturer())
                .description(lecture.getDescription())
                .lectureType(lecture.getLectureType())
                .capacity(lecture.getCapacity())
                .lectureDate(lecture.getLectureDate())
                .currentEnrollment(lecture.getCurrentEnrollment())
                .build();
    }
}
