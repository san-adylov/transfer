package com.example.app.services;

import com.example.app.dto.request.auth.SignInRequest;
import com.example.app.dto.response.auth.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse singIn(SignInRequest request);
}
