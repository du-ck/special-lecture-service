package com.hh.lecturereservation.domain.dto.types;

/**
 * History 의 Action 종류
 * - ENROLL : 등록 성공
 * - CANCEL : 취소
 * - FAIL   : 등록 실패 (Exception)
 */
public enum HistoryActionType {
    ENROLL,
    CANCEL,
    FAIL
}
