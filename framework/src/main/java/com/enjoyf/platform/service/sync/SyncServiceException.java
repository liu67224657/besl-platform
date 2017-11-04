package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncServiceException extends ServiceException {
    public static final SyncServiceException SYNC_GET_AUTHURL_SERVICE_EXCEPTION = new SyncServiceException(BASE_SYNC + 1, "get authurl error.");

    public static final SyncServiceException SYNC_AUTH_FALIED_SERVICE_EXCEPTION = new SyncServiceException(BASE_SYNC + 4, "auth user falied.");

    public static final SyncServiceException SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION = new SyncServiceException(BASE_SYNC + 2, "unsupport this method");

    public static final SyncServiceException SYNC_UNSUPPORT_APP_SERVICE_EXCEPTION = new SyncServiceException(BASE_SYNC + 3, "unsupport this app");

    public static final SyncServiceException SYNC_GETFRIENDS_EXCEPTION = new SyncServiceException(BASE_SYNC + 5, "getfriends this app");

    public static final SyncServiceException SYNC_ATUSERS_EXCEPTION = new SyncServiceException(BASE_SYNC + 6, "atusers this app");

    public SyncServiceException(int i, String s) {
        super(i, s);
    }
}
