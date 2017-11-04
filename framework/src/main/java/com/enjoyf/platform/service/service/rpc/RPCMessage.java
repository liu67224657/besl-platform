package com.enjoyf.platform.service.service.rpc;

/**
 * @author wuqiang
 */
public class RPCMessage {
	
	private byte messageType;
	private String name;
	private boolean async;
	private int partitionHashing;
	private Class<?>[] args;
	
	public byte getMessageType() {
		return messageType;
	}
	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
	public boolean isAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?>[] getArgs() {
		return args;
	}
	public void setArgs(Class<?>[] classes) {
		this.args = classes;
	}
	public int getPartitionHashing() {
		return partitionHashing;
	}
	public void setPartitionHashing(int partitionHashing) {
		this.partitionHashing = partitionHashing;
	}
	
}

