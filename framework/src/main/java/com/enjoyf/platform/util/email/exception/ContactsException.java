package com.enjoyf.platform.util.email.exception;

/**
 * 导入联系人异常类
 * 
 */
public class ContactsException extends Exception {

	private static final long serialVersionUID = 5189838472580965120L;

	public ContactsException(String msg) {
		super(msg);
	}

	public ContactsException(String msg, Throwable e) {
		super(msg, e);
	}

}
