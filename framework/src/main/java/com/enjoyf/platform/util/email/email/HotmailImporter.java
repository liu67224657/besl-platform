package com.enjoyf.platform.util.email.email;

import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;


import com.enjoyf.platform.util.email.Contact;
import com.enjoyf.platform.util.email.exception.ContactsException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;

/**
 * 导入Hotmail联系人
 * 
 * @author 
 * 
 */
public class HotmailImporter extends EmailImporter
{
  private String securityToken = null;

  public HotmailImporter(String email, String password)
  {
    super(email, password);
  }

  public void doLogin()
    throws ContactsException
  {
    try
    {
      String loginData = doSoapPost(loginRequestUrl(), loginRequestXml(), null);
      loginResponseHandle(loginData);
    } catch (Exception e) {
      throw new ContactsException("Hotmail protocol has changed", e);
    }
  }

  public List<Contact> parseContacts()
    throws ContactsException
  {
    try
    {
      String contactsData = doSoapPost(contactsRequestUrl(), contactsRequestXml(), contactsRequestAction());

      return contactsResponseHandle(contactsData);
    } catch (Exception e) {
      throw new ContactsException("Hotmail protocol has changed", e);
    }
  }

  private String loginRequestXml() {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    xml = xml + "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2003/06/secext\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2002/12/policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/03/addressing\" xmlns:wssc=\"http://schemas.xmlsoap.org/ws/2004/04/sc\" xmlns:wst=\"http://schemas.xmlsoap.org/ws/2004/04/trust\">";
    xml = xml + "<Header>";
    xml = xml + "<ps:AuthInfo xmlns:ps=\"http://schemas.microsoft.com/Passport/SoapServices/PPCRL\" Id=\"PPAuthInfo\">";
    xml = xml + "<ps:HostingApp>{3:B}</ps:HostingApp>";
    xml = xml + "<ps:BinaryVersion>4</ps:BinaryVersion>";
    xml = xml + "<ps:UIVersion>1</ps:UIVersion>";
    xml = xml + "<ps:Cookies></ps:Cookies>";
    xml = xml + "<ps:RequestParams>AQAAAAIAAABsYwQAAAAzMDg0</ps:RequestParams>";
    xml = xml + "</ps:AuthInfo>";
    xml = xml + "<wsse:Security>";
    xml = xml + "<wsse:UsernameToken Id=\"user\">";
    xml = xml + "<wsse:Username>" + this.email + "</wsse:Username>";
    xml = xml + "<wsse:Password>" + this.password + "</wsse:Password>";
    xml = xml + "</wsse:UsernameToken>";
    xml = xml + "</wsse:Security>";
    xml = xml + "</Header>";
    xml = xml + "<Body>";
    xml = xml + "<ps:RequestMultipleSecurityTokens xmlns:ps=\"http://schemas.microsoft.com/Passport/SoapServices/PPCRL\" Id=\"RSTS\">";
    xml = xml + "<wst:RequestSecurityToken Id=\"RST0\">";
    xml = xml + "<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>";
    xml = xml + "<wsp:AppliesTo>";
    xml = xml + "<wsa:EndpointReference>";
    xml = xml + "<wsa:Address>http://Passport.NET/tb</wsa:Address>";
    xml = xml + "</wsa:EndpointReference>";
    xml = xml + "</wsp:AppliesTo>";
    xml = xml + "</wst:RequestSecurityToken>";
    xml = xml + "<wst:RequestSecurityToken Id=\"RST1\">";
    xml = xml + "<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>";
    xml = xml + "<wsp:AppliesTo>";
    xml = xml + "<wsa:EndpointReference>";
    xml = xml + "<wsa:Address>contacts.msn.com</wsa:Address>";
    xml = xml + "</wsa:EndpointReference>";
    xml = xml + "</wsp:AppliesTo>";
    xml = xml + "<wsse:PolicyReference URI=\"MBI\">";
    xml = xml + "</wsse:PolicyReference>";
    xml = xml + "</wst:RequestSecurityToken>";
    xml = xml + "<wst:RequestSecurityToken Id=\"RST2\">";
    xml = xml + "<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>";
    xml = xml + "<wsp:AppliesTo>";
    xml = xml + "<wsa:EndpointReference>";
    xml = xml + "<wsa:Address>storage.msn.com</wsa:Address>";
    xml = xml + "</wsa:EndpointReference>";
    xml = xml + "</wsp:AppliesTo>";
    xml = xml + "<wsse:PolicyReference URI=\"MBI\">";
    xml = xml + "</wsse:PolicyReference>";
    xml = xml + "</wst:RequestSecurityToken>";
    xml = xml + "</ps:RequestMultipleSecurityTokens>";
    xml = xml + "</Body>";
    xml = xml + "</Envelope>";
    return xml;
  }

