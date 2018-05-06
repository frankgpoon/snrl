package controller;

import model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import util.DeviceRepository;
import util.UserRepository;

import java.util.Collections;
import java.util.List;

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
    @GetMapping(value = "/devices/all", produces = "application/json; charset=UTF-8")
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping(value = "/devices/get", produces = "application/json; charset=UTF-8")
    public List<Device> getDevices(@RequestHeader("User-Id") String userId) {
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
     * Changes the Device's display name.
     * @param deviceId the ID of the Device to be modified
     * @param deviceName the new display name
     * @return the Device that was modified
     */
    @GetMapping(value = "/devices/update", produces = "application/json; charset=UTF-8")
    public Device updateDevice(@RequestHeader("Device-Id") String deviceId, @RequestHeader("Device-Name") String deviceName) {
        if (!deviceRepository.existsById(deviceId)) {
            // TODO error handling
        }
        Device device = deviceRepository.findById(deviceId).orElse(null);
        device.setName(deviceName);
        return deviceRepository.save(device);
    }

    /**
     * Removes a device with the given ID.
     * @param deviceId the ID associated with the device
     * @return the Device that was removed
     */
    @GetMapping(value = "/devices/remove", produces = "application/json; charset=UTF-8")
    public Device removeDevice(@RequestHeader("Device-Id") String deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            // TODO error handling
        }
        Device removedDevice = deviceRepository.findById(deviceId).orElse(null);
        deviceRepository.deleteById(deviceId);
        return removedDevice;
    }


}
