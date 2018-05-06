package controller;

import model.Device;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.DeviceRepository;
import util.UserRepository;

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
    @GetMapping(value = "/users/all", produces = "application/json; charset=UTF-8")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns the user corresponding to the given unique ID, or an error if a no user exists with the given ID.
     * @param userId the User's ID. A 12 digit String
     * @return The User corresponding to the given ID, or an error if no match is found.
     */
    @GetMapping(value = "/users/get", produces = "application/json; charset=UTF-8")
    public User getUserById(@RequestParam("userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Adds a new User to the database.
     * @param userName the User's display name. Changeable.
     * @return the User that was added.
     */
    @GetMapping(value = "/users/add", produces = "application/json; charset=UTF-8")
    public User addUser(@RequestParam("userName") String userName, @RequestParam("deviceName") String deviceName) {
        User newUser = userRepository.save(new User(userName));
        deviceRepository.save(new Device(deviceName, newUser.getId()));
        return newUser;
    }

    /**
     * Changes the User's display name. Shows an error if no user exists with the given ID.
     * @param userId the ID of the User to be modified
     * @param userName the new display name
     * @return the updated User
     */
    @GetMapping(value = "/users/update", produces = "application/json; charset=UTF-8")
    public User updateUser(@RequestParam("userId") String userId, @RequestParam(value = "userName") String userName){
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        User user = userRepository.findById(userId).orElse(null);
        user.setName(userName);
        return userRepository.save(user);
    }

    /**
     * Removes the User with the given ID.
     * TODO fix authentication
     * @param userId the ID of the user to remove
     * @return the removed User for information to be preserved.
     */
    @GetMapping(value = "/users/remove", produces = "application/json; charset=UTF-8")
    public User removeUser(@RequestParam("userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        // TODO find a better message to output after removing removedUser
        User removedUser = userRepository.findById(userId).orElse(null);
        userRepository.deleteById(userId);
        return removedUser;
    }



}
