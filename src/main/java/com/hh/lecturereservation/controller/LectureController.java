package com.hh.lecturereservation.controller;

import com.hh.lecturereservation.dto.common.data.ResponseData;
import com.hh.lecturereservation.dto.detail.LectureDetail;
import com.hh.lecturereservation.dto.request.ApplyRequest;
import com.hh.lecturereservation.dto.response.LecturesResponse;
import com.hh.lecturereservation.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private static final Logger log = LoggerFactory.getLogger(LectureController.class);
    private final LectureService lectureService;

    /**
     * 특강선택api (심화)
     * 특강 목록을 조회할 수 있다.
     * 날짜별로 특강이 존재하고, 특강 시작시간 1시간 전(미정)까지 조회 및 신청이 가능하다.
     * 특강의 정원은 30명이 기본이고, 특강 목록 및 가능여부 등 조회가능해야한다.
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ResponseData> lectures() {
        List<LectureDetail> returnList = new ArrayList<>();
        ResponseData responseData = ResponseData.builder()
                .data(LecturesResponse.builder()
                        .lectures(returnList).build())
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
            @RequestBody ApplyRequest request
            ) {
        ResponseData responseData = ResponseData.builder()
                .data(null)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * 신청완료 여부를 조회할 수 있는 api
     * 신청 성공시 true 실패시 false
     * @param id
     * @return
     */
    @GetMapping("/application/{id}")
    public ResponseEntity<ResponseData> isSuccess(
            @PathVariable(name = "id") String id
    ) {
        ResponseData responseData = ResponseData.builder()
                .data(null)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
