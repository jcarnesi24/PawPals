package pawpals_db;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetRepository;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

/**
 * 
 * @author Jacob Carnesi, Zane Lenz
 * 
 */ 

@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users
    /**
     * 
     * @param basicUserRepository repository for the User entity
     * @param petRepository repository for the Pet entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(BasicUserRepository basicUserRepository, PetRepository petRepository) {
        return args -> {
//            BasicUser basicUser1 = new BasicUser("John", "john@somemail.com");
//            BasicUser basicUser2 = new BasicUser("Jane", "jane@somemail.com");
//            BasicUser basicUser3 = new BasicUser("Justin", "justin@somemail.com");
//            basicUserRepository.save(basicUser1);
//            basicUserRepository.save(basicUser2);
//            basicUserRepository.save(basicUser3);

        };
    }

}