  private String loginRequestUrl() {
    String url = "";
    if (this.email.indexOf("@msn.com") == -1)
      url = "https://login.live.com/RST.srf";
    else {
      url = "https://msnia.login.live.com/pp650/RST.srf";
    }
    return url;
  }

  private void loginResponseHandle(String data) throws Exception {
    if (data.indexOf("FailedAuthentication") >= 0) {
      throw new ContactsException("failed authentication");
    }

    if (data.indexOf("<wsse:BinarySecurityToken") < 1) {
      throw new ContactsException("failed authentication");
    }

    XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty("org.xmlpull.v1.XmlPullParserFactory"), null);
    factory.setNamespaceAware(true);

    XmlPullParser xpp = factory.newPullParser();
    xpp.setInput(new StringReader(data));

    int eventType = xpp.getEventType();
    while (eventType != 1) {
      if ((eventType == 2) && (xpp.getName().equals("BinarySecurityToken")) &&
        (xpp.getAttributeValue(null, "Id").equals("Compact1"))) {
        xpp.next();
        this.securityToken = xpp.getText().replace("&", "&amp;");
      }

      xpp.next();
      eventType = xpp.getEventType();
    }
  }

  private String contactsRequestXml() {
    String xml = "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'\n";
    xml = xml + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n";
    xml = xml + "xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n";
    xml = xml + "xmlns:soapenc='http://schemas.xmlsoap.org/soap/encoding/'>\n";
    xml = xml + "<soap:Header>\n";
    xml = xml + "<ABApplicationHeader xmlns='http://www.msn.com/webservices/AddressBook'>\n";
    xml = xml + "<ApplicationId>CFE80F9D-180F-4399-82AB-413F33A1FA11</ApplicationId>\n";
    xml = xml + "<IsMigration>false</IsMigration>\n";
    xml = xml + "<PartnerScenario>Initial</PartnerScenario>\n";
    xml = xml + "</ABApplicationHeader>\n";
    xml = xml + "<ABAuthHeader xmlns='http://www.msn.com/webservices/AddressBook'>\n";
    xml = xml + "<ManagedGroupRequest>false</ManagedGroupRequest>\n";
    xml = xml + "<TicketToken>" + this.securityToken + "</TicketToken>\n";
    xml = xml + "</ABAuthHeader>\n";
    xml = xml + "</soap:Header>\n";
    xml = xml + "<soap:Body>\n";
    xml = xml + "<ABFindAll xmlns='http://www.msn.com/webservices/AddressBook'>\n";
    xml = xml + "<abId>00000000-0000-0000-0000-000000000000</abId>\n";
    xml = xml + "<abView>Full</abView>\n";
    xml = xml + "<deltasOnly>false</deltasOnly>\n";
    xml = xml + "<lastChange>0001-01-01T00:00:00.0000000-08:00</lastChange>\n";
    xml = xml + "</ABFindAll>\n";
    xml = xml + "</soap:Body>";
    xml = xml + "</soap:Envelope>";

    return xml;
  }

  private String contactsRequestUrl() {
    return "http://contacts.msn.com/abservice/abservice.asmx";
  }

  private String contactsRequestAction() {
    return "http://www.msn.com/webservices/AddressBook/ABFindAll";
  }

  private List<Contact> contactsResponseHandle(String data) throws Exception {
    List contacts = new ArrayList();

    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    factory.setNamespaceAware(true);
    XmlPullParser xpp = factory.newPullParser();
    xpp.setInput(new StringReader(data));

    int eventType = xpp.getEventType();

    String username = null;
    String email = null;
    while (eventType != 1) {
      if ((eventType == 2) && ("Contact".equals(xpp.getName())))
      {
        while ((eventType != 3) || (!("Contact".equals(xpp.getName()))))
        {
          if ((eventType == 2) && ("ContactEmail".equals(xpp.getName())))
          {
            while ((eventType != 3) || (!("ContactEmail".equals(xpp.getName()))))
            {
              if ((eventType == 2) && ("email".equals(xpp.getName()))) {
                xpp.next();
                email = xpp.getText();
              }
              xpp.next();
              eventType = xpp.getEventType();
            }
          }

          if ((eventType == 2) && ("passportName".equals(xpp.getName()))) {
            xpp.next();
            email = xpp.getText();
          }

          if ((eventType == 2) && ("displayName".equals(xpp.getName()))) {
            xpp.next();
            username = xpp.getText();

            Contact contact = new Contact(username, email);
            contacts.add(contact);
          }

          xpp.next();
          eventType = xpp.getEventType();
        }
      }
      xpp.next();
      eventType = xpp.getEventType();
    }

    return contacts;
  }
}
