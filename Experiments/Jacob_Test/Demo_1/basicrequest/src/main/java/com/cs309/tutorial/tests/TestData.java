package com.cs309.tutorial.tests;

import java.util.ArrayList;

/**
 * I have no idea why they didn't provide documentation or guidance. Would kill 'em to add a
 * structure or push in the right direction? Anyway, TestData is a class that, essentially, is here
 * to store information gathered from the Controller's post methods, be accessed by get,
 * edited by post, and deleted by delete. Use all of these in conjunction to form the tests
 * in the test class.
 *
 * Feel free to add additional variables to store more info on a user's profile.
 */
public class TestData {

	/**
	 * No relation to "message" in the TestController class.
	 */
	private String message;

	/**
	 * The user's name, part of their profile.
	 */
	private String username;

	/**
	 * A list of the pets a user has. May be empty if they are a loser.
	 * This will be a separate class in the full version.
	 */
	private ArrayList<String> pets;

	/**
	 * Default Constructor
	 */
	public TestData() {}

	/**
	 * Creates a new user from a username
	 *
	 * @param username
	 */
	public TestData(String username) {
		this.message = username;
		this.pets = new ArrayList<String>();
	}

	/**
	 * Creates a new user and their first pet.
	 *
	 * @param username
	 * @param petName
	 */
	public TestData(String username, String petName) {
		this.username = username;
		this.pets = new ArrayList<String>();
		addPet(petName);
	}

	// ----- Setters -----
	public void setMessage(String message) {
		this.message = message;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addPet(String petName) {
		this.pets.add(petName);
	}

	// ----- Getters -----
	public String getMessage() {
		return message;
	}

	public String getUsername() {
		return username;
	}

	public String getAPet(int index) {
		return (index >= 0 && index < pets.size()) ? pets.get(index) : "You have no pets in PawPals!";
	}

	public boolean removePet(String name){
		return pets.remove(name);
	}
}
