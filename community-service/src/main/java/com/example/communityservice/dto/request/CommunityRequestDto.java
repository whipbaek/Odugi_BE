package com.example.communityservice.dto.request;

import com.example.communityservice.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequestDto {
    private String name;
    private String profileImage;

    public Community toCommunity(){
        return Community.builder()
                .name(this.name)
                .profileImage(this.profileImage)
                .build();
    }
}
