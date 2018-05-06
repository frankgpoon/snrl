package controller;

import model.Device;
import model.DeviceToken;
import util.DeviceRepository;
import util.DeviceTokenAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.UserRepository;

@RestController
public class TokenController {
    // repositories for getting user/device data
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private static final DeviceTokenAuthenticator authenticator = new DeviceTokenAuthenticator(300);

    @Autowired
    public TokenController(UserRepository userRepository, DeviceRepository deviceRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping(value = "/devices/add", produces = "application/json; charset=UTF-8")
    public DeviceToken addDevice(@RequestParam("userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        return authenticator.issueToken(userRepository.findById(userId).orElse(null));
    }

    @PostMapping(value = "/devices/authenticate", produces = "application/json; charset=UTF-8")
    public Device authenticateDevice(@RequestHeader("Pin") String pinStr, @RequestHeader("Authentication-Code") String authenticationCode) {
        int pin = Integer.parseInt(pinStr);
        DeviceToken authenticationToken = new DeviceToken(pin, authenticationCode);

        if (authenticator.isValid(authenticationToken)) {
            return deviceRepository.save(new Device("", authenticator.getUser(authenticationToken).getId()));
        }
        // TODO error handling
        return null;
    }
}
