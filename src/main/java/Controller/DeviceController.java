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
    public List<Device> getDevices(@RequestParam("userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        Collections.sort(devices);
        return devices;
    }

    /*
     * Note: Adding Devices is handled by a separate controller for Device authentication and validation.
     */

    /**
     * Changes the Device's diaplay name.
     * @param id the ID of the Device to be modified
     * @param name the new display name
     * @return the Device that was modified
     */
    @GetMapping("/devices/update")
    public Device updateDevice(@RequestParam("id") String id, @RequestParam(value = "name", defaultValue = "") String name) {
        if (!userRepository.existsById(id)) {
            // TODO error handling
        }
        Device device = deviceRepository.findById(id).orElse(null);
        device.setName(name);
        return deviceRepository.save(device);
    }

    /**
     * Removes a device with the given ID.
     * @param id the ID associated with the device
     * @return the Device that was removed
     */
    @GetMapping("/devices/remove")
    public Device removeDevice(@RequestParam("id") String id) {
        if (!deviceRepository.existsById(id)) {
            // TODO error handling
        }
        Device removedDevice = deviceRepository.findById(id).orElse(null);
        deviceRepository.deleteById(id);
        return removedDevice;
    }


}
