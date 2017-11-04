/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceSngl;

/**
 * This is a specialization of the ServiceConfig class that deals with
 * using a NamingService to find services. Consider this a convenience class
 * for putting together the proper ReqProcessor object to be used by
 * a service class.
 */
public class ServiceConfigNaming extends ServiceConfig {
    private NamingService namingService = null;
    private ServiceRequest serviceRequest = null;
    private int frequency;
    private int offset;
    private ConnChooser serviceChooser;
    private TransProfileContainer transProfileContainer;
    private boolean registerForEventsFlag = true;

    public ServiceConfigNaming(ServiceRequest req) {
        this(NamingServiceSngl.get(), req);
    }

    /**
     * Set whether or not we want events when the state of the
     * services change (for the particular ServiceRequest) within
     * the naming service. By default, this is true.
     */
    public void setRegisterForEvents(boolean registerForEvents) {
        registerForEventsFlag = registerForEvents;
    }

    /**
     * Optionally set a TransProfileContainer object. If one is set,
     * transaction stats are logged by the underlying ReqProcessor object.
     */
    public void setTransProfileContainer(TransProfileContainer profileContainer) {
        transProfileContainer = profileContainer;
    }

    /**
     * This ctor is used to explicitly specify the NamingService
     * object to be used.
     *
     * @param service The NamingService object to use.
     * @param req     The service request to use.
     */
    public ServiceConfigNaming(NamingService service, ServiceRequest req) {
        namingService = service;
        serviceRequest = req;
    }

    /**
     * Use a refresh thread when querying the naming service. This
     * is necessary for services that want to keep updating the
     * current set of servers from the naming service in order to
     * get the current load on those services.
     *
     * @param frequency The frequency in msecs to query for the
     *                  services. If 0, no query is used.
     * @param offset    If non-zero, it randomizes the frequency
     *                  between frequency-offset to frequency+offset. In msecs.
     */
    public void useRefreshThread(int frequency, int offset) {
        this.frequency = frequency;
        this.offset = offset;
    }

    /**
     * Override the choice of ConnChooser. The default is
     * ConnChooserRR. If you want to use your own, you must
     * call this method prior to calling getReqProcessor().
     */
    public void setConnChooser(ConnChooser chooser) {
        serviceChooser = chooser;
    }

    /**
     * Sez whether or not this object has been configured correctly.
     */
    public boolean isValid() {
        return serviceRequest != null;
    }

    /**
     * Retrieve the ReqProcessor object.
     */
    public ReqProcessor getReqProcessor() {
        if (reqProcessor != null) {
            return reqProcessor;
        }

        //--------------------------------------------------------
        // We don't build the object up right away, we wait until
        // all necessary routines are called (namely setTimeout())
        // and then we build the object (but only once).
        //--------------------------------------------------------

        if (namingService == null) {
            throw new RuntimeException("ServiceConfigNaming: NamingService is null");
        }

        //default chooser is RR.
        ConnChooser chooser = serviceChooser;
        if (chooser == null) {
            chooser = new ConnChooserRR(getTimeout());
        }

        ServiceFinderNaming finder = new ServiceFinderNaming(namingService, serviceRequest);
        finder.setRegisterForEvents(registerForEventsFlag);
        if (frequency != 0) {
            finder.useRefreshThread(frequency, offset);
        }

        chooser.setServiceFinder(finder);
        reqProcessor = allocReqProcessor(chooser, getTimeout());

        //--
        // If we have a TransProfileContainer, use it for spitting out
        // service transaction stats. If we don't, we just create an
        // empty one and let the stats gathering default (ie, you won't
        // get nice names for the transactions, just integers).
        //--
        if (transProfileContainer == null) {
            transProfileContainer = new TransProfileContainer();
        }
        reqProcessor.setTransProfileContainer(transProfileContainer);

        return reqProcessor;
    }

    /**
     * Retrieve the ServiceRequest object configured.
     */
    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    /**
     * Provided so derived classes can allocate their own ReqProcessor.
     */
    protected ReqProcessor allocReqProcessor(ConnChooser chooser, int timeout) {
        return new ReqProcessor(chooser, new ProcessStrategyStandard(), timeout);
    }

    public String getId() {
        return serviceRequest == null ? "UNKNOWN" : serviceRequest.toString();
    }
}
