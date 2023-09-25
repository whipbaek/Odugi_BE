package com.example.communityservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessages {

    COMMUNITY_VIEW_SUCCESS("커뮤니티 조회 성공"),
    COMMUNITY_CREATE_SUCCESS("커뮤니티 생성 완료"),
    CATEGORY_CREATE_SUCCESS("카테고리 생성 완료"),
    CHANNEL_CREATE_SUCCESS("채널 생성 완료"),
    COMMUNITY_PERSONAL_SUCCESS("커뮤니티 개인 정보 조회 완료"),
    COMMUNITY_MEMBERS_VIEW_SUCCESS("커뮤니티 멤버들 조회 완료"),
    COMMUNITY_CATEGORY_VIEW_SUCCESS("커뮤니티 카테고리 조회 완료"),
    COMMUNITY_CHANNEL_VIEW_SUCCESS("커뮤니티 채널 조회 완료"),
    COMMUNITY_JOIN_SUCCESS("커뮤니티 가입 완료"),
    COMMUNITY_DELETE_SUCCESS("커뮤니티 삭제 완료"),
    CATEGORY_DELETE_SUCCESS("카테고리 삭제 완료"),
    CHANNEL_DELETE_SUCCESS("채널 삭제 완료"),
    COMMUNITY_MODIFY_SUCCESS("커뮤니티 수정 완료"),
    CATEGORY_MODIFY_SUCCESS("카테고리 수정 완료"),
    CHANNEL_MODIFY_SUCCESS("채널 수정 완료");

    private String message;
}
