package com.hh.lecturereservation.domain.dto;

import com.hh.lecturereservation.infra.entity.LectureEntity;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import com.hh.lecturereservation.utils.DateUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LectureParticipant {
    private Long participantId;
    private Lecture lecture;
    private Long studentId;
    private String studentName;

    public static LectureParticipant toDto(LectureParticipantEntity lectureParticipantEntity) {
        return LectureParticipant.builder()
                .participantId(lectureParticipantEntity.getParticipantId())
                .lecture(Lecture.builder()
                        .lectureId(lectureParticipantEntity.getLectureEntity().getLectureId())
                        .title(lectureParticipantEntity.getLectureEntity().getTitle())
                        .lecturer(lectureParticipantEntity.getLectureEntity().getLecturer())
                        .description(lectureParticipantEntity.getLectureEntity().getDescription())
                        .capacity(lectureParticipantEntity.getLectureEntity().getCapacity())
                        .lectureDate(DateUtils.removeTAndUntilMinute(lectureParticipantEntity.getLectureEntity().getLectureDate()))
                        .currentEnrollment(lectureParticipantEntity.getLectureEntity().getCurrentEnrollment())
                        .build())
                .studentId(lectureParticipantEntity.getStudentEntity().getStudentId())
                .studentName(lectureParticipantEntity.getStudentEntity().getName())
                .build();
    }

    public static List<LectureParticipant> toDtoList(List<LectureParticipantEntity> lectureParticipantEntityList) {
        return lectureParticipantEntityList.stream().map(m -> LectureParticipant.toDto(m)).toList();
    }
}
