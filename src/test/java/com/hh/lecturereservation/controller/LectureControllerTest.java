package com.hh.lecturereservation.controller;

import com.hh.lecturereservation.dto.detail.LectureDetail;
import com.hh.lecturereservation.dto.response.LecturesResponse;
import com.hh.lecturereservation.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * GET 요청을 통해 특강 목록을 조회하는 기능의 정상여부 판단을 위해 작성
     */
    @Test
    void lectures() throws Exception {
        List<LectureDetail> returnList = new ArrayList<>();
        returnList.add(LectureDetail.builder()
                .title("백엔드")
                .description("백엔드 플러스")
                .capacity(30)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(10)
                .build());
        returnList.add(LectureDetail.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(40)
                .lectureDate(LocalDateTime.now())
                .currentEnrollment(20)
                .build());

        LecturesResponse lecturesResponse = LecturesResponse.builder()
                .lectures(returnList)
                .build();

        given(lectureService.getLectures())
                .willReturn(Optional.of(lecturesResponse));

        mockMvc.perform(get("/lectures/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lectures[*].title").exists())
                .andExpect(jsonPath("$.data.lectures[*].description").exists())
                .andExpect(jsonPath("$.data.lectures[*].capacity").exists())
                .andExpect(jsonPath("$.data.lectures[*].lectureDate").exists())
                .andExpect(jsonPath("$.data.lectures[*].currentEnrollment").exists());
    }
}