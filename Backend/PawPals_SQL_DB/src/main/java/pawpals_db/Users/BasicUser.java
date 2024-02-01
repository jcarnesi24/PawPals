package pawpals_db.Users;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import pawpals_db.Buyers.BuyerRepository;
import pawpals_db.Pets.Pet;
import pawpals_db.Chat.Message;
import pawpals_db.Chat.Conversation;
import pawpals_db.Buyers.Buyer;
import pawpals_db.Sellers.Seller;
import pawpals_db.Sellers.SellerRepository;
import pawpals_db.Sitters.Sitter;
import pawpals_db.Sitters.SitterRepository;

/**
 * Every PawPals account starts as a BasicUser, which stores the information common to all actors.
 * The types of actors are Seller, Buyer, Dog Sitter, Moderators, and Administrators. An account has
 * an @OneToOne relation to the associated actor types.
 *
 * @author Jacob Carnesi
 * @author Zane Lenz
 * 
 */ 

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BasicUser {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * The username, which I think is displayed to other users. I don't think we store real names.
     * If we do store real names, that variable will be called realName.
     */
    private String username;

    private String firstname;
    private String lastname;

    /**
     * The user's email address, used for account recovery I guess.
     */
    private String emailAddress;

    // Should we store age?


    /**
     * A user's bio information, which is basically just a description of who they are and
     * why they should be allowed around your dog.
     */
    private String userBio;

    /**
     * The user's address, which is used to localize the profiles they are shown or shown to.
     * I don't know how we're going to implement this, or if we will.
     */
    private String userAddress;

    /*
     * Ideally, we will store the user's password as a hash. Refactor to passwordHash if we
     * end up doing that.
     */
    private String password; // Also, store the salt in this class.

    /*
     * Holds all the conversations that this user currently has
     * Why is this an error?
     */
    @OneToMany(mappedBy = "thisUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conversation> conversations;

    /**
     * When a user logs in with the login method, this has to be set to "True" and changed to "False"
     * upon logout. If we want to have any actual security, this would have to be checked before
     * responding to any request. Other ways of implementing this would be actually using tokens.
     * I don't know how to do that though.
     */
    private boolean isAuthenticated; // TODO: Build in the functionality to make this actually used.

    // Relations for each different role the user can take on.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private Seller sellerProfile;
    private boolean seller;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id")
    private Buyer buyerProfile;
    private boolean buyer;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sitter_id")
    private Sitter sitterProfile;
    private boolean sitter;

    // ~~~~~~~~~~ Constructors ~~~~~~~~~~ \\
    // A default constructor is required.

    public BasicUser() {
        //this.admin = false;
        this.conversations = new ArrayList<Conversation>();
        this.sitter = false;
        this.seller = false;
        this.buyer = false;
    }

    public BasicUser(String username, String emailAddress) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.isAuthenticated = true;
        this.conversations = new ArrayList<>();
        //this.isAuthenticated = true;
        this.sitter = false;
        this.seller = false;
        this.buyer = true;
    }

    public BasicUser(String firstname, String lastname, String emailAddress, String username, String password, String userBio) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
        this.userBio = userBio;

        // booleans?
        this.sitter = false;
        this.seller = false;
        this.buyer = true;
    }

    // TODO: might not actually need this...
    // Or, convert to a copyBasicUser() function.
    public BasicUser(BasicUser other) {
        this.firstname = other.firstname;
        this.lastname = other.lastname;
        this.emailAddress = other.emailAddress;
        this.username = other.username;
        this.password = other.password;
        this.userBio = other.userBio;

        this.sitter = other.sitter;
        this.seller = other.seller;
        this.buyer = other.buyer;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~Getters and setters for each field~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    public List<Conversation> getConversations() { return conversations; }
    public void setConversations(ArrayList<Conversation> convos) { this.conversations = convos; }

    public void addConversation(Conversation convo) {
        this.conversations.add(convo);
    }
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public boolean isAuthenticated(){
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated){
        this.isAuthenticated = authenticated;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Seller getSellerProfile() {
        return sellerProfile;
    }

    public void setSellerProfile(Seller sellerProfile) {
        this.sellerProfile = sellerProfile;
        this.seller = true;
    }

    public Buyer getBuyerProfile() {
        return buyerProfile;
    }

    public void setBuyerProfile(Buyer buyerProfile) {
        this.buyerProfile = buyerProfile;
        this.buyer = true;
    }

    public Sitter getSitterProfile() {
        return sitterProfile;
    }

    public void setSitterProfile(Sitter sitterProfile) {
        this.sitterProfile = sitterProfile;
        this.sitter = true;
    }

    public void toggleSitterProfile(boolean isSitter) {
        this.sitter = isSitter;
    }

    public void toggleBuyerProfile(boolean isBuyer) {
        this.buyer = isBuyer;
    }

    public void toggleSellerProfile(boolean isSeller) {
        this.seller = isSeller;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isSeller() {
        return seller;
    }

    public boolean isBuyer() {
        return buyer;
    }

    public boolean isSitter() {
        return sitter;
    }

//    public boolean isAdmin() {
//        return admin;
//    }
    // TODO: Validate user method that will return a BasicUser and, if specified, their profile.
    // TODO: The method will ensure the specified BasicUser and profile exist in the table.
    public static BasicUser findValidatedUser(int id) {

        return null;
    }

}
