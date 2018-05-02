package Controller;

import Model.Device;
import Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class DeviceController {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceController(UserRepository userRepository, DeviceRepository repository) {
        this.userRepository = userRepository;
        this.deviceRepository = repository;
    }

    /**
     * Returns a list of all devices by all users.
     * TODO fix this to require developer authentication
     * @return A list of all devices
     */
    @GetMapping("/devices/all")
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/devices/get")
    public List<Device> getDevices(@RequestParam(value = "userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        Collections.sort(devices);
        return devices;
    }

    /**
     * Adds a new device with the given name associated with the given User ID.
     * @param name the name of the device
     * @param userId the User ID associated with the device
     * @return the Device that was added
     */
    @GetMapping("/devices/add")
    public Device addDevice(@RequestParam(value =
            "name") String name, @RequestParam(value = "userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        return deviceRepository.save(new Device(name, userId));
    }


}
