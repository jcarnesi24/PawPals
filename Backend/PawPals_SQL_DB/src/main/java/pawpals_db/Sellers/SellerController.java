package pawpals_db.Sellers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawpals_db.Buyers.Buyer;
import pawpals_db.Pets.Pet;
import pawpals_db.Pets.PetRepository;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jacob Carnesi, Zane Lenz
 *
 */

@RestController
@Api(tags = "Seller Controller", description = "Operations related to Sellers")
public class SellerController {
    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    SellerRepository sellerRepository;

    private String success = "{\"message\":\"success :)\"}";
    private String failure = "{\"message\":\"failure :(\"}";

    /**
     *
     * @return - A list of all sellers on the database.
     *
     */
    @GetMapping(path = "/users/sellers")
    @ApiOperation(value = "Get all Sellers", notes = "Get a list of all sellers on the database.")
    List<Seller> getAllSellers(){
        return sellerRepository.findAll();
    }

    // TODO: Add swagger annotations.
    /**
     * Returns an account's Seller profile, which includes their list
     * of owned Pets/dogs.
     *
     * @param id - The BasicUser account ID.
     * @return - the Seller profile information.
     */
    @GetMapping(path = "/users/{id}/sellers")
    @ApiOperation(value = "Get Seller by User ID", notes = "Get an account's Seller profile, which includes their list of owned Pets/dogs.")
    Seller getByUserId( @ApiParam(value = "BasicUser account ID", required = true) @PathVariable int id){
        if (basicUserRepository.findById(id) != null && basicUserRepository.findById(id).getSellerProfile() != null) {
            return basicUserRepository.findById(id).getSellerProfile();
        }
        else {
            return null;
        }
    }
    // List of a user's pets returned by BasicUserController

//    /**
//     * Updates the user's profile image for a seller profile.
//     *
//     * @param id - a BasicUser ID.
//     * @param profilePic - A url to the image on the internet.
//     * @return - the list of pets, now associated with the user.
//     */
//    @PutMapping(path = "/users/{id}/sellers")
//
//    @ApiOperation(value = "Update User's Pet List", notes = "Update the user's entire list of pets. (Will be phased out and replaced.)")
//    void updateProfilePic(@PathVariable int id, @RequestParam String profilePic) {
//
//            if (basicUserRepository.findById(id) == null && basicUserRepository.findById(id).getSellerProfile() == null) {
//                return;
//            }
//            Seller s = basicUserRepository.findById(id).getSellerProfile();
//            s.setSellerImage(profilePic);
//            sellerRepository.save(s);
//        }

//    void updateProfilePic(@PathVariable int id, @RequestBody String profilePic) {
//        Seller s = basicUserRepository.findById(id).getSellerProfile();
//        s.setSellerImage(profilePic);
//        sellerRepository.save(s);
//    }

    @PutMapping(path = "/users/{id}/sellers")
    @ApiOperation(value = "Update Seller's Profile Picture", notes = "Update the seller's profile picture.")
    void updateSitterProfilePic(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String profilePic = requestBody.get("profilePic");
        Seller s = basicUserRepository.findById(id).getSellerProfile();
        s.setSellerImage(profilePic);
        sellerRepository.save(s);
    }


    /**
     * Adds a seller profile to a user's profile and links the
     * objects in the database.
     *
     * @param id - The ID of the user the profile will be added to.
     * @return - The newly initialized profile.
     */
    @PostMapping(path = "/users/{id}/sellers")
    @ApiOperation(value = "Create Seller Profile", notes = "Add a seller profile to a user profile and link the objects in the database.")
    Seller createSellerProfile(@ApiParam(value = "ID of the user the profile will be added to", required = true) @PathVariable int id) {
        if (basicUserRepository.findById(id) == null) {
            return null;
        }
        else {
            Seller s = new Seller();
            BasicUser u = basicUserRepository.findById(id);
            sellerRepository.save(s);
            u.setSellerProfile(s);
            basicUserRepository.save(u);
            return s;
        }
    }
}
