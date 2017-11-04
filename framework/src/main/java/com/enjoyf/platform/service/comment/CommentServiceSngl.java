/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.serv.comment.CommentConstants;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class CommentServiceSngl {
    //
    private static CommentService instance;

    public static CommentService get() {
        if (instance == null) {
            synchronized (CommentServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(CommentConstants.SERVICE_SECTION, CommentConstants.SERVICE_TYPE);
                    try {
                        instance = new RPCClient<CommentService>(cfg).createService(CommentService.class);
                    } catch (InstantiationException e) {
                        GAlerter.lab("Cannot instantiate client:", e);
                    } catch (IllegalAccessException e) {
                        GAlerter.lab("Cannot instantiate client:", e);
                    }
                }
            }
        }

        return instance;
    }
}
