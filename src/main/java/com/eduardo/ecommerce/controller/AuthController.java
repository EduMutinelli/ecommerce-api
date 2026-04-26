package com.eduardo.ecommerce.controller;

import com.eduardo.ecommerce.dto.AuthLoginDTO;
import com.eduardo.ecommerce.dto.AuthRefreshDTO;
import com.eduardo.ecommerce.dto.AuthResponseDTO;
import com.eduardo.ecommerce.model.RefreshToken;
import com.eduardo.ecommerce.model.User;
import com.eduardo.ecommerce.security.JwtService;
import com.eduardo.ecommerce.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginDTO dto) {
        User user = (User) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()))
                .getPrincipal();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.ok(new AuthResponseDTO(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody AuthRefreshDTO dto) {
        RefreshToken refreshToken = refreshTokenService.validate(dto.refreshToken());
        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.create(user);

        return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, newRefreshToken.getToken()));
    }
}