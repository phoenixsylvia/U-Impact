package com.phoenix.uimpact.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {

    @NotNull(message = "New password must not be empty")
    private String newPassword;
    private String token;
}
