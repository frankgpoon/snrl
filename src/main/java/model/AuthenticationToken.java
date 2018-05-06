package model;

import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;

public class DeviceToken {
    // a pin with an integer value
    private int pin;
    // an alphabetical passcode that is used along with the pin to validate a DeviceToken
    private String passcode;
    // how long this token is valid for
    private int duration;
    // the datetime that this DeviceToken was created
    private Calendar timestamp;

    public static final int DEFAULT_DURATION = 300;

    /**
     * Creates a new DeviceToken with the given pin, passcode, and duration
     * @param pin the pin of the DeviceToken
     * @param passcode the corresponding passcode code
     * @param duration how long the token is valid for, in seconds
     */
    public DeviceToken(int pin, String passcode, int duration) {
        this.pin = pin;
        this.passcode = passcode;
        this.duration = duration;
        this.timestamp = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));
        checkInvariant();
    }

    /**
     * Creates a new DeviceToken with the given pin and passcode, with the default duration of 300 seconds.
     * @param pin the pin of the DeviceToken
     * @param passcode the corresponding passcode code
     */
    public DeviceToken(int pin, String passcode) {
        this(pin, passcode, DEFAULT_DURATION);
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
    public String getPasscode() {
        return passcode;
    }

    /**
     * Returns the duration of this token, or how long it's valid for, in seconds.
     * @return the duration of this token in seconds
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the datetime that this DeviceToken was created
     * @return the datetime that this DeviceToken was created
     */
    public Calendar getTimestamp() {
        return (Calendar) timestamp.clone();
    }

    public boolean isExpired() {
        Calendar expireTime = getTimestamp();
        expireTime.add(Calendar.SECOND, duration);
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC)).compareTo(expireTime) >= 0;
    }

    /**
     * Returns a hashcode for this DeviceToken.
     * @return a hashcode for this DeviceToken
     */
    @Override
    public int hashCode() {
        return passcode.hashCode() * 13 + pin;
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
            return this.pin == token.pin && this.passcode.equals(token.passcode);
        }
        return false;
    }

    /**
     * Parses a Base 64 encoded String containing a DeviceToken and returns the object
     * @param encodedToken the String to parse
     * @return a DeviceToken containing the pin and passcode from the encoded token, along with the default duration
     */
    public static DeviceToken parseBase64(String encodedToken)  {
        String decodedToken = new String(Base64.getDecoder().decode(encodedToken));
        String[] tokenArray = decodedToken.split(":");
        return new DeviceToken(Integer.parseInt(tokenArray[0]), tokenArray[1], DEFAULT_DURATION);
    }

    // checks that rep invariant holds
    private void checkInvariant() {
        assert pin >= 0;
        assert passcode != null;
        assert duration > 0;
        assert timestamp != null;
    }
}
