package com.example.communityservice.dto.response;

import com.example.communityservice.domain.CommunityRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityMemberResponseDto {
    private String name;
    private String email;
    private String introduction;
    private CommunityRole role;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime joinedAt;

}
