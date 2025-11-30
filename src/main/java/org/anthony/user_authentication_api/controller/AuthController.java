package org.anthony.user_authentication_api.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.anthony.user_authentication_api.model.User;
import org.anthony.user_authentication_api.repository.UserRepository;
import org.anthony.user_authentication_api.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        authService.register(request.username, request.email, request.password);
        return "success";
    }

    @PostMapping("login")
    public AuthResponse login(@RequestBody LoginRequest request){
        String token = authService.login(request.usernameOrEmail, request.password);
        return new AuthResponse(token);
    }

    @GetMapping("/me")
    public User me(Authentication authentication){
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow();
    }

    @Data
    public static class RegisterRequest{
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class LoginRequest{
        private String usernameOrEmail;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class AuthResponse{
        private String token;
    }
}
