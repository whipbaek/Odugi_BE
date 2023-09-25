package com.example.communityservice.dto.response;

import com.example.communityservice.domain.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelResponseDto {
    private Long id;
    private Long categoryId;
    private ChannelType type;
    private String name;

}
