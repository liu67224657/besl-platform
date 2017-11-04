package com.enjoyf.platform.service.rate;

import java.io.Serializable;

/**
 * @author Yin Pengyi
 */
@SuppressWarnings("serial")
public class RateKeyDomain implements Serializable {

    //wrong passwd rate
    public static final RateKeyDomain LOGIN_WRONG_PWD = new RateKeyDomain("logn.wrg.pwd");
    //register ip rate.
    public static final RateKeyDomain REGISTER_IP = new RateKeyDomain("reg.ip");
    public static final RateKeyDomain REGISTER_COOKIE = new RateKeyDomain("reg.ckie");
    public static final RateKeyDomain GAME_RATING_ADD_FREQUENCY  = new RateKeyDomain(
            "game.rating.add.frequency", new RateLimit(30, 1));

    private String domain;
    private RateLimit rateLimit = new RateLimit();

    private RateKeyDomain(String d) {
        domain = d;
    }

    private RateKeyDomain(String d, RateLimit limit) {
        this.domain = d;
        this.rateLimit = limit;
    }

    /**
     * Constructs a RateKeyDomain
     *
     * @param domain - elementary RateKeyDomain to base off
     * @param limit  - RateLimit to add
     */
    public RateKeyDomain(RateKeyDomain domain, RateLimit limit) {
        if (domain == null) {
            throw new IllegalArgumentException("missing rate key domain");
        }

        if (limit == null) {
            throw new IllegalArgumentException("missing rate limit");
        }

        this.domain = domain.getDomain();
        this.rateLimit = limit;
    }

    /**
     * Gets the associated limit
     *
     * @return RateLimit - associated limit
     */
    public boolean isLimitSet() {
        return rateLimit != null;
    }

    public void setLimit(RateLimit limit) {
        this.rateLimit = limit;
    }

    /**
     * Gets the associated limit
     *
     * @return RateLimit - associated limit
     */
    public RateLimit getLimit() {
        return rateLimit;
    }

    /**
     * Gets the domain string
     *
     * @return String - domain string
     */
    public String getDomain() {
        return domain;
    }

    /**
     * String representatione
     *
     * @return String - string representation
     */
    public String toString() {
        return "RateKeyDomain [" + domain + ", " + rateLimit + "]";
    }

    /**
     * hashCode override
     */
    public int hashCode() {
        return domain.hashCode() + rateLimit.hashCode();
    }

    /**
     * equals override
     *
     * @param o - object to compare this to
     */
    public boolean equals(Object o) {
        // quick tests to get us out of here
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // it's a RateKeyDomain, but it's not this one, so check it for equality
        RateKeyDomain keyDomain = (RateKeyDomain) o;
        return domain.equalsIgnoreCase(keyDomain.domain) && rateLimit.equals(keyDomain.rateLimit);
    }
}