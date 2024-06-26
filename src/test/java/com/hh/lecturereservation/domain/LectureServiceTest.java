package com.hh.lecturereservation.domain;

import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.domain.dto.Student;
import com.hh.lecturereservation.domain.dto.types.HistoryActionType;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.infra.*;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private LectureParticipantRepository lectureParticipantRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParticipantHistoryRepository participantHistoryRepository;

    private Long studentId;
    private Long lectureId;
    private Lecture frontLecture;
    private Lecture backLecture;

    @BeforeEach
    void setUp() {
        studentId = 1L;
        lectureId = 1L;
        frontLecture = Lecture.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .lecturer("신형만")
                .lectureId(lectureId)
                .lectureType(LectureType.REACT)
                .capacity(20L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(10L)
                .build();
        backLecture = Lecture.builder()
                .title("백엔드")
                .description("백엔드 플러스")
                .lectureId(lectureId)
                .lecturer("봉미선")
                .lectureType(LectureType.JAVA)
                .capacity(30L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(10L)
                .build();
    }

    /**
     * 특강목록 조회의 기본적인 기능  성공 여부 테스트
     */
    @Test
    void 신청가능한_특강목록_조회_테스트() {
        List<Lecture> returnList = new ArrayList<>();
        returnList.add(backLecture);
        returnList.add(frontLecture);

        //과거 수업은 포함x
        given(lectureRepository.findByLectureDateAfter(any(LocalDateTime.class)))
                .willReturn(returnList);

        Optional<List<Lecture>> result = lectureService.getLectures();

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(2, result.get().size());
    }

    /**
     * 특강신청의 기본적인 기능 성공 여부 테스트
     */
    @Test
    void 특강신청_기능_테스트() throws Exception {
        LectureParticipant lectureParticipant = LectureParticipant.builder()
                .participantId(1L)
                .lecture(frontLecture)
                .studentId(studentId)
                .studentName("짱구")
                .participantDate(LocalDateTime.now())
                .build();
        Student student = Student.builder()
                .studentId(studentId)
                .build();

        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(frontLecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));

        List<LectureParticipant> returnList = new ArrayList<>();
        //userId 기준 이미 신청한 특강인지 validate
        given(lectureParticipantRepository.checkLectureParticipant(studentId, frontLecture))
                .willReturn(Optional.of(returnList));
        given(lectureParticipantRepository.save(any(LectureParticipant.class)))
                .willReturn(Optional.of(lectureParticipant));
        frontLecture.enroll();
        given(lectureRepository.save(any(Lecture.class)))
                .willReturn(Optional.of(frontLecture));
        Optional<LectureParticipant> result = lectureService.applyLectures(studentId, lectureId);

        Assertions.assertEquals(result.get(), lectureParticipant);
    }

    /**
     * 특강의 정원이 찼을 경우 신청실패가 나야하므로 테스트 작성
     */
    @Test
    void 특강신청_정원초과_테스트() throws Exception {
        Lecture lecture = Lecture.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .lecturer("신형만")
                .lectureId(lectureId)
                .lectureType(LectureType.REACT)
                .capacity(20L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(20L)
                .build();
        Student student = Student.builder()
                .studentId(studentId)
                .build();
        ParticipantHistory history = ParticipantHistory.builder()
                .participantId(0L)
                .lectureId(lectureId)
                .studentId(studentId)
                .actionType(HistoryActionType.FAIL)
                .actionDate(LocalDateTime.now())
                .build();

        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(lecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));
        given(participantHistoryRepository.save(history))
                .willReturn(true);

        Exception exception = Assertions.assertThrows(ApplyException.class,
                () -> lectureService.applyLectures(studentId, lectureId));

        Assertions.assertEquals("정원 초과입니다", exception.getMessage());
    }

    /**
     * 신청한 학생id가 존재하지 않는 학생일 경우
     */
    @Test
    void 존재하지_않는_학생() {
        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(frontLecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> lectureService.applyLectures(studentId, lectureId));

        //예외 메세지 검증
        Assertions.assertEquals("존재하지 않는 학생입니다", exception.getMessage());
    }

    /**
     * 신청한 특강id가 존재하지 않는 특강일 경우
     */
    @Test
    void 존재하지_않는_특강() {
        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> lectureService.applyLectures(studentId, lectureId));

        //예외 메세지 검증
        Assertions.assertEquals("존재하지 않는 특강입니다", exception.getMessage());
    }

    /**
     * 특강 신청 완료 여부 조회 기능성공 여부 테스트
     */
    @Test
    void 신청완료여부_조회() {
        LectureParticipant lectureParticipant = LectureParticipant.builder()
                .participantId(1L)
                .lecture(backLecture)
                .studentId(studentId)
                .studentName("짱구")
                .participantDate(LocalDateTime.now())
                .build();
        List<LectureParticipant> returnList = new ArrayList<>();
        returnList.add(lectureParticipant);

        given(lectureParticipantRepository.getLectureParticipant(anyLong()))
                .willReturn(Optional.of(returnList));

        Optional<List<LectureParticipant>> result = lectureService.getLectureParticipant(studentId);

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(1, result.get().size());
    }
}