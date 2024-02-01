/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @Modified By Tanmay Ghosh
 * @Modified By Vivek Bengre
 */
@RestController
class OwnerController {

    @Autowired
    OwnerRepository ownersRepository;

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @RequestMapping(method = RequestMethod.POST, path = "/owners/new")
    public String saveOwner(@RequestBody Owners owner) { // Added @RequestBody annotation
        ownersRepository.save(owner); // ownersRepository is the database, OwnerRepository is the object
        return "New Owner "+ owner.getFirstName() + " Saved";
    }
     // function just to create dummy data. Corrected to "owners" instead of "owner"
    @RequestMapping(method = RequestMethod.GET, path = "/owners/create")
    public String createDummyData() {
        Owners o1 = new Owners(1, "John", "Connor", "404 Not found", "some numbers");
        Owners o2 = new Owners(2, "Sarah", "Connor", "Its a secret", "you wish");
        Owners o3 = new Owners(3, "Carah", "Sonnor", "Right next to the Library", "515-345-41213");
        Owners o4 = new Owners(4, "Chad", "Champion", "Reddit memes corner", "420-420-4200");
        Owners o5 = new Owners(5, "Kyle", "Reese", "Tomorrow", "nice try");
        ownersRepository.save(o1);
        ownersRepository.save(o2);
        ownersRepository.save(o3);
        ownersRepository.save(o4);
        ownersRepository.save(o5);
        return "Successfully created dummy data";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/owners")
    public List<Owners> getAllOwners() {
        logger.info("Entered into Controller Layer");
        List<Owners> results = ownersRepository.findAll();
        logger.info("Number of Records Fetched:" + results.size());
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/owners/{ownerId}")
    public Optional<Owners> findOwnerById(@PathVariable("ownerId") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Owners> results = ownersRepository.findById(id);
        return results;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/owners{idNum}/edit{number}")
    public String changeTelephone(@PathVariable("idNum") String idNum, @PathVariable("number") String phone){
        int id = Integer.parseInt(idNum);
        // Optional<Owners> is
        Optional<Owners> owner = ownersRepository.findById(id);
        // Doesn't like using the int for some reason
        if (owner.isPresent()){
            Owners editor = owner.get();
            editor.setTelephone(phone);
            ownersRepository.save(editor);
            return String.format("Updated phone number to %s", phone);
        }

        else {
            return "Owner does not exist.";
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/owners/delete")
    public String deleteOwner(@RequestParam(value = "id") int id) {

        if (ownersRepository.existsById(id)){
            ownersRepository.deleteById(id);
            return "Successfully deleted person";
        }
        else {
            return "Deletion unsuccessful.";
        }
    }

}
