package co.kr.spring_security.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {
    @Bean
    public PasswordEncoder pEncoder() {
        return new BCryptPasswordEncoder();
    }
}
