package cs309.godclass.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that stores all of the values of each profile
 */
public class Profile implements Serializable {
    String bio;
    String name;
    String firstname;
    String lastname;
    String email;
    String address;
    String password;
    String username;
    Boolean buyer;
    Boolean seller;
    Boolean sitter;
    String currUser;
    String buyerProfilePic;
    String sellerProfilePic;
    String sitterProfilePic;
    String hide;


    ArrayList<Message> messages = new ArrayList<Message>();
    String id;
    Buyer buyerProfile;
    Seller sellerProfile;
    Sitter sitterProfile;


    /**
     * Constructor for profile
     * @param firstname
     * @param lastname
     * @param email
     * @param password
     * @param username
     * @param buyer
     * @param seller
     * @param sitter
     * @param messages
     * @param id
     */
    public Profile(String firstname,String lastname, String email, String password, String username, Boolean buyer, Boolean seller, Boolean sitter, ArrayList<Message> messages, String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.username = username;
        this.buyer = buyer;
        this.seller = seller;
        this.sitter = sitter;
        this.name = firstname + " " +  lastname;

        this.id = id;

        this.messages = messages;

        if (buyer) {
            buyerProfile = new Buyer(this, buyerProfilePic);
        }
        if (seller) {
            sellerProfile = new Seller(this, sellerProfilePic);

        }
        if (sitter) {
            sitterProfile = new Sitter(this, sitterProfilePic);
        }

        bio = "This is the default bio. Go to settings to change your bio";
    }


    /**
     * firstname getter method
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * id getter method
     *
     * @return
     */
    public String getId() { return id; }

    /**
     * lastname getter method
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * username getter method
     * @return
     */
    public String getUsername() { return username; }

    /**
     * buyer getter method
     * @return
     */
    public Boolean getBuyer() {
        return buyer;
    }

    /**
     * seller getter method
     * @return
     */
    public Boolean getSeller() {
        return seller;
    }

    /**
     * sitter getter method
     * @return
     */
    public Boolean getSitter() {
        return sitter;
    }

    /**
     * name getter method
     * @return
     *  name
     */
    public String getName() {
        return name;
    }

    /**
     * hide getter method
     * @return
     */
    public String getHide(){return hide;}

    /**
     * email getter method
     * @return
     *  email
     */
    public String getEmail() {
        return email;
    }

    /**
     * bio getter method
     * @return
     *  bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Address getter method
     * @return
     *  Address
     */
    public String getAddress() {
        return address;
    }

    public String getBuyerProfilePic() {
        return buyerProfilePic;
    }

    /**
     * Password getter method
     * @return
     *  Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * CurrUser getter method
     * @return
     */
    public String getCurrUser() {
        return currUser;
    }

    /**
     * seller profile getter method
     * @return
     */
    public Seller getSellerProfile() { return sellerProfile; }

    /**
     * Buyer profile getter method
     * @return
     */
    public Buyer getBuyerProfile() {
        return buyerProfile;
    }

    /**
     * Sitter profile getter method
     * @return
     */
    public Sitter getSitterProfile() {
        return sitterProfile;
    }

    /**
     * Messages getter method
     * @return
     */
    public ArrayList<Message> getMessages() { return messages; }

    /**
     * Adds message to the arrayList
     * @param message
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * name setter method
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * username setter method
     * @param username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * email setter method
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Address setter method
     * @param address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * bio setter method
     * @param bio
     */
    public void setBio(String bio){
        this.bio = bio;
    }

    /**
     * password setter method
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * fistname setter method
     * @param firstname
     */
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    /**
     * lastname setter method
     * @param lastname
     */
    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    /**
     * buyer setter method
     * @param buyer
     */
    public void setBuyer(Boolean buyer){
        this.buyer = buyer;
    }

    /**
     * seller setter method
     * @param seller
     */
    public void setSeller(Boolean seller){
        this.seller = seller;
    }

    /**
     * hide setter method
     * @param hide
     */
    public void setHide(String hide){
        this.hide = hide;
    }

    /**
     * sitter setter method
     * @param sitter
     */
    public void setSitter(Boolean sitter){
        this.sitter = sitter;
    }

    /**
     * User setter method
     * @param currUser
     */
    public void setUser(String currUser){this.currUser = currUser;}

    /**
     * id setter method
     * @param id
     */
    public void setId(String id){this.id = id;}

}
