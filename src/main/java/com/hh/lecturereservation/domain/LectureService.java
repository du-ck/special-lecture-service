package com.hh.lecturereservation.domain;

import com.hh.lecturereservation.controller.dto.api.Apply;
import com.hh.lecturereservation.domain.entity.LectureEntity;
import com.hh.lecturereservation.domain.entity.LectureParticipantEntity;
import com.hh.lecturereservation.domain.entity.StudentEntity;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureParticipantRepository lectureParticipantRepository;
    private final StudentRepository studentRepository;

    public Optional<List<LectureEntity>> getLectures() {
        List<LectureEntity> lectureList = lectureRepository.findByLectureDateAfter(LocalDateTime.now());
        return Optional.of(lectureList);
    }

    public boolean applyLectures(Apply.Request request) throws Exception {
        //userid 기준 선착순
        //같은 날짜, 같은 강의는 신청 불가 (이미 신청한 수업이면 불가)
        //신청 성공이든 실패든 history 에 이력이 남음
        LectureEntity lecture = lectureRepository.findById(request.getLectureId()).orElseThrow(
                () -> new ApplyException("존재하지 않는 특강입니다")
        );

        studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new ApplyException("존재하지 않는 학생입니다")
        );

        //특강 최대정원이 현재등록자보다 많아야 함.
        if (lecture.getCurrentEnrollment() < lecture.getCapacity()) {

            //select LectureParticipant  여기서 같은날짜 같은강의에 이력이있는지 체크 (Empty 여야 이력이 없는 것)
            List<LectureParticipantEntity> resultList = lectureParticipantRepository.checkLectureParticipant(request.getStudentId(), request.getLectureId());
            if (CollectionUtils.isEmpty(resultList)) {
                //3. insert LectureParticipant + lecture update
                LectureParticipantEntity lectureParticipantEntity = LectureParticipantEntity.builder()
                        .lectureEntity(LectureEntity.builder()
                                .lectureId(request.getLectureId())
                                .build())
                        .studentEntity(StudentEntity.builder()
                                .studentId(request.getStudentId())
                                .build())
                        .participantDate(LocalDateTime.now())
                        .build();

                LectureParticipantEntity saveItem = lectureParticipantRepository.save(lectureParticipantEntity);
                if (saveItem != null) {
                    //lecture update (capacity 및 current enroll 변경)
                    //4. insert participant_history (finally  문에 넣기 try 범위 생각할 것)
                    return saveItem != null;
                }
            } else {
                //이미 신청한 수업
                throw new ApplyException("이미 신청한 수업입니다");
            }
        } else {
            //정원 초과
            throw new ApplyException("정원 초과입니다");
        }
        return false;
    }
}
