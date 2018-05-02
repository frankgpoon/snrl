package Controller;

import Model.Device;
import Model.DeviceToken;
import Model.DeviceTokenAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/devices/add")
    public DeviceToken addDevice(@RequestParam("userId") String userId) {
        if (!userRepository.existsById(userId)) {
            // TODO error handling
        }
        return authenticator.issueToken(userRepository.findById(userId).orElse(null));
    }

    @GetMapping("/devices/authenticate")
    public Device authenticateDevice(@RequestParam("pin") String pinStr, @RequestParam("authenticationCode") String authenticationCode) {
        int pin = Integer.parseInt(pinStr);
        DeviceToken authenticationToken = new DeviceToken(pin, authenticationCode);

        if (authenticator.isValid(authenticationToken)) {
            return deviceRepository.save(new Device("", authenticator.getUser(authenticationToken).getId()));
        }
        // TODO error handling
        return null;
    }
}
