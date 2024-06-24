package com.hh.lecturereservation.service;

import com.hh.lecturereservation.dto.response.LecturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {

    public Optional<LecturesResponse> getLectures() {
        return null;
    }
}
