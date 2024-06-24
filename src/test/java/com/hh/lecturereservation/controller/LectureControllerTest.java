package com.hh.lecturereservation.controller;

import com.hh.lecturereservation.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}