package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.io.mail.MailMessage;
import com.enjoyf.platform.io.mail.MailMessageText;
import com.enjoyf.platform.service.mail.EmailService;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FixedTimer;
import com.enjoyf.platform.util.RateLimitedAlert;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yin Pengyi
 */
class MailLogic implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(MailLogic.class);

	/**
	 * The config object.
	 */
	private MailConfig config;
	private QueueThreadN emailProcessQueueThreadN;
	private RateLimitedAlert rateLimitAlerter = new RateLimitedAlert(60);
	private int lastSize = 0;
	private ThreadLocal<Sender> senders = null;
	private String hostname = "UNKNOWN";
	private FixedTimer timer;
	private StatsCollector collector;
	private Refresher refresher;
	private String myName = "unknown@platform.com";

	MailLogic(MailConfig cfg) {
		this.config = cfg;
		logger.info("USING CONFIG: " + this.config);

		emailProcessQueueThreadN = new QueueThreadN(
				config.getNumberOfSendThreads(),
				new QueueListener() {
					public void process(Object obj) {
						p_process((MailMessage) obj);
					}
				},
				new FQueueQueue(config.getQueueDiskStorePath(), "emailProcessQueue")
		);

		//--
		// Use a ThreadLocal to manage having one Sender object per thread.
		//--
		senders = new ThreadLocal<Sender>() {
			protected Sender initialValue() {
				return new Sender();
			}
		};

		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			GAlerter.lab("COULD NOT GET LOCAL HOSTNAME! ");
		}

		collector = new StatsCollector(this.config.getStatsPeriod(), this.config.getStatsNumPeriods());

		refresher = new Refresher(10 * 1000);

