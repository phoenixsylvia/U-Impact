package com.phoenix.uimpact.service;

import com.phoenix.uimpact.dto.request.*;
import com.phoenix.uimpact.dto.response.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;


import java.io.IOException;


public interface UserService {

    ResponseEntity<ApiResponse> register(RegisterDto registerDto) throws ValidationException;

    ApiResponse verifyLink(VerifyTokenDto verifyTokenDto);

    ResponseEntity<String> login(LoginRequestDto loginRequestDto);

    ApiResponse forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) throws IOException;

    ApiResponse<String> resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

    ApiResponse<String> updatePassword(UpdatePasswordResetDto updatePasswordResetDto);

}
