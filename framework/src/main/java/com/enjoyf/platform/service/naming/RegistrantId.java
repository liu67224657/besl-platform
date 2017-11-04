/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

/**
 * This class represents an id for a Registrant object.
 */
public class RegistrantId implements java.io.Serializable {
    private static long globalId = 1;

    private long id;
    private long clientId;

    public RegistrantId(long id) {
        this.id = p_nextId();

        clientId = id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        RegistrantId regId = (RegistrantId) obj;
        if (regId.id != id) {
            return false;
        }

        return regId.clientId == clientId;
    }

    public int hashCode() {
        return (int) id + (int) clientId;
    }

    public String toString() {
        return "id=" + id + ":clientId=" + clientId;
    }

    private static long p_nextId() {
        return globalId++;
    }
}
