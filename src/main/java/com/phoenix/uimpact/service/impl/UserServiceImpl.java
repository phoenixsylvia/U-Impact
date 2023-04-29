package com.phoenix.uimpact.service.impl;

import com.phoenix.uimpact.Enum.Role;
import com.phoenix.uimpact.configuration.mail.MailService;
import com.phoenix.uimpact.configuration.security.CustomUserDetailService;
import com.phoenix.uimpact.configuration.security.JwtUtils;
import com.phoenix.uimpact.dto.request.*;
import com.phoenix.uimpact.dto.response.ApiResponse;
import com.phoenix.uimpact.entity.User;
import com.phoenix.uimpact.exception.InvalidCredentialsException;
import com.phoenix.uimpact.exception.UserNotFoundException;
import com.phoenix.uimpact.exception.ValidationException;
import com.phoenix.uimpact.repository.UserRepository;
import com.phoenix.uimpact.service.UserService;
import com.phoenix.uimpact.utils.AppUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CustomUserDetailService customUserDetailService;

    private final JwtUtils jwtUtils;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final AppUtil appUtil;


    public ResponseEntity<ApiResponse> register(RegisterDto registerDto) throws ValidationException{
        Boolean isUserExist = userRepository.existsByEmail(registerDto.getEmail());
        if(isUserExist)
            throw new ValidationException("User Already Exists");

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setRole(Role.ROLE_USER);

        String token = jwtUtils.generateRegistrationConfirmationToken(registerDto.getEmail());
        user.setConfirmationToken(token);
        userRepository.save(user);

        String URL = "http://localhost:8084/api/v1/userController/verifyLink/?token=" + token;
        String link = "<h3>Hello "  + registerDto.getFirstName()  +"<br> Click the link below to activate your account <a href=" + URL + "><br>Activate</a></h3>";

        mailService.sendEmail(registerDto.getEmail(),"UImpact: Verify Your Account", link);

        return ResponseEntity.ok(new ApiResponse<>("Successful", "Registration Successful. Check your mail to activate your account", null));
    }


    public ApiResponse verifyLink(VerifyTokenDto verifyTokenDto){
        Optional<User> existingUser = userRepository.findByConfirmationToken(verifyTokenDto.getToken());
        if(existingUser.isPresent()){
           if(existingUser.get().isActive()){
               return ApiResponse.builder().message("Account already verified").status("false").build();
           }

            existingUser.get().setConfirmationToken(null);
            existingUser.get().setActive(true);
            userRepository.save(existingUser.get());
            return ApiResponse.builder().message("Success").status("Account created successfully").build();
        }
        throw new UserNotFoundException("Error: No Account found! or Invalid Token");
        }

    @Override
    public ResponseEntity<String> login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()
                -> new UserNotFoundException("User Not Found"));
        if(!user.isActive()) {
            throw new ValidationException(" User Not Active");
        }

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        UserDetails userDetails = customUserDetailService.loadUserByUsername(loginRequestDto.getEmail());

        if (userDetails != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }
        return ResponseEntity.status(400).body("Some Error Occurred");
    }

    @Override
    public ApiResponse forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) throws IOException {
        String email = forgotPasswordRequestDto.getEmail();

        Boolean isEmailExist = userRepository.existsByEmail(email);
        if (!isEmailExist)
            throw new UserNotFoundException("User Does Not Exist!");

        User user = userRepository.findByEmail(email).get();
        String token = jwtUtils.resetPasswordToken(email);
        user.setConfirmationToken(token);
        userRepository.save(user);

        String resetPasswordLink = "http://localhost:8084/api/v1/userController/resetPassword" + token;
        String resetLink = "<h3>Hello " + user.getFirstName() + ",<br> Click the link below to reset your password <a href=" + resetPasswordLink + "><br>Reset Password</a></h3>";

        mailService.sendEmail(email, "UImpact: Click on the link to reset your Password", resetLink);
        return new ApiResponse<>(null, "A password reset link has been sent to your email", null);

    }

    public ApiResponse<String> resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        String password = resetPasswordRequestDto.getNewPassword();
        User user = userRepository.findByConfirmationToken(resetPasswordRequestDto.getToken()).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return new ApiResponse<String>("Success", "password reset successful", null);
    }

    @Override
    public ApiResponse<String> updatePassword(UpdatePasswordResetDto updatePasswordResetDto) {
        String currentPassword = updatePasswordResetDto.getCurrentPassword();
        String newPassword = updatePasswordResetDto.getNewPassword();

        User user = appUtil.getLoggedInUser();

        String savedPassword = user.getPassword();

        if(!passwordEncoder.matches(currentPassword, savedPassword))
            throw new InvalidCredentialsException("Credentials must match");

        else
            updatePasswordResetDto.setNewPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        mailService.sendEmail(user.getEmail(), "Update Password", "Your password has been updated  successfully. Ensure to keep it a secret. Never disclose your password to a third party.");
        return new ApiResponse<>( "Success", "Password reset successful", null);
    }


}



