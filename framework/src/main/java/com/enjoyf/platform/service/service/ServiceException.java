package com.enjoyf.platform.service.service;

import com.enjoyf.platform.util.StringUtil;

/**
 * A class to encapsulate error codes. This class is intended for
 * derivation by other classes. ServiceExceptions are represented as static
 * objects. This class defines some set of errors, while derived
 * classes augment the set.
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
    private int value;

    /**
     * A name given to the exception at static init time.
     */
    private String name = "";

    /**
     * Additional detail for a particular instance of the exception in
     * order to give more info. This should never be used by code to
     * decide on different code paths. It should only appear in logs.
     */
    private String detail = "";
    /**
     * This is used to indicate if our ServiceException or subclasses are in a temporary error state, for example
     * 'TIMEOUT'.  This allows us to filter Exceptions up the framework heirarchy and determine if an alert
     * bug needs to be logged.
     */
    private boolean transientFlag;

    /**
     * Every error consists of a numeric code and a String
     * representation. Since derived classes need to somehow
     * specify an error code, we will allocate ranges in this
     * class. This means that every time we create a new
     * derived clas of ServiceException, we must add an entry
     * below, to specify the base number for that class.
     * I think if we use ranges of one thousand we will be fine.
     *
     * When a specific error msg is constructed, the base is added
     * to the specific error code.
     */

    ////////////////////////////////////////////////////////////////////
    /**
     * Base for this class.
     */
    protected static final int BASE_SERVICE = 1000;

    /**
     * Base for db exceptions.
     */
    protected static final int BASE_DB = 6000;

    ////////////////////////////////////////////////////////////////////
    //the commom services
    ////////////////////////////////////////////////////////////////////
    /**
     * Base for account service.
     */
    protected static final int BASE_ACCOUNT = 10000;

    protected static final int BASE_CONTENT = 11000;

    protected static final int BASE_REGCODE = 12000;

    protected static final int BASE_SOCIAL = 13000;

    protected static final int BASE_SYNC = 14000;

    protected static final int BASE_VOTE = 15000;

    protected static final int BASE_FAVORITE = 16000;

    protected static final int BASE_POINT = 165000;

    protected static final int BASE_LOTTERY = 17000;

    protected static final int BASE_ACTIVITY = 18000;

    protected static final int BASE_USERCENTER = 19000;
    /**
     * Base for advertise service.
     */
    protected static final int BASE_ADVERTISE = 15000;

    /**
     * Base for audit service.
     */
    protected static final int BASE_AUDIT = 20000;

    /**
     * Base for badge service.
     */
    protected static final int BASE_BADGE = 25000;

    /**
     * Base for billing service.
     */
    protected static final int BASE_BILLING = 30000;

    /**
     * Base for card service.
     */
    protected static final int BASE_CARD = 35000;

    /**
     * Base for challenge service.
     */
    protected static final int BASE_CHALLENGE = 40000;

    /**
     * Base for event service.
     */
    protected static final int BASE_EVENT = 45000;

    /**
     * Base for friends service.
     */
    protected static final int BASE_FRIENDS = 50000;

    /**
     * Base for game data service.
     */
    protected static final int BASE_GAMEDATA = 52000;

    /**
     * Base for game interface service.
     */
    protected static final int BASE_GAMEINTERFACE = 55000;

    /**
     * Base for onject service.
     */
    protected static final int BASE_GAMEMONITOR = 60000;

    /**
     * bas for group service
     */
    protected static final int BASE_GROUP = 62000;

    /**
     * bas for guest service
     */
    protected static final int BASE_GUEST = 65000;

    /**
     * bas for high score service
     */
    protected static final int BASE_HIGHSCORE = 70000;

    /**
     * bas for hotel service
     */
    protected static final int BASE_HOTEL = 75000;

    /**
     * Base for item service.
     */
    protected static final int BASE_IMAGE = 80000;

    /**
     * Base for item service.
     */
    protected static final int BASE_ITEM = 85000;

    /**
     * bas for find player service
     */
    protected static final int BASE_LOCATION = 90000;

    /**
     *
     */
    protected static final int BASE_LOCK = 95000;

    /**
     * Base for mail server.
     */
    protected static final int BASE_MAIL = 100000;

    /**
     * Base for message server.
     */
    protected static final int BASE_MESSAGE = 105000;

    /**
     * Base for message server.
     */
    protected static final int BASE_MISC = 110000;

    /**
     * Base for naming server.
     */
    protected static final int BASE_NAMING = 115000;

    /**
     * Base for page view service.
     */
    protected static final int BASE_PAGEVIEW = 120000;

    /**
     * Base for prize service.
     */
    protected static final int BASE_PRIZE = 125000;

    /**
     * Base for profile service.
     */
    protected static final int BASE_PROFILE = 130000;

    /**
     * Base for game zone key service.
     */
    protected static final int BASE_PROMOTEKEY = 135000;

    /**
     * Base for rate service.
     */
    protected static final int BASE_RATE = 140000;

    /**
     * bas for room service
     */
    protected static final int BASE_ROOM = 145000;

    /**
     * Base for search service.
     */
    protected static final int BASE_SEARCH = 150000;

    /**
     * Base for shop service.
     */
    protected static final int BASE_SHOP = 155000;

    /**
     * Base for spreader service.
     */
    protected static final int BASE_SPREADER = 160000;

    /**
     * Base for sso service.
     */
    protected static final int BASE_SSO_AUTH = 165000;

    /**
     * Base for stats service.
     */
    protected static final int BASE_STATS = 170000;

    /**
     * Base for task service.
     */
    protected static final int BASE_TASK = 175000;

    /**
     * Base for ticket service.
     */
    protected static final int BASE_TICKET = 180000;

    /**
     * Base for upload file service.
     */
    protected static final int BASE_UPLOADFILE = 185000;

    /**
     * Base for upload game service.
     */
    protected static final int BASE_UPLOADGAME = 190000;



    /**
     * Base for user props service.
     */
    protected static final int BASE_USERPROPS = 195000;

     protected static final int BASE_VIEWLINE = 200000;

    ////////////////////////////////////////////////////////////
    /**
     * Unknown/unset error.
     * This is really stupid, we should define error number instead of instances of ServiceExceptions
     */
    public static final int UNKNOWN = BASE_SERVICE;

    /**
     * Connect error.
     */
    public static final int CONNECT = BASE_SERVICE + 1;
    /**
     * Timeout error.
     */
    public static final int TIMEOUT = BASE_SERVICE + 2;
    /**
     * Bad packet error.
     */
    public static final int BAD_PACKET = BASE_SERVICE + 3;
    /**
     * Some sort of monitor error.
     */
    public static final int MONITOR_ERR = BASE_SERVICE + 4;
    /**
     * A generic exception.
     */
    public static final int GENERIC = BASE_SERVICE + 5;
    /**
     * An exception thrown to indicate that we need to synch up.
     */
    public static final int NEED_SYNCH = BASE_SERVICE + 6;

    /**
     * An exception thrown to indicate that we are in the middle of
     * synching and cannot logicProcess any requests right now.
     */
    public static final int IS_SYNCHING = BASE_SERVICE + 7;

    /**
     * An exception to indicate that a server is overloaded.
     */
    public static final int OVERLOADED = BASE_SERVICE + 8;

    /**
     * An exception to indicate that a connection is in progress.
     */
    public static final int CONNECT_IN_PROGRESS = BASE_SERVICE + 9;

    /**
     * An exception to indicate that the adjustment failed
     */
    public static final int ADJUSTMENT_FAILED = BASE_SERVICE + 10;

    /**
     * An exception to indicate that the argument is invalid.
     */
    public static final int INVALID_ARGUMENT = BASE_SERVICE + 11;

    /**
     * An exception to indicate that many transactions of a particular
     * type have exceeded db resource limits and are being fast-failed.
     */
    public static final int FAST_FAIL_RESOURCE_LIMIT = BASE_SERVICE + 12;

    /**
     * An exception to indicate we encountered a serialization exception.
     */
    public static final int SERIALIZATION_ERROR = BASE_SERVICE + 13;

    /**
     * An exception to indicate we encountered an unknown host exception
     * in the framework layers.
     */
    public static final int UNKNOWN_HOST = BASE_SERVICE + 14;

    /**
     * An exception to indicate we encountered an unknown host exception
     * in the framework layers.
     */
    public static final int BAD_SERVICE_IMPLEMENTATION = BASE_SERVICE + 15;


    protected ServiceException() {
        this(BASE_SERVICE + 0, "UNKNOWN");
    }

    protected ServiceException(ServiceException e) {
        this(e.getValue(), e);
    }

    public ServiceException(int value) {
        this(value, null, null, null, true);
    }

    public ServiceException(int value, String name) {
        this(value, name, null, null, true);
    }

    public ServiceException(int value, Throwable cause) {
        this(value, null, null, cause, true);
    }

    /**
     * This was the constructor normall used before jdk1.4. Now
     * the constructor that carries a possible cause should be used instead.
     *
     * @param value  This is the integer code uniquely identifying
     *               this type of exception.
     * @param name   This is the name of the exception. Strictly for
     *               informational purposes (ie, it appears in logs)
     * @param detail Additional detail to differentiate between
     *               instances of the same type of exception. Again, strictly for
     *               informational purposes; not to be used by business logic.
     *               If you need additional data in your exception that is used by
     *               business logic, then define your own exception class (see
     *               service.juser.VerifyNameException as an example).
     */
    public ServiceException(int value, String name, String detail) {
        this(value, name, detail, null);
    }

    /**
     * This is the old constructor, before adding isTransient methods.
     *
     * @param value  This is the integer code uniquely identifying
     *               this type of exception.
     * @param name   This is the name of the exception. Strictly for
     *               informational purposes (ie, it appears in logs)
     * @param detail Additional detail to differentiate between
     *               instances of the same type of exception. Again, strictly for
     *               informational purposes; not to be used by business logic.
     *               If you need additional data in your exception that is used by
     *               business logic, then define your own exception class (see
     *               service.juser.VerifyNameException as an example).
     * @param cause  the cause (which is saved for later retrieval by the
     *               {@link #getCause()} method).  (A <tt>null</tt> value is
     *               permitted, and indicates that the cause is nonexistent or
     *               unknown.)
     */
    public ServiceException(int value, String name, String detail, Throwable cause) {
        this(value, name, detail, cause, false);
    }

    /**
     * This is THE constructor.
     * All other constructors should call this one to make
     * sure that all fields are properly initialized.
     *
     * @param value       This is the integer code uniquely identifying
     *                    this type of exception.
     * @param name        This is the name of the exception. Strictly for
     *                    informational purposes (ie, it appears in logs)
     * @param detail      Additional detail to differentiate between
     *                    instances of the same type of exception. Again, strictly for
     *                    informational purposes; not to be used by business logic.
     *                    If you need additional data in your exception that is used by
     *                    business logic, then define your own exception class (see
     *                    service.juser.VerifyNameException as an example).
     * @param isTransient - This is used to indicate if our ServiceException
     *                    or subclasses are in a temporary error state, for example 'TIMEOUT'.
     * @param cause       the cause (which is saved for later retrieval by the
     *                    {@link #getCause()} method).  (A <tt>null</tt> value is
     *                    permitted, and indicates that the cause is nonexistent or
     *                    unknown.)
     */
    public ServiceException(int value, String name, String detail, Throwable cause, boolean isTransient) {
        super(name, cause);
        this.value = value;
        if (StringUtil.isEmpty(name)) {
            name = "UNKNOWN";
        }
        this.name = name;
        this.detail = detail;
        this.transientFlag = isTransient;
    }

    public ServiceException(ServiceException exception, String s) {
        this(exception.getValue(), s);
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    /**
     * Returns the value of this exception
     *
     * @return int, the value of this exception.
     */
    public int getValue() {
        return value;
    }

    public boolean isTransient() {
        return transientFlag;
    }

    /**
     * The std equals method.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceException)) {
            return false;
        }
        ServiceException serr = (ServiceException) obj;
        return serr.value == value;
    }

    /**
     * The std hashcode method.
     */
    public int hashCode() {
        return value;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(":").append(detail).append(":");
        return sb.toString();
    }

}