package pawpals_db.Pets;

import com.mysql.cj.xdevapi.JsonArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pawpals_db.Sellers.Seller;
import pawpals_db.Sellers.SellerRepository;
import pawpals_db.SwipeSockets.PetSwipeController;
import pawpals_db.Users.*;
import pawpals_db.Users.BasicUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.valueOf;

/**
 * Controls network connections and client interactions with
 * the Pet class directly.
 *
 * @author Jacob Carnesi
 */
@RestController
@Api(tags = "Pet Controller", description = "Operations related to Pets")
public class PetController {
    @Autowired
    PetRepository petRepository;

    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    SellerRepository sellerRepository;

    /**
     * Provides a list of all Pet objects in the database.
     *
     * @return - a list of all Pets in the database.
     */
    @GetMapping(path = "/pets")
    @ApiOperation(value = "Get All Pets", notes = "Get a list of all pets in the database.")
    List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    /**
     * Creates an initial list of dogs for swiping. Will be phased out
     * in the final version and this task will be done by the SwipeController.onOpen()
     * method.
     */
    @GetMapping(path = "/pets/queue/{id}")
    @ApiOperation(value = "Send initial swipe pet", notes = "Sends an initial default pet to the user.")
    Pet initializeSwipeQueue(@ApiParam(value = "ID of the user that wants to swipe", required = true) @PathVariable int id) {
        // TODO: Make sure they're sending the right ID
        return PetSwipeController.queuePet(basicUserRepository.findById(id));
    }



    /**
     * Returns all of a particular user's pets.
     *
     * @param id - the ID of the user.
     * @return - A list of all that user's pets.
     */
    @GetMapping(path = "users/{id}/sellers/pets")
    List<Pet> getUserPets(@ApiParam(value = "ID of BasicUser", required = true) @PathVariable int id) {
        BasicUser user = basicUserRepository.findById(id);
        if (user.isSeller() && user.getSellerProfile().getDawgs() != null) {
            return user.getSellerProfile().getDawgs();
        }
        return null;
    }

    // All searching will be done on the frontend

    /**
     * Add a new pet to a user's profile. The user must have a
     * Seller profile to add a new pet. This method will not add the
     * profile if it is not already present.
     *
     * @param id - The ID of the user.
     * @param request - a Pet object, without its ID or relations set up.
     * @return - The Pet, now with its relations configured and saved in the database.
     */
    @PostMapping("/users/{id}/sellers/pets/{petId}") // No pet ID since it's a new pet.
    Pet addPet(@ApiParam(value = "ID of BasicUser", required = true) @PathVariable int id,
               @ApiParam(value = "ID of the pet to be updated", required = true) @PathVariable int petId,
               @ApiParam(value = "Pet object, without its ID or relations set up", required = true) @RequestBody Pet request){
        BasicUser basicUser = basicUserRepository.findById(id);
        if (basicUser == null || !basicUser.isSeller())
            return null;
        Seller s = basicUser.getSellerProfile();
        request.setOwner(s);

        petRepository.save(request);
        basicUserRepository.findById(id).getSellerProfile().addDawg(request);
        sellerRepository.save(s);
        return petRepository.findById(request.getId());
    }

    /**
     * Updates a Pet's fields and saves the changes in the database.
     * It does NOT add a new pet to the database and operates under
     * the assumption the pet with the ID already exists.
     *
     * @param id - The ID of the user/account.
     * @param petId - The ID of the pet to be updated.
     * @param request - A Pet with the altered fields.
     * @return - The pet, now that the changes have been saved.
     */
    @PutMapping("/users/{id}/sellers/pets/{petId}")
    @ApiOperation(value = "Update Pet Information", notes = "Updates a Pet's fields and saves the changes in the database.")
    Pet updatePetInfo(@ApiParam(value = "ID of the user/account", required = true) @PathVariable int id,
                      @ApiParam(value = "ID of the pet to be updated", required = true) @PathVariable int petId,
                      @ApiParam(value = "Pet with the altered fields", required = true) @RequestBody Pet request) {
        BasicUser basicUser = basicUserRepository.findById(id);
        if (basicUser == null || !basicUser.isSeller() || basicUser.getSellerProfile().getDawgs().isEmpty())
            return null;
        Pet p = petRepository.findById(petId);
        // Update fields to match those of the request.
        // The only fields the front end has are name, breed, and age.
        p.setAge(request.getAge());
        p.setBreed(request.getBreed());
        p.setPetName(request.getPetName());
        p.setIndoorPet(request.isIndoorPet());
        p.setPetBio(request.getPetBio());
        p.setPottyTrained(request.isPottyTrained());
        p.setPetImage(request.getPetImage());
        petRepository.save(p);

        basicUserRepository.findById(id).getSellerProfile().getDawgs().remove(petRepository.findById(petId));
        basicUserRepository.findById(id).getSellerProfile().getDawgs().add(request);

        sellerRepository.save(basicUserRepository.findById(id).getSellerProfile());
        return p;
    }

    /**
     * Deletes a Pet from the database and unlinks all their relations.
     *
     * @param id - ID of the user that owned the pet.
     * @param petId - The ID of the pet to be deleted
     * @return - True if the pet was successfully deleted, else false.
     */
    @DeleteMapping("/users/{id}/sellers/pets/{petId}")
    @ApiOperation(value = "Delete Pet", notes = "Deletes a Pet from the database and unlinks all their relations.")
    boolean updatePetList(@ApiParam(value = "ID of the user that owned the pet", required = true) @PathVariable int id,
                          @ApiParam(value = "ID of the pet to be deleted", required = true) @PathVariable int petId) {
        BasicUser user = basicUserRepository.findById(id);
        if (user.isSeller() && user.getSellerProfile().getDawgs() != null) {
            basicUserRepository.findById(id).getSellerProfile().getDawgs().remove(petRepository.findById(petId));
            petRepository.deleteById(petId);

            return true;
        }
        else {
            return false;
        }
    }




}
