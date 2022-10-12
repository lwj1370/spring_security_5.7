package co.kr.spring_security.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private static final long JWT_TOKEN_EXPIRE_DATE_LENGTH = 10 * 60 * 60 * 1000;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private final JwtUserDetailService jwtUserDetailService;

    public JwtTokenProvider(JwtUserDetailService jwtUserDetailService) {
        this.jwtUserDetailService = jwtUserDetailService;
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        return
                userDetails.getUsername() == null ?
                        null :
                        doGenerateToken(userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUserNameFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);
        return (userName != null &&
                userName.equals(userDetails.getUsername()) &&
                expiration.after(new Date())
        );
    }

    public Boolean validateToken(String token) {
        final String userName = getUserNameFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);
        return (userName != null &&
                expiration.after(new Date())
        );
    }

    public Authentication getAuthentication(String name) {
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(name);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
    }
    public Authentication getAuthenticationFromToken(String token) {
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(getUserNameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
    }

    private String doGenerateToken(String subject) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRE_DATE_LENGTH))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
    }

}
