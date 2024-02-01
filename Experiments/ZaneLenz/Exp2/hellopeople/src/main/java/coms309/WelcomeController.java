package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Zane Lenz
 */

@RestController
@RequestMapping
class WelcomeController {

    public User testUser0 = new User("zanelenz", 123);
    public User testUser1 = new User("janedoe", 456);
    public User testUser2 = new User("johndoe", 789);

    public User users[] = {testUser0, testUser1, testUser2};
    public UserRepository userRepo =  new UserRepository(users);

    public WelcomeController() {
        //Wow
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable int userId) {
        int ID = userId;
        String s = userRepo.find_by_id(ID).userName;
        //String s = use.userName;
        return "Username: " + s;
    }

    @GetMapping("/")
    public String welcome() {
        return "Accessing user information access different users with: /123, /456, /789";
    }
}
