package com.enjoyf.webapps.image.webpage.controller.socialclient;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppAudioDto;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppImageDto;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppMediaDto;
import com.enjoyf.platform.webapps.common.dto.upload.SocialMFaceDto;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.image.serv.ImageServerConfig;
import com.enjoyf.webapps.image.weblogic.file.FileWebLogic;
import com.enjoyf.webapps.image.webpage.ImageBaseRestSpringController;
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
import java.io.IOException;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-11 上午11:38
 * Description:
 */
@Controller
@RequestMapping(value = "/app/social")
public class JsonSocialClientUploadController extends ImageBaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(JsonSocialClientUploadController.class);

    @Resource(name = "fileWebLogic")
    private FileWebLogic fileWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    /**
     * 描述 : <事先就知道确切的上传文件数目>. <br>
     * <p/>
     *
     * @param
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public String handleImport(@RequestParam(value = "audio", required = false) MultipartFile audio,
                               @RequestParam(value = "pic", required = false) MultipartFile image,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量
        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "upload.image.failed");

        try {
            SocialAppMediaDto mediaDto = new SocialAppMediaDto();


            if ((image == null || image.getSize() == 0)) {
                reMsg.setMsg("image.is.empty");
                reMsg.setRs(ResultObjectMsg.CODE_E);
                return JsonBinder.buildNonNullBinder().toJson(reMsg);
            }


            if (audio != null && audio.getSize() > 0) {
                SocialAppAudioDto audioDto = fileWebLogic.processAudio(audio);

                mediaDto.setAudio(audioDto);

            }
            if (image != null && image.getSize() > 0) {
                SocialAppImageDto picDto = fileWebLogic.processImage(image);
                mediaDto.setPic(picDto);
            }


            reMsg.setMsg("success");
            reMsg.setRs(ResultObjectMsg.CODE_S);
            reMsg.setResult(mediaDto);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    private SocialAppMediaDto repaceUrl(SocialAppMediaDto dto) {
        // dto.getAudio()
        return dto;
    }

    /**
     * 手机注册 头像上传
     *
     * @param head
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/mface")
    @ResponseBody
    public String mFace(@RequestParam(value = "head", required = false) MultipartFile head, HttpServletRequest request, HttpServletResponse response) {


        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量

        ResultObjectMsg reMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E, "mface.upload.image.failed");

        if ((head == null || head.getSize() == 0)) {
            reMsg.setMsg("head.is.empty");
            reMsg.setRs(ResultObjectMsg.CODE_E);
            return JsonBinder.buildNonNullBinder().toJson(reMsg);
        }
        try {
            if (head != null && StringUtils.hasText(head.getOriginalFilename())) {
                Map<String, String> map = fileWebLogic.saveMobileFaceFile(head, uploadPath, getIp(request));
                SocialMFaceDto sfDto = new SocialMFaceDto();
                String filePrefix = getFilePath();
                if (!CollectionUtil.isEmpty(map)) {
                    sfDto.setBpic(filePrefix + map.get("b"));
                    sfDto.setMpic(filePrefix + map.get("m"));
                    sfDto.setSpic(filePrefix + map.get("s"));
                    sfDto.setSspic(filePrefix + map.get("ss"));
                    reMsg.setMsg("success");
                    reMsg.setRs(ResultObjectMsg.CODE_S);
                }
                reMsg.setResult(sfDto);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            logger.error(e.getMessage(), e);
        }
        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    private String getFilePath() {
        return "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain();
    }

}
