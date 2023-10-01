package com.example.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StateRequestDto {

    private String channelId;
    private String userId;
    private String state;
}
