package onetoone.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Laptops.Laptop;
import onetoone.Laptops.LaptopRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LaptopRepository laptopRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //Returns a JSON listing all of the users and their information
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Returns a JSON with the specified user's information
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users/login")
    String login(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            User use = userRepository.findByUsername(user.getUsername());
            if (use.getPassword().equals(user.getPassword())) {
                return "{\"message\" : \"Login successful\"}";
            }
            else {
                return "{\"message\" : \"No user found\"}";
            }
        }
        else {
            User useless = new User();
            return "{\"message\" : \"Password incorrect\"}";
        }
    }
    //Creates a new user with information specified in POST request body
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    //Replaces information of current user with information given in PUT request
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        user.setName(request.getName());
        user.setEmailId(request.getEmailId());
        userRepository.save(user);
        return userRepository.findById(id);
    }   

    //Replaces user's laptop information by that given in the PUT request body
    @PutMapping("/users/{userId}/laptops/{laptopId}")
    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
        User user = userRepository.findById(userId);
        Laptop laptop = laptopRepository.findById(laptopId);
        if(user == null || laptop == null)
            return failure;
        laptop.setUser(user);
        user.setLaptop(laptop);
        userRepository.save(user);
        return success;
    }

    //Remove user specified by their unique ID
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    //Removes all users in the database
    @DeleteMapping(path = "/users/extinction")
    String extinction(){
        userRepository.deleteAll();
        return success;
    }
}
