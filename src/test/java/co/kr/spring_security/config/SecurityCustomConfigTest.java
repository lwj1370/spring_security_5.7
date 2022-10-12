package co.kr.spring_security.config;

import co.kr.spring_security.config.jwt.CustomPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityCustomConfigTest {

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Test
    void passwordCustomEncoder() {
        String encodeValue1 = passwordEncoder.pEncoder().encode("1234");
        String encodeValue2 = passwordEncoder.pEncoder().encode("1234");

        System.out.println(encodeValue1);
        System.out.println(encodeValue2);

        System.out.println(passwordEncoder.pEncoder().matches("1234", encodeValue1));
        System.out.println(passwordEncoder.pEncoder().matches("1234", encodeValue2));
    }
}