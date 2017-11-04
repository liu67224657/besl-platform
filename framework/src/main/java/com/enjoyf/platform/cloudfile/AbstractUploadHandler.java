package com.enjoyf.platform.cloudfile;

import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.util.ResourceFilePathUtil;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/12
 * Description:
 */
abstract class AbstractUploadHandler implements UploadHandler {

    @Override
    public String getPath(String thirdKey, ResourceFileType fileType, String fileName, String extname) {
        return ResourceFilePathUtil.getFilePath(thirdKey, fileName, fileType) + ResourceFilePathUtil.getFileName(fileName) + extname;
    }

}
