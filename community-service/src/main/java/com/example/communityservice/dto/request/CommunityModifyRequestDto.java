package com.example.communityservice.dto.request;

import com.example.communityservice.domain.CommunityRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityModifyRequestDto {
    private String name;
    private Long communityId;
    private Long categoryId;
    private Long channelId;
    private CommunityRole role;
}
