package com.eduardo.ecommerce.service;

import com.eduardo.ecommerce.exception.BusinessException;
import com.eduardo.ecommerce.model.RefreshToken;
import com.eduardo.ecommerce.model.User;
import com.eduardo.ecommerce.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshExpiration;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken create(User user) {
        repository.deleteByUser(user);
        repository.flush();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpiration));

        return repository.save(refreshToken);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Refresh token inválido"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new BusinessException("Refresh token expirado. Faça login novamente");
        }

        return refreshToken;
    }
}