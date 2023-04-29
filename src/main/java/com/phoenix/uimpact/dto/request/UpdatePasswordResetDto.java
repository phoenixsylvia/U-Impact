package com.phoenix.uimpact.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UpdatePasswordResetDto {

        @NotNull(message = "Password must not be empty")
        private String currentPassword;

        @NotNull(message = "Password must not be empty")
        private String newPassword;

    }
