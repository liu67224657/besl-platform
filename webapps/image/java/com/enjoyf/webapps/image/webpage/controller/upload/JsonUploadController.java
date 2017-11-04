/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.image.webpage.controller.upload;

import com.enjoyf.platform.cloudfile.ImageProcessObject;
import com.enjoyf.platform.service.content.ContentServiceException;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.service.gameres.GameDevice;
import com.enjoyf.platform.service.gameres.GameDeviceSet;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.ResourceFilePathUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppAudioDto;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppImageDto;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.picture.AppCardGenerator;
import com.enjoyf.webapps.image.serv.ImageServerConfig;
import com.enjoyf.webapps.image.weblogic.dto.AppIOSDto;
import com.enjoyf.webapps.image.weblogic.dto.FileEntity;
import com.enjoyf.webapps.image.weblogic.file.FileWebLogic;
import com.enjoyf.webapps.image.weblogic.file.ImageCropInfo;
import com.enjoyf.webapps.image.weblogic.file.UploadWebLogic;
import com.enjoyf.webapps.image.webpage.ImageBaseRestSpringController;
import com.enjoyf.webapps.image.webpage.bese.SessionConstants;

import net.sf.json.JSONObject;

import org.im4java.core.IM4JavaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

//import com.enjoyf.platform.webapps.common.picture.QiniuPicManager;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-10 上午11:35
 * Description:
 */
