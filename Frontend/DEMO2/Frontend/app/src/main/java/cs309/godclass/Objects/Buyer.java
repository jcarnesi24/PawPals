package cs309.godclass.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Seth Proctor
 * @author Alexander Ryan
 */

/**
 * Class creates a buyer object.
 */
public class Buyer implements Serializable {

    private Profile account;

    //buy dogs
    //swipe left on dog
    //swipe right on dog
    //unmatch with dog

    private String buyerProfilePic;

    boolean wantAge = false;
    int minAge = 0;
    int maxAge = 0;
    boolean wantBreed = false;
    String breedPreference;
    boolean wantPottyTrained = false;
    boolean wantInsideDog = false;


    /**
     * Constructor for the buyer profile
     * @param account
     */
    public Buyer(Profile account, String buyerProfilePic ) {
        this.account = account;
        this.buyerProfilePic = buyerProfilePic;
    }

    public void setAgePreference(int min, int max) {
        wantAge = true;
        this.maxAge = max;
        this.minAge = min;
    }
    public boolean isWantAge() { return wantAge; }

    public void clearAgePreference() {
        wantAge = false;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setBreedPreference(String breed) {
        wantBreed = true;
        breedPreference = breed;
    }
    public boolean isWantBreed() { return wantBreed; }
    public void clearBreedPreference() {
        wantBreed = false;
    }

    public String getBreedPreference() { return breedPreference; }

    public void wantPottyTrained() { wantPottyTrained = true; }
    public void noWantPottyTrained() { wantPottyTrained = false; }

    public boolean isWantPottyTrained() { return wantPottyTrained; }

    public void wantInsideDog() { wantInsideDog = true; }
    public void noWantInsideDog() { wantInsideDog = false; }
    public boolean isWantInsideDog() { return wantInsideDog; }
    /**
     * Returns the acount of the profile
     * @return
     */
    public Profile getAccount() {
        return account;
    }


    /**
     * Gets the buyer profile pictures url.
     * @return
     */
    public String getBuyerProfilePic() {
        return buyerProfilePic;
    }

    /**
     * Sets the account for a profile
     * @param account
     */
    public void setAccount(Profile account) {
        this.account = account;
    }

    public void setBuyerProfilePic(String buyerProfilePic) {
        this.buyerProfilePic = buyerProfilePic;
    }



}
