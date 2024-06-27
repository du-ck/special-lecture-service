package com.hh.lecturereservation;

import com.hh.lecturereservation.domain.LectureService;
import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.infra.LectureParticipantRepository;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.ParticipantHistoryRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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


    @PersistenceContext
    private EntityManager em;

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
        Long capacity = 3L;
        int numThreads = 30;    //쓰레드 개수

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
            int finalI = i;
            executorService.submit(() -> {
                try {
                    //여러 student가 동시에 같은 과목을 신청
                    Exception exception = Assertions.assertThrows(ApplyException.class,
                            () -> lectureService.applyLectures(studentId + finalI, saveLecture.get().getLectureId()));
                    exMsg.add(exception.getMessage());
                    //lectureService.applyLectures(studentId + finalI, saveLecture.get().getLectureId());
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

        exMsg.forEach(msg -> System.out.println("!!Exception Message :: " + msg));
        Optional<List<LectureParticipant>> result = lectureService.getLectureParticipant(studentId);
        if (result.isPresent() && !CollectionUtils.isEmpty(result.get())) {
            Optional<List<LectureParticipant>> members = lectureParticipantRepository.getLectureParticipantMember(LectureEntity.toDto(backEndLecture));
            if (members.isPresent() && !CollectionUtils.isEmpty(members.get())) {
                members.get().forEach(member -> System.out.println(member.getStudentId()));
                //memberList 의 size 가 capacity(최대정원) 와 같아야 한다.
                Assertions.assertEquals(members.get().size(), capacity);
            }
        }
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
