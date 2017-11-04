package com.enjoyf.platform.service.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.GreetInfoStandard;
import com.enjoyf.platform.service.service.GreeterDefault;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfigRecon;
import com.enjoyf.platform.util.collection.CircularQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThread;
import com.enjoyf.platform.util.log.Alert;

/**
 * This class is used to log messages to a central location.
 */
public class AlertService {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
	
    private ReqProcessor reqProcessor = null;
    private QueueThread queueThread = null;

    /**
     * Ctor the object using the passed in config object.
     */
    public AlertService(ServiceConfigRecon scfg) {
        if (scfg == null) {
            throw new RuntimeException("AlertService.ctor:cfg is null!");
        }

        if (!scfg.isValid()) {
            throw new RuntimeException("AlertService.ctor:cfg is invalid!");
        }

        reqProcessor = scfg.getReqProcessor();
        reqProcessor.setGreeter(new GreeterDefault(new GreetInfoStandard(scfg.getClientName())));

        queueThread = new QueueThread(
                new QueueListener() {
                    public void process(Object obj) {
                        queueProcess(obj);
                    }
                },
                new CircularQueue(100)
        );
    }

    /**
     * Log a msg to a central location.
     *
     * @param alert The message to log.
     */
    public void log(Alert alert) {
        queueThread.add(alert);
    }


    private void queueProcess(Object obj) {
        WPacket output = new WPacket();

        Alert alert = (Alert) obj;
        output.writeSerializable(alert);

        Request req = new Request(AlertConstants.REPORT, output);
        req.setBlocking(false);

        try {
            reqProcessor.process(req);
        } catch (Exception e) {
            logger.error("AlertService.queueProcess, alert=" + alert, e);
        }
    }
}
