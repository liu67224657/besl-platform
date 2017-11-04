/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.image;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午4:18
 * Description:
 */
class ImageServiceImpl implements ImageService {
    //
    private ReqProcessor reqProcessor = null;

    //
    public ImageServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ImageServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public boolean removeResourceFile(String fileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(fileId);

        Request req = new Request(ImageConstants.RESOURCE_FILE_REMOVE, wp);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }
}
