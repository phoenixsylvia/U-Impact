package com.phoenix.uimpact.controller;

import com.phoenix.uimpact.dto.request.*;
import com.phoenix.uimpact.dto.response.ApiResponse;
import com.phoenix.uimpact.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/userController")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register( @Valid @RequestBody RegisterDto registerDto){
        return userService.register(registerDto);
    }

    @PutMapping("/verifyLink")
    public ResponseEntity<ApiResponse> verifyLink(@RequestBody VerifyTokenDto verifyTokenDto){
        ApiResponse response = userService.verifyLink(verifyTokenDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto)throws IOException {
        ApiResponse<String> forgotPasswordResponse = userService.forgotPassword(forgotPasswordRequestDto);
        return new ResponseEntity<>(forgotPasswordResponse, HttpStatus.CREATED);
    }

    @PostMapping("/resetPassword")
    ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        ApiResponse<String> resetPasswordResponse = userService.resetPassword(resetPasswordRequestDto);
        return new ResponseEntity<>(resetPasswordResponse, HttpStatus.CREATED);
    }

    @PutMapping("/updatePassword")
    ResponseEntity<ApiResponse<String>> updatePassword (@Valid @RequestBody UpdatePasswordResetDto updatePasswordResetDto) {
        ApiResponse<String> updatePasswordResponse = userService.updatePassword(updatePasswordResetDto);
        return new ResponseEntity<>(updatePasswordResponse, HttpStatus.CREATED);
    }
}
