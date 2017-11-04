package com.enjoyf.platform.serv.management;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel
 */
public class Log4jManagement implements Log4jManagementMBean {

	private static Logger logger = LoggerFactory.getLogger(Log4jManagement.class);

	public void go() throws Exception {
		while (true) {
			logger.debug("DEBUG");
			logger.info("INFO");
			Thread.sleep(2000);
		}
	}

	@Override
	public void setRootLoggingLevel(String level) {
		logger.info("Setting root logging level to: " + level);
		Level newLevel = Level.toLevel(level, Level.INFO);
		org.apache.log4j.Logger.getRootLogger().setLevel(newLevel);
	}
	
	@Override
	public String getRootLoggingLevel() {
		return org.apache.log4j.Logger.getRootLogger().getLevel().toString();
	}

	@Override
	public void setLoggingLevel(String packageName, String level) {
		logger.info("Setting [" + packageName + "] logging level to: " + level);
		Level newLevel = Level.toLevel(level, Level.INFO);
		org.apache.log4j.Logger.getLogger(packageName).setLevel(newLevel);
	}

	@Override
	public String getLoggingLevel(String packageName) {
		return org.apache.log4j.Logger.getLogger(packageName).getLevel().toString();
	}

}
