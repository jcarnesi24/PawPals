package pawpals_db.Sitters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pawpals_db.Users.BasicUser;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jacob Carnesi
 * @author Zane Lenz
 */
@Entity
public class Sitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "basic_id")
    @JsonIgnore
    private BasicUser account;

    //List of this sitter's matches
    // Will either be a List or ArrayList so we can have variable size.
    private ArrayList<BasicUser> matches;

    //This sitter's rating
    private double rating;

    //Count of how many dogs this sitter has sat through the app
    private int dogsSat;

    private String sitterImage;

    //Constructors for sitter

    public Sitter(ArrayList<BasicUser> matches, float rating, int dogsSat) {
        this.matches = matches;
        this.rating = rating;
        this.dogsSat = dogsSat;
        this.sitterImage = null;
    }

    public Sitter() {
        this.rating = 5;
        this.matches = new ArrayList<BasicUser>();
        this.dogsSat = 0;
        this.sitterImage = null;
    }
    public Sitter(BasicUser account) {
        this.rating = 5;
        this.matches = new ArrayList<BasicUser>();
        this.dogsSat = 0;
        this.account = account;
        this.sitterImage = null;
    }

    public void setAccount(BasicUser account) {
        this.account = account;
    }
    public BasicUser getAccount() {
        return account;
    }

    //methods

    /**
     * Lists all matches for the user
     * @return
     */
    public ArrayList<BasicUser> getMatches() { return matches; }

    /**
     * Sets user's list of matches to be equal to the given list of matches
     * @param arrMatches
     */
    public void setMatches(ArrayList<BasicUser> arrMatches) { matches = arrMatches; }

    /**
     * Returns this sitter's rating
     * @return
     */
    public double getRating() { return rating; }

    /**
     * Sets this sitter's rating
     * @param transactionRating - A rating on a particular interaction.
     */
    public void setRating(double transactionRating) {
        // TODO: Change this to take in a new rating and alter the current rating
        // to incorporate the new rating like EBay or Amazon does.
        // This would probably be something like averaging all the past ratings plus the new one
        rating = transactionRating;
    }

    /**
     * Returns recorded number of dogs this sitter has sat
     * @return
     */
    public int getDogsSat() { return dogsSat; }

    /**
     * Sets number of dogs this sitter has sat
     * @param numDogs
     */
    public void setDogsSat(int numDogs) { dogsSat = numDogs; }

    /**
     * Called to increment the number of dogs sat by this sitter.
     */
    public void satADog() { dogsSat++; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSitterImage() {
        return sitterImage;
    }

    public void setSitterImage(String sitterImage) {
        this.sitterImage = sitterImage;
    }

    @Override
    public String toString() {
        return account.getFirstname() + " " + account.getLastname() + "," + id + "," + account.getUserBio() + ","
                + account.getEmailAddress() + "," + sitterImage + "," + rating + "," + dogsSat + ",";
    }
}
