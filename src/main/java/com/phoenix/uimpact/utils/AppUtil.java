package com.phoenix.uimpact.utils;

import com.phoenix.uimpact.entity.User;
import com.phoenix.uimpact.exception.ResourceNotFoundException;
import com.phoenix.uimpact.exception.UserNotFoundException;
import com.phoenix.uimpact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUtil {

    private final UserRepository userRepository;
    public User getLoggedInUser() throws ResourceNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(((UserDetails)principal).getUsername())
                .orElseThrow(() -> new UserNotFoundException("Error getting logged in user"));
    }

}
