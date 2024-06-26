package com.hh.lecturereservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.lecturereservation.controller.dto.api.Apply;
import com.hh.lecturereservation.domain.LectureService;
import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import com.hh.lecturereservation.domain.dto.types.LectureType;
import com.hh.lecturereservation.infra.entity.LectureEntity;
import com.hh.lecturereservation.infra.entity.LectureParticipantEntity;
import com.hh.lecturereservation.infra.entity.StudentEntity;
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

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    LectureService lectureService;

    private Long studentId;
    private Long lectureId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentId = 1L;
        lectureId = 1L;
    }

    /**
     * GET 요청을 통해 특강 목록을 조회하는 기능의 정상여부 판단을 위해 작성
     */
    @Test
    void lectures() throws Exception {
        List<Lecture> returnList = new ArrayList<>();
        returnList.add(Lecture.builder()
                .title("백엔드")
                .description("백엔드 플러스")
                .capacity(30L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(10L)
                .build());
        returnList.add(Lecture.builder()
                .title("프론트엔드")
                .description("프론트엔드 플러스")
                .capacity(40L)
                .lectureDate(LocalDateTime.now().plusDays(1))
                .currentEnrollment(20L)
                .build());


        given(lectureService.getLectures())
                .willReturn(Optional.of(returnList));

        mockMvc.perform(get("/lectures/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lectures[*].title").exists())
                .andExpect(jsonPath("$.data.lectures[*].description").exists())
                .andExpect(jsonPath("$.data.lectures[*].capacity").exists())
                .andExpect(jsonPath("$.data.lectures[*].lectureDate").exists())
                .andExpect(jsonPath("$.data.lectures[*].currentEnrollment").exists());
    }

    /**
     * POST 요청을 통해 특강 신청 기능의 정상여부 판단을 위해 작성
     */
    @Test
    void apply() throws Exception {
        Apply.Request req = Apply.Request.builder()
                .studentId(studentId)
                .lectureId(lectureId)
                .build();
        LectureParticipant result = LectureParticipant.builder()
                .participantId(1L)
                .lecture(Lecture.builder()
                        .title("백엔드")
                        .description("백엔드 플러스")
                        .lectureId(lectureId)
                        .lecturer("신형만")
                        .lectureType(LectureType.JAVA)
                        .capacity(30L)
                        .lectureDate(LocalDateTime.now().plusDays(1))
                        .currentEnrollment(10L)
                        .build())
                .studentId(studentId)
                .studentName("짱구")
                .participantDate(LocalDateTime.now())
                .build();

        given(lectureService.applyLectures(anyLong(), anyLong()))
                .willReturn(Optional.of(result));

        mockMvc.perform(post("/lectures/apply")
                        .content(objMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.participantId").exists())
                .andExpect(jsonPath("$.data.studentId").exists())
                .andExpect(jsonPath("$.data.studentName").exists())
                .andExpect(jsonPath("$.data.lecture").exists())
                .andExpect(jsonPath("$.data.lecture.lectureId").exists())
                .andExpect(jsonPath("$.data.lecture.title").exists())
                .andExpect(jsonPath("$.data.lecture.lecturer").exists())
                .andExpect(jsonPath("$.data.lecture.description").exists())
                .andExpect(jsonPath("$.data.lecture.lectureType").exists())
                .andExpect(jsonPath("$.data.lecture.capacity").exists())
                .andExpect(jsonPath("$.data.lecture.lectureDate").exists())
                .andExpect(jsonPath("$.data.lecture.currentEnrollment").exists());
    }

    /**
     * GET 요청을 통해 특강 신청 완료 여부 기능의 정상여부 판단을 위해 작성
     * @throws Exception
     */
    @Test
    void isSuccess() throws Exception {
        List<LectureParticipant> returnList = new ArrayList<>();
        returnList.add(LectureParticipant.builder()
                .participantId(1L)
                .lecture(Lecture.builder()
                        .title("백엔드")
                        .description("백엔드 플러스")
                        .lectureId(lectureId)
                        .lecturer("신형만")
                        .lectureType(LectureType.JAVA)
                        .capacity(30L)
                        .lectureDate(LocalDateTime.now().plusDays(1))
                        .currentEnrollment(10L)
                        .build())
                .studentId(studentId)
                .studentName("짱구")
                .participantDate(LocalDateTime.now())
                .build());

        given(lectureService.getLectureParticipant(anyLong()))
                .willReturn(Optional.of(returnList));

        mockMvc.perform(get("/lectures/application/{userId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.userName").exists())
                .andExpect(jsonPath("$.data.lectures[*].title").exists())
                .andExpect(jsonPath("$.data.lectures[*].description").exists())
                .andExpect(jsonPath("$.data.lectures[*].capacity").exists())
                .andExpect(jsonPath("$.data.lectures[*].lectureDate").exists())
                .andExpect(jsonPath("$.data.lectures[*].currentEnrollment").exists());
    }
}