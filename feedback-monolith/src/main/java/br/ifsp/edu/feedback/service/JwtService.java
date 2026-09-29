package br.ifsp.edu.feedback.service;

import java.time.Instant;
import java.util.List;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.ifsp.edu.feedback.model.User;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
    private final 	JwtEncoder jwtEncoder;
    
    public JwtService(JwtEncoder encoder) {
        this.jwtEncoder = encoder;
    }
    
    public String generateToken(User user) {
        Instant now = Instant.now();
        long expire = 3600L;

        List<String> roles = List.of("ROLE_" + user.getRole().name()); 

        var claims = JwtClaimsSet.builder()
                .issuer("spring-security")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expire))
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", roles) 
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT não disponível");
        }
        return authHeader.substring(7);
    }

}