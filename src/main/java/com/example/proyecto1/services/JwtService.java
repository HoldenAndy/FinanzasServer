    package com.example.proyecto1.services;

    import com.example.proyecto1.models.entities.Usuario;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;

    import javax.crypto.SecretKey;
    import java.nio.charset.StandardCharsets;
    import java.util.Date;

    @Service
    public class JwtService {
        @Value("${spring.jwt.secret}")
        private String secretKey;
        @Value("${spring.jwt.expiration}")
        private String jwtExpiracion;

        private SecretKey key;

        @PostConstruct
        protected void init() {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        public String generateToken(Usuario usuario){
            long tiempoActual = System.currentTimeMillis();
            long tiempoExpiracion = tiempoActual + Long.parseLong(jwtExpiracion);
            return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(tiempoActual))
                .setExpiration(new Date(tiempoExpiracion))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        }

        public String extracUsername(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        public boolean isTokenValid(String token, Usuario usuario){
            final String username = extracUsername(token);
            return username.equals(usuario.getEmail()) && !isTokenExpired(token);
        }

        private boolean isTokenExpired(String token) {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        }
    }
