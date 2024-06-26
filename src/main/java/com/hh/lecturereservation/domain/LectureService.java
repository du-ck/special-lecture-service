package com.hh.lecturereservation.domain;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.infra.ParticipantHistoryRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import com.hh.lecturereservation.infra.entity.ParticipantHistoryEntity;
import com.hh.lecturereservation.infra.entity.StudentEntity;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.exception.ResourceNotFoundException;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.StudentRepository;
import com.hh.lecturereservation.infra.entity.types.HistoryActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureParticipantRepository lectureParticipantRepository;
    private final StudentRepository studentRepository;
    private final ParticipantHistoryRepository participantHistoryRepository;

    public Optional<List<Lecture>> getLectures() {
        List<LectureEntity> lectureList = lectureRepository.findByLectureDateAfter(LocalDateTime.now());
        List<Lecture> returnList = Lecture.toDtoList(lectureList);
        return Optional.of(returnList);
    }

    public boolean applyLectures(long studentId, long lectureId) throws Exception {
        //userid 기준 선착순
        //같은 날짜, 같은 강의는 신청 불가 (이미 신청한 수업이면 불가)
        //신청 성공이든 실패든 history 에 이력이 남음

        //history 객체 먼저 생성 (exception 시에도 이력은 남도록)
        ParticipantHistoryEntity history = ParticipantHistoryEntity.builder()
                .participantId(0L)
                .lectureId(lectureId)
                .studentId(studentId)
                .actionType(HistoryActionType.FAIL)
                .actionDate(LocalDateTime.now())
                .build();

        try {
            LectureEntity lecture = lectureRepository.findById(lectureId).orElseThrow(
                    () -> new ResourceNotFoundException("존재하지 않는 특강입니다")
            );

            studentRepository.findById(studentId).orElseThrow(
                    () -> new ResourceNotFoundException("존재하지 않는 학생입니다")
            );

            //특강 최대정원이 현재등록자보다 많아야 함.
            if (lecture.getCurrentEnrollment() < lecture.getCapacity()) {

                //1. select LectureParticipant  여기서 같은날짜 같은강의에 이력이있는지 체크 (Empty 여야 이력이 없는 것)
                List<LectureParticipantEntity> resultList = lectureParticipantRepository.checkLectureParticipant(studentId, lectureId);
                if (CollectionUtils.isEmpty(resultList)) {
                    //2. insert LectureParticipant
                    LectureParticipantEntity lectureParticipantEntity = LectureParticipantEntity.builder()
                            .lectureEntity(LectureEntity.builder()
                                    .lectureId(lectureId)
                                    .build())
                            .studentEntity(StudentEntity.builder()
                                    .studentId(studentId)
                                    .build())
                            .participantDate(LocalDateTime.now())
                            .build();

                    LectureParticipantEntity saveItem = lectureParticipantRepository.save(lectureParticipantEntity);
                    if (saveItem != null) {
                        //3. lecture update (capacity 및 current enroll 변경)
                        lecture = lecture.toBuilder()
                                .currentEnrollment(lecture.getCurrentEnrollment() + 1)
                                .build();
                        LectureEntity saveLecture = lectureRepository.save(lecture);

                        if (saveLecture != null) {
                            history = history.toBuilder()
                                    .participantId(saveItem.getParticipantId())
                                    .actionType(HistoryActionType.ENROLL)
                                    .build();
                        }
                        return saveLecture != null;
                    }
                } else {
                    //이미 신청한 수업
                    throw new ApplyException("이미 신청한 수업입니다");
                }
            } else {
                //정원 초과
                throw new ApplyException("정원 초과입니다");
            }
        } finally {
            //4. insert participant_history
            participantHistoryRepository.save(history);
        }
        return false;
    }

    public Optional<List<LectureParticipant>> getLectureParticipant(long studentId) {
        List<LectureParticipantEntity> lectureParticipantList = lectureParticipantRepository.getLectureParticipant(studentId);
        List<LectureParticipant> returnList = LectureParticipant.toDtoList(lectureParticipantList);
        return Optional.of(returnList);
    }
}
