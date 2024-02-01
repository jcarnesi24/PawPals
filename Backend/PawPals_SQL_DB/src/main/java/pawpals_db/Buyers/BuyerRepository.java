package pawpals_db.Buyers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Buyer findById(int id);

    @Transactional
    void deleteById(int id);
}
