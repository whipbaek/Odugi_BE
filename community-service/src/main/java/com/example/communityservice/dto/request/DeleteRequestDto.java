package com.example.communityservice.dto.request;

import com.example.communityservice.domain.CommunityRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequestDto {
    private Long communityId;
    private Long categoryId;
    private Long channelId;
    private CommunityRole role;

}
