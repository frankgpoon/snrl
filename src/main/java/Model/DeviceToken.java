package Model;

import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

public class DeviceToken {
    // a pin with an integer value
    private int pin;
    // an alphabetical authenticationCode that is used along with the authentication code to validate a DeviceToken
    private String authenticationCode;
    // the datetime that this DeviceToken was created
    private Calendar timestamp;

    /**
     * Creates a new DeviceToken with the given pin and authentication code
     * @param pin the pin of the DeviceToken
     * @param authenticationCode the corresponding authentication code
     */
    public DeviceToken(int pin, String authenticationCode) {
        this.pin = pin;
        this.authenticationCode = authenticationCode;
        this.timestamp = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));
        checkInvariant();
    }

    // private DeviceToken constructor with custom date
    private DeviceToken(int pin, String authenticationCode, Calendar timestamp) {
        this.pin = pin;
        this.authenticationCode = authenticationCode;
        this.timestamp = (Calendar) timestamp.clone();
    }

    /**
     * Returns the pin.
     * @return the pin
     */
    public int getPin() {
        return pin;
    }

    /**
     * Returns the authentication code.
     * @return the authentication code
     */
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    /**
     * Returns the datetime that this DeviceToken was created
     * @return the datetime that this DeviceToken was created
     */
    public Calendar getTimestamp() {
        return (Calendar) timestamp.clone();
    }

    /**
     * Returns a copy of this DeviceToken.
     * @return a copy of this DeviceToken
     */
    public DeviceToken clone() {
        return new DeviceToken(pin, authenticationCode, timestamp);
    }

    /**
     * Returns a hashcode for this DeviceToken.
     * @return a hashcode for this DeviceToken
     */
    @Override
    public int hashCode() {
        return authenticationCode.hashCode() * 13 + pin;
    }

    /**
     * Returns true if pin and authentication code in both objects are equal, false otherwise.
     * @param o the object to be compared
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof DeviceToken) {
            DeviceToken token = (DeviceToken) o;
            return this.pin == token.pin && this.authenticationCode.equals(token.authenticationCode);
        }
        return false;
    }

    // checks that rep invariant holds
    private void checkInvariant() {
        assert pin >= 0;
        assert authenticationCode != null;
        assert timestamp != null;
    }
}
