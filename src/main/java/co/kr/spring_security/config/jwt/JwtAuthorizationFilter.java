package co.kr.spring_security.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    private final CustomAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(CustomAuthenticationManager authenticationManager,
                                  JwtTokenProvider jwtTokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDetails userDetails;
        InputStream stream = null;
        try {
            stream = request.getInputStream();
            userDetails = mapper.readValue(stream, UserDetails.class);
        } catch (Exception e) {
            log.warn("Failed to Read Jwt Token : {}", stream);
            return null;
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String token = jwtTokenProvider.generateToken(userDetails);

        response.getWriter().write("Bearer " + token);
        response.getWriter().flush();
    }

}
