package com.phoenix.uimpact.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "firstname cannot be blank")
    private String firstName;

    @NotBlank(message = "lastname cannot be blank")
    private String lastname;

    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "password cannot be blank")
    private String password;

}
