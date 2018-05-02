package Model;

import java.time.ZoneOffset;
import java.util.*;

public class DeviceTokenAuthenticator {
    private static Random random = new Random();
    private static final int MAX_PIN_COMBINATIONS = 1000000;
    private static final int AUTH_CODE_LENGTH = 4;

    // maps DeviceTokens to corresponding users
    private Map<DeviceToken, User> tokenMap;
    // sets how long tokens are valid in seconds
    int tokenDuration;

    /**
     * Creates a new DeviceTokenAuthenticator with the given token duration.
     *
     * @param tokenDuration the number of seconds a token is valid for.
     */
    public DeviceTokenAuthenticator(int tokenDuration) {
        this.tokenDuration = tokenDuration;
        tokenMap = new HashMap<DeviceToken, User>();
    }

    /**
     * Returns the token duration.
     * @return the token duration
     */
    public int getTokenDuration() {
        return tokenDuration;
    }

    /**
     * Gets the expiration datetime of a {@link DeviceToken}.
     *
     * @param token the token to get the expiration of
     * @return the expiration datetime
     */
    public Calendar getExpiration(DeviceToken token) {
        Calendar timestamp = token.getTimestamp();
        timestamp.add(Calendar.SECOND, tokenDuration);
        return timestamp;
    }

    /**
     * Checks if a token is expired.
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired(DeviceToken token) {
        return Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC)).compareTo(getExpiration(token)) >= 0;
    }

    /**
     * Issues a new token associated with the given user. The token is valid for the set duration.
     *
     * @param user the User to associate the token to
     * @return the generated token
     */
    public DeviceToken issueToken(User user) {
        DeviceToken token = new DeviceToken(generatePin(), generateAuthenticationCode());
        tokenMap.put(token, user);
        return token.clone();
    }

    /**
     * Checks if the given external token matches the internal one in this {@link DeviceTokenAuthenticator}. Valid is
     * defined as matching pin and authentication code, that both tokens are not expired, and that the internal token
     * was issued before the external token to prevent token forging.
     *
     * @param externalToken a token received from outside sources to validate
     * @return true if the given external token is valid, false otherwise
     */
    public boolean isValid(DeviceToken externalToken) {
        if (tokenMap.containsKey(externalToken)) {

            DeviceToken internalToken = externalToken;
            for (DeviceToken token : tokenMap.keySet()) {
                if (token.equals(externalToken)) {
                    internalToken = token;
                }
            }
            return !isExpired(internalToken) && !isExpired(externalToken)
                    && externalToken.getTimestamp().compareTo(internalToken.getTimestamp()) >= 0;
        }
        return false;
    }

    /**
     * Returns the {@link User} that the given token corresponds to, or null if the token is not valid.
     * @param externalToken a token received from outside sources
     * @return the User that the given token corresponds to, or null if the token is not valid
     */
    public User getUser(DeviceToken externalToken) {
        if (isValid(externalToken)) {
            return tokenMap.remove(externalToken);
        }
        return null;
    }

    // creates a random pin
    private int generatePin() {
        return random.nextInt(MAX_PIN_COMBINATIONS);
    }

    // creates a random authentication code
    private String generateAuthenticationCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder authenticationCode = new StringBuilder();
        for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
            int alphabetIndex = random.nextInt(alphabet.length());
            authenticationCode.append(alphabet.charAt(alphabetIndex));
        }
        return authenticationCode.toString();
    }
}
