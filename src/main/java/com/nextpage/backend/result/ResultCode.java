package com.nextpage.backend.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // user
    USER_CREATE_SUCCESS("U001", "회원 생성 성공"),
    USER_DELETE_SUCCESS("U002", "회원 탈퇴 성공"),
    USER_DETAIL_INFO_SUCCESS("U003", "회원 상세 조회 성공"),
    USER_UPDATE_SUCCESS("U004", "회원 상세정보 수정 성공"),

    // AUTH
    TOKEN_CREATE_SUCCESS("A001", "토큰 생성 성공"),

    // STORY
    STORY_LIST_SUCCESS("S001", "스토리 목록 조회 성공"),
    STORY_DETAIL_INFO_SUCCESS("S002", "스토리 상세 조회 성공"),
    STORY_CREATE_SUCCESS("S201","스토리 생성 성공"),
    STORY_IMAGE_CREATE_SUCCESS("S201","이미지 생성 성공")
    ;

    private final String code;
    private final String message;
}
