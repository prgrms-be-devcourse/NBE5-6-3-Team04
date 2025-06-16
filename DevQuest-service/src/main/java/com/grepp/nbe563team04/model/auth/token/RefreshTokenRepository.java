package com.grepp.nbe563team04.model.auth.token;

import com.grepp.nbe563team04.model.auth.token.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAccessTokenId(String id);

    long deleteByToken(String token);
}