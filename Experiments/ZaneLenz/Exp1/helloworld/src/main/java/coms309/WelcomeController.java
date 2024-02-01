package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 *
 * @author Zane Lenz
 */
@RestController
class WelcomeController {

    public int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    //Displays text to the webpage made by Application.java
    @GetMapping("/")
    public String welcome() {

        return "Hello and welcome to COMS 309";
    }

    //welcome function and number function conflict when both not commented out

    //Displays text to the webpage given my user after localhost.8080/
    /*@GetMapping("/{name}")
    public String welcome(@PathVariable String name) {

        return "Hello and welcome to COMS 309: " + name;
    }

    //Displays ints given by user when launching site
    //Seems like you need to convert to a string before it will display numbers properly
    @GetMapping("/{num}")
        public String number(@PathVariable int num) {
            return "Your number is: " + num;
        }*/

    @GetMapping("/{secret}")
        public String secret(@PathVariable String secret) {
            if (secret.equals("secret")) {
                int i;
                String s = "";
                //\n seems to just do spaces instead of new lines on webpage
                for (i = 0; i < arr.length; ++i) {
                    s = s + arr[i] + "\n";
                }
                secret = s;
                return secret;
            }
            else {
                return "Access Denied";
            }
        }
    }