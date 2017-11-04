package com.enjoyf.platform.service.service.rpc;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.util.log.GAlerter;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Daniel
 */
public class RPCClient<T> {

    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);

    private ReqProcessor reqProcessor = null;
    private final int partitionNum;

    public RPCClient(ServiceConfigNaming scfg) {
        if (scfg == null) {
            throw new RuntimeException("RPCClient.ctor: ServiceConfig is null!");
        }
        this.partitionNum = -1;
        this.reqProcessor = scfg.getReqProcessor();
    }

    public RPCClient(ServiceConfigNaming scfg, int partitionNum, int partitionFailoverNum) {
        if (scfg == null) {
            throw new RuntimeException("RPCClient.ctor: ServiceConfig is null!");
        }

        scfg.setConnChooser(new ConnChooserRingPartition(partitionNum, partitionFailoverNum, EnvConfig.get().getRequestTimeoutMsecs()));

        this.partitionNum = partitionNum;
        this.reqProcessor = scfg.getReqProcessor();
    }

    @SuppressWarnings("unchecked")
    public T createService(Class<T> svcInterface) throws InstantiationException, IllegalAccessException {
        ProxyFactory factory = new ProxyFactory();

        factory.setInterfaces(new Class[]{svcInterface});
        final Map<String, RPCMessage> methodMap = new RPCMessageScanner(svcInterface).getRPCMessagesByName();

        Class<?> klass = factory.createClass(
                new MethodFilter() {
                    @Override
                    public boolean isHandled(Method m) {
                        return methodMap.containsKey(m.getName());
                    }
                }
        );

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                try {
                    WPacket wp = new WPacket();
                    if (args != null && args.length > 0) {
                        for (Object arg : args) {
                            if (arg == null) {
                                wp.writeSerializable(null);
                            } else {
                                wp.writeSerializable((Serializable) arg);
                            }
                        }
                    }

                    RPCMessage rpcMsg = methodMap.get(thisMethod.getName());
                    Request req = new Request(rpcMsg.getMessageType(), wp);
                    if (partitionNum > 0 && rpcMsg.getPartitionHashing() >= 0) {
                        Object param = args[rpcMsg.getPartitionHashing()];
                        req.setPartition(Math.abs(param.hashCode()) % partitionNum);

                        if (logger.isTraceEnabled()) {
                            logger.trace("Req " + req.getType() + " for " + req.getServiceAddress() + " goes to partition " + req.getPartition());
                        }
                    }
                    RPacket rPacket = reqProcessor.process(req);

                    return rPacket.readSerializable();
                } catch (Exception ex) {
                    GAlerter.lab("Cannot invoke from the client RPC delegation", ex);

                    throw ex;
                }
            }
        };

        ProxyObject proxy = (ProxyObject) klass.newInstance();
        proxy.setHandler(handler);

        return (T) proxy;
    }

}
