package cs309.godclass.Objects;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sitter in the system, responsible for pet sitting and managing associated information.
 *
 * @author Alexander Ryan
 * @author Seth Proctor
 */
public class Sitter implements Serializable {

    private Profile account;

    private float rating;

    private int dogsSat;

    private String name;

    private String bio;

    private String email;

    private String id;

    private String sitterProfilePic;

    private Sitter sitterQueue;

    /**
     * Constructs a Sitter object with a default rating of 0, no dogs sat, and associates it with a given profile.
     *
     * @param account The profile associated with the sitter.
     */
    public Sitter(Profile account, String sitterProfilePic) {
        this.rating = 0;
        this.dogsSat = 0;
        this.account = account;
        this.sitterProfilePic = sitterProfilePic;
    }

    public Sitter(String name, String email, String bio, String sitterProfilePic, float rating, String id, int dogsSat){
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.sitterProfilePic = sitterProfilePic;
        this.id = id;
        this.dogsSat = dogsSat;
        this.rating = rating;
    }


    public void setSitterQueue(Sitter sitter) {
        this.sitterQueue = sitter;
    }

    /**
     * Sets the profile associated with the sitter.
     *
     * @param account The profile to be set for the sitter.
     */

    public void setAccount(Profile account) {
        this.account = account;
    }


    /**
     * Retrieves the profile associated with the sitter.
     *
     * @return The profile associated with the sitter.
     */
    public Profile getAccount() {
        return account;
    }

    public String getSitterProfilePic() {
        return sitterProfilePic;
    }

    /**
     * Retrieves the rating of the sitter.
     *
     * @return The rating of the sitter.
     */
    public float getRating() { return rating; }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getBio() { return bio; }

    /**
     * Sets the rating for the sitter.
     *
     * @param transactionRating A rating.
     */
    public void setRating(float transactionRating) {
        rating = transactionRating;
    }

    /**
     * Retrieves the recorded number of dogs sat by the sitter.
     *
     * @return The number of dogs sat by the sitter.
     */
    public int getDogsSat() { return dogsSat; }

    public Sitter getSitterQueue() { return sitterQueue; }

    /**
     * Sets the number of dogs sat by the sitter.
     *
     * @param numDogs The number of dogs sat to be set for the sitter.
     */
    public void setDogsSat(int numDogs) { dogsSat = numDogs; }

    public void setSitterProfilePic(String sitterProfilePic) {
        this.sitterProfilePic = sitterProfilePic;
    }



}
