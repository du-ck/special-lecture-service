package com.hh.lecturereservation.domain;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.domain.dto.Student;
import com.hh.lecturereservation.domain.dto.types.HistoryActionType;
import com.hh.lecturereservation.domain.lock.LockHelper;
import com.hh.lecturereservation.infra.*;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {
    private static final Logger log = LoggerFactory.getLogger(LectureService.class);
    private final LectureRepository lectureRepository;
    private final LectureParticipantRepository lectureParticipantRepository;
    private final StudentRepository studentRepository;
    private final ParticipantHistoryRepository participantHistoryRepository;
    private final LockHelper lockHelper;

    public Optional<List<Lecture>> getLectures() {
        List<Lecture> lectureList = lectureRepository.findByLectureDateAfter(LocalDateTime.now());
        return Optional.of(lectureList);
    }


    @Transactional
    public Optional<LectureParticipant> applyLectures(long studentId, long lectureId) throws Exception {
        //userid 기준 선착순
        //같은 날짜, 같은 강의는 신청 불가 (이미 신청한 수업이면 불가)
        //신청 성공이든 실패든 history 에 이력이 남음

        //history 객체 먼저 생성 (exception 시에도 이력은 남도록)
        ParticipantHistory history = ParticipantHistory.builder()
                .participantId(0L)
                .lectureId(lectureId)
                .studentId(studentId)
                .actionType(HistoryActionType.FAIL)
                .actionDate(LocalDateTime.now())
                .build();

        long stamp = lockHelper.getWriteLock(); // 쓰기 잠금 획득

        try {
            Lecture lecture = lectureRepository.findByIdWithLock(lectureId).orElseThrow(
                    () -> new ResourceNotFoundException("존재하지 않는 특강입니다")
            );
            System.out.println("before start lecture currentEnrollment : " + lecture.getCurrentEnrollment());

            Student student = studentRepository.findById(studentId).orElseThrow(
                    () -> new ResourceNotFoundException("존재하지 않는 학생입니다")
            );

            //특강 최대정원이 현재등록자보다 많아야 함.
            if (lecture.getCurrentEnrollment() < lecture.getCapacity()) {
                //1. select LectureParticipant  여기서 같은날짜 같은강의에 이력이있는지 체크 (Empty 여야 이력이 없는 것)
                Optional<List<LectureParticipant>> resultList = lectureParticipantRepository.checkLectureParticipant(studentId, lecture); //todo lock
                if (CollectionUtils.isEmpty(resultList.get())) {
                    //2. insert LectureParticipant
                    LectureParticipant lectureParticipant = LectureParticipant.builder()
                            .lecture(lecture)
                            .studentId(studentId)
                            .participantDate(LocalDateTime.now())
                            .studentName(student.getName())
                            .build();

                    Optional<LectureParticipant> saveItem = lectureParticipantRepository.save(lectureParticipant);
                    if (saveItem.isPresent()) {
                        //3. lecture update (capacity 및 current enroll 변경)
                        lecture.enroll();
                        Optional<Lecture> saveLecture = lectureRepository.save(lecture);
                        if (saveLecture.isPresent()) {
                            history = history.toBuilder()
                                    .participantId(saveItem.get().getParticipantId())
                                    .actionType(HistoryActionType.ENROLL)
                                    .build();
                        }
                        return saveItem;
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
            lockHelper.loseWriteLock(stamp);
            try {
                //4. insert participant_history
                //unit Test를 위해 따로 try-문에 담음
                participantHistoryRepository.save(history);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            finally {
                //lockHelper.loseWriteLock(stamp);
            }
        }
        return Optional.empty();
    }

    public Optional<List<LectureParticipant>> getLectureParticipant(long studentId) {
        Optional<List<LectureParticipant>> lectureParticipantList = lectureParticipantRepository.getLectureParticipant(studentId);
        if (!CollectionUtils.isEmpty(lectureParticipantList.get())) {
            return lectureParticipantList;
        }
        return Optional.empty();
    }
}
