package com.enjoyf.platform.tools.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Commands to be processed follow the command pattern, with the
 * BaseProcessor class as the abstraction, and each concrete class
 * implementing 'process'.
 */
abstract class BaseProcessor implements Processor {
    BaseProcessor() {
    }

    static class Quit extends BaseProcessor {
        public String process(State state, String cmd) {
            System.exit(0);
            return null;
        }
    }

    static class Help extends BaseProcessor {
        public String process(State state, String cmd) {
            StringBuffer sb = new StringBuffer();
            Collection l = Command.getAll();
            for (Iterator itr = l.iterator(); itr.hasNext(); ) {
                Command c = (Command) itr.next();
                sb.append(c.getName() + "\t" + c.getHelp());
                sb.append("\n");
            }

            return sb.toString();
        }
    }

    static class Refresh extends BaseProcessor {
        public String process(State state, String cmd) {
            String errMsg = state.refresh();
            if (errMsg == null) {
                errMsg = "ok";
            }

            return errMsg;
        }
    }

    static class ListCmd extends BaseProcessor {
        public String process(State state, String cmd) {
            //GL.lh("ListCmd.process: cmd=" + cmd);
            StringBuffer sb = new StringBuffer();
            StringTokenizer t = new StringTokenizer(cmd);
            t.nextToken();
            if (!t.hasMoreTokens()) {
                //--
                // Just want all of the service ids.
                //--
                Collection c = state.getAll();
                for (Iterator itr = c.iterator(); itr.hasNext(); ) {
                    ServiceId sid = (ServiceId) itr.next();
                    sb.append(sid);
                    sb.append("\n");
                }
                return sb.toString();
            }
            String serviceType = t.nextToken();
            if (!t.hasMoreTokens()) {
                //--
                // If here, we have the serviceType, retrieve all
                // the services and query them.
                //--
                Collection c = state.getAllByType(serviceType);
                p_processServiceIds(sb, state, c);
                return sb.toString();
            }

            //--
            // If here, we must have a service name.
            //--
            String serviceName = t.nextToken();
            Collection c = state.getAllWithName(serviceType, serviceName);
            p_processServiceIds(sb, state, c);
            return sb.toString();
        }

        private void p_processServiceIds(StringBuffer sb, State state, Collection c) {
            if (c == null) {
                return;
            }

            if (c != null) {
                for (Iterator itr = c.iterator(); itr.hasNext(); ) {
                    ServiceId sid = (ServiceId) itr.next();
                    p_doOne(state, sb, sid);
                }
            }
        }

        private void p_doOne(State state, StringBuffer sb, ServiceId sid) {
            ServiceRequest req = new ServiceRequest(sid.getServiceType(), sid.getServiceName());
            ServiceAddress saddr = state.getServiceAddress(req);
            sb.append(sid + "\t\t");
            sb.append(saddr == null ? "NOT FOUND" : saddr.toString());
            sb.append("\n");
        }
    }

    static class ListAllCmd extends BaseProcessor {
        public String process(State state, String cmd) {
            //GL.lh("ListAllCmd.process: cmd=" + cmd);
            StringBuffer sb = new StringBuffer();

            //--
            // Want all the data.
            //--
            Set serviceInfos = state.getAllServiceInfos();
            for (Iterator itr = serviceInfos.iterator(); itr.hasNext(); ) {
                ServiceInfo serviceInfo = (ServiceInfo) itr.next();
                sb.append(serviceInfo.getServiceId());
                sb.append("\t");
                sb.append(serviceInfo.getServiceAddress());
                sb.append("\n");
            }

            return sb.toString();
        }
    }
}
