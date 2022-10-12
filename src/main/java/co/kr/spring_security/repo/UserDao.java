package co.kr.spring_security.repo;

import co.kr.spring_security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Long> {

    UserEntity findUserEntityByUsername(String username);
    UserEntity findUserEntityByUsernameAndPassword(String username, String password);
}