@Controller
@RequestMapping(value = "/json/upload")
public class JsonUploadController extends ImageBaseRestSpringController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "fileWebLogic")
    private FileWebLogic fileWebLogic;

    @Resource(name = "uploadWebLogic")
    private UploadWebLogic uploadWebLogic;


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    /**
     * 描述 : todo 不在维护 不推荐使用 <br>
     * <p/>
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    @Deprecated
    @RequestMapping(value = "/single")
    @ResponseBody
    public String single(@RequestParam(value = "Filedata", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            reMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }
        try {
            if (file != null && StringUtils.hasText(file.getOriginalFilename())) {

                String fileUrl = uploadWebLogic.uploadImage(file, ResourceFileType.IMAGE);
                Map<String, String> map = new HashMap<String, String>();
                map.put("b", fileUrl);
                map.put("m", fileUrl);
                map.put("s", fileUrl);
                map.put("ss", fileUrl);

                reMsg.setStatus_code(JoymeResultMsg.CODE_S);
                reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
                //返回结果的list
                List list = new ArrayList();
                list.add(map);

                reMsg.setResult(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }


    /**
     * 描述 :  todo 不在维护 不推荐使用 上传图片链接<br>
     * <p/>
     *
     * @param url 网络链接
     * @return
     * @throws IOException
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/imagelink")
    public String imageLink(@RequestParam(value = "url", required = false) String url, HttpServletRequest request) {

        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            reMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        try {
            //url 为空
            if (StringUtil.isEmpty(url)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
                return JsonBinder.buildNonNullBinder().toJson(reMsg);
            }

            url = URLDecoder.decode(url, "UTF-8");
            String fileUrl = uploadWebLogic.uploadImageByUrl(url, ResourceFileType.IMAGE);
            Map<String, String> map = new HashMap<String, String>();
            map.put("b", fileUrl);
            map.put("m", fileUrl);
            map.put("s", fileUrl);
            map.put("ss", fileUrl);

            reMsg.setStatus_code(JoymeResultMsg.CODE_S);

            //得到图片的img
            StringBuffer imageName = new StringBuffer(url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));
            imageName.append(url.substring(url.lastIndexOf(".")));
            reMsg.setMsg(imageName.toString());
            //返回结果的list
            List list = new ArrayList();
            list.add(map);

            reMsg.setResult(list);
        } catch (IOException e) {
            GAlerter.lab("imageLink occured IOexception", e);
            if (e instanceof FileNotFoundException) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            } else {
                reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            }
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (ServiceException e) {
            GAlerter.lab("imageLink occured ServiceException", e);
            if (e.equals(ContentServiceException.UPLOAD_IMAGE_ILLIGEL_EXCEPTION)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
            } else if (e.equals(ContentServiceException.UPLOAD_IMAGE_FILETYPE_ERROR)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
            } else if (e.equals(ContentServiceException.UPLOAD_IMAGE_OUT_SIZE)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.out.size", new Object[]{}, Locale.CHINA));
            }
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab("imageLink occured Exception", e);
            reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);

    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/replyimagelink")
    public String replyimageLink(@RequestParam(value = "url", required = false) String url, HttpServletRequest request) {

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            reMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        try {
            //url 为空
            if (StringUtil.isEmpty(url)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
                return JsonBinder.buildNonNullBinder().toJson(reMsg);
            }

            url = URLDecoder.decode(url, "UTF-8");

            String fileUrl = uploadWebLogic.uploadImageByUrl(url, ResourceFileType.IMAGE);
            Map<String, String> map = new HashMap<String, String>();
            map.put("b", fileUrl);
            map.put("m", fileUrl);
            map.put("s", fileUrl);
            map.put("ss", fileUrl);

            reMsg.setStatus_code(JoymeResultMsg.CODE_S);

            //得到图片的img
            StringBuffer imageName = new StringBuffer(url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));
            imageName.append(url.substring(url.lastIndexOf(".")));
            reMsg.setMsg(imageName.toString());
            //返回结果的list
            List list = new ArrayList();
            list.add(map);

            reMsg.setResult(list);
        } catch (IOException e) {
            GAlerter.lab("imageLink occured IOexception", e);
            if (e instanceof FileNotFoundException) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            } else {
                reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            }
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (ServiceException e) {
            GAlerter.lab("imageLink occured ServiceException", e);
            if (e.equals(ContentServiceException.UPLOAD_IMAGE_ILLIGEL_EXCEPTION)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
            } else if (e.equals(ContentServiceException.UPLOAD_IMAGE_FILETYPE_ERROR)) {
                reMsg.setMsg(i18nSource.getMessage("upload.image.exname.error", new Object[]{}, Locale.CHINA));
            } else if (e.equals(ContentServiceException.UPLOAD_IMAGE_OUT_SIZE)) {
                reMsg.setMsg(i18nSource.getMessage("upload.reply.image.out.size", new Object[]{}, Locale.CHINA));
            }
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab("imageLink occured Exception", e);
            reMsg.setMsg(i18nSource.getMessage("upload.image.urllink.error", new Object[]{}, Locale.CHINA));
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);

    }

    /**
     * 描述 : <上传头像>. <br>
     * <p/>
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/app/ios")
    @ResponseBody
    public String uploadApp(@RequestParam(value = "appid", required = false) String appid,
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

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

//        AuthToken authToken = getAuthTokenBySession(request);
        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            resultMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(resultMsg);
        }

        String uploadPath = getUploadPath();
        String basePath = getStaticRealPath(request);

        //动态坐标
        Map<AppCardGenerator.PictureElement, String> appParamMap = new HashMap<AppCardGenerator.PictureElement, String>();
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_PRICE, price);
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_SIZE, fileSize);
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_RATING, String.valueOf(currentRating));
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_NAME, appName);
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_CATEGORY, appCategory);
        //
        Set<AppCardGenerator.AppDevicePic> devicePicTypes = new HashSet<AppCardGenerator.AppDevicePic>();
        GameDeviceSet deviceSet = new GameDeviceSet();
        for (String device : devices.split(",")) {
            devicePicTypes.add(AppCardGenerator.AppDevicePic.getByCode(device));
            GameDevice gameDeviceCode = GameDevice.getByCode(device);
            if (gameDeviceCode != null) {
                deviceSet.add(GameDevice.getByCode(device));
            }
        }

        Set<String> screenShotSet = new HashSet<String>();
        if (!StringUtil.isEmpty(screenShot) && screenShot.indexOf(",") != -1) {
            for (String screenShotStr : screenShot.split(",")) {
                screenShotSet.add(screenShotStr);
            }
        }

        try {
            //生成图片
            AppIOSDto appIOSDto = fileWebLogic.uploadApp(icon, appName, appParamMap, devicePicTypes, screenShotSet, uploadPath, basePath, authToken, getIp(request));
            if (logger.isDebugEnabled()) {
                logger.debug("generator appIOSDto" + appIOSDto);
            }

            List<AppIOSDto> list = new ArrayList<AppIOSDto>();
            list.add(appIOSDto);
            resultMsg.setResult(list);

        } catch (InterruptedException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            GAlerter.lab("uploadApp occured InterruptedException.e:", e);
        } catch (IM4JavaException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            GAlerter.lab("uploadApp occured IM4JavaException.e:", e);
        } catch (IOException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            GAlerter.lab("uploadApp occured IOException.e:", e);
        } catch (Exception e) {
            GAlerter.lab("uploadApp occured Exception.e:", e);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


    @Deprecated
    @RequestMapping(value = "/resource/logo")
    @ResponseBody
    public String upload(@RequestParam(value = "Filedata", required = false) MultipartFile fileData,
                         @RequestParam(value = "resourceDomain") String domainCode,
                         @RequestParam(value = "scale", required = false, defaultValue = "3:4") String socale, HttpServletRequest request) throws IOException {

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            reMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }
        try {
            if (fileData != null && StringUtils.hasText(fileData.getOriginalFilename())) {
                String fileUrl = uploadWebLogic.uploadImage(fileData, ResourceFileType.GAME);
                Map<String, String> map = new HashMap<String, String>();
                map.put("b", fileUrl);
                map.put("m", fileUrl);
                map.put("s", fileUrl);
                map.put("ss", fileUrl);

                reMsg.setStatus_code(JoymeResultMsg.CODE_S);
                reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
                //返回结果的list
                List list = new ArrayList();
                list.add(map);

                reMsg.setResult(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }


    /**
     * 描述 :todo 不在维护 <br>
     * <p/>
     *
     * @return
     * @throws IOException
     */
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/face")
    public String handleFaceImport(@RequestParam(value = "Filedata", required = false) MultipartFile fileData, HttpServletRequest request) throws IOException {

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        try {
            if (fileData != null && StringUtils.hasText(fileData.getOriginalFilename())) {
                String fileUrl = uploadWebLogic.uploadImage(fileData, ResourceFileType.GAME);
                Map<String, String> map = new HashMap<String, String>();
                map.put("b", fileUrl);
                map.put("m", fileUrl);
                map.put("s", fileUrl);
                map.put("ss", fileUrl);


                reMsg.setStatus_code(JoymeResultMsg.CODE_S);
                reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
                //返回结果的list
                List list = new ArrayList();
                list.add(map);

                reMsg.setResult(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }


    /**
     * 描述 : <调整缩略图>. <br>
     * <p/>
     *
     * @return
     * @throws IOException
     */
    @Deprecated //直接调用七牛
    @RequestMapping(value = "/thumbnail")
    @ResponseBody
    public String thumbnail
    (@RequestParam(value = "filename", required = true) String filename,
     @RequestParam(value = "x1", required = true) int x1,
     @RequestParam(value = "y1", required = true) int y1,
     @RequestParam(value = "x2", required = false) int x2,
     @RequestParam(value = "y2", required = false) int y2,
     @RequestParam(value = "w", required = true) int w,
     @RequestParam(value = "h", required = true) int h,
     @RequestParam(value = "rtype", required = true, defaultValue = "image") String rtype,
     HttpServletRequest request) throws IOException {

        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_E));
        jsonObject.put("msg", String.valueOf(i18nSource.getMessage("upload.image.failed", null, Locale.CHINA)));

        FileEntity entity = new FileEntity();
        entity.setHight(h);
        entity.setWidth(w);
        entity.setX1(x1);
        entity.setX2(x2);
        entity.setY1(y1);
        entity.setY2(y2);
        entity.setName(filename);
        entity.setPath(uploadPath);

        try {

            ResourceFileType type = ResourceFileType.getByCode(rtype) != null ? ResourceFileType.getByCode(rtype) : ResourceFileType.IMAGE;
            //todo 七牛 还需要aliyun支持
            ImageProcessObject processObject = new ImageProcessObject();
            processObject.setCroper(new ImageProcessObject.Croper(x1, y1, w, h));
            String url = uploadWebLogic.processImage(filename, processObject);

            String key = uploadWebLogic.uploadImageByUrl(url, type);

            jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_S));
            jsonObject.put("url", key);
            jsonObject.put("msg", "");

            return jsonObject.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return jsonObject.toString();
    }

    /**
     * 描述 : 上传一张图片. <br>
     * <p/>
     *
     * @param Filedata
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/orgupload")
    @ResponseBody
    public String common(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                         @RequestParam(value = "filetype", required = false, defaultValue = "original") String fileType,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {

        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

        AuthToken authToken = null;
        if (request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN) != null) {
            authToken = (AuthToken) request.getAttribute(SessionConstants.KEY_ACCESS_TOKEN);
        }

        if (authToken == null) {
            reMsg.setMsg("token_faild");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        ResourceFileType resourceFileType = ResourceFileType.getByCode(fileType);

        if (resourceFileType == null) {
            reMsg.setMsg("filetype_error");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }


        try {
            if (Filedata != null && StringUtils.hasText(Filedata.getOriginalFilename())) {
                String uploadResultPath = uploadWebLogic.uploadImage(Filedata, resourceFileType);


                reMsg.setStatus_code(JoymeResultMsg.CODE_S);
                reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));

                //返回结果的list
                List<String> list = new ArrayList<String>();
                list.add(uploadResultPath);

                reMsg.setResult(list);
            }
        } catch (Exception e) {
            GAlerter.lab(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    /**
     * 描述 : 上传 第三方开放平台 头像 <br>
     * <p/>
     *
     * @param url 网络链接
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/figureurl")
    @ResponseBody
    public String figureUrl(@RequestParam(value = "url", required = false) String url, HttpServletRequest request, HttpServletResponse response) {
        //用户上传根路径
        String callback = request.getParameter("callback");
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_E));
        //url 为空
        if (StringUtil.isEmpty(url)) {
            jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_E));
            jsonObject.put("msg", String.valueOf(i18nSource.getMessage("upload.image.exname.error", null, Locale.CHINA)));

            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "(" + jsonObject.toString() + ")";
            }
        }

        try {
            url = URLDecoder.decode(url, "UTF-8");

            String tunmbnail = uploadWebLogic.uploadImageByUrl(url, ResourceFileType.HEADICON);

            jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_S));
            if (tunmbnail == null) {
                jsonObject.put("rs", String.valueOf(JoymeResultMsg.CODE_E));
                jsonObject.put("msg", i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));

                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "(" + jsonObject.toString() + ")";
                }
            }

            jsonObject.put("url", tunmbnail);
            jsonObject.put("msg", "");

            if (StringUtil.isEmpty(callback)) {
                return jsonObject.toString();
            } else {
                return callback + "(" + jsonObject.toString() + ")";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "(" + jsonObject.toString() + ")";
        }
    }

    @RequestMapping(value = "/profilepic")
    @ResponseBody
    public String profileimage(@RequestParam(value = "picfile", required = false) MultipartFile picfile,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量
        JSONObject jsonObject = new JSONObject();
        try {
            if ((picfile == null || picfile.getSize() == 0)) {
                return ResultCodeConstants.UPLOAD_IMAGE_FAILED.getJsonString();
            }

            String profileId = request.getParameter("profileid");
            if (StringUtil.isEmpty(profileId)) {
                String uno = request.getParameter("uno");
                String appkey = AppUtil.getAppKey(request.getParameter("appkey"));
                AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
                profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            String image = uploadWebLogic.uploadImage(picfile, ResourceFileType.IMAGE);

            Map<String, String> map = new HashMap<String, String>();
            map.put("pic", image);
            String flag = request.getParameter("flag");
            if (!StringUtil.isEmpty(flag)) {
                map.put("flag", flag);
            }

            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            jsonObject.put("result", map);

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

    }


    /**
     * 描述 : 上传一张图片. <br>
     * <p/>
     *
     * @param Filedata
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/qiniu")
    @ResponseBody
    public String qiniu(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                        @RequestParam(value = "filetype", required = false, defaultValue = "original") String fileType,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {

    	
        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));


        ResourceFileType resourceFileType = ResourceFileType.getByCode(fileType);

        if (resourceFileType == null) {
            reMsg.setMsg("filetype_error");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }


        try {
            String image = uploadWebLogic.uploadImage(Filedata, resourceFileType);
            reMsg.setStatus_code(JoymeResultMsg.CODE_S);
            reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
            //返回结果的list
            List<String> list = new ArrayList<String>();
            list.add(image);
            reMsg.setResult(list);
        } catch (Exception e) {
            GAlerter.lab(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    /**
     * 描述 : 上传一张图片. <br>
     * <p/>
     *
     * @param Filedata
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/qiniu/crop")
    @ResponseBody
    public String qiniuCrop(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                            @RequestParam(value = "filetype", required = false, defaultValue = "original") String fileType,
                            @RequestParam(value = "x", required = true) int x,
                            @RequestParam(value = "y", required = true) int y,
                            @RequestParam(value = "width", required = true) int width,
                            @RequestParam(value = "height", required = true) int height,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {

    	
        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));


        ResourceFileType resourceFileType = ResourceFileType.getByCode(fileType);

        if (resourceFileType == null) {
            reMsg.setMsg("filetype_error");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        if (width<=0 || height <=0) {
            reMsg.setMsg("width or height error");
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
		}

        try {
        	ImageCropInfo imageCropInfo=new ImageCropInfo();
        	imageCropInfo.setX(x);
        	imageCropInfo.setY(y);
        	imageCropInfo.setWidth(width);
        	imageCropInfo.setHeight(height);
        	
            String image = uploadWebLogic.uploadImageCrop(Filedata, resourceFileType,imageCropInfo);
            reMsg.setStatus_code(JoymeResultMsg.CODE_S);
            reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
            //返回结果的list
            List<String> list = new ArrayList<String>();
            list.add(image);
            reMsg.setResult(list);
        } catch (Exception e) {
            GAlerter.lab(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }
    
    
    //    /////////todo//////////////////
    @RequestMapping(value = "/mp3")
    @ResponseBody
    public String mp3(@RequestParam(value = "Filedata", required = false) MultipartFile audio,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uploadPath = getUploadPath();

        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "upload.image.failed");

        if ((audio == null || audio.getSize() == 0)) {
            reMsg.setMsg("audio.is.empty");
            reMsg.setRs(ResultObjectMsg.CODE_E);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        String outputFileName = ResourceFilePathUtil.getFileName(audio.getOriginalFilename());
        String outputFilePath = uploadPath + ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), audio.getOriginalFilename(), ResourceFileType.AUDIO);
        try {
            String returnAudio = fileWebLogic.saveAudioMp3(audio, outputFileName, outputFilePath);

            reMsg.setMsg("success");
            reMsg.setRs(ResultObjectMsg.CODE_S);
            reMsg.setResult(returnAudio);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    @RequestMapping(value = "/wav")
    @ResponseBody
    public String audio(@RequestParam(value = "Filedata", required = false) MultipartFile audio,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uploadPath = getUploadPath();

        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "upload.image.failed");

        if ((audio == null || audio.getSize() == 0)) {
            reMsg.setMsg("audio.is.empty");
            reMsg.setRs(ResultObjectMsg.CODE_E);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }

        String outputFileName = ResourceFilePathUtil.getFileName(audio.getOriginalFilename());
        String outputFilePath = uploadPath + ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), audio.getOriginalFilename(), ResourceFileType.AUDIO);
        try {
            String returnAudio = fileWebLogic.saveAudioWav(audio, outputFileName, outputFilePath);

            reMsg.setMsg("success");
            reMsg.setRs(ResultObjectMsg.CODE_S);
            reMsg.setResult(returnAudio);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    @RequestMapping(value = "/socialapp/pic")
    @ResponseBody
    public String pic(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量
        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "upload.image.failed");

        try {
            if ((Filedata == null || Filedata.getSize() == 0)) {
                reMsg.setMsg("image.is.empty");
                reMsg.setRs(ResultObjectMsg.CODE_E);
                return JsonBinder.buildNonNullBinder().toJson(reMsg);
            }

            SocialAppImageDto picDto = fileWebLogic.processImage(Filedata);

            reMsg.setMsg("success");
            reMsg.setRs(ResultObjectMsg.CODE_S);
            reMsg.setResult(picDto);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    @RequestMapping(value = "/socialapp/amr")
    @ResponseBody
    public String amr(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量
        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "upload.image.failed");
        try {
            if ((Filedata == null || Filedata.getSize() == 0)) {
                reMsg.setMsg("image.is.empty");
                reMsg.setRs(ResultObjectMsg.CODE_E);
                return JsonBinder.buildNonNullBinder().toJson(reMsg);
            }

            SocialAppAudioDto audioDto = fileWebLogic.processAudio(Filedata);

            reMsg.setMsg("success");
            reMsg.setRs(ResultObjectMsg.CODE_S);
            reMsg.setResult(audioDto);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }
    //    /////////todo//////////////////


}
