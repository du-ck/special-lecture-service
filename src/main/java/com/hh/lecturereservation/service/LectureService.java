package com.hh.lecturereservation.service;

import com.hh.lecturereservation.dto.detail.LectureDetail;
import com.hh.lecturereservation.dto.response.LecturesResponse;
import com.hh.lecturereservation.entity.Lecture;
import com.hh.lecturereservation.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    public Optional<LecturesResponse> getLectures() {
        Optional<List<Lecture>> lectureList = lectureRepository.findAllByActive('Y');

        if (lectureList.isPresent()) {
            List<LectureDetail> responseLectureList = lectureList.get().stream()
                    .map(lecture -> LectureDetail.toDTO(lecture)).collect(Collectors.toList());

            LecturesResponse result = LecturesResponse.builder()
                    .lectures(responseLectureList)
                    .build();

            return Optional.of(result);
        }
        return Optional.empty();
    }
}
