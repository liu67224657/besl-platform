/**
 *
 */
package com.enjoyf.webapps.image.weblogic.file;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.ResourceFilePathUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.audio.FfmpegUtil;
import com.enjoyf.platform.util.image.ImageGenerator;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppAudioDto;
import com.enjoyf.platform.webapps.common.dto.upload.SocialAppImageDto;
import com.enjoyf.platform.webapps.common.picture.AppCardGenerator;
import com.enjoyf.platform.webapps.common.picture.HandleFacePic;
import com.enjoyf.platform.webapps.common.picture.HandlePic;
import com.enjoyf.webapps.image.serv.ImageServerConfig;
import com.enjoyf.webapps.image.weblogic.dto.AppIOSDto;
import org.im4java.core.IM4JavaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 文件上传服务 todo 不在维护 请使用uploadWebLogic
 *
 * @author xinzhao
 */
@Service("fileWebLogic")
@Deprecated
public class FileWebLogic {
    //
    private static final Logger logger = LoggerFactory.getLogger(FileWebLogic.class);

    private static final String USER_UPLOAD_PATH = "/upload";

    public Map saveImgUrl(String imageLink, String uploadpath, AuthToken authToken, String uploadIp) throws IOException, ServiceException, InterruptedException, IM4JavaException {
        Map map = new HashMap();

        int idxLastDot = imageLink.lastIndexOf(".");
        if (idxLastDot == -1) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_ILLIGEL_EXCEPTION, "upload image exception" + imageLink);
        }

        //检查扩展名类型是否可上传
        String exName = imageLink.substring(idxLastDot + 1).toLowerCase();
        if (!WebappConfig.get().getUploadExtList().contains(exName)) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_FILETYPE_ERROR, "upload image exception" + imageLink);
        }

        URL url = new URL(imageLink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if ((conn.getContentLength() / 1024) > WebappConfig.get().getUploadMaxSize()) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_OUT_SIZE, "upload image exception" + imageLink);
        }

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(conn.getInputStream());
            map = createImgPhoto(bufferedInputStream, imageLink, uploadpath, authToken, uploadIp);
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }

        return map;
    }

    /*
      *生成3种图片
      * inputStream 输入文件流
      * inputFileName 输入文件名
      * path 输出路径
      * */
    @Deprecated
    private Map createImgPhoto(InputStream inputStream, String inputFileName, String path, AuthToken authToken, String uploadIp) throws IOException, IM4JavaException, InterruptedException {
        //根据规则生成的路径及文件名
        String sOutputDir = ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), inputFileName, ResourceFileType.IMAGE);
        String sOutputFileName = ResourceFilePathUtil.getFileName(inputFileName);

        long start = System.currentTimeMillis(); // 开始时间

        Map<String, String> map = HandlePic.handlePhoto(inputStream, inputFileName, path, sOutputDir, sOutputFileName);

        if (logger.isDebugEnabled()) {
            logger.debug("upload spent：" + (System.currentTimeMillis() - start));
        }

        if (!CollectionUtil.isEmpty(map)) {
            insertResourceFile(map.get("b"), authToken.getCredentialId(), ResourceFileType.IMAGE, uploadIp);
        }

        return map;
    }

    /*
      * 上传头像图片
      *
      * @param MultipartFile  spring file 对象
      * */
    @Deprecated
    public Map saveFaceImgFile(MultipartFile multifile, String uploadpath) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("saveImgFile service impl");
        }

        //根据规则生成的路径及文件名
        String sOutputDir = ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), multifile.getOriginalFilename(), ResourceFileType.HEADICON);
        String sOutputFileName = ResourceFilePathUtil.getFileName(multifile.getOriginalFilename());

        int start = (int) System.currentTimeMillis(); // 开始时间
        Map<String, String> map = HandleFacePic.hadnleFP(multifile.getInputStream(), multifile.getOriginalFilename(), uploadpath, sOutputDir, sOutputFileName);
        int end = (int) System.currentTimeMillis(); // 结束时间
        if (logger.isDebugEnabled()) {
            logger.debug("upload spend：" + ((end - start) / 1000));
        }

        return map;
    }

    @Deprecated
    private String getUploadPath() {
        return WebappConfig.get().getUploadRootpath() + USER_UPLOAD_PATH;
    }


    @Deprecated
    public Map saveReplyImgUrl(String imageLink, String uploadpath, AuthToken authToken, String uploadIp) throws IOException, ServiceException, InterruptedException, IM4JavaException {
        Map map = new HashMap();

        int idxLastDot = imageLink.lastIndexOf(".");
        if (idxLastDot == -1) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_ILLIGEL_EXCEPTION, "upload image exception" + imageLink);
        }

        //检查扩展名类型是否可上传
        String exName = imageLink.substring(idxLastDot + 1).toLowerCase();
        if (!WebappConfig.get().getUploadExtList().contains(exName)) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_FILETYPE_ERROR, "upload image exception" + imageLink);
        }

        URL url = new URL(imageLink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if ((conn.getContentLength() / 1024) > WebappConfig.get().getUploadReplyMaxSize()) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_OUT_SIZE, "upload image exception" + imageLink);
        }

        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(conn.getInputStream());
            map = createImgPhoto(bufferedInputStream, imageLink, uploadpath, authToken, uploadIp);
        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }

        return map;
    }



    public AppIOSDto uploadApp(String logoUrl, String appName, Map<AppCardGenerator.PictureElement, String> paramMap, Set<AppCardGenerator.AppDevicePic> appDevicePics, Set<String> screenShotSet, String uploadpath, String basePath, AuthToken authToken, String uploadIp) throws IOException, InterruptedException, IM4JavaException {
        AppIOSDto appIOSDto = new AppIOSDto();

        BufferedInputStream bufferedInputStream = null;
        try {
            AppCardGenerator cardGenerator = new AppCardGenerator();
            String appLogoDir = ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), appName, ResourceFileType.APP);
            String appFileName = ResourceFilePathUtil.getFileName(logoUrl);

            String appLogoFileName = appFileName + "_i";
            URL url = new URL(logoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            bufferedInputStream = new BufferedInputStream(conn.getInputStream());
            String logoSrc = cardGenerator.generatorAppLogo(bufferedInputStream, appLogoDir, appLogoFileName, uploadpath, basePath);
            appIOSDto.setIconSrc(logoSrc);

            String appDir = ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), appName, ResourceFileType.APP);
            String cardSrc = cardGenerator.generatorImage(appDir, appFileName, uploadpath + logoSrc, paramMap, appDevicePics, uploadpath, basePath);

            appIOSDto.setAppCardSrc(cardSrc);

        } finally {
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }

        //process screenShot
        if (!CollectionUtil.isEmpty(screenShotSet)) {
            List<Map<String, String>> screenList = new ArrayList<Map<String, String>>();
            for (String screenShot : screenShotSet) {
                try {
                    Map imageMap = saveImgUrl(screenShot, uploadpath, authToken, uploadIp);
                    screenList.add(imageMap);
                } catch (Exception e) {
                    logger.error("add link map occured ServiceException.", e);
                }
            }
            appIOSDto.setScreenShot(screenList);
        }

        return appIOSDto;
    }



    private void insertResourceFile(String src, String uploadUno, ResourceFileType fileType, String uploadIp) {
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setFileId(ResourceFileIdGenerator.genFileId(src));

        if (StringUtil.isEmpty(resourceFile.getFileId())) {
            GAlerter.lan(this.getClass().getName() + "fileId is empty src: " + src);
            return;
        }

        resourceFile.setOwnUno(uploadUno);
        resourceFile.setResourceFileType(fileType);
        resourceFile.setCreateIp(uploadIp);

        try {
            ContentServiceSngl.get().postResourceFile(resourceFile);
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " insertResourceFile occured Exception.", e);
        }
    }

    public SocialAppAudioDto processAudio(MultipartFile audioFile) throws IOException {
        String uploadPath = getUploadPath();

        String outputFileNamePrefix = ResourceFilePathUtil.getFileName("uuid");
        String outputFilePath = uploadPath + ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), audioFile.getOriginalFilename(), ResourceFileType.AUDIO);


        return saveAudio(audioFile, outputFilePath, outputFileNamePrefix);
    }

    public SocialAppImageDto processImage(MultipartFile imageFile) throws IOException, IM4JavaException, InterruptedException {
        String uploadPath = getUploadPath();

        String outputFileNamePrefix = ResourceFilePathUtil.getFileName("uuid");
        String outputFilePath = uploadPath + ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), imageFile.getOriginalFilename(), ResourceFileType.SOCIAL_APP_PIC);


        return savePic(imageFile, outputFilePath, outputFileNamePrefix);
    }

    private SocialAppImageDto savePic(MultipartFile imageFile, String filePath, String fileNamePrefix) throws IOException, InterruptedException, IM4JavaException {
        if (!FileUtil.isFileOrDirExist(filePath)) {
            try {
                FileUtil.createDirectory(filePath);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        SocialAppImageDto picDto = new SocialAppImageDto();

        String extName = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        String fileRealPath = filePath + fileNamePrefix;
        FileCopyUtils.copy(imageFile.getBytes(), new File(fileRealPath + extName));

        String resizeRealPath = fileRealPath + "_S" + extName;
        ImageGenerator.resizeImage(fileRealPath + extName, 320, 320, 100.00, resizeRealPath);

        String picWebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + fileRealPath.replaceAll(getUploadPath(), "");
        String picsWebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + resizeRealPath.replaceAll(getUploadPath(), "");

        picDto.setPic(picWebPath + extName);
        picDto.setPic_s(picsWebPath);

        return picDto;
    }

    public Map saveMobileFaceFile(MultipartFile multifile, String uploadpath, String uploadIp) throws IOException, ServiceException, InterruptedException, IM4JavaException {
        if (logger.isDebugEnabled()) {
            logger.debug("saveImgFile service impl");
        }

        //根据规则生成的路径及文件名
        String sOutputDir = ResourceFilePathUtil.getFilePath(ImageServerConfig.get().getUploadResouceName(), multifile.getOriginalFilename(), ResourceFileType.HEADICON);
        String sOutputFileName = ResourceFilePathUtil.getFileName(multifile.getOriginalFilename());

        int start = (int) System.currentTimeMillis(); // 开始时间
        Map<String, String> map = HandleFacePic.hadnleFP(multifile.getInputStream(), multifile.getOriginalFilename(), uploadpath, sOutputDir, sOutputFileName);
        int end = (int) System.currentTimeMillis(); // 结束时间
        if (logger.isDebugEnabled()) {
            logger.debug("upload spend：" + (end - start));
        }

        return map;
    }


    public String saveAudioMp3(MultipartFile audio, String outputFileName, String outputFilePath) throws IOException {
        if (!FileUtil.isFileOrDirExist(outputFilePath)) {
            try {
                FileUtil.createDirectory(outputFilePath);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        //save file
        FileUtil.saveFileFromInputStream(audio.getInputStream(), outputFilePath, outputFileName + ".mp3");

        FfmpegUtil.runMp3ToAmrProcess(outputFilePath + outputFileName + ".mp3", outputFilePath + outputFileName + ".amr");

        String filePath = outputFilePath + outputFileName + ".mp3";
        GAlerter.lab("save back Audio mp3 filePath=" + filePath);

        String mp3WebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + filePath.replaceAll(getUploadPath(), "");

        return mp3WebPath;
    }

    public String saveAudioWav(MultipartFile audio, String outputFileName, String outputFilePath) throws IOException {
        if (!FileUtil.isFileOrDirExist(outputFilePath)) {
            try {
                FileUtil.createDirectory(outputFilePath);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        //save file
        FileUtil.saveFileFromInputStream(audio.getInputStream(), outputFilePath, outputFileName + ".wav");

        String filePath = outputFilePath + outputFileName + ".wav";
        GAlerter.lab("save back Audio wav filePath=" + filePath);

        String mp3WebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + filePath.replaceAll(getUploadPath(), "");

        return mp3WebPath;
    }

    private SocialAppAudioDto saveAudio(MultipartFile audioFile, String filePath, String fileNamePrefix) throws IOException {
        if (!FileUtil.isFileOrDirExist(filePath)) {
            try {
                FileUtil.createDirectory(filePath);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        SocialAppAudioDto appAudioDto = new SocialAppAudioDto();


        String fileRealPath = filePath + fileNamePrefix;
        String mpfileRealPath = fileRealPath + ".mp3";
        GAlerter.lab("save Audio fileRealPath=" + fileRealPath + ",mpfileRealPath=" + mpfileRealPath);

        //save file
        FileUtil.saveFileFromInputStream(audioFile.getInputStream(), filePath, fileNamePrefix + ".amr");

        FfmpegUtil.runProcess(fileRealPath + ".amr", mpfileRealPath);

        String amrWebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + fileRealPath.replaceAll(getUploadPath(), "");
        String mapWebPath = "http://" + ImageServerConfig.get().getUploadResouceName() + "." + WebappConfig.get().getDomain() + mpfileRealPath.replaceAll(getUploadPath(), "");

        appAudioDto.setAmr(amrWebPath + ".amr");
        appAudioDto.setMp3(mapWebPath);

        return appAudioDto;
    }


}
