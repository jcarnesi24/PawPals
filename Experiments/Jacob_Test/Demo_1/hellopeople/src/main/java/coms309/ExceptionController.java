package coms309;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Vivek Bengre
 */

import org.springframework.web.bind.annotation.*;

@RestController
class ExceptionController {

    @RequestMapping(method = RequestMethod.GET, path = "/oops")
    public String triggerException() {
        // An @RequestMapping annotation lets you pick any of the
        // types of request as the method (or even multiple types) and specify the path
        throw new RuntimeException("Check to see what happens when an exception is thrown");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/myBad")
    public String postException(@RequestParam(value = "data") int data) {
        // Add something to a database based on the parameter
        if (data < 100){
            // "add"
            return String.format("Number of pets updated to %d", data);
        }

        else {
            throw new RuntimeException("Exception: There is no way you have more than 100 pets.");
        }
    }

}
