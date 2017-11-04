package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class BatchManageHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(BatchManageHotdeployConfig.class);

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String BATCHMANAGE_BATCHCODE = "batchmanage.batchcode";

    private ReadCache readCache;
    private Random random = new Random();

    public BatchManageHotdeployConfig() {
        super(EnvConfig.get().getBatchManageHotdeployConfig());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public synchronized void reload() {
        super.reload();
        List<String> batchCode = getList(BATCHMANAGE_BATCHCODE);
        ReadCache tmpCache = new ReadCache();
        tmpCache.setBatchCode(batchCode);
        this.readCache = tmpCache;
        logger.info("Event Props init finished.");
    }

    private class ReadCache {
        private List<String> batchCode = new ArrayList<String>();
        public List<String> getBatchCode() {
            return batchCode;
        }
        public List<String> setBatchCode(List<String> batchCode) {
            return this.batchCode=batchCode;
        }
    }

    public List<String> getBatchCode() {
        return readCache.getBatchCode();
    }


}
