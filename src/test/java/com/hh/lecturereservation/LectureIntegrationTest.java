package com.hh.lecturereservation;

import com.hh.lecturereservation.domain.LectureService;
import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.ParticipantHistory;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.ParticipantHistoryRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
public class LectureIntegrationTest {
    @Autowired
    private LectureService lectureService;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private LectureParticipantRepository lectureParticipantRepository;
    @Autowired
    private ParticipantHistoryRepository participantHistoryRepository;

    private Long studentId;
    private Long lectureId;
    @BeforeEach
    void setUp() {
        studentId = 1L;
        lectureId = 20L;
        lectureRepository.deleteAll();
        participantHistoryRepository.deleteAll();
        lectureParticipantRepository.deleteAll();
    }

    @Test
    void 특강신청_동시성테스트() throws Exception {
        Long capacity = 100L;
        int numThreads = 50;    //쓰레드 개수

        LectureEntity backEndLecture = LectureEntity.builder()
                .title("백엔드")
                .lecturer("신형만")
                .description("백엔드 플러스")
                .lectureType(LectureType.JAVA)
                .capacity(capacity)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(0L)
                .build();
        Optional<Lecture> saveLecture = lectureRepository.save(LectureEntity.toDto(backEndLecture));
        List<String> exMsg = new ArrayList<>();

        AtomicLong count = new AtomicLong(); // 쓰레드 개수만큼 메서드가 실행되었는지 확인을 위한 count

        CountDownLatch latch = new CountDownLatch(numThreads);  //쓰레드들을 동시 시작 및 종료를 관리하기 위한 객체
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //정해진 쓰레드들(numThreads)에게 동시에 작업할당을 하기위한 객체

        for (int i = 0; i < numThreads; i++) {
            Long finalI = Long.valueOf(i);
            executorService.submit(() -> {
                try {
                    //여러 student가 동시에 같은 과목을 신청
                    Exception exception = Assertions.assertThrows(Exception.class,
                            () -> lectureService.applyLectures(studentId + finalI, saveLecture.get().getLectureId()));
                    exMsg.add(exception.getMessage() + " stuId : " + studentId + " finalI : " + finalI);
                    count.getAndIncrement();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown(); //쓰레드 풀 종료

        Optional<List<LectureParticipant>> members = lectureParticipantRepository.getLectureParticipantMember(LectureEntity.toDto(backEndLecture));

        exMsg.forEach(msg -> System.out.println("exception Message :: " + msg));
        //lecture 의 현재 수강인원이 numThreads 와 같은지 비교
        Optional<Lecture> lecture = lectureRepository.findByIdWithLock(saveLecture.get().getLectureId());
        System.out.println("신청 전 인원 : " +  saveLecture.get().getCurrentEnrollment() + " / 신청 후 인원 : " + lecture.get().getCurrentEnrollment());
        System.out.println("신청완료정보에 들어간 인원 " + members.get().size());

        //신청완료 테이블에 들어간 수강생의 수와 특강테이블에 들어간 현재수강인원의 수가 같아야 한다.
        Assertions.assertEquals(members.get().size(), lecture.get().getCurrentEnrollment());
        //시도한 수강생들(numThreads)과 신청완료 테이블에 들어간 수강생의 수가 같아야 한다.
        Assertions.assertEquals(members.get().size(), numThreads);

    }

    @Test
    void 특강신청_정원초과_동시성테스트() throws Exception {
        Long capacity = 20L;
        int numThreads = 50;    //쓰레드 개수

        LectureEntity backEndLecture = LectureEntity.builder()
                .title("백엔드")
                .lecturer("신형만")
                .description("백엔드 플러스")
                .lectureType(LectureType.JAVA)
                .capacity(capacity)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(0L)
                .build();
        Optional<Lecture> saveLecture = lectureRepository.save(LectureEntity.toDto(backEndLecture));
        List<String> exMsg = new ArrayList<>();

        AtomicLong count = new AtomicLong(); // 쓰레드 개수만큼 메서드가 실행되었는지 확인을 위한 count

        CountDownLatch latch = new CountDownLatch(numThreads);  //쓰레드들을 동시 시작 및 종료를 관리하기 위한 객체
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //정해진 쓰레드들(numThreads)에게 동시에 작업할당을 하기위한 객체

        for (int i = 0; i < numThreads; i++) {
            Long finalI = Long.valueOf(i);
            executorService.submit(() -> {
                try {
                    //여러 student가 동시에 같은 과목을 신청
                    Exception exception = Assertions.assertThrows(Exception.class,
                            () -> lectureService.applyLectures(studentId + finalI, saveLecture.get().getLectureId()));
                    exMsg.add(exception.getMessage() + " stuId : " + studentId + " finalI : " + finalI);
                    count.getAndIncrement();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown(); //쓰레드 풀 종료

        //특강완료테이블 조회
        Optional<List<LectureParticipant>> members = lectureParticipantRepository.getLectureParticipantMember(LectureEntity.toDto(backEndLecture));

        //exMsg list로 정원 초과 입니다 < 의 갯수 파악 가능.
        exMsg.forEach(msg -> System.out.println("exception Message :: " + msg));
        //lecture 의 현재 수강인원이 numThreads 와 같은지 비교
        Optional<Lecture> lecture = lectureRepository.findByIdWithLock(saveLecture.get().getLectureId());
        System.out.println("신청 전 인원 : " +  saveLecture.get().getCurrentEnrollment() + " / 신청 후 인원 : " + lecture.get().getCurrentEnrollment());
        System.out.println("신청정보에 들어간 인원 " + members.get().size());

        // 해당 특강의 정원과 현재수강생 수는 서로 동일해야 한다.
        Assertions.assertEquals(capacity, lecture.get().getCurrentEnrollment());

        // 정원초과된 인원(exMsg.size())이 [시도한 수강생(numThreads) - 특강정원(capacity)] 과 같아야 한다.
        Assertions.assertEquals(numThreads - capacity, exMsg.size());

        //history 성공/실패에 상관없이 history table(로그성) 에는 numThreads 만큼 쌓여야 한다. (lecture_id 기준)
        Optional<List<ParticipantHistory>> historyList = participantHistoryRepository.findByLectureId(lecture.get().getLectureId());
        Assertions.assertEquals(numThreads, historyList.get().size());
    }

    /**
     * 같은 userId 가 같은날짜, 같은 특강은 신청을 못해야 하므로 테스트 작성
     */
    @Test
    void 특강신청_같은날짜_같은특강_중복_테스트() throws Exception {
        LectureEntity backEndLecture = LectureEntity.builder()
                .title("백엔드")
                .lecturer("신형만")
                .description("백엔드 플러스")
                .lectureType(LectureType.JAVA)
                .capacity(10L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(3L)
                .build();
        LectureEntity backEndLecture2 = LectureEntity.builder()
                .title("백엔드")
                .lecturer("신형만")
                .description("백엔드 플러스")
                .lectureType(LectureType.JAVA)
                .capacity(10L)
                .lectureDate(LocalDateTime.now().plusDays(1).plusHours(2))
                .currentEnrollment(6L)
                .build();

        //backEndLecture 와 backEndLecture2 는 같은 날짜수업이며 시간만 2시간 차이가 난다.
        //같은 강의인지 구분은 studentId 기준, lecturer, lecture_date, lecture_Type 이다.
        //이러면 1을 apply 하고 2를 apply 할때 exception 을 뱉어야 한다.
        Optional<Lecture> saveLecture1 = lectureRepository.save(LectureEntity.toDto(backEndLecture));
        Optional<Lecture> saveLecture2 = lectureRepository.save(LectureEntity.toDto(backEndLecture2));

        if (saveLecture1.isPresent() && saveLecture2.isPresent()) {
            Optional<LectureParticipant> result = lectureService.applyLectures(studentId, saveLecture1.get().getLectureId());
            if (result.isPresent()) {
                Exception exception = Assertions.assertThrows(ApplyException.class,
                        () -> lectureService.applyLectures(studentId, saveLecture2.get().getLectureId()));

                Assertions.assertEquals("이미 신청한 수업입니다", exception.getMessage());
            }
        }
    }
}
