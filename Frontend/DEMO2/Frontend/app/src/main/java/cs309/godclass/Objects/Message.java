package cs309.godclass.Objects;

import java.io.Serializable;

/**
 * Class that holds a message object
 */
public class Message implements Serializable {

    String otherUsername;

    String id;
    String otherUserType;

    /**
     * Constructor for the message class. Creates a new message with id of the other user and their id.
     * @param otherUsername
     * @param id
     */
    public Message(String otherUsername, String id, String otherUserType){

        this.otherUsername = otherUsername;
        this.otherUserType = otherUserType;
        this.id = id;

    }


    /**
     * Getter method for otherUsername
     * @return
     */
    public String getOtherUsername(){return otherUsername;}

    /**
     * Getter method for the id
     * @return
     */
    public String getId(){return id;}

    /**
     * Getter method for the type of the other user
     * @return
     */
    public String getOtherUserType() { return otherUserType; }

    /**
     * Sets the otherUsername
     * @param otherUsername
     */
    public void setOtherUsername(String otherUsername){
        this.otherUsername = otherUsername;
    }

    /**
     * Sets the id
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }




}
