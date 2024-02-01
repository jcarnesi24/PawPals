package pawpals_db.Pets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pawpals_db.Pets.Pet;

/**
 * @author Jacob Carnesi
 */

public interface PetRepository extends JpaRepository<Pet, Long>{

    Pet findById(int id);

    @Transactional
    void deleteById(int id);
}
