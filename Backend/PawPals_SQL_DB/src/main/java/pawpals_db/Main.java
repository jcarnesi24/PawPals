package pawpals_db;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.stereotype.Component;
import pawpals_db.Pets.Pet;
import pawpals_db.Sellers.Seller;
import pawpals_db.Sellers.SellerRepository;
import pawpals_db.Users.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Runs the Spring Boot server and ensures documentation is generated using
 * Swagger UI.
 *
 * @author Jacob Carnesi, Zane Lenz
 */ 

@SpringBootApplication
@EnableJpaRepositories
@EnableSwagger2
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 Sellers, each with a dog
    /**
     * 
     * @param basicUserRepository repository for the User entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(BasicUserRepository basicUserRepository, SellerRepository sellerRepository) {
        return args -> {

        };
    }

    @Bean
    public Docket getAPIdocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
