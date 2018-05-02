package Model;

import org.springframework.data.annotation.Id;

import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A Model.Device represents a single device belonging to a Model.User. It consists of a unique id for identification
 * and a device name set by the Model.User.
 */
public class Device implements Comparable<Device> {


    // An immutable ID assigned by MongoDB. It is unique to devices.
    @Id
    private String id;
    // should be less than 32 characters for usability purposes
    private String name;
    // The User ID that this device is associated with.
    private String userId;
    // The datetime that a particular Device is created, in UTC
    private Calendar dateCreated;

    /**
     * Creates a new Model.Device from the given id, name, and type
     *
     * @param name A name for the user to identify the device. It can be changed.
     */
    public Device(String name, String userId) {
        this.name = name;
        this.userId = userId;
        dateCreated = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));
        checkInvariant();
    }

    /**
     * Returns the user-identifiable name of the device.
     *
     * @return the name of the device
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique ID of the device.
     *
     * @return the unique ID of the device
     */
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Returns the datetime that this device was added in UTC.
     * @return the datetime that this device was added in UTC
     */
    public Calendar getDateCreated() {
        return (Calendar) dateCreated.clone();
    }

    /**
     * Changes the name of the device.
     *
     * @param name the new name of the device
     */
    public void setName(String name) {
        if (name.length() > 32) {
            throw new IllegalArgumentException("Name length should be less than 32 characters");
        }
        this.name = name;
        checkInvariant();
    }

    /**
     * Returns true if the two objects are equal (if they are both Devices have equal ids)
     *
     * @param o the object to be compared to
     * @return true if the two objects are Devices have equal ids
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Device && this.id.equals(((Device) o).id);
    }

    /**
     * Returns >0 if this device is lexicographically greater than the other device, 0 if they are equal, and <0 if this
     * device is lexicographically less than the other device. Compares first by name, then by id if names are
     * identical.
     *
     * @param device the other device to compare to
     * @return an integer indicating whether this device is lexicographically greater, equal, or less than the other
     * device.
     */
    @Override
    public int compareTo(Device device) {
        if (this.name.equals(device.name)) {
            // guaranteed that ids are unique so no need to compare type
            return this.id.compareTo(device.id);
        }
        return this.name.compareTo(device.name);
    }

    /**
     * Returns a hashcode corresponding to the device.
     *
     * @return a hashcode corresponding to the device
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    // private method for ensuring the rep invariant stays true
    private void checkInvariant() {
        assert this.id != null;
        assert this.name != null;
        assert this.dateCreated != null;
        assert this.name.length() <= 32;
    }
}
