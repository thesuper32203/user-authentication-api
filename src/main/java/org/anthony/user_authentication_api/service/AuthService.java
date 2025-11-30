package org.anthony.user_authentication_api.service;


import lombok.RequiredArgsConstructor;
import org.anthony.user_authentication_api.model.Role;
import org.anthony.user_authentication_api.model.User;
import org.anthony.user_authentication_api.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(String username, String email, String rawPassword) {
        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("User with username " + username + " already exists");
        }
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("email " + email + " already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public String login(String usernameOrEmail, String password){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
        );

        String username = auth.getName();
        return jwtService.generateToken(username);
    }
}
