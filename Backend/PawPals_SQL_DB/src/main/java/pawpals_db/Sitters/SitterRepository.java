package pawpals_db.Sitters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SitterRepository extends JpaRepository<Sitter, Long> {
    Sitter findById(int id);

    @Transactional
    void deleteById(int id);
}
