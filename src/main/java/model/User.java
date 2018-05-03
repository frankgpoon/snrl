package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneOffset;
import java.util.*;

/**
 * A model.User is a class for unique users of Unifeed. A model.User consists of a unique id to identify them, a display name for
 * them to use, and devices belonging to that model.User.
 */
@Document
public class User {
    // An immutable ID assigned by MongoDB. It is unique compared to other users.
    @Id
    private String id;
    // Username. Mutable, <= 32 characters for usability
    private String name;
    // The datetime that a particular user is created, in UTC
    private Calendar dateCreated;


    /**
     * Creates a new model.User with the given id and name.
     *
     * @param name The display name of the user. It can be changed.
     */
    public User(String name) {
        this.name = name;
        dateCreated = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    /**
     * Returns the unique ID of the user.
     *
     * @return the unique ID of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the user's display name.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the datetime that this user was created in UTC.
     * @return the datetime that this user was created in UTC
     */
    public Calendar getDateCreated() {
        return (Calendar) dateCreated.clone();
    }

    /**
     * Changes the user s display name.
     *
     * @param name the new username
     */
    public void setName(String name) {
        this.name = name;
        checkInvariant();
    }

    /**
     * Returns true if the two objects are equal (if they are both Users and have equal id)
     *
     * @param o The object to be compared to
     * @return true if the two objects are Users and have equal ids
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof User && this.id.equals(((User) o).id);
    }

    /**
     * Returns a hashcode corresponding to the user.
     *
     * @return a hashcode corresponding to the user
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    // private method to ensure rep invariant still holds
    private void checkInvariant() {
        assert this.id != null;
        assert this.name != null;
        assert this.dateCreated != null;
        assert this.name.length() <= 32;
    }
}
