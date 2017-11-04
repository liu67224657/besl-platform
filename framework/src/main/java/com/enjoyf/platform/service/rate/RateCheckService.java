package com.enjoyf.platform.service.rate;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
public class RateCheckService implements RateService {
    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    /**
     * Constructs a RateCheckService
     *
     * @param scfg - configuration object for the service
     */
    public RateCheckService(ServiceConfig scfg) {
        this(scfg, EnvConfig.get().getServicePartitionNum(RateConstants.SERVICE_SECTION));
    }

    /**
     * Constructs a RateCheckService
     *
     * @param scfg - configuration object for the service
     */
    public RateCheckService(ServiceConfig scfg, int numPartitions) {
        if (scfg == null) {
            throw new RuntimeException("RateCheckService.ctor:" + " cfg is null!");
        }

        if (!scfg.isValid()) {
            throw new RuntimeException("RateCheckService.ctor:" + " cfg is invalid!");
        }

        reqProcessor = scfg.getReqProcessor();
        this.numOfPartitions = numPartitions;
    }

    /**
     * Gets the current Rate
     *
     * @param key - particular count
     */
    public Rate getRate(RateKey key) throws ServiceException {
        if (!key.getDomain().isLimitSet()) {
            throw new RateServiceException(RateServiceException.LIMIT_NOT_SET);
        } else {
            return getRate(key.getDomain().getLimit(), key);
        }
    }

    /**
     * Gets the current Rate
     *
     * @param limit - particular limit
     * @param key   - particular count
     */
    public Rate getRate(RateLimit limit, RateKey key) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(limit);
        wp.writeSerializable(key);
        Request req = new Request(RateConstants.GET_RATE, wp);
        req.setPartition(Math.abs(key.getPartitionSeed()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        Rate rate = (Rate) rp.readSerializable();
        //comment by gonghaigang for exception-control-logic bed smell
//        if (rate.isExceeded()) {
//            throw new RateServiceException(key);
//        }

        return rate;
    }

    /**
     * Gets the new Rate
     *
     * @param key - particular count
     */
    public Rate recordCount(RateKey key) throws ServiceException {
        if (!key.getDomain().isLimitSet()) {
            throw new RateServiceException(RateServiceException.LIMIT_NOT_SET);
        } else {
            return recordCount(key.getDomain().getLimit(), key);
        }
    }

    /**
     * Gets the new Rate
     *
     * @param limit - particular limit
     * @param key   - particular count
     */
    public Rate recordCount(RateLimit limit, RateKey key) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable(limit);
        wp.writeSerializable(key);
        Request req = new Request(RateConstants.RECORD_COUNT, wp);
        req.setPartition(Math.abs(key.getPartitionSeed()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        Rate rate = (Rate) rp.readSerializable();
        //comment by gonghaigang for exception-control-logic bed smell
//        if (rate.isExceeded()) {
//            throw new RateServiceException(key);
//        }

        return rate;
    }
}
