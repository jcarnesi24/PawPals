package pawpals_db.Sellers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pawpals_db.Pets.Pet;
import pawpals_db.Users.BasicUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A seller is the only type of user that owns dogs in PawPals. Dog ownership is not transferred
 * to the buyer in our database - we don't plan to collect that much information.
 *
 * @author Jacob Carnesi
 */
@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> dawgs;


    @OneToOne
    @JoinColumn(name = "basic_id")
    @JsonIgnore
    private BasicUser account;

    /**
     * A list of users that have matched with this Seller profile
     */
    private ArrayList<BasicUser> matches;

    /**
     * A user's satisfaction rating out of 5 stars
     * Starts at 5?
     */
    private double rating;

    private String sellerImage;


    public Seller() {
        this.dawgs = new ArrayList<Pet>();
        this.rating = 5;
        this.matches = new ArrayList<BasicUser>();
        this.sellerImage = null;
    }
    public Seller(BasicUser account) {
        this.dawgs = new ArrayList<Pet>();
        this.rating = 5;
        this.matches = new ArrayList<BasicUser>();
        this.sellerImage = null;
        this.account = account;
    }


    public Pet getDawg(int i) {
        return dawgs.get(i);
    }

    public List<Pet> getDawgs() {
        return dawgs;
    }

    public void setDawgs(List<Pet> dawgs) {
        this.dawgs = dawgs;
    }

    public void addDawg(Pet newDawg) {
        this.dawgs.add(newDawg);
    }

    public BasicUser getAccount() {
        return account;
    }

    public void setAccount(BasicUser account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    // I don't think we want to be able to set the id
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<BasicUser> getMatches() {
        return matches;
    }

    public void addMatch(BasicUser match) {
        this.matches.add(match);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }
}
