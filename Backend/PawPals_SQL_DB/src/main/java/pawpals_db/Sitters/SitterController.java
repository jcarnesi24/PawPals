package pawpals_db.Sitters;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pawpals_db.Buyers.Buyer;
import pawpals_db.Pets.Pet;
import pawpals_db.Sellers.Seller;
import pawpals_db.Users.BasicUser;
import pawpals_db.Users.BasicUserRepository;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Jacob Carnesi
 */
@RestController
@Api(tags = "Sitter Controller", description = "Operations related to Sitters")
public class SitterController {
    @Autowired
    BasicUserRepository basicUserRepository;

    @Autowired
    SitterRepository sitterRepository;

    private String success = "{\"message\":\"success :)\"}";
    private String failure = "{\"message\":\"failure :(\"}";

    /**
     *
     * @return - a list of all Sitter profiles.
     *
     */
    @GetMapping(path = "/users/sitters")
    @ApiOperation(value = "Get All Sitters", notes = "Get a list of all Sitter profiles.")
    List<Sitter> getAllSellers(){
        return sitterRepository.findAll();
    }

    /**
     * Returns a user's Sitter profile.
     *
     * @param id - the BasicUser ID.
     * @return - the user's Sitter profile information.
     */
    @GetMapping(path = "/users/{id}/sitters")
    @ApiOperation(value = "Get Sitter by User ID", notes = "Get a user's Sitter profile information.")
    Sitter getByUserId(@ApiParam(value = "BasicUser ID", required = true) @PathVariable int id){
        if (basicUserRepository.findById(id) != null && basicUserRepository.findById(id).getSitterProfile() != null) {
            return basicUserRepository.findById(id).getSitterProfile();
        }
        else {
            return null;
        }
    }

    private class QueueDTO {
        private String name;
        private int sitterId;
        private String bio;
        private String email;
        private String sitterImage;
        private double rating;
        private int dogSat;

        QueueDTO() {
            this.name = "";
            this.bio = "";
            this.email = "";
        }
        QueueDTO(BasicUser b) {
            Sitter sitter = b.getSitterProfile();
            this.name = b.getFirstname() + " " + b.getLastname();
            this.sitterId = sitter.getId();
            this.bio = b.getUserBio();
            this.email = b.getEmailAddress();
            this.sitterImage = sitter.getSitterImage();
            this.rating = sitter.getRating();
            this.dogSat = sitter.getDogsSat();
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getBio() {
            return bio;
        }
        public void setBio(String bio) {
            this.bio = bio;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public int getSitterId() {
            return sitterId;
        }
        public void setSitterId(int sitterId) {
            this.sitterId = sitterId;
        }
        public String getSitterImage() {
            return sitterImage;
        }
        public void setSitterImage(String sitterImage) {
            this.sitterImage = sitterImage;
        }
        public double getRating() {
            return rating;
        }
        public void setRating(double rating) {
            this.rating = rating;
        }
        public int getDogSat() {
            return dogSat;
        }
        public void setDogSat(int dogSat) {
            this.dogSat = dogSat;
        }
    }
    @GetMapping(path = "/sitters/queue/{id}")
    @ApiOperation(value = "Send initial sitter for swiping", notes = "Sends an initial default sitter to the user.")
    QueueDTO initializeSwipeQueue(@ApiParam(value = "ID of the user that wants to swipe", required = true) @PathVariable int id) {
        Random rand = new Random(); // To find a random profile.
        int upperBound = (int)sitterRepository.count();
        Sitter upNext = null;

        while (upNext == null) {
            upNext = sitterRepository.findById(rand.nextInt(upperBound) + 1);
        }
        return new QueueDTO(upNext.getAccount());
    }

//    /**
//     * Updates a user's Sitter profile image.
//     *
//     * @param id - The account that owns the sitter profile.
//     * @param profilePic - The URL of the image.
//     */
//    @PutMapping(path = "/users/{id}/sitters")
//    void updateProfilePic(@PathVariable int id, @RequestParam String profilePic) {
//        Sitter s = basicUserRepository.findById(id).getSitterProfile();
//        s.setSitterImage(profilePic);
//        sitterRepository.save(s);
//    }

    @PutMapping(path = "/users/{id}/sitters")
    @ApiOperation(value = "Update Sitter's Profile Picture", notes = "Update the sitter's profile picture.")
    void updateSitterProfilePic(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String profilePic = requestBody.get("profilePic");
        Sitter s = basicUserRepository.findById(id).getSitterProfile();
        s.setSitterImage(profilePic);
        sitterRepository.save(s);
    }

    /**
     * Adds a new profile to a BasicUser.
     *
     * @param id - the BasicUser's ID.
     * @return - the newly created and properly linked profile.
     */
    @PostMapping(path = "/users/{id}/sitters")
    @ApiOperation(value = "Create Sitter Profile", notes = "Adds a new profile to a BasicUser.")
    Sitter createSitterProfile(@ApiParam(value = "BasicUser ID", required = true) @PathVariable int id) {
        if (basicUserRepository.findById(id) == null) {
            return null;
        }
        else {
           Sitter s = new Sitter();
            BasicUser u = basicUserRepository.findById(id);
            sitterRepository.save(s);
            u.setSitterProfile(s);
            basicUserRepository.save(u);
            return s;
        }
    }


}
