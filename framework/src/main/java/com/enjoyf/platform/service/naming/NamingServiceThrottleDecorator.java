/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import java.util.Vector;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * A Decorator for the NamingService. This implementation
 * throttles access when the naming service is down. If a request
 * is received by this object, and the naming service is down,
 * it won't try again for 'checkInterval' msecs. Also, if 'sleepInterval'
 * is set to non-zero, it will sleep for that amount of msecs.
 */
public class NamingServiceThrottleDecorator extends NamingServiceDecorator {
    private long lastDownTime = 0;
    private boolean down = false;
    private int checkInterval = 5 * 1000;
    private int sleepInterval = 5 * 1000;

    public NamingServiceThrottleDecorator(NamingService namingService, int checkInterval, int sleepInterval) {
        super(namingService);
        this.checkInterval = checkInterval;
        this.sleepInterval = sleepInterval;
    }

    public NamingServiceThrottleDecorator(NamingService namingService, int checkInterval) {
        this(namingService, checkInterval, 5 * 1000);
    }

    public NamingServiceThrottleDecorator(NamingService namingService) {
        this(namingService, 5 * 1000, 5 * 1000);
    }

    public void setSleepInterval(int sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    private synchronized void p_preCheck() throws ServiceException {
        int diff = (int) (System.currentTimeMillis() - lastDownTime);

        if (down && (diff < checkInterval)) {
            if (sleepInterval != 0) {
                //--
                // Using the wait() as a sleep. So we synch while
                // perform all the computation, but we don't block
                // other threads from entering.
                //--
                try {
                    wait(sleepInterval - diff);
                }
                catch (InterruptedException e) {
                }

                diff = (int) (System.currentTimeMillis() - lastDownTime);
            }

            if (diff < checkInterval) {
                throw new ServiceException(ServiceException.CONNECT,
                        "Can't talk to naming service yet");
            }
        }
    }

    private synchronized void p_connectError() {
        down = true;
        lastDownTime = System.currentTimeMillis();
    }

    private void p_processRequest(Request req) throws ServiceException {
        p_preCheck();
        
        try {
            req.process(namingService);
        }
        catch (ServiceException e) {
            if (e.equals(ServiceException.CONNECT)) {
                p_connectError();
            }

            throw e;
        }
    }

    /**
     * Retrieve a Vector of service types (String objects).
     */
    public Vector getServiceTypes() throws ServiceException {
        GetServiceTypes req = new GetServiceTypes();
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Return a vector of serviceNames for a particular serviceType.
     *
     * @return May return null if the serviceType is not found.
     */
    public Vector getServiceNames(String serviceType) throws ServiceException {
        GetServiceNames req = new GetServiceNames(serviceType);
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Return a Vector of ServiceId objects.
     */
    public Vector getServiceIds() throws ServiceException {
        GetAllServiceIds req = new GetAllServiceIds();
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Return a Vector of ServiceId objects that correspond to a
     * particular service type.
     */
    public Vector getServiceIds(String serviceType) throws ServiceException {
        GetServiceIds req = new GetServiceIds(serviceType);
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Return a ServiceAddress for a particular request.
     */
    public Vector getServiceAddress(ServiceRequest sreq) throws ServiceException, NamingException {
        GetServiceAddress req = new GetServiceAddress(sreq);
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Retrieve a single service address. Note that any metric
     * is acceptable. If a metric happens to return more than one
     * ServiceAddress, the first one is returned. A convenience method.
     */
    public ServiceAddress getOneServiceAddress(ServiceRequest sreq) throws ServiceException, NamingException {
        GetOneServiceAddress req = new GetOneServiceAddress(sreq);
        p_processRequest(req);
        return req.getResult();
    }

    /**
     * Dealing with applying the same pre and post handling to all
     * incoming requests can be done in two ways, you either do it
     * within the method (duplicating the pre/post code in every
     * single method), or use the double-dispatch pattern. When
     * the pre/post code is fairly small (maybe a function call),
     * it's probably ok to just add this code everywhere. But when
     * the code gets longer, especially when dealing with exceptions,
     * double-dispatch becomes more attractive. True, double-dispatch
     * requires an extra class for each method call, but it has lots
     * of flexibility. In particular, the same request objects can be
     * used by other classes to do other processing. So i'm using
     * double-dispatch. Right now i'm including these classes in here
     * since i don't anticipate they will be used elsewhere, but they
     * can always be pulled out.
     */
    abstract class Request {
        public abstract void process(NamingService service) throws ServiceException;
    }

    abstract class VectorResultRequest extends Request {
        protected Vector result;

        public Vector getResult() {
            return result;
        }
    }

    class GetServiceTypes extends VectorResultRequest {
        public void process(NamingService service) throws ServiceException {
            result = service.getServiceTypes();
        }
    }

    class GetServiceNames extends VectorResultRequest {
        private String serviceType;

        public GetServiceNames(String serviceType) {
            this.serviceType = serviceType;
        }

        public void process(NamingService service) throws ServiceException {
            result = service.getServiceNames(serviceType);
        }
    }

    class GetAllServiceIds extends VectorResultRequest {
        public void process(NamingService service) throws ServiceException {
            result = service.getServiceIds();
        }
    }

    class GetServiceIds extends VectorResultRequest {
        private String serviceType;

        public GetServiceIds(String serviceType) {
            this.serviceType = serviceType;
        }

        public void process(NamingService service) throws ServiceException {
            result = service.getServiceIds(serviceType);
        }
    }

    class GetServiceAddress extends VectorResultRequest {
        private ServiceRequest request;

        public GetServiceAddress(ServiceRequest req) {
            request = req;
        }

        public void process(NamingService service) throws ServiceException {
            result = service.getServiceAddress(request);
        }
    }

    class GetOneServiceAddress extends Request {
        private ServiceRequest request;
        private ServiceAddress result;

        public GetOneServiceAddress(ServiceRequest req) {
            request = req;
        }

        public void process(NamingService service) throws ServiceException {
            result = service.getOneServiceAddress(request);
        }

        public ServiceAddress getResult() {
            return result;
        }
    }
}
