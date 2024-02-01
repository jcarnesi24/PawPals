package com.cs309.tutorial.tests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
	
	//Sending a GET request
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	} //Access site with localhost:8080/getTest
	  //Concatinate ?username=<Whatever> after above line to send parameters to the site
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		//TODO
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}

	//Sending a POST request and receiving a json response
	@PostMapping("/users")
	public ResponseEntity<Map<String, String>> createUser(@RequestHeader(value = "Accept") String acceptHeader,
														  @RequestHeader(value = "Authorization") String authorizationHeader) {
		Map<String, String> returnValue = new HashMap<>();
		returnValue.put("Accept", acceptHeader);
		returnValue.put("Authorization", authorizationHeader);

		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@DeleteMapping("/deleteTest")
	public String deleteTest() {
		return "DELETE Request sent";
	}
	
	@PutMapping("/putTest")
	public String putTest() {
		return "PUT request sent";
	}
}
