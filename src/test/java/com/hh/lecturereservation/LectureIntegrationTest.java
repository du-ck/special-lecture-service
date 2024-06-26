package com.hh.lecturereservation;

import com.hh.lecturereservation.domain.LectureService;
import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.exception.ApplyException;
import com.hh.lecturereservation.infra.LectureRepository;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
@Transactional
public class LectureIntegrationTest {
    @Autowired
    private LectureService lectureService;
    @Autowired
    private LectureRepository lectureRepository;

    private Long studentId;
    private Long lectureId;
    @BeforeEach
    void setUp() {
        studentId = 1L;
        lectureId = 20L;
    }

    @Test
    void 특강신청_동시성테스트() throws Exception {
        int numThreads = 10;    //쓰레드 개수
        AtomicLong count = new AtomicLong(); // 쓰레드 개수만큼 메서드가 실행되었는지 확인을 위한 count

        CountDownLatch latch = new CountDownLatch(numThreads);  //쓰레드들을 동시 시작 및 종료를 관리하기 위한 객체
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //정해진 쓰레드들(numThreads)에게 동시에 작업할당을 하기위한 객체

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    lectureService.applyLectures(studentId, lectureId);
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
