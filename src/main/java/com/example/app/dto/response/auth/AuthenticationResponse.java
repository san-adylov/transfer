package com.example.app.dto.response.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token,
        String role
) {
}
