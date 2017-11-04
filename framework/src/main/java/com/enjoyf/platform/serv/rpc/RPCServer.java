package com.enjoyf.platform.serv.rpc;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;
import com.enjoyf.platform.service.service.rpc.RPCMessage;
import com.enjoyf.platform.service.service.rpc.RPCMessageScanner;
import com.enjoyf.platform.util.log.GAlerter;
import com.esotericsoftware.reflectasm.MethodAccess;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Daniel
 */
public class RPCServer<T> {

    private T logic;

    public RPCServer(T logic) {
        this.logic = logic;
    }

    public PacketDecoder createService(Class<T> svcInterface) throws InstantiationException, IllegalAccessException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(PacketDecoder.class);
        final Map<Byte, RPCMessage> methodMap = new RPCMessageScanner(svcInterface).getRPCMessagesByType();

        Class<?> klass = factory.createClass(
                new MethodFilter() {
                    @Override
                    public boolean isHandled(Method m) {
                        return "logicProcess".equals(m.getName());
                    }
                }
        );

        final MethodAccess methodAccess = MethodAccess.get(logic.getClass());

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                RPacket rPacket = (RPacket) args[1];

                byte type = rPacket.getType();
                RPCMessage rpcMsg = methodMap.get(type);
                if (rpcMsg == null) {
                    GAlerter.lab("Unrecognized type found: ", String.valueOf(type));

                    throw new ServiceException(ServiceException.BAD_PACKET);
                }

                Object result = null;
                if (rpcMsg.getArgs() == null || rpcMsg.getArgs().length == 0) {
                    result = methodAccess.invoke(logic, rpcMsg.getName(), (Object[]) null);
                } else {
                    Object[] logicMethodArgs = new Object[rpcMsg.getArgs().length];

                    for (int i = 0; i < rpcMsg.getArgs().length; i++) {
                        logicMethodArgs[i] = rPacket.readSerializable();
                    }

                    result = methodAccess.invoke(logic, rpcMsg.getName(), logicMethodArgs);
                }

                WPacket wp = new WPacket();
                wp.writeByteNx(ServiceConstants.OK);

                //if the result is null, it's ok.
                if (result == null) {
                    wp.writeSerializable(null);
                } else {
                    wp.writeSerializable((Serializable) result);
                }

                return wp;
            }
        };

        ProxyObject proxy = (ProxyObject) klass.newInstance();
        proxy.setHandler(handler);

        PacketDecoder packetDecoder = (PacketDecoder) proxy;
        TransProfileContainer transProfileContainer = new TransProfileContainer();
        for (Byte msgType : methodMap.keySet()) {
            transProfileContainer.put(new TransProfile(msgType, methodMap.get(msgType).getName()));
        }
        packetDecoder.setTransContainer(transProfileContainer);

        return packetDecoder;
    }

}
