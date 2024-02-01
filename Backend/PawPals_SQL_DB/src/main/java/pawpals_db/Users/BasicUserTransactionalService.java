package pawpals_db.Users;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BasicUserTransactionalService {

    @Autowired
    private BasicUserRepository basicUserRepository;

    @Transactional
    public BasicUser getBasicUserWithConvos(int basicUserId) {
        BasicUser b = basicUserRepository.findById(basicUserId);
        Hibernate.initialize(b.getConversations());

        return b;
    }
}
