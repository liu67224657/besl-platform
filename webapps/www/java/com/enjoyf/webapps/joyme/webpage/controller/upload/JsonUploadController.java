package com.enjoyf.webapps.joyme.webpage.controller.upload;

import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.service.usercenter.Icon;
import com.enjoyf.platform.service.usercenter.Icons;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.HttpUpload;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 文件上传action
 *
 * @author xinzhao
 */

@Controller
@RequestMapping("/json/upload")
public class JsonUploadController extends BaseRestSpringController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @RequestMapping(value = "/imagelink")
    @ResponseBody
    public String sendLink(@RequestParam(value = "url", required = false) String url, HttpServletRequest request) {

        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN + "/json/upload/imagelink";

        logger.info("imagelink uploadUrl: " + uploadUrl);

        UserSession userSession = this.getUserBySession(request);

        return new HttpUpload().post(uploadUrl, new PostParameter[]{new PostParameter("url", url), new PostParameter(CookieUtil.ACCESS_TOKEN, userSession.getAuthToken().getToken())}, userSession.getAuthToken().getToken());
    }

    @RequestMapping(value = "/replyimagelink")
    @ResponseBody
    public String sendReplyImageLink(@RequestParam(value = "url", required = false) String url, HttpServletRequest request) {

        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN + "/json/upload/replyimagelink";

        logger.info("imagelink uploadUrl: " + uploadUrl);

        UserSession userSession = this.getUserBySession(request);

        return new HttpUpload().post(uploadUrl, new PostParameter[]{new PostParameter("url", url), new PostParameter(CookieUtil.ACCESS_TOKEN, userSession.getAuthToken().getToken())}, userSession.getAuthToken().getToken());
    }

    @RequestMapping(value = "/app/ios")
    @ResponseBody
    public String sendLink(@RequestParam(value = "appid", required = false) String appid,
                           @RequestParam(value = "appName", required = false) String appName,
                           @RequestParam(value = "icon", required = false) String icon,
                           @RequestParam(value = "price", required = false) String price,
                           @RequestParam(value = "fileSize", required = false) String fileSize,
                           @RequestParam(value = "appCategory", required = false) String appCategory,
                           @RequestParam(value = "currentRating", required = false) Float currentRating,
                           @RequestParam(value = "currentRatingCount", required = false) Integer currentRatingCount,
                           @RequestParam(value = "totalRating", required = false) Float totalRating,
                           @RequestParam(value = "totalRatingCount", required = false) Integer totalRatingCount,

                           @RequestParam(value = "iosDesc", required = false) String iosDesc,
                           @RequestParam(value = "publishDate", required = false) String publishDate,
                           @RequestParam(value = "appVersion", required = false) String appVersion,
                           @RequestParam(value = "language", required = false) String language,
                           @RequestParam(value = "develop", required = false) String develop,
                           @RequestParam(value = "devices", required = false) String devices,

                           @RequestParam(value = "resourceUrl", required = false) String resourceUrl,
                           @RequestParam(value = "screenShot", required = false) String screenShot,
                           HttpServletRequest request) {

        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN + "/json/upload/app/ios";

        UserSession userSession = this.getUserBySession(request);

        PostParameter[] postParameters = new PostParameter[]{
                new PostParameter("appid", appid),
                new PostParameter("appName", appName),
                new PostParameter("icon", icon),
                new PostParameter("price", price),
                new PostParameter("fileSize", fileSize),
                new PostParameter("appCategory", appCategory),
                new PostParameter("currentRating", currentRating),
                new PostParameter("currentRatingCount", currentRatingCount),
                new PostParameter("totalRating", totalRating),
                new PostParameter("totalRatingCount", totalRatingCount),
                new PostParameter("iosDesc", iosDesc),


                new PostParameter("publishDate", publishDate),
                new PostParameter("appVersion", appVersion),
                new PostParameter("language", language),
                new PostParameter("develop", develop),
                new PostParameter("devices", devices),
                new PostParameter("resourceUrl", resourceUrl),
                new PostParameter("screenShot", screenShot),
                new PostParameter(CookieUtil.ACCESS_TOKEN, userSession.getAuthToken().getToken())
        };

        return new HttpUpload().post(uploadUrl, postParameters, userSession.getAuthToken().getToken());

    }


    /**
     * 描述 : <调整缩略图>. <br>
     * <p/>
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/thumbnail")
    public
    @ResponseBody
    String handleThumImport(
            @RequestParam(value = "filename", required = true) String filename,
            @RequestParam(value = "x1", required = true) String x1,
            @RequestParam(value = "y1", required = true) String y1,
            @RequestParam(value = "x2", required = true) String x2,
            @RequestParam(value = "y2", required = true) String y2,
            @RequestParam(value = "w", required = true) String w,
            @RequestParam(value = "h", required = true) String h,

            HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量
//        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_E));
        jsonObject.put("msg", i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        try {
            String uploadUrl = "http://" + ResourceServerMonitor.get().getThumbnailResourceDomain(filename) + "." + Constant.DOMAIN + "/json/upload/thumbnail";
            UserCenterSession userSession = this.getUserCenterSeesion(request);

            PostParameter[] postParameters = new PostParameter[]{
                    new PostParameter("url", uploadUrl),
                    new PostParameter(CookieUtil.ACCESS_TOKEN, userSession.getToken()),
                    new PostParameter("x1", x1),
                    new PostParameter("y1", y1),
                    new PostParameter("x2", x2),
                    new PostParameter("y2", y2),
                    new PostParameter("w", w),
                    new PostParameter("h", h),
                    new PostParameter("filename", filename),
                    new PostParameter("uno", request.getParameter("uno")),
                    new PostParameter("appkey", request.getParameter("appkey"))
            };

            String result = new HttpUpload().post(uploadUrl, postParameters, userSession.getToken());

//            reMsg = JsonBinder.buildNormalBinder().fromJson(result, JoymeResultMsg.class);

            jsonObject = JSONObject.fromObject(result);
            String rs = jsonObject.getString("rs");

            if (rs.equals(JoymeResultMsg.CODE_S)) {
                if (userSession != null) {
                    String url = jsonObject.getString("url");
                    userSession.setIcon(jsonObject.getString("url"));
                    if (userSession.getIcons() == null) {
                        userSession.setIcons(new Icons());
                    }

                    if (userSession.getIcons().getIconList() == null) {
                        userSession.getIcons().setIconList(new ArrayList<Icon>());
                    }
                    userSession.getIcons().getIconList().add(new Icon(userSession.getIcons().getIconList().size(), url));
                }
            }
            return jsonObject.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return jsonObject.toString();
    }

}
