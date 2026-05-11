package com.app.ecomapplication.service.impl;
import com.app.ecomapplication.exception.AuthException;
import com.app.ecomapplication.model.User;
import com.app.ecomapplication.repository.UserRepository;
import com.app.ecomapplication.security.JwtUtil;
import com.app.ecomapplication.service.AuthService;
import org.springframework.stereotype.Service;
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                        new AuthException("Invalid username"));
        if (!user.getPassword().equals(password)) {
            throw new AuthException("Invalid password");
        }
        return jwtUtil.generateToken(username);
    }
}