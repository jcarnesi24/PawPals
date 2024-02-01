package pawpals_db.Buyers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Buyers", description = "Operations related to Buyer class.")
public class BuyerController {
    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    BuyerRepository buyerRepository;

    @GetMapping(path = "/buyers")
    @ApiOperation(value = "Get all buyers", notes = "Retrieves a list of all buyers.")
    List<Buyer> getAllBuyers() { return buyerRepository.findAll(); }
    @GetMapping(path = "/users/{id}/buyers")
    @ApiOperation(value = "Get a specific Buyer profile.", notes = "Retrieves Buyer profile info.")
    Buyer getByUserId(@ApiParam(value = "ID of the user/account", required = true) @PathVariable int id) {
        if (basicUserRepository.findById(id) != null && basicUserRepository.findById(id).getBuyerProfile() != null) {
            return basicUserRepository.findById(id).getBuyerProfile();
        }
        else {
            return null;
        }
    }

//    /**
//     * Updates a user's Buyer profile image.
//     *
//     * @param id - The account that owns the buyer profile.
//     * @param profilePic - The URL of the image.
//     */
//    @PutMapping(path = "/users/{id}/buyers/pic")
//    @ApiOperation(value = "Update a buyer's profile picture.", notes = "Adds or changes a buyer profile's cover picture.")
//    void updateProfilePic(@ApiParam(value = "ID of the associated account") @PathVariable int id,
//                          @ApiParam(value = "URL string to the image on the World Wide Web.")
//                          @RequestParam(name = "profilePic") String profilePic) {
//        Buyer b = basicUserRepository.findById(id).getBuyerProfile();
//        b.setBuyerImage(profilePic);
//        buyerRepository.save(b);
//    }

    @PutMapping(path = "/users/{id}/buyers/pic")
    @ApiOperation(value = "Update a buyer's profile picture.", notes = "Adds or changes a buyer profile's cover picture.")
    void updateProfilePic(@ApiParam(value = "ID of the associated account") @PathVariable int id,
                          @ApiParam(value = "Request body containing the profile picture URL.")
                          @RequestBody Map<String, String> requestBody) {
        String profilePic = requestBody.get("profilePic");
        Buyer b = basicUserRepository.findById(id).getBuyerProfile();
        b.setBuyerImage(profilePic);
        buyerRepository.save(b);
    }

    @PutMapping(path = "/users/{id}/buyers")
    @ApiOperation(value = "Update a buyer's profile info", notes = "Updates any information in the Buyer profile," +
            " primarily their Pet filtering preferences.")
    void updateProfile(@PathVariable int id, @RequestBody Buyer request) {
        Buyer b = basicUserRepository.findById(id).getBuyerProfile();
        // Age Preference
        b.setMinAge(request.getMinAge());
        b.setMaxAge(request.getMaxAge());
        b.setFilterByAge(request.filtersByAge());
        // Breed Preference
        b.setBreedPreference(request.getBreedPreference());
        b.setFilterByBreed(request.filtersByBreed());

        b.setIndoorPetFilter(request.wantsIndoorPet());

        b.setPottyTrainedFilter(request.wantsPottyTrainedPet());

        buyerRepository.save(b);
    }

    @PostMapping(path = "/users/{id}/buyers")
    @ApiOperation(value = "Create a new Buyer profile.", notes = "Adds a Buyer profile to the specified user.")
    Buyer createBuyerProfile(@ApiParam(value = "ID of the user/account", required = true) @PathVariable int id) {
        if (basicUserRepository.findById(id) == null) {
            return null;
        }
        else {
            Buyer b = new Buyer();
            BasicUser u = basicUserRepository.findById(id);
            buyerRepository.save(b);
            u.setBuyerProfile(b);
            basicUserRepository.save(u);
            return b;
        }
    }


}
