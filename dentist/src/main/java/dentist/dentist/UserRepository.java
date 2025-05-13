package dentist.dentist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 继承 JpaRepository<User, Long> 以启用 Spring Data JPA
public interface UserRepository extends JpaRepository<User, Long> {
    // 正确声明方法返回类型为 Optional<User>
    Optional<User> findByUsername(String username);
}