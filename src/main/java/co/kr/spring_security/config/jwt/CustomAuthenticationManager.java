package co.kr.spring_security.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final JwtUserDetailService jwtUserDetailService;
    private final CustomPasswordEncoder customPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(authentication.getName());
        if (!customPasswordEncoder.pEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword()))
            throw new BadCredentialsException("Wrong Password");

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
    }
}
