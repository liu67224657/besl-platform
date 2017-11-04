/**
 *
 */
package com.enjoyf.webapps.image.weblogic.file;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.cloudfile.ImageProcessObject;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.ContentServiceException;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.ResourceFilePathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * 文件上传服务
 *
 * @author xinzhao
 */
@Service("uploadWebLogic")
public class UploadWebLogic {
    //
    private static final Logger logger = LoggerFactory.getLogger(UploadWebLogic.class);


    public String uploadImageByUrl(String imageLink, ResourceFileType resourceFileType) throws Exception {
        String sOutputFileName = ResourceFilePathUtil.getFileName(imageLink);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(imageLink);
            conn = (HttpURLConnection) url.openConnection();
            if ((conn.getContentLength() / 1024) > WebappConfig.get().getUploadMaxSize()) {
                throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_OUT_SIZE, "upload image exception" + imageLink);
            }
            BucketInfo bucketInfo = getBucketInfo();
            String extname = conn.getContentType().contains("png") ? ".png" : ".jpg";
            String uploadPath = FileHandlerFactory.getPath(bucketInfo, resourceFileType, sOutputFileName, extname);

            String key = FileHandlerFactory.upload(bucketInfo, uploadPath, conn.getInputStream(), conn.getContentType(), conn.getContentLength());

            return FileHandlerFactory.getUrlByPath(key);
        } catch (Exception e) {
            throw new ServiceException(ContentServiceException.UPLOAD_IMAGE_OPEN_IMAEG, "createImgPhoto upload image exception" + imageLink);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }


    public String processImage(String url, ImageProcessObject object) {
        BucketInfo bucketInfo = getBucketInfo();

        return FileHandlerFactory.process(bucketInfo, url, object);
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

    /**
     * 表单上传调用该方法(截图)
     *
     * @param multifile        多媒体文件
     * @param resourceFileType 文件的类型
     * @param imageCropInfo 截图参数
     * @return 返回文件key
     * @throws Exception
     */
    public String uploadImageCrop(MultipartFile multifile, ResourceFileType resourceFileType,ImageCropInfo imageCropInfo) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("saveImgFile service impl");
        }

        //根据规则生成的路径及文件名
        
        String exname = multifile.getOriginalFilename().substring(multifile.getOriginalFilename().lastIndexOf("."));
        String stuffix=multifile.getOriginalFilename().substring(multifile.getOriginalFilename().lastIndexOf(".")+1);
        BucketInfo bucketInfo = getBucketInfo();
        String url = FileHandlerFactory.getPath(bucketInfo, resourceFileType, multifile.getOriginalFilename(), exname);

        ImageInputStream iis = null;
        InputStream in = null;
        ImageOutputStream ios = null;
        try {
    		iis=ImageIO.createImageInputStream(multifile.getInputStream());
    		Iterator<ImageReader> irs=ImageIO.getImageReaders(iis);
    		ImageReader iReader=irs.next();
    		iReader.setInput(iis, true);
    		ImageReadParam param=iReader.getDefaultReadParam();
    		Rectangle rectangle=new Rectangle(imageCropInfo.getX(),imageCropInfo.getY(),imageCropInfo.getWidth(),imageCropInfo.getHeight());
    		param.setSourceRegion(rectangle);
    		BufferedImage bImage=iReader.read(0,param);
    		bImage.flush();

    		ByteArrayOutputStream bao=new ByteArrayOutputStream();
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
		}

    }
    
    private BucketInfo getBucketInfo() {
        return BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
    }

}
