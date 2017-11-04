package com.enjoyf.platform.service.service.rpc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RPCMessageScanner {

	private Map<String, RPCMessage> messageMapByMethods;
	private Map<Byte, RPCMessage> messageMapByTypes;

	public Map<String, RPCMessage> getRPCMessagesByName() 
	{
		return this.messageMapByMethods;
	}
	
	public Map<Byte, RPCMessage> getRPCMessagesByType() 
	{
		return this.messageMapByTypes;
	}
	
	public RPCMessageScanner(Class<?> svcInterface) 
	{
		Method[] methods = svcInterface.getDeclaredMethods();
    	Arrays.sort(methods, new Comparator<Method>(){
			@Override public int compare(Method m1, Method m2) {
				return m1.getName().compareTo(m2.getName());
			}});
    
    	Map<String, RPCMessage> methodMapByName = new HashMap<String, RPCMessage>();
    	Map<Byte, RPCMessage> methodMapByType = new HashMap<Byte, RPCMessage>();
    	byte msgType = 0;
    	for (Method method : methods) {
    		RPC rpcAnno = method.getAnnotation(RPC.class);
    		if (rpcAnno != null) {
    			RPCMessage messageType = new RPCMessage();
    			messageType.setAsync(rpcAnno.async());
    			messageType.setPartitionHashing(rpcAnno.partitionHashing());
    			messageType.setMessageType( ++msgType );
    			messageType.setName(method.getName());
    			messageType.setArgs(method.getParameterTypes());
    			methodMapByName.put(method.getName(), messageType);
    			methodMapByType.put(msgType, messageType);
    			if (msgType > 127) {
    				throw new IllegalArgumentException("Too many service methods defined:" + svcInterface);
    			}
    		}
    	}
		this.messageMapByMethods = methodMapByName;
		this.messageMapByTypes = methodMapByType;
		
		
	}

}
