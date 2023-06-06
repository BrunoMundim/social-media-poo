package br.com.mundim.rede.social.repository;

import br.com.mundim.rede.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    List<User> findByUsernameContaining(String keyword);
}
