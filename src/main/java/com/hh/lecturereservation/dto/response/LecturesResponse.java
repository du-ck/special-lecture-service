package com.hh.lecturereservation.dto.response;

import com.hh.lecturereservation.dto.detail.LectureDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LecturesResponse {
    private List<LectureDetail> lectures;
}
