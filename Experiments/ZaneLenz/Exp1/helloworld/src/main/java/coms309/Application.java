package coms309;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Experiments with launching a website and displaying information on that site.
 * 
 * @author Zane Lenz
 */

@SpringBootApplication
public class Application {

    //Appears to create a website that can be accessed with localhost.8080/<Whatever>
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
