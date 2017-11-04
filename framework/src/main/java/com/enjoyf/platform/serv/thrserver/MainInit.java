/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.serv.util.ServerStartup;
import com.enjoyf.platform.service.CompoundKey;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.service.naming.NamingServiceUtil;
import com.enjoyf.platform.service.naming.Registrant;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInit;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.log.ServerId;

/**
 * Singleton class to support common startup behavior in servers.
 * Servers should invoke the init() method followed by the start()
 * method in their main routine.
 * <p/>
 * MainInit.instance().init(...);
 * ...
 * MainInit.instance().start();
 * <p/>
 * Note that all of the init() methods assume the existence of the
 * two std property files: the env props (from the -Dcn.platform.env
 * arg) and the server props file (from the -Dcn.platform.props). If these
 * files are missing, things won't work properly.
 * <p/>
 * Note that this object can startup any number of ServerThread objects,
 * as well as register any number of services with the naming service
 * although typically there is only one ServerThread/service pair.
 */
public class MainInit {

    private static final Logger logger = LoggerFactory.getLogger(MainInit.class);

    private static MainInit instance = new MainInit();

    private MainConfig mainConfig;

    private MainInit() {
    }

    public static MainInit instance() {
        return instance;
    }

    public void init() {
        init(new MainConfig());
    }

    /**
     * This initializer is used to just register with the naming service
     * but w/o an associated service. Primarily to be used for being able
     * to monitor a server with the SALT tool.
     *
     * @param servicePrefix This is the prefix of the necessary properties
     *                      from the Props.instance().getServProps() properties file:
     *                      <p/>
     *                      <servicePrefix>.TYPE
     *                      <p/>
     *                      <servicePrefix>.NAME
     */
    public void init(String servicePrefix) {
        FiveProps servProps = Props.instance().getServProps();

        ServerWrap serverWrap = new ServerWrap(servicePrefix, servProps);
        MainConfig cfg = new MainConfig(servProps, new ServiceConfig(serverWrap));
        init(cfg);
    }

    /**
     * Convenience method for simpler initialization. Note that
     * this just performs initialization. The start() method needs
     * to be invoked in order to fire things up.
     *
     * @param server        A ServerThread object that will be started when
     *                      start() is called.
     * @param servicePrefix If this arg is not null, it is the
     *                      prefix in the server props file ( accessed via
     *                      Props.instance().getServProps() ) containing the properties
     *                      necessary to register a service with the naming service.
     *                      In other words, the properties <servicePrefix>.PORT,
     *                      <servicePrefix>.TYPE and <servicePrefix>.NAME are looked for
     *                      and used to register with the naming service.
     * @param exitOnDie     If true, the logicProcess will exit if the ServerThread
     *                      object dies.
     */
    public void init(ServerThread server, String servicePrefix, boolean exitOnDie) {
        FiveProps servProps = Props.instance().getServProps();

        ServerWrap serverWrap = new ServerWrap(server, servicePrefix, servProps);
        serverWrap.setExitOnDie(exitOnDie);

        MainConfig cfg = new MainConfig(servProps, new ServiceConfig(serverWrap));
        init(cfg);
    }

    /**
     * Convenience method for simpler initialization. Note that
     * this just performs initialization. The start() method needs
     * to be invoked in order to fire things up.
     *
     * @param server        A ServerThread object that will be started when
     *                      start() is called.
     * @param servicePrefix If this arg is not null, it is the
     *                      prefix in the server props file ( accessed via
     *                      Props.instance().getServProps() ) containing the properties
     *                      necessary to register a service with the naming service.
     *                      In other words, the properties <servicePrefix>.PORT,
     *                      <servicePrefix>.TYPE and <servicePrefix>.NAME are looked for
     *                      and used to register with the naming service.
     */
    public void init(ServerThread server, String servicePrefix) {
        init(server, servicePrefix, true);
    }

    /**
     * Convenience method for simpler initialization. Note that
     * this just performs initialization. The start() method needs
     * to be invoked in order to fire things up.
     *
     * @param server A ServerThread object that will be started when
     *               start() is called.
     */
    public void init(ServerThread server) {
        init(server, null, true);
    }

    /**
     * Convenience method for simpler initialization. Note that
     * this just performs initialization. The start() method needs
     * to be invoked in order to fire things up.
     *
     * @param serverWrap This encapsulates the ServerThread object
     *                   that will be started when start() is called. It allows the caller
     *                   more control over how the initialization of this object.
     */
    public void init(ServerWrap serverWrap) {
        init(new MainConfig(Props.instance().getServProps(), serverWrap));
    }

