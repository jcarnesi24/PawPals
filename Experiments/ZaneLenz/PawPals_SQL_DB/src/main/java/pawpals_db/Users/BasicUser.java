package pawpals_db.Users;

import javax.persistence.*;

import pawpals_db.Pets.Pet;

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
    private String password;

    /**
     * When a user logs in with the login method, this has to be set to "True" and changed to "False"
     * upon logout. If we want to have any actual security, this would have to be checked before
     * responding to any request. Other ways of implementing this would be actually using tokens.
     * I don't know how to do that though.
     */
    private boolean isAuthenticated; // TODO: Build in the functionality to make this actually used.

    // TODO: Add relations for each different role the user can take on.
    // The different roles, like seller or dogsitter solicitor, will have the pet/dog relations.

    // Preliminary testing setup: BasicUser owns the pet.
//    @OneToMany
//    @JoinColumn(name = "pet_id")
//    private Pet testDawg;

    public BasicUser(String username, String emailAddress) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.isAuthenticated = true;
    }

    public BasicUser() {
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~Getters and setters for each field~~~~~~~~~~~~~~~~~~~~~~~~~~ //

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

    public boolean getIsActive(){
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated){
        this.isAuthenticated = authenticated;
    }

    // TODO: add the CORRECT methods to access pet
//
//    private Pet getTestDawg() {
//        return testDawg;
//    }
//
//    private void setTestDawg(Pet newDawg) {
//        this.testDawg = newDawg;
//    }

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

    // TODO: Zane and Seth's round trip uses the password methods. I don't think you'll want
    // to have these basic and unconditional getters and setters for a password.
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
