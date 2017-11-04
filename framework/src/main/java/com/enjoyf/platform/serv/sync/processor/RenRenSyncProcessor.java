package com.enjoyf.platform.serv.sync.processor;

import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RenRenSyncProcessor implements SyncProcessor {
    private Logger logger = LoggerFactory.getLogger(RenRenSyncProcessor.class);


    @Override
    public boolean processShare(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