    /**
     * This init method assumes that the server has both an env
     * prop and a serv prop file specified on the runstring:
     * -Dcn.platform.env=... and -Dcn.platform.props=...
     *
     * @param cfg A MainConfig object used to configure how we
     *            start things up.
     * @throws IllegalArgumentException Thrown if we could not
     *                                  find either the env props or the serv props file.
     */
    public void init(MainConfig cfg) {
        //--
        // This performs basic initialization for all processes.
        //--
        ServerStartup.instance().startup();

        mainConfig = cfg;
        FiveProps mainProps = mainConfig.getServProps();

        if (mainProps == null) {
            mainConfig.setServProps(Props.instance().getServProps());
            if (mainConfig.getServProps() == null) {
                GAlerter.lab("SERVER PROPS FILE NULL!");
                throw new IllegalArgumentException("Server Props file is null!");
            }
        }

        //--
        // Set the id for the logicProcess.
        //--
        p_setId();

        FiveProps envProps = Props.instance().getEnvProps();
        if (envProps == null) {
            GAlerter.lab("ENV PROPS FILE NULL!");
            throw new IllegalArgumentException("Env Props file is null!");
        }

        p_initLogger(cfg.getServProps());

        //--
        // Initialize all of the services from the env props.
        //--
        if (cfg.shouldInitServices()) {
            ServiceInit.instance().init();
        } else {
            if (NamingServiceSngl.get() == null) {
                try {
                    NamingServiceSngl.set(NamingServiceUtil.createStdNamingService(envProps));
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Sets the esm ProcessId.
     */
    private void p_setProcessId() {
        ServiceId serviceId = p_getFirstServiceId();
        if (serviceId == null) {
            logger.error("MainInit: Not setting ProcessId as there are no services");
            return;
        }

        CompoundKey id = new CompoundKey(serviceId.getServiceType() + "." + serviceId.getServiceName());
        logger.info("MainInit: Setting logicProcess id to: " + id);
        //ProcessId.set(id);
    }

    private void p_setId() {
        String id = mainConfig.getProcessId();
        if (id == null) {
            ServiceId serviceId = p_getFirstServiceId();
            if (serviceId != null) {
                id = serviceId.toString();
            }
        }
        if (id == null) {
            id = "ANONYMOUS";
        }

        logger.info("Setting ServerId to: " + id);
        ServerId.setId(id);
    }

    /**
     * Utility to return the first ServiceId we find in the registered
     * serviceData. A null may be returned.
     */
    private ServiceId p_getFirstServiceId() {
        //--
        // If the user didn't specify an id for this logicProcess, let's
        // use the ServiceId from the first Registrant object that
        // we find.
        //--
        ServiceId id = null;
        List<ServiceConfig> serviceConfigList = mainConfig.getServiceConfigList();
        if (serviceConfigList == null || serviceConfigList.size() == 0) {
            return null;
        }

        ServiceConfig serviceConfig = serviceConfigList.get(0);

        for (Iterator<ServerWrap> itr = serviceConfig.getServiceWraps().iterator();
             itr.hasNext();) {
            ServerWrap serverWrap = itr.next();
            Registrant registrant = serverWrap.getRegistrant();
            if (registrant != null) {
                id = registrant.getServiceInfo().getServiceId();
                break;
            }
        }
        return id;
    }

    /**
     * Initializa and start in one call.
     */
    public void start(MainConfig cfg) {
        init(cfg);
        start();
    }

    /**
     * Call this method once the init() call has been invoked. This will
     * start up any ServerThread objects, as well as register any services
     * with the naming service.
     */
    public void start() {
        //--
        // Fire up all the services as specified by each ServiceConfig.
        //--
        for (Iterator<ServiceConfig> itr = mainConfig.getServiceConfigList().iterator(); itr.hasNext();) {
            ServiceConfig serviceConfig = itr.next();

            //--
            // TODO: Use a factory to find a starter, since for
            // multiple ServiceConfig objects we might want to start
            // them in parallel.
            //--
            ServiceStarter.instance().start(serviceConfig);
        }

        p_setProcessId();
    }

    /**
     * Initialize from a props file.
     */
    private void p_initLogger(FiveProps props) {
        if (props == null) {
            return;
        }

        String sval = props.get("logger.severity");
        if (sval != null) {
            logger.info("Setting GLogger logger severity to: " + sval);
        }
    }
}
