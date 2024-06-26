package com.hh.lecturereservation.domain;

import com.hh.lecturereservation.infra.entity.LectureEntity;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import com.hh.lecturereservation.infra.entity.StudentEntity;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.exception.ResourceNotFoundException;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.ParticipantHistoryRepository;
import com.hh.lecturereservation.infra.StudentRepository;
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
class LectureEntityServiceTest {

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

    @BeforeEach
    void setUp() {
        studentId = 1L;
        lectureId = 1L;
    }

    /**
     * 특강목록 조회의 기본적인 기능  성공 여부 테스트
     */
    @Test
    void 신청가능한_특강목록_조회_테스트() {
        List<LectureEntity> returnList = new ArrayList<>();
        returnList.add(LectureEntity.builder()
                .title("백엔드")
                .description("백엔드 플러스")
                .capacity(30L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(10L)
                .build());
        returnList.add(LectureEntity.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(40L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(20L)
                .build());

        //과거 수업은 포함x
        given(lectureRepository.findByLectureDateAfter(any(LocalDateTime.class)))
                .willReturn(returnList);

        Optional<List<LectureEntity>> result = lectureService.getLectures();

        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(2, result.get().size());
    }

    /**
     * 특강신청의 기본적인 기능 성공 여부 테스트
     */
    @Test
    void 특강신청_기능_테스트() throws Exception {
        LectureEntity lecture = LectureEntity.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(20L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(10L)
                .build();
        StudentEntity student = StudentEntity.builder()
                .studentId(studentId)
                .build();

        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(lecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));

        List<LectureParticipantEntity> returnList = new ArrayList<>();
        //userId 기준 이미 신청한 특강인지 validate
        given(lectureParticipantRepository.checkLectureParticipant(studentId, lectureId))
                .willReturn(returnList);
        given(lectureParticipantRepository.save(any(LectureParticipantEntity.class)))
                .willReturn(LectureParticipantEntity.builder()
                        .participantDate(LocalDateTime.now())
                        .build());

        boolean result = lectureService.applyLectures(studentId, lectureId);

        Assertions.assertTrue(result);
    }

    /**
     * 특강의 정원이 찼을 경우 신청실패가 나야하므로 테스트 작성
     */
    @Test
    void 특강신청_정원초과_테스트() throws Exception {
        LectureEntity lecture = LectureEntity.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(20L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(20L)
                .build();
        StudentEntity student = StudentEntity.builder()
                .studentId(studentId)
                .build();

        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(lecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));

        Exception exception = Assertions.assertThrows(ApplyException.class,
                () -> lectureService.applyLectures(studentId, lectureId));

        Assertions.assertEquals("정원 초과입니다", exception.getMessage());
    }

    /**
     * 같은 userId 가 같은날짜, 같은 특강은 신청을 못해야 하므로 테스트 작성
     */
    @Test
    void 특강신청_같은날짜_같은특강_중복_테스트() throws Exception {
        LectureEntity lecture = LectureEntity.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(20L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(10L)
                .build();
        StudentEntity student = StudentEntity.builder()
                .studentId(studentId)
                .build();
        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(lecture));
        given(studentRepository.findById(studentId))
                .willReturn(Optional.of(student));

        List<LectureParticipantEntity> returnList = new ArrayList<>();
        returnList.add(LectureParticipantEntity.builder()
                .participantId(1L)
                .build());
        //userId 기준 이미 신청한 특강인지 validate
        given(lectureParticipantRepository.checkLectureParticipant(studentId, lectureId))
                .willReturn(returnList);

        Exception exception = Assertions.assertThrows(ApplyException.class,
                () -> lectureService.applyLectures(studentId, lectureId));

        //예외 메세지 검증
        Assertions.assertEquals("이미 신청한 수업입니다", exception.getMessage());
    }

    /**
     * 신청한 학생id가 존재하지 않는 학생일 경우
     */
    @Test
    void 존재하지_않는_학생() {
        LectureEntity lecture = LectureEntity.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(20L)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(20L)
                .build();
        given(lectureRepository.findById(lectureId))
                .willReturn(Optional.of(lecture));
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
}