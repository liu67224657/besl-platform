/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.webapps.image.serv;


import com.enjoyf.platform.service.image.ImageService;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.collection.QueueList;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>  ,zx
 */

class ImageServerLogic implements ImageService {
    //
    private static final Logger logger = LoggerFactory.getLogger(ImageServerLogic.class);

    //
    private ImageServerConfig config;

    //the remove file queue.
    private QueueThreadN resourceFileQueueThreadN;

    //
    ImageServerLogic(ImageServerConfig cfg) {
        config = cfg;

        //
        resourceFileQueueThreadN = new QueueThreadN(config.getQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                //
                if (obj instanceof String) {
                    //
                    processQueuedFile((String) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new QueueList(), "resourceFileQueueThreadN");
    }

    @Override
    public boolean removeResourceFile(String fileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Receive the remove file id:" + fileId);
        }

        resourceFileQueueThreadN.add(fileId);

        return true;
    }

    //
    private void processQueuedFile(String fileId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start to processQueuedFile:" + fileId);
        }

        try {
            //parse all the image files path in the os.

            //remove the file from the file system.
            FileUtil.deleteFileOrDir(null);

            //update the fileremove status.
            //ContentServiceSngl.get().
        } catch (Exception e) {
            GAlerter.lab("ImageServerLogic processQueuedFile error.", e);
        }


    }
}
