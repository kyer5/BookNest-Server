package spring.univ_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.univ_board.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
