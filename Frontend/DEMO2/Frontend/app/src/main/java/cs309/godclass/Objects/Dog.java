package cs309.godclass.Objects;

import java.io.Serializable;

/**
 * Class that stores the paramaters for each dog object
 */
public class Dog implements Serializable {
    String petName;
    String breed;
    String age;
    String id;
    String dogImage;
    String bio;
    boolean insideDog;
    boolean pottyTrained;


    /**
     * Basic constructor
     * @param petName
     * @param breed
     * @param age
     */
    public Dog(String petName, String breed, String age, String id, String dogImage, String bio, boolean pottyTrained, boolean insideDog) {
        this.petName = petName;
        this.breed = breed;
        this.age = age;
        this.id = id;
        this.dogImage = dogImage;
        this.pottyTrained = pottyTrained;
        this.insideDog = insideDog;
        this.bio = bio;

    }

    /**
     * Construstor that as everything preset to blank
     */
    public Dog() {
        petName = "";
        breed = "";
        age = "";
        bio = "";
        insideDog = false;
        pottyTrained = false;
        id = "0";
    }

    public String getBio() { return bio; }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isInsideDog() { return insideDog; }
    public void setInsideDog(boolean insideDog) { this.insideDog = insideDog;}
    public void setPottyTrained(boolean pottyTrained) {this.pottyTrained = pottyTrained; }
    public boolean isPottyTrained() { return pottyTrained; }

    /**
     * name getter method
     * @return
     *  dog's name
     */
    public String getName() {
        return petName;
    }

    /**
     * breed getter method
     * @return
     *  dog's breed
     */
    public String getBreed() {
        return breed;
    }

    /**
     * Age getter method
     * @return
     *  dog's age
     */
    public String getAge() {
        return age;
    }

    public String getDogImage() {
        return dogImage;
    }

    /**
     * name setter method
     * @param name
     */
    public void setName(String name) {
        this.petName = name;
    }

    public void setDogImage(String dogImage) {
        this.dogImage = dogImage;
    }

    /**
     * id getter method
     * @return
     *  dog id
     */
    public String getId() { return id; };

    /**
     * breed setter method
     * @param breed
     */
    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * age setter method
     * @param age
     */
    public void setAge(String age){
        this.age = age;
    }

    public void setId(String newDogId) {
        this.id = newDogId;
    }
}
