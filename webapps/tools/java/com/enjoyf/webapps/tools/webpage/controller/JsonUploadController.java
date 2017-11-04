package com.enjoyf.webapps.tools.webpage.controller;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.HttpUpload;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.tools.weblogic.kindeditor.ImageJson;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 文件上传action
 *
 * @author xinzhao
 */

@Controller
@RequestMapping("/json/upload")
public class JsonUploadController extends ToolsBaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;
    
    @RequestMapping(value = "/image")
    @ResponseBody
    public String sendLink(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                           @RequestParam(value = "localurl", required = false) String localurl,
                           HttpServletRequest request) throws FileNotFoundException {

        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + WebUtil.getDomain() + "/json/upload/qiniu";

        JsonBinder jsonBinder = JsonBinder.buildNormalBinder();
        ImageJson imageJson = new ImageJson();
        imageJson.setError(1);

        logger.info("imagelink uploadUrl: " + uploadUrl);

        String tempDir = "/opt/uploads/temp/image/";
        if (!FileUtil.isFileOrDirExist(tempDir)) {
            boolean bVal = FileUtil.createDirectory(tempDir);
            if (bVal) {
                System.out.println("not create temp dir");
            }
        }
        String fileName = tempDir + System.currentTimeMillis() + Filedata.getOriginalFilename().substring(Filedata.getOriginalFilename().lastIndexOf("."), Filedata.getOriginalFilename().length());

        try {
            FileUtil.createFile(fileName, Filedata.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return jsonBinder.toJson(imageJson);
        }

        try {
            JoymeResultMsg resultSet = jsonBinder.fromJson(new HttpUpload().postMultipart(uploadUrl, new PostParameter[]{
                    new PostParameter("Filedata", new File(fileName)),
                    new PostParameter("filetype", "original"),
                    new PostParameter("token", "joymeplatform")}, "joymeplatform"), JoymeResultMsg.class);

            System.out.println(resultSet);

            if (resultSet.getStatus_code().equals(JoymeResultMsg.CODE_S)) {
                imageJson.setError(0);
                imageJson.setUrl(resultSet.getResult().get(0) + "");
            } else {
                imageJson.setError(1);
            }

            return jsonBinder.toJson(imageJson);
        } finally {
            FileUtil.deleteFileOrDir(fileName);
        }
    }

    @RequestMapping(value = "/video")
    @ResponseBody
    public String qiniu(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                        @RequestParam(value = "filetype", required = false, defaultValue = "video") String fileType,
                        HttpServletRequest request, HttpServletResponse response) throws IOException {

        //用户上传根路径
        String uploadPath = getUploadPath();

        //需要判断用户上传数量
        JoymeResultMsg reMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E, i18nSource.getMessage("upload.image.failed", null, Locale.CHINA));


        ResourceFileType resourceFileType = ResourceFileType.getByCode(fileType);

        if (resourceFileType == null) {
            reMsg.setMsg("filetype_error");
            return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNonNullBinder().toJson(reMsg);
        }


        try {
            String video = uploadImage(Filedata, resourceFileType);
            reMsg.setStatus_code(JoymeResultMsg.CODE_S);
            reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
            //返回结果的list
            List<String> list = new ArrayList<String>();
            list.add(video);
            reMsg.setResult(list);
        } catch (Exception e) {
            GAlerter.lab(e.getMessage(), e);
        }

        return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNonNullBinder().toJson(reMsg);
    }

    /**
     * 表单上传调用该方法
     *
     * @param multifile        多媒体文件
     * @param resourceFileType 文件的类型
     * @return 返回文件key
     * @throws Exception
     */
    public String uploadImage(MultipartFile multifile, ResourceFileType resourceFileType) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("saveImgFile service impl");
        }

        //根据规则生成的路径及文件名
        String exname = multifile.getOriginalFilename().substring(multifile.getOriginalFilename().lastIndexOf("."));
        BucketInfo bucketInfo = getBucketInfo();
        String url = FileHandlerFactory.getPath(bucketInfo, resourceFileType, multifile.getOriginalFilename(), exname);

        String key = FileHandlerFactory.upload(bucketInfo, url, multifile.getInputStream(), multifile.getContentType(), multifile.getSize());
        return FileHandlerFactory.getUrlByPath(key);
    }

    private BucketInfo getBucketInfo() {
        return BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
    }

    //@ResponseBody表示该方法返回的结果直接输入到ResponseBody中去
    @ResponseBody
    @RequestMapping("/imgcropper")
    public String imgCropper
    (@RequestParam(value = "filename", required = true) String filename,
     @RequestParam(value = "x1", required = true, defaultValue = "0.0") double x1,
     @RequestParam(value = "y1", required = true, defaultValue = "0.0") double y1,
     @RequestParam(value = "w", required = true, defaultValue = "0.0") double w,
     @RequestParam(value = "h", required = true, defaultValue = "0.0") double h,
     @RequestParam(value = "at", required = true) String at,
     @RequestParam(value = "rw", required = false) String rw,
     @RequestParam(value = "rh", required = false) String rh,
     @RequestParam(value = "rtype", required = false, defaultValue = "image") String rtype,
     HttpServletRequest request, HttpServletResponse response) throws IOException {

        //需要判断用户上传数量

        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + WebUtil.getDomain() + "/json/upload/thumbnail";

        int x1Int = (int) Math.round(x1);
        int y1Int = (int) Math.round(y1);
        int wInt = (int) Math.round(w);
        int hInt = (int) Math.round(h);
        if (StringUtil.isEmpty(rw)) {
            rw = wInt + "";
        }
        if (StringUtil.isEmpty(rh)) {
            rh = hInt + "";
        }

        PostParameter[] postParameters = new PostParameter[]{
                new PostParameter("url", uploadUrl),
                new PostParameter(CookieUtil.ACCESS_TOKEN, at),
                new PostParameter("x1", x1Int),
                new PostParameter("y1", y1Int),
                new PostParameter("w", wInt),
                new PostParameter("h", hInt),
                new PostParameter("y2", 0),
                new PostParameter("x2", 0),
                new PostParameter("filename", filename),
                new PostParameter("rtype", rtype)
        };
        String result = new HttpUpload().post(uploadUrl, postParameters, "joymeplatform");

        return result;
    }


    /**
     * 描述 : 上传截图. <br>
     * <p/>
     *
     * @param Filedata
     * @return
     * @throws java.io.IOException
     */
    
    @ResponseBody
    @RequestMapping(value = "/crop",method=RequestMethod.POST)
    public String qiniuCrop(@RequestParam(value = "Filedata", required = false) MultipartFile Filedata,
                            @RequestParam(value = "filetype", required = false, defaultValue = "original") String fileType,
                            @RequestParam(value = "x", required = false) int x,
                            @RequestParam(value = "y", required = false) int y,
                            @RequestParam(value = "width", required = false) int width,
                            @RequestParam(value = "height", required = false) int height,
                            @RequestParam(value = "w", required = false)int w) throws IOException {

    	
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
        	String filename=uploadImageCrop(Filedata, resourceFileType,x,y,width,height,w);
            reMsg.setStatus_code(JoymeResultMsg.CODE_S);
            reMsg.setMsg(i18nSource.getMessage("upload.image.success", null, Locale.CHINA));
            //返回结果的list
            List<String> list = new ArrayList<String>();
            list.add(filename);
            reMsg.setResult(list);
        } catch (Exception e) {
            GAlerter.lab(e.getMessage(), e);
        }

        return JsonBinder.buildNonNullBinder().toJson(reMsg);
    }
    
    /**
     * 表单上传调用该方法(裁剪图片)
     *
     * @param multifile        多媒体文件
     * @param resourceFileType 文件的类型
     * @return 返回文件key
     * @throws Exception
     */
    public String uploadImageCrop(MultipartFile multifile, ResourceFileType resourceFileType,int x, int y, int width,int height,int w) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("saveImgFile service impl");
        }
        //根据规则生成的路径及文件名
        
        String exname = multifile.getOriginalFilename().substring(multifile.getOriginalFilename().lastIndexOf("."));
        String stuffix=multifile.getOriginalFilename().substring(multifile.getOriginalFilename().lastIndexOf(".")+1);
        BucketInfo bucketInfo = getBucketInfo();
        String url = FileHandlerFactory.getPath(bucketInfo, resourceFileType, multifile.getOriginalFilename(), exname);

        BufferedImage bufferedImage=ImageIO.read(multifile.getInputStream());
        x=x*bufferedImage.getWidth()/w;
        y=y*bufferedImage.getWidth()/w;
        width=width*bufferedImage.getWidth()/w;
        height=height*bufferedImage.getWidth()/w;
        System.out.println("x:"+x+",y:"+y+",w:"+width+",h:"+height+",w:"+w);
        ImageInputStream iis = null;
        InputStream in = null;
        ImageOutputStream ios = null;
        ByteArrayOutputStream bao= null;
        try {
    		iis=ImageIO.createImageInputStream(multifile.getInputStream());
    		Iterator<ImageReader> irs=ImageIO.getImageReaders(iis);
    		ImageReader iReader=irs.next();
    		iReader.setInput(iis, true);
    		ImageReadParam param=iReader.getDefaultReadParam();
    		Rectangle rectangle=new Rectangle(x,y,width,height);
    		param.setSourceRegion(rectangle);
    		BufferedImage bImage=iReader.read(0,param);
    		bImage.flush();

    		bao = new ByteArrayOutputStream();
    		ios = ImageIO.createImageOutputStream(bao);
    		ImageIO.write(bImage,stuffix,ios);
    		
    		in = new ByteArrayInputStream(bao.toByteArray());
    		
            String key = FileHandlerFactory.upload(bucketInfo, url, in, multifile.getContentType(), multifile.getSize());
            
            return FileHandlerFactory.getUrlByPath(key);
		} catch (Exception e) {
			return null;
		} finally {
			if (null != iis) {
				iis.close();
			}
			if (null != in) {
				in.close();
			}
			if (null != ios) {
				ios.close();
			}
			if (null != bao) {
				bao.close();
			}
		}
    }
}
