package pawpals_db.Sellers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findById(int id);

    @Transactional
    void deleteById(int id);
}
