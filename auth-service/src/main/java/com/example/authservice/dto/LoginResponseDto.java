package com.example.authservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LoginResponseDto {
    String access_token;
    String refresh_token;
    long timeToLive;
}
