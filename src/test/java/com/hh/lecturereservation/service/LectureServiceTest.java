package com.hh.lecturereservation.service;

import com.hh.lecturereservation.dto.detail.LectureDetail;
import com.hh.lecturereservation.dto.response.LecturesResponse;
import com.hh.lecturereservation.entity.Lecture;
import com.hh.lecturereservation.repository.LectureRepository;
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

    @BeforeEach
    void setUp() {
    }

    /**
     * 특강목록 조회의 기본적인 기능  성공 여부 테스트
     */
    @Test
    void 신청가능한_특강목록_조회_테스트() {
        List<Lecture> returnList = new ArrayList<>();
        returnList.add(Lecture.builder()
                .title("백엔드")
                .description("백엔드 플러스")
                .capacity(30)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(10)
                .build());
        returnList.add(Lecture.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(40)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(20)
                .build());

        given(lectureRepository.findAll())
                .willReturn(returnList);

        Optional<LecturesResponse> result = lectureService.getLectures();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.get().getLectures().size());
    }
}