package com.enjoyf.platform.util.email.email;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


import com.enjoyf.platform.util.email.Contact;
import com.enjoyf.platform.util.email.exception.ContactsException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * 导入Yahoo联系人
 * 
 * @author
 * 
 */
public class YahooImporter extends EmailImporter {
	// 预登录Url
	private String beforeLoginUrl = "http://mail.cn.yahoo.com/";

	// 登录Url
	private String loginUrl = "https://edit.bjs.yahoo.com/config/login";

	// 联系人Url
	private String contactsUrl = "http://cn.address.yahoo.com/yab/cn?VPC=contact_list";

	/**
	 * 构造函数
	 * 
	 * @param email
	 * @param password
	 */
	public YahooImporter(String email, String password) {
		super(email, password);
	}

	/**
	 * 登录yahoo邮箱
	 * 
	 * @throws ContactsException
	 */
	public void doLogin() throws ContactsException {
		try {
			String content = doGet(beforeLoginUrl);
			String challenge = getInputValue(".challenge", content);

			NameValuePair[] params = new NameValuePair[] {
					new NameValuePair(".intl", getInputValue(".intl", content)),
					new NameValuePair(".done", getInputValue(".done", content)),
					new NameValuePair(".src", getInputValue(".src", content)),
					new NameValuePair(".cnrid",
							getInputValue(".cnrid", content)),
					new NameValuePair(".challenge", challenge),
					new NameValuePair("login", email),
					new NameValuePair("passwd", password) };
			content = doPost(loginUrl, params, beforeLoginUrl);

			client.getState().addCookie(
					new Cookie("mail.cn.yahoo.com", "cn_challenge", challenge, "/", null, false));
			String redirectUrl = getJSRedirectLocation(content);
			doGet(redirectUrl);
		} catch (Exception e) {
			throw new ContactsException("Yahoo protocol has changed", e);
		}
	}

	/**
	 * 进入联系人列表页面，并读取所有的联系人信息
	 * 
	 * @return 所有的联系人信息
	 * @throws ContactsException
	 */
	public List<Contact> parseContacts() throws ContactsException {
		try {
			String content = doGet(contactsUrl);
			List<Contact> contacts = new ArrayList<Contact>();
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new ByteArrayInputStream(content
					.getBytes())));
			NodeList nodes = parser.getDocument().getElementsByTagName("td");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
                                if (node.getAttributes().getNamedItem("class") != null &&
                                        node.getAttributes().getNamedItem("class").getNodeValue().equals("nobottom")) {
                                    String username = node.getChildNodes().item(1).getTextContent().trim();
                                    i++;
                                    String email = nodes.item(i).getChildNodes().item(1).getChildNodes().item(1).getTextContent().trim();
                                    contacts.add(new Contact(username, email));
				}
			}
			return contacts;
		} catch (Exception e) {
			throw new ContactsException("Yahoo protocol has changed", e);
		}
	}

}
