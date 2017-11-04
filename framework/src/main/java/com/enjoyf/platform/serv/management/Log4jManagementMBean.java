package com.enjoyf.platform.serv.management;

/**
 * @author Daniel
 */
public interface Log4jManagementMBean {
	
    public void setLoggingLevel(String packageName, String level);
    
    public String getLoggingLevel(String packageName);
    
    public void setRootLoggingLevel(String level);
    
    public String getRootLoggingLevel();

}