package com.enjoyf.platform.text;

import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-27 下午3:47
 * Description:
 */
public class JsonItemImageProcessStragy implements JsonItemProcessStragy {

    @Override
    public TextJsonItem processorText(TextJsonItem jsonItem) {
        URL url = null;
        InputStream inputStream = null;
        try {
            url = new URL(jsonItem.getItem());
            inputStream = url.openStream();
            Image img = ImageIO.read(inputStream);
            jsonItem.setWidth(img.getWidth(null));
            jsonItem.setHeight(img.getHeight(null));
        } catch (Exception e) {
            jsonItem.setWidth(200);
            jsonItem.setHeight(200);
        } finally {
            StreamUtil.closeInputStream(inputStream);
        }
        return jsonItem;
    }

    private void zipImage(String url) throws FileNotFoundException {
        String tempDir = "/opt/uploads/temp/image/";
        if (!FileUtil.isFileOrDirExist(tempDir)) {
            boolean bVal = FileUtil.createDirectory(tempDir);
            if (bVal) {
                System.out.println("not create temp dir");
            }
        }
        String fileName = tempDir + System.currentTimeMillis() + url.substring(url.lastIndexOf("."), url.length());
//
//        try {
//            FileUtil.createFile(fileName, Filedata.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return jsonBinder.toJson(imageJson);
//        }
//
//        if (getSession().getAttribute(SessionConstants.UPLOAD_TOKEN) == null) {
//            return jsonBinder.toJson(imageJson);
//        }
//
//        try {
//            String at = ((AuthToken) getSession().getAttribute(SessionConstants.UPLOAD_TOKEN)).getToken();
//            JoymeResultMsg resultSet = jsonBinder.fromJson(new HttpUpload().postMultipart(uploadUrl, new PostParameter[]{
//                    new PostParameter("Filedata", new File(fileName)),
//                    new PostParameter("filetype", "original"),
//                    new PostParameter(SessionConstants.KEY_ACCESS_TOKEN, at)}, at), JoymeResultMsg.class);
//
//            System.out.println(resultSet);
//            //{"error":0,"url":"\/ke4\/attached\/W020091124524510014093.jpg"}
//
//            if (resultSet.getStatus_code().equals(JoymeResultMsg.CODE_S)) {
//                imageJson.setError(0);
//                imageJson.setUrl(ImageURLTag.parseOrgImg((String) resultSet.getResult().get(0)));
//            } else {
//                imageJson.setError(1);
//            }
//        }
    }
}
