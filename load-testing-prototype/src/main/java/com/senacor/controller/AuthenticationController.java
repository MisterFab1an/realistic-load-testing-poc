package com.senacor.controller;

import com.senacor.entity.AuthenticationRequest;
import com.senacor.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "public")
public class AuthenticationController {
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("authenticate")
    public ResponseEntity login(@RequestBody AuthenticationRequest request) {
        try {
            // skip authentication and assign user
            User user = new User(request.getUsername(), request.getPassword(), new ArrayList<>());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateAccessToken(user)
                    )
                    .build();
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
