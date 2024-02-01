package pawpals_db.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import pawpals_db.Users.BasicUser;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> getMessagesByUserName(String username);
}


/*
I hold array of messages (name,
Front end sends post holding with the other persons ID and I return chat history as it comes

 */