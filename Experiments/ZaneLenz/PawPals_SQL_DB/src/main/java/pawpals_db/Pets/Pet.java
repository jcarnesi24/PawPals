package pawpals_db.Pets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pawpals_db.Users.BasicUser;

import javax.persistence.*;

/**
 * Every pet/dog on PawPals is a Pet object, which stores all the necessary information.
 *
 * @author Jacob Carnesi
 */
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String petName;

    private String breed;

    private int age;

    // location is pulled from the associated BasicUser

    // Array of Enum personality tags.

    // photos

    private boolean pottyTrained;

    /**
     * A "true" value indicate the pet lives indoors, while "false" means it lives outside.
     */
    private boolean indoorPet;

    private String petBio;

    /**
     * Determines whether a pet is shown or hidden for swipe interface.
     */
    private boolean shown;

    // TODO: Add table relations. Should be an @ManyToOne relation with its owner, which has an @OneToMany relation with it.
    // TODO: Don't forget @JsonIgnore annotations.

//    @ManyToOne
//    @JsonIgnore
//    private BasicUser owner;

    /**
     * The most basic type of pet creation.
     *
     * @param petName - name of the pet.
     * @param breed - the pet's breed.
     * @param age - how old the pet is.
     */
    public Pet(String petName, String breed, int age) {
        this.petName = petName;
        this.breed = breed;
        this.age = age;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~Getters and setters for each field~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPottyTrained() {
        return pottyTrained;
    }

    public void setPottyTrained(boolean pottyTrained) {
        this.pottyTrained = pottyTrained;
    }

    public boolean isIndoorPet() {
        return indoorPet;
    }

    public void setIndoorPet(boolean indoorPet) {
        this.indoorPet = indoorPet;
    }

    public String getPetBio() {
        return petBio;
    }

    public void setPetBio(String petBio) {
        this.petBio = petBio;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    // TODO: Figure this out
//    public BasicUser getOwner(){
//        return owner;
//    }
//
//    public void setOwner(BasicUser basicUser){
//        this.owner = basicUser;
//    }
}