//        timer = new FixedTimer(
//                this.config.getStatsStartTime(),
//                this.config.getStatsReportPeriod(),
//
//                new FixedTimer.Listener() {
//                    public void timerExpired(FixedTimer t) {
//                        String report = collector.getReport();
//                        collector.clear();
//                        StringBuffer sb = new StringBuffer();
//                        sb.append("ReportTime: " + new Date() + "\n");
//                        sb.append("Hostname: " + hostname + "\n");
//                        sb.append("numberOfSendThreads: " + config.getNumberOfSendThreads() + "\n");
//                        sb.append("mail service configs:" + config.getMailServiceConfigMap());
//                        sb.append(report);
//
//                        p_sendReport(new String(sb));
//                    }
//                }
//        );
//
//        timer.setRecurring(true);
//        timer.start();

		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			myName = iaddr.getHostName();
		} catch (Exception e) {
			GAlerter.lab("MailLogic.ctor: Could not get my own name! ", e);
		}
	}

	private void p_sendReport(String report) {
		if (logger.isDebugEnabled()) {
			logger.debug("MailServer.p_sendReport: " + report);
		}

		MailMessage msg = new MailMessageText();
		msg.setBody(report);
		msg.setSubject("mail server stats report");
//        msg.setFrom("MailServer", "MailServer@" + myName);
		msg.setFrom("serviceReport", "service@joyme.com");
		msg.setToList(config.getStatsReportUsers());

		p_process(msg);
	}

	public void send(MailMessage msg) throws ServiceException {
		emailProcessQueueThreadN.add(msg);

		if (logger.isDebugEnabled() && emailProcessQueueThreadN.size() > 10 && refresher.shouldRefresh()) {
			logger.debug("MailLogic.send: Mail message queue size = " + emailProcessQueueThreadN.size());
		}
	}

	public void setSendRate(int delay) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("MailLogic.setSendRate: Setting delay to: " + delay);
		}
		config.setSendDelay(delay);
	}


	private void p_process(MailMessage msg) {
		//--
		// Note this method will be called by multiple threads at
		// the same time. Use a ThreadLocal to retrieve a Sender
		// object to be used to send the msg in a thread safe way.
		//--
		Sender sender = senders.get();
		sender.send(msg);
	}

	class Sender {
		private Map<String, JavaMailService> javaMailServiceMap = new HashMap<String, JavaMailService>();

		Sender() {
		}

		void send(MailMessage msg) {
			//
			int qsize = emailProcessQueueThreadN.size();

			if (qsize > 1000 && refresher.shouldRefresh()) {
				logger.warn("THERE ARE " + emailProcessQueueThreadN.size() + " messages in queue");
			}

			if (qsize > (12 * 3600 * 1000 / config.getSendDelay())) {
				if (qsize >= lastSize) {
					rateLimitAlerter.alert("MailServer: There are " + emailProcessQueueThreadN.size() + " messages queued up!");
				}

				lastSize = qsize;
			}

			//
			boolean ok = true;
			if (logger.isDebugEnabled()) {
				logger.debug("MailLogic.p_process: Sending msg: " + msg);
			}

			//send email logic
			MailService mailService = getMailService(msg.getFrom().getEmailAddress());

			long t1 = System.currentTimeMillis();
			try {
				mailService.send(msg);
			} catch (MailMessageException e) {
				GAlerter.lab("MailLogic.p_process: Could not send email! ", msg.toString(), e);

				ok = false;
			} catch (MailServiceException e) {
				GAlerter.lab("MailLogic.p_process: Could not send email! ", msg.toString(), e);
				//--
				// Presumably this means the conn is down to the mail server.
				//--
				ok = false;
				try {
					mailService.close();
				} catch (Exception e1) {
					logger.error("MailLogic.p_process: " + "Error trying to close conn to server.", e1);
				}

				Utility.sleepExc(5000);
				if (mailService instanceof JavaMailService) {
					JavaMailServiceConfig mailServiceConfig = getMailServiceConfig(msg.getFrom().getEmailAddress());

					mailService = new JavaMailService(
							mailServiceConfig.getSmtpHostname(),
							mailServiceConfig.isNeedAuth(),
							mailServiceConfig.getAuthUser(),
							mailServiceConfig.getAuthPwd());

					javaMailServiceMap.put(mailServiceConfig.getSenderMailAddress(), (JavaMailService) mailService);
				}

				ok = true;

				mailService = getMailService(msg.getFrom().getEmailAddress());
				try {
					mailService.send(msg);
				} catch (Exception e2) {
					GAlerter.lab("MailLogic.p_process: Could not send email a 2nd time: ", msg.toString(), e2);

					ok = false;
				}
			}

			long diff = System.currentTimeMillis() - t1;

			if (ok) {
				logger.debug("MailLogic.p_process: Took " + diff + " msecs to send msg");
			} else {
				GAlerter.lab("MailLogic.p_process: FATAL error trying to send msg: ", msg.toString());
			}

			collector.add((int) diff, emailProcessQueueThreadN.size());

			int delay = config.getSendDelay();
			if (delay != 0) {
				Utility.sleepExc(delay);
			}

			if (delay > 5000) {
				rateLimitAlerter.alert("MailServer: the set delay between msgs is: " + delay + " msecs!");
			}

			if (diff > 10000) {
				GAlerter.lab("MailServer: Hmm, seems to be taking " + diff + " msecs to send one msg!");
			}
		}

		private MailService getMailService(String senderMailAddress) {
			JavaMailService returnValue = null;

			//get the mail service config
			JavaMailServiceConfig mailServiceConfig = getMailServiceConfig(senderMailAddress);

			//get the java mail service from the map, if not exist create one.
			returnValue = javaMailServiceMap.get(mailServiceConfig.getSenderMailAddress());
			if (returnValue == null) {
				returnValue = new JavaMailService(
						mailServiceConfig.getSmtpHostname(),
						mailServiceConfig.isNeedAuth(),
						mailServiceConfig.getAuthUser(),
						mailServiceConfig.getAuthPwd());

				javaMailServiceMap.put(mailServiceConfig.getSenderMailAddress(), returnValue);
			}

			return returnValue;
		}

		private JavaMailServiceConfig getMailServiceConfig(String senderMailAddress) {
			//check the mail service configure
			JavaMailServiceConfig returnValue = config.getMailServiceConfigMap().get(senderMailAddress.toLowerCase());

			if (returnValue == null) {
				returnValue = config.getDefaultJavaMailServiceConfig();
			}

			return returnValue;
		}

		void close() {
			for (JavaMailService mailService : javaMailServiceMap.values()) {
				try {
					mailService.close();
				} catch (Exception e) {
					//
				}
			}
		}

		protected void finalize() throws Throwable {
			close();

			super.finalize();
		}
	}
}