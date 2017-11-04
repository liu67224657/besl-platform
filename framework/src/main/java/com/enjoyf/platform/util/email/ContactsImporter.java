package com.enjoyf.platform.util.email;

import com.enjoyf.platform.util.email.exception.ContactsException;

import java.util.List;





/**
 * 导入联系人的接口
 * 
 * @author 
 * 
 */
public interface ContactsImporter {
	public List<Contact> getContacts() throws ContactsException, ContactsException;
}
