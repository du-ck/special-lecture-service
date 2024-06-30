package com.hh.lecturereservation.infra.entity;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.domain.dto.types.HistoryActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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


    public static ParticipantHistory toDto(ParticipantHistoryEntity entity) {
        return ParticipantHistory.builder()
                .historyId(entity.getHistoryId())
                .participantId(entity.getParticipantId())
                .lectureId(entity.getLectureId())
                .studentId(entity.getStudentId())
                .actionType(entity.getActionType())
                .actionDate(entity.getActionDate())
                .build();
    }

    public static List<ParticipantHistory> toDtoList(List<ParticipantHistoryEntity> entityList) {
        return entityList.stream().map(m -> ParticipantHistoryEntity.toDto(m)).toList();
    }

    public static ParticipantHistoryEntity toEntity(ParticipantHistory domain) {
        return ParticipantHistoryEntity.builder()
                .historyId(domain.getHistoryId())
                .participantId(domain.getParticipantId())
                .lectureId(domain.getLectureId())
                .studentId(domain.getStudentId())
                .actionType(domain.getActionType())
                .actionDate(domain.getActionDate())
                .build();
    }
}
