package pawpals_db.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetRepository;

/**
 * 
 * @author Jacob Carnesi, Zane Lenz
 * 
 */ 

@RestController
public class BasicUserController {

    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    PetRepository petRepository;

    private String success = "{\"message\":\"success :)\"}";
    private String failure = "{\"message\":\"failure :(\"}";

    @GetMapping(path = "/users")
    List<BasicUser> getAllUsers(){
        return basicUserRepository.findAll();
    }

    /**
     * Returns the necessary information to display a user's profile as specified by the
     * id path parameter.
     * @param id - the id of the user to be displayed.
     * @return - The BasicUser object for the given user.
     */
    // TODO: I think this will be used by Alex and I in Demo 2 for displaying the bio.
    @GetMapping(path = "/users/{id}")
    BasicUser getUserById(@PathVariable int id){
        return basicUserRepository.findById(id);
    }

    /**
     * Creates a new user given the correct information.
     *
     * @param basicUser - A new user object.
     * @return - the newly created user so their info can be displayed in their profile?
     */
    // TODO: Zane and Seth will probably use this for extra credit.
    @PostMapping(path = "/users")
    String createUser(@RequestBody BasicUser basicUser){
        if (basicUser == null)
            return failure;
        // TODO: Probably should have functionality to make sure they have the minimum fields filled in.
        // TODO: This could be where we hash the password, if we decide to implement security.
        basicUserRepository.save(basicUser);
        return success;
    }

    /**
     * Intakes a JSON containing an username and password
     * Returns a JSON containing a string, telling the user if logic was successful
     *
     * @param user - The JSON containing username and password
     * @return - message containing logic results
     */
    @PostMapping(path = "/users/login")
    String loginUser(@RequestBody BasicUser user) {
        if (basicUserRepository.findByUsername(user.getUsername()) != null) {
            BasicUser use = basicUserRepository.findByUsername(user.getUsername());
            if (use.getPassword().equals(user.getPassword())) {
                return "{\"message\" : \"yes\"}";
            }
            else {
                return "{\"message\" : \"Incorrect password\"}";
            }
        }
        else {
            BasicUser useless = new BasicUser();
            return "{\"message\" : \"No users found\"}";
        }
    }

    /**
     * Updates any part of the user's bio. The JSON request body must include all the relevant fields,
     * NOT just the variable that is being updated.
     *
     * @param id - The ID of the user that is being updated.
     * @param request - The BasicUser object with altered fields.
     * @return - The updated BasicUser.
     */
    // TODO: Alex and I will use this to update the bio and display it.
    @PutMapping("/users/{id}")
    BasicUser updateUser(@PathVariable int id, @RequestBody BasicUser request){
        BasicUser basicUser = basicUserRepository.findById(id);
        if(basicUser == null)
            return null;
        basicUser.setUserBio(request.getUserBio());
        basicUser.setEmailAddress(request.getEmailAddress());
        basicUserRepository.save(basicUser);
        return basicUserRepository.findById(id);
    }   
//    Example of how to edit an object field.
//    Delete this once we have our own example.
//    @PutMapping("/users/{userId}/laptops/{laptopId}")
//    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
//        BasicUser basicUser = basicUserRepository.findById(userId);
//        Laptop laptop = laptopRepository.findById(laptopId);
//        if(basicUser == null || laptop == null)
//            return failure;
//        laptop.setUser(basicUser);
//        basicUser.setLaptop(laptop);
//        basicUserRepository.save(basicUser);
//        return success;
//    }

    /**
     * Deletes the given user. Deletion includes deleting all the user's different
     * actor types and pets. The success message is a picture of the app mascot dog
     * looking at the user with big, sad eyes and saying something like "I'll miss you :("
     *
     * @param id - The ID of the BasicUser to be deleted.
     * @return - A success message that will make them feel bad for deleting their account.
     */
    // TODO: Make sure this deletes all their associated objects.
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        basicUserRepository.deleteById(id);
        return success;
    }
}
