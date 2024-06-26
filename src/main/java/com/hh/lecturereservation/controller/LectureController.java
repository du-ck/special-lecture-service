package com.hh.lecturereservation.controller;

import com.hh.lecturereservation.controller.dto.api.participationLectures;
import com.hh.lecturereservation.domain.dto.Lecture;
import com.hh.lecturereservation.controller.dto.api.Lectures;
import com.hh.lecturereservation.controller.dto.api.data.ResponseData;
import com.hh.lecturereservation.controller.dto.api.Apply;
import com.hh.lecturereservation.domain.LectureService;
import com.hh.lecturereservation.domain.dto.LectureParticipant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;

    /**
     * 특강선택api (심화 )
     * 특강 목록을 조회할 수 있다.
     * 날짜별로 특강이 존재하고, 특강 시작시간 1시간 전(미정)까지 조회 및 신청이 가능하다.
     * 특강의 정원은 30명이 기본이고, 특강 목록 및 가능여부 등 조회가능해야한다.
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ResponseData> lectures() {
        ResponseData responseData = ResponseData.builder()
                .data("No Data")
                .build();

        Optional<List<Lecture>> lectures = lectureService.getLectures();

        if (CollectionUtils.isEmpty(lectures.get())) {
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }

        Lectures.Response lecturesResponse = Lectures.Response.builder()
                .lectures(lectures.get())
                .build();

        responseData = ResponseData.builder()
                .data(lecturesResponse)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * 특강선택api 에서 가능한 목록중 하나를 신청하면 선착순으로 특강신청을 하는 api
     * userId 기준으로 선착순으로 하며
     * 같은날짜의 같은 강의는 동일인물이 신청하지 못한다. (다른날짜의 같은 수업은 가능)
     *
     * 정해진 정원이 초과되면 신청자는 요청을 실패한다.
     * @return
     */
    @PostMapping("/apply")
    public ResponseEntity<ResponseData> apply(
            @RequestBody Apply.Request request
            ) throws Exception {
        Optional<LectureParticipant> result = lectureService.applyLectures(request.getStudentId(), request.getLectureId());
        ResponseData responseData = ResponseData.builder()
                .data(Apply.Response.builder()
                        .participantId(result.get().getParticipantId())
                        .studentId(result.get().getStudentId())
                        .studentName(result.get().getStudentName())
                        .lecture(result.get().getLecture())
                        .build())
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * 신청완료 여부를 조회할 수 있는 api
     * 
     *
     * @return
     */
    @GetMapping("/application/{userId}")
    public ResponseEntity<ResponseData> participationLectures(
            @PathVariable(name = "userId") long userId
            ) {
        ResponseData responseData = ResponseData.builder()
                .data("No Data")
                .build();

        Optional<List<LectureParticipant>> result = lectureService.getLectureParticipant(userId);

        if (CollectionUtils.isEmpty(result.get())) {
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }

        List<Lecture> lectures = result.get().stream()
                .map(m -> m.getLecture())
                .sorted(Comparator.comparing(Lecture::getLectureDate))
                .toList();
        String userName = result.get().stream().map(name -> name.getStudentName()).findFirst().get();
        participationLectures.Response response = participationLectures.Response.builder()
                .userId(userId)
                .userName(userName)
                .lectures(lectures)
                .build();

        responseData = ResponseData.builder()
                .data(response)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
