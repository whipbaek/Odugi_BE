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
public class CategoryRequestDto {
    private String name;
    private Long communityId;
    private CommunityRole role;
}
