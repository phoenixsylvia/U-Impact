package com.phoenix.uimpact.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
    @NotNull(message = "Email must not be empty")
    private String email;
}
