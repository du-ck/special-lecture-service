package com.hh.lecturereservation.infra.entity;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
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
@Table(name = "lecture_participant")
public class LectureParticipantEntity {
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    /*@Column(name = "lecture_id", nullable = false)
    private int lectureId;*/

    /*@Column(name = "student_id", nullable = false)
    private int studentId;*/

    @Column(name = "participant_date", nullable = false)
    private LocalDateTime participantDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;

    public static LectureParticipant toDto(LectureParticipantEntity lectureParticipantEntity) {
        return LectureParticipant.builder()
                .participantId(lectureParticipantEntity.getParticipantId())
                .lecture(Lecture.builder()
                        .lectureId(lectureParticipantEntity.getLectureEntity().getLectureId())
                        .title(lectureParticipantEntity.getLectureEntity().getTitle())
                        .lecturer(lectureParticipantEntity.getLectureEntity().getLecturer())
                        .description(lectureParticipantEntity.getLectureEntity().getDescription())
                        .capacity(lectureParticipantEntity.getLectureEntity().getCapacity())
                        .lectureDate(lectureParticipantEntity.getLectureEntity().getLectureDate())
                        .currentEnrollment(lectureParticipantEntity.getLectureEntity().getCurrentEnrollment())
                        .build())
                .studentId(lectureParticipantEntity.getStudentEntity().getStudentId())
                .studentName(lectureParticipantEntity.getStudentEntity().getName())
                .build();
    }

    public static List<LectureParticipant> toDtoList(List<LectureParticipantEntity> lectureParticipantEntityList) {
        return lectureParticipantEntityList.stream().map(m -> LectureParticipantEntity.toDto(m)).toList();
    }

    public static LectureParticipantEntity toEntity(LectureParticipant domain) {
        return LectureParticipantEntity.builder()
                .participantId(domain.getParticipantId())
                .studentEntity(StudentEntity.builder()
                        .studentId(domain.getStudentId())
                        .name(domain.getStudentName())
                        .build())
                .lectureEntity(LectureEntity.toEntity(domain.getLecture()))
                .build();
    }
}
