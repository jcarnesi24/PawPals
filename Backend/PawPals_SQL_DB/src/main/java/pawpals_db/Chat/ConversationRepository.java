package pawpals_db.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findById(int id);

    @Transactional
    void deleteById(int id);
}
