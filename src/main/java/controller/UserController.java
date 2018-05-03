package controller;

import model.Device;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController handles all REST operations pertaining to adding, viewing, updating, or removing users.
 */
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public UserController(UserRepository userRepository, DeviceRepository deviceRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Returns a list for all users.
     * TODO fix this to require developer authentication
     * @return A list of all users
     */
    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns the user corresponding to the given unique ID, or an error if a no user exists with the given ID.
     * @param id the User's ID. A 12 digit String
     * @return The User corresponding to the given ID, or an error if no match is found.
     */
    @GetMapping("/users/get")
    public User getUserById(@RequestParam("id") String id) {
        if (!userRepository.existsById(id)) {
            // TODO error handling
        }
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Adds a new User to the database.
     * @param name the User's display name. Changeable.
     * @return the User that was added.
     */
    @GetMapping("/users/add")
    public User addUser(@RequestParam("name") String name, @RequestParam("deviceName") String deviceName) {
        User newUser = userRepository.save(new User(name));
        deviceRepository.save(new Device(deviceName, newUser.getId()));
        return newUser;
    }

    /**
     * Changes the User's display name. Shows an error if no user exists with the given ID.
     * @param id the ID of the User to be modified
     * @param name the new display name
     * @return the updated User
     */
    @GetMapping("/users/update")
    public User updateUser(@RequestParam("id") String id, @RequestParam(value = "name", defaultValue = "") String name){
        if (!userRepository.existsById(id)) {
            // TODO error handling
        }
        User user = userRepository.findById(id).orElse(null);
        user.setName(name);
        return userRepository.save(user);
    }

    /**
     * Removes the User with the given ID.
     * TODO fix authentication
     * @param id the ID of the user to remove
     * @return the removed User for information to be preserved.
     */
    @GetMapping("/users/remove")
    public User removeUser(@RequestParam("id") String id) {
        if (!userRepository.existsById(id)) {
            // TODO error handling
        }
        // TODO find a better message to output after removing removedUser
        User removedUser = userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
        return removedUser;
    }



}
