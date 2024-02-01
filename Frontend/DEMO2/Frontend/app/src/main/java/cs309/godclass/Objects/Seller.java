package cs309.godclass.Objects;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a seller in the system, responsible for owning dogs and managing associated information.
 */
public class Seller implements Serializable {

    private List<Dog> Dogs = new ArrayList<>();
    
    private Profile account;

    private double rating;

    private String sellerProfilePic;

    /**
     * Constructs a Seller object with a default rating of 5 and associates it with a given profile.
     *
     * @param profile The profile associated with the seller.
     */
    public Seller(Profile profile, String sellerProfilePic) {
        this.account = profile;
        this.rating = 5;
        this.sellerProfilePic = sellerProfilePic;
    }


    /**
     * Retrieves a specific dog from the seller's list of dogs based on the provided index.
     *
     * @param i The index of the dog in the list.
     * @return The dog at the specified index.
     */
    public Dog getDog(int i) {
        return Dogs.get(i);
    }

    public String getSellerProfilePic() {
        return sellerProfilePic;
    }

    /**
     * Retrieves the list of dogs owned by the seller.
     *
     * @return The list of dogs owned by the seller.
     */
    public List<Dog> getDogs() {
        return Dogs;
    }

    /**
     * Sets the list of dogs owned by the seller.
     *
     * @param Dogs The list of dogs to be set for the seller.
     */
    public void setDogs(List<Dog> Dogs) {
        this.Dogs = Dogs;
    }

    /**
     * Retrieves the profile associated with the seller.
     *
     * @return The profile associated with the seller.
     */
    public Profile getAccount() {
        return account;
    }

    /**
     * Sets the profile associated with the seller.
     *
     * @param account The profile to be set for the seller.
     */
    public void setAccount(Profile account) {
        this.account = account;
    }

    /**
     * Retrieves the rating of the seller.
     *
     * @return The rating of the seller.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the rating for the seller.
     *
     * @param rating The rating to be set for the seller.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setSellerProfilePic(String sellerProfilePic) {
        this.sellerProfilePic = sellerProfilePic;
    }
}
