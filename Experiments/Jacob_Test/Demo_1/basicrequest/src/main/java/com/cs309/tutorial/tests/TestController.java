package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Annotations (like @GetMapping) are used to map a type of request
 * to a specific URL. They do not enforce the type of behavior the
 * request should dictate. It is your job to make sure the annotations
 * and behavior match!
 */

@RestController
public class TestController {

	// IT DID NOT LIKE THIS.
	// Create a dummy user to be accessed? I really don't know if that's how this works
	//TestData dummyUser = new TestData("Sarge", "Donut");
	//TestData dummy2 = new TestData("Tucker", "Blarg");
	/**
	 * The @GetMapping annotation makes a method to handle a particular suffix to the url.
	 * The parameter follows the suffix as /getTest?username=Name
	 */
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}

	/**
	 * Test of getting multiple parameters in the same url.
	 * Multiple parameters start the same as a single parameter, with a ? mark.
	 * Successive parameters are separated by & symbols, but still use = to specify the value.
	 */
	@GetMapping("/multiTest")
	public String multiTest(@RequestParam(value = "name") String message, @RequestParam(value = "theme",
			defaultValue = "orange") String themeColor){
		return String.format("Hi, %s, and welcome to PawPals! Enjoy experiencing the %s theme color.",
				message, themeColor);
	}

	/**
	 * Creating a new user profile from a name and pet name.
	 * Would've been nice to know that I had to use a whole other program
	 * besides my browser to perform other types of mapping.
	 *
	 * All the other types of mapping keep their parameters in the request body
	 * and not the URL.
	 * @param username - The name of the new user.
	 * @return - A message that shows the username and pet name.
	 */
	@PostMapping("/newProfile")
	public String newProfileTest(@RequestParam(value = "username") String username, @RequestParam(value = "petName")
			String petName) {
		TestData newUser = new TestData(username, petName); // This would presumably need to be stored somehow...
		return String.format("Hello, %s! You created a new profile for yourself and %s!", newUser.getUsername(), newUser.getAPet(0));
	}

	/**
	 * Test creating a new user page from a username.
	 * The @RequestBody annotation indicates the data is pulled from
	 * the body of the request packet, not the URL.
	 *
	 * @param testData - The request includes the necessary data to make a new user.
	 * @return - A confirmation message.
	 */
	@PostMapping("/newUser")
	public String newUserTest(@RequestBody TestData testData) {
		//TODO
		// I don't think there's anything to do really.
		return String.format("Hello, %s! You sent a post request with a requestbody! Your message is '%s'", testData.getUsername(), testData.getMessage());
	}

	/**
	 * Deletes a user resource, like a pet that has found a new home
	 */
	@DeleteMapping("/deleteTest")
	public void deleteTest(@RequestParam(value = "username") String username,
						   @RequestParam(value = "petName") String soldPet) {
		// First, find a user in the database based on username.
		// Not the point of this exercise.

		// Then use the removePet() method to remove the pet.

		// I think in real practice it would be best to check if the pet was
		// removed or not here and do something to show if it was a success or not.

		// Without making a database, I don't know what to do with this.
	}

	/**
	 * Adds a pet to a user's profile.
	 */
	@PutMapping("/putTest")
	public void putTest() {
		//TODO

		// A PUT request is used to alter an entry in a database.
		// Again, without a database there's basically no point to
		// writing actual code for this...
	}
}
