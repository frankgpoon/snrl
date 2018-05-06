package util;

import model.DeviceToken;
import model.User;

import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeviceTokenAuthenticator {
    private static Random random = new Random();
    private static final int MAX_PIN_COMBINATIONS = 1000000;
    private static final int AUTH_CODE_LENGTH = 4;
    private static final int TOKEN_DURATION = 300;

    // maps DeviceTokens to corresponding users
    private Map<DeviceToken, User> tokenMap;
    // sets how long tokens are valid in seconds


    /**
     * Creates a new DeviceTokenAuthenticator.
     */
    public DeviceTokenAuthenticator() {
        tokenMap = new HashMap<DeviceToken, User>();
        ScheduledExecutorService tokenCleanerService = Executors.newScheduledThreadPool(1);
        tokenCleanerService.scheduleWithFixedDelay(new DeviceTokenCleaner(tokenMap), 60, 1200, TimeUnit.SECONDS);
    }

    /**
     * Issues a new token associated with the given user. The token is valid for the set duration.
     *
     * @param user the User to associate the token to
     * @return the generated token
     */
    public DeviceToken issueToken(User user) {
        DeviceToken token = new DeviceToken(generatePin(), generateAuthenticationCode(), TOKEN_DURATION);
        tokenMap.put(token, user);
        return token;
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
            return internalToken.isExpired() && externalToken.isExpired()
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

    private class DeviceTokenCleaner implements Runnable {
        // tokenMap to clean
        private Map<DeviceToken, User> tokenMap;

        /**
         * Creates a new {@link DeviceTokenCleaner} with the given tokenMap.
         * @param tokenMap the tokenMap to clean
         */
        public DeviceTokenCleaner(Map<DeviceToken, User> tokenMap) {
            this.tokenMap = tokenMap;
        }

        /**
         * Cleans expired tokens.
         */
        @Override
        public void run() {
            Iterator<DeviceToken> tokenIterator = tokenMap.keySet().iterator();
            while (tokenIterator.hasNext()) {
                if (tokenIterator.next().isExpired()) {
                    tokenIterator.remove();
                }
            }
        }
    }
}
