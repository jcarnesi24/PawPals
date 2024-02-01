package pawpals_db.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Jacob Carnesi, Zane Lenz
 * 
 */ 
@Repository
public interface BasicUserRepository extends JpaRepository<BasicUser, Long> {
    BasicUser findById(int id);

    BasicUser findByUsername(String username);
    BasicUser findByEmailAddress(String emailAddress);

    @Transactional
    void deleteById(int id);
}
