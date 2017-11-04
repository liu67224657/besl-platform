package com.enjoyf.platform.serv.sync.processor;

import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public interface SyncProcessor {

    public boolean processShare(TokenInfo tokenInfo, SyncContent syncContent) throws ServiceException;
}
