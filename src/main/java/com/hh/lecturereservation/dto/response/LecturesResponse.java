package com.hh.lecturereservation.dto.response;

import com.hh.lecturereservation.dto.detail.LectureDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class LecturesResponse {
    private List<LectureDetail> lecture;
}
