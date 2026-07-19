package com.secondhand.backend.service;

import com.secondhand.backend.dto.AuthResponse;
import com.secondhand.backend.dto.LoginRequest;
import com.secondhand.backend.dto.RegisterRequest;
import com.secondhand.backend.entity.Role;
import com.secondhand.backend.entity.User;
import com.secondhand.backend.entity.UserStatus;
import com.secondhand.backend.exception.DuplicateResourceException;
import com.secondhand.backend.repository.UserRepository;
import com.secondhand.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration and login.
 * <p>
 * Registration ensures username and phone are unique, then creates a new user
 * with default role {@code USER} and status {@code ACTIVE}.
 * Login verifies password, checks user is not blocked, and returns a JWT.
 * </p>
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("This phone number is already registered");
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName(),
                request.getPhone(),
                request.getEmail()
        );
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved, saved.getRole().name());
        return new AuthResponse(token, saved.getId(), saved.getUsername(), saved.getFullName(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new LockedException("This account has been blocked");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (DisabledException | LockedException e) {
            throw e;
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user, user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getFullName(), user.getRole().name());
    }
}
