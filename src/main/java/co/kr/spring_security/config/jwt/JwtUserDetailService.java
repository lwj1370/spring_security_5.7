package co.kr.spring_security.config.jwt;

import co.kr.spring_security.config.SecurityCustomConfig;
import co.kr.spring_security.entity.UserEntity;
import co.kr.spring_security.repo.UserDao;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserDao userDao;
    private final CustomPasswordEncoder CustomPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserEntity userEntity = userDao.findUserEntityByUsername(userName);
        if (userEntity == null) throw new UsernameNotFoundException(userName);

        return User
                .withUsername(userName)
                .password(userEntity.getPassword())
                .authorities(userEntity.getAuthorities().split(","))
                .build();
    }
}
