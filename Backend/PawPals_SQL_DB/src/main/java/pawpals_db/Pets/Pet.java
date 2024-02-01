package pawpals_db.Pets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pawpals_db.Sellers.Seller;

import javax.persistence.*;

/**
 * Every pet/dog on PawPals is a Pet object, which stores all the necessary information
 * on that individual pet/dog. A seller may own multiple different individual dogs.
 * Pets are owned by sellers and accessed by buyers on the swiping interface.
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

    private String petImage;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller owner;

    // ~~~~~~~~~~ Constructors ~~~~~~~~~~ \\

    /**
     * Must include a default constructor.
     */
    public Pet() {
        this.petName = "";
        this.petBio = "";
        this.age = 0;
        //this.petImage = "";
        this.breed = "";
        this.indoorPet = false;
        this.pottyTrained = false;
        this.shown = false;
    }
    /**
     * The most basic type of pet creation.
     *
     * @param petName - name of the pet.
     * @param breed - the pet's breed.
     * @param age - how old the pet is.
     * @param petBio - bio for a pet. Should be written in 1st person.
     */
    public Pet(String petName, String breed, int age, String petBio, String petImage) {
        this.petName = petName;
        this.breed = breed;
        this.age = age;
        this.petBio = petBio;
        this.petImage = petImage;
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
    public Seller getOwner(){
        return owner;
    }

    public void setOwner(Seller owner){
        this.owner = owner;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        if (this.getId() == ((Pet)other).getId()) {
            return true;
        }
        else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {

        return petName + "," + id + "," + breed + "," + age + "," + petImage + "," + this.getOwner().getRating() + "," + petBio + "," + pottyTrained + "," + indoorPet + ",";    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;

    }
}
