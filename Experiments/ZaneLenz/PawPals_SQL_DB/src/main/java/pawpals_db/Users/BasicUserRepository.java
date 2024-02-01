package pawpals_db.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Jacob Carnesi, Zane Lenz
 * 
 */ 

public interface BasicUserRepository extends JpaRepository<BasicUser, Long> {
    BasicUser findById(int id);

    //Finds an user by the username given as a parameter
    BasicUser findByUsername(String username);

    @Transactional
    void deleteById(int id);
}
