package com.enjoyf.webapps.tools.webpage.controller;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.props.WebappConfig;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ericliu on 2017/6/27.
 */
@Controller
@RequestMapping("/qiniu")
public class QiniuController {

    @RequestMapping("/token/get")
    @ResponseBody
    public String getToken(){
        BucketInfo bucketInfo = BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
        String token = null;
        try {
            token = FileHandlerFactory.getToken(bucketInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uploadtoken", token);

        return jsonObject.toString();
    }
}
