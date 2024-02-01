package pawpals_db.Buyers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import pawpals_db.Users.BasicUser;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * @author Jacob Carnesi
 * @author Zane Lenz
 */
@Entity
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "basic_id")
    @JsonIgnore
    private BasicUser account;

    //buy dogs
    //swipe left on dog
    //swipe right on dog
    //unmatch with dog

    //Array containing all the "BasicUsers" that this buyer matches with
    // Can and should be a list, but requests have to return a List or something. IDK
    private ArrayList<BasicUser> matches;
    private String buyerImage;

    private int minAge;
    private int maxAge;
    private boolean filterByAge;
    private String breedPreference;
    private boolean filterByBreed;
    private boolean wantsIndoorPet;
    private boolean wantsPottyTrainedPet;


    //Constructors for buyer
    public Buyer() {
        this.matches = new ArrayList<BasicUser>();
        this.buyerImage = null;
        this.minAge = 0;
        this.maxAge = 0;
        this.filterByAge = false;
        this.breedPreference = "";
        this.filterByBreed = false;
        this.wantsIndoorPet = false;
        this.wantsPottyTrainedPet = false;
    }

    public Buyer(BasicUser account) {
        this.matches = new ArrayList<BasicUser>();
        this.buyerImage = null;
        this.minAge = 0;
        this.maxAge = 0;
        this.filterByAge = false;
        this.breedPreference = "";
        this.filterByBreed = false;
        this.wantsIndoorPet = false;
        this.wantsPottyTrainedPet = false;
        this.account = account;
    }

    public Buyer(ArrayList<BasicUser> matches) {
        this.matches = matches;
    }

    // ~~~~~~~~~~ Methods ~~~~~~~~~~ \\
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BasicUser getAccount() {
        return account;
    }

    public void setAccount(BasicUser account) {
        this.account = account;
    }

    /**
     * Lists all users currently in this users list of matches
     * @return
     */
    public ArrayList<BasicUser> getMatches() {
        return matches;
    }

    /**
     * Sets user's list of matches to be equal to the input list
     * @param arrMatches
     */
    public void setMatches(ArrayList<BasicUser> arrMatches) { this.matches = arrMatches; }

    /**
     * Digs through array of matches and finds BasicUser specified by ID if they exist
     * @param userId
     * @return
     */
    public BasicUser getMatchById(int userId) {
        for (int i = 0; i < matches.size(); ++i) {
            if (matches.get(i).getId() == userId) {
                return matches.get(i);
            }
        }
        return null;
    }

    /**
     * Digs through array of matches and finds BasicUser specified by username if they exist
     * @param username
     */
    public BasicUser getMatchByUsername(String username) {
        for (int i = 0; i < matches.size(); ++i) {
            if (matches.get(i).getUsername().equals(username)) {
                return matches.get(i);
            }
        }
        return null;
    }

    /**
     * Removes all matches from this user's list of matches
     * @param userId
     */
    public void clearMatches(int userId) {
        matches = new ArrayList<BasicUser>();
    }

    //Removes specific user from given users list
    public void removeMatch() {
        // TODO
    }

    //Sends a request to "match" with a BasicUser
    public void requestMatch() {
        // TODO
    }

    public String getBuyerImage() {
        return buyerImage;
    }

    public void setBuyerImage(String buyerImage) {
        this.buyerImage = buyerImage;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
    @JsonProperty("filterByAge")
    public boolean filtersByAge() {
        return filterByAge;
    }

    public void setFilterByAge(boolean filterByAge) {
        this.filterByAge = filterByAge;
    }

    public String getBreedPreference() {
        return breedPreference;
    }

    public void setBreedPreference(String breedPreference) {
        this.breedPreference = breedPreference;
    }
    @JsonProperty("filterByBreed")
    public boolean filtersByBreed() {
        return filterByBreed;
    }

    public void setFilterByBreed(boolean filterByBreed) {
        this.filterByBreed = filterByBreed;
    }
    @JsonProperty("wantsIndoorPet")
    public boolean wantsIndoorPet() {
        return wantsIndoorPet;
    }

    public void setIndoorPetFilter(boolean wantIndoorPet) {
        this.wantsIndoorPet = wantIndoorPet;
    }
    @JsonProperty("wantsPottyTrainedPet")
    public boolean wantsPottyTrainedPet() {
        return wantsPottyTrainedPet;
    }

    public void setPottyTrainedFilter(boolean wantPottyTrainedPet) {
        this.wantsPottyTrainedPet = wantPottyTrainedPet;
    }


}
