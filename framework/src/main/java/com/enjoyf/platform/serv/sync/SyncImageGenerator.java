package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.props.SyncTextConfig;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.image.*;
import com.enjoyf.platform.util.log.GAlerter;
import org.apache.commons.io.FileUtils;
import org.im4java.core.IM4JavaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
class SyncImageGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SyncLogic.class);

    private static final String FONT_COLOR = "#555";
    private static final int FONT_LINEHEIGHT = 10;
    private static final int ELEMENT_X_AXIAS = 12;
    private static final ImageBorder IMAGE_BORDER = new ImageBorder(1, 1, "#bdbdbd");

    private static final int FORWARDTITLE_FONTSIZE = 12;
    private static final String FORWARDTITLE_FONTCOLOR = "#494949";

    private static final int SUBJECT_FONTSIZE = 22;
    private static final int SUBJECT_LINE_SIZE = 36;

    private static final int CONTENT_LINE_SIZE = 30;
    private static final int CONTENT_FONTSIZE = 14;


    private static final int APP_IMAGEHIEGHT = 245;

    private static final int THUMBIMG_WIDTH = 412;

    private static final int IMAGE_WIDTH = 440;

    private static final int MARGIN_TOP = 16;

    private static final double COPY_IMAGE_QUALITY = 87.00;
    private static final double GENERATOR_IMAGE_QUALITY = 100.00;

    public String generatorTempImage(String imgUrl, String tempImageDir, String contentId) {
        String outPutPath = "";
        URL url = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            url = new URL(imgUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bufferedInputStream = new BufferedInputStream(conn.getInputStream());

            outPutPath = ImageGenerator.copyImage(bufferedInputStream, COPY_IMAGE_QUALITY, tempImageDir + contentId + DateUtil.formatDateToString(new Date(), "yyyyMMdd") + ".jpg");
        } catch (MalformedURLException e) {
            GAlerter.lab(getClass().getName() + " generator forward generatorTempImage Image occured MalformedURLException.e:", e);
        } catch (IOException e) {
            GAlerter.lab(getClass().getName() + " generator forward generatorTempImage Image occured IOException.e:", e);
        } catch (IM4JavaException e) {
            GAlerter.lab(getClass().getName() + " generator forward generatorTempImage Image occured IM4JavaException.e:", e);
        } catch (InterruptedException e) {
            GAlerter.lab(getClass().getName() + " generator forward generatorTempImage Image occured InterruptedException.e:", e);
        } finally {
            StreamUtil.closeInputStream(bufferedInputStream);
        }
        return outPutPath;
    }


    public String generatorRelationImage(Content content, String tempImageDir, String SubjectTtf, String forwardTitleTtf) {
        String outputPath = "";
        List<String> removeImageList = null;
        try {

            //处理文章内容
            ResolveContent resolveContent = ResolveContent.transferByContent(content);
            resolveContent = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_SYNC_RELATION_CONTENT).process(resolveContent);

            //add subject
            int lineHeight = 0;
            int imageHeight = 0;
            List<FontElement> fontElementList = new ArrayList<FontElement>();

            lineHeight += MARGIN_TOP;
            try {
                lineHeight += FORWARDTITLE_FONTSIZE;
                Profile profile = ProfileServiceSngl.get().getProfileByUno(content.getUno());
                FontElement titleElement = new FontElement();
                titleElement.setText(SyncTextConfig.get().getSyncForwardTitleKey() + profile.getBlog().getScreenName());
                titleElement.setTtfPath(forwardTitleTtf);
                titleElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
                titleElement.setFontSize(FORWARDTITLE_FONTSIZE);
                titleElement.setxAxis(ELEMENT_X_AXIAS);
                titleElement.setyAxis(lineHeight);
                titleElement.setFontColor(FORWARDTITLE_FONTCOLOR);
                fontElementList.add(titleElement);
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " generatorRelationImage generator oauth forward cotnent image. occured ServiceException", e);
            }

            if (!StringUtil.isEmpty(content.getSubject())) {
                //标题
                lineHeight += MARGIN_TOP;
                List<String> subjectList = getSyncSubjectStrings(content.getSubject());
                for (String subject : subjectList) {
                    lineHeight += SUBJECT_FONTSIZE;
                    FontElement fontElement = new FontElement();
                    fontElement.setText(subject);
                    fontElement.setTtfPath(SubjectTtf);
                    fontElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
                    fontElement.setFontSize(SUBJECT_FONTSIZE);
                    fontElement.setxAxis(ELEMENT_X_AXIAS);
                    fontElement.setyAxis(lineHeight);
                    fontElement.setFontColor(FONT_COLOR);
                    fontElementList.add(fontElement);
                    lineHeight += FONT_LINEHEIGHT;
                }
            }
            //内容
            List<String> contentList = getSyncContentStrings(resolveContent.getContent());
            if (!CollectionUtil.isEmpty(contentList)) {
                lineHeight += MARGIN_TOP;
                for (int lineNum = 0; lineNum < contentList.size(); lineNum++) {
                    lineHeight += CONTENT_FONTSIZE;
                    FontElement fontElement = new FontElement();
                    fontElement.setText(contentList.get(lineNum));
                    fontElement.setTtfPath(SubjectTtf);
                    fontElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
                    fontElement.setFontSize(CONTENT_FONTSIZE);
                    fontElement.setxAxis(ELEMENT_X_AXIAS);
                    fontElement.setyAxis(lineHeight);
                    fontElement.setFontColor(FONT_COLOR);
                    fontElementList.add(fontElement);
                    lineHeight += FONT_LINEHEIGHT;
                }
            }

            imageHeight = lineHeight + MARGIN_TOP;
            String tempPathPrefix = tempImageDir + content.getContentId() + DateUtil.formatDateToString(new Date(), "yyyyMMdd");

            List<ImageElement> imageElementList = new ArrayList<ImageElement>();
            removeImageList = new ArrayList<String>();
            if (!CollectionUtil.isEmpty(resolveContent.getApps())) {
                lineHeight += MARGIN_TOP;

                String tempPath = tempPathPrefix + "_APP.jpg";
                try {
                    tempPath = ImageGenerator.addBorder(IMAGE_BORDER, ImageResolveUtil.genImageByTemplate(resolveContent.getApps().get(0).getAppSrc(), null), GENERATOR_IMAGE_QUALITY, tempPath);
                    removeImageList.add(tempPath);

                    ImageElement imageElement = new ImageElement();
                    imageElement.setImgSrc(tempPath);
                    imageElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
                    imageElement.setxAxis(ELEMENT_X_AXIAS);
                    imageElement.setyAxis(lineHeight);
                    imageElementList.add(imageElement);
                    imageHeight += APP_IMAGEHIEGHT + MARGIN_TOP;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (!CollectionUtil.isEmpty(resolveContent.getImages())) {
                int imageIdx = 0;
                lineHeight += MARGIN_TOP;
                while (imageIdx < 3 && imageIdx <= resolveContent.getImages().size() - 1) {

                    String imageSrc = ImageResolveUtil.genImageByTemplate(resolveContent.getImages().get(imageIdx).getM(), ImageSize.IMAGE_SIZE_M);
                    imageIdx++;

                    Image image = null;
                    URL url = new URL(imageSrc);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                    try {
                        image = ImageIO.read(bufferedInputStream);
                    } catch (IOException e) {
                        logger.error("generatorRelationImage.ImageIO.read occued Exception,e:" + tempImageDir);
                        continue;
                    } finally {
                        StreamUtil.closeInputStream(bufferedInputStream);
                    }

                    String tempPath = tempPathPrefix + "_" + imageIdx + ".jpg";
                    ThumbImageElement tempElement = genSyncTempImage(image, imageSrc, tempPath);

                    //某一张图片为空
                    if (tempElement == null) {
                        logger.error("generator image is empty.tempImageDir is:" + tempImageDir);
                        continue;
                    }
                    removeImageList.add(tempElement.getSrc());

                    ImageElement imageElement = new ImageElement();
                    imageElement.setImgSrc(tempElement.getSrc());
                    imageElement.setLocation(ElementLocation.LOCATION_NORTHWEST);
                    imageElement.setxAxis(ELEMENT_X_AXIAS);
                    imageElement.setyAxis(lineHeight);
                    imageElement.setImageBoder(IMAGE_BORDER);
                    imageElementList.add(imageElement);

                    lineHeight += tempElement.getHeight();
                    lineHeight += FONT_LINEHEIGHT;

                }
                imageHeight = lineHeight;
            }

            //输出
            outputPath = tempPathPrefix + ".jpg";
            if (logger.isDebugEnabled()) {
                logger.debug("generator forward realtionContent Image.outputPath:" + outputPath);
            }
            outputPath = ImageGenerator.generatImages(IMAGE_WIDTH, imageHeight, "#FFF", outputPath);
            outputPath = ImageGenerator.addBorder(IMAGE_BORDER, outputPath, GENERATOR_IMAGE_QUALITY, outputPath);
            outputPath = ImageGenerator.printImages(fontElementList, outputPath, GENERATOR_IMAGE_QUALITY, outputPath);
            for (ImageElement imageElement : imageElementList) {
                ImageGenerator.compositeImage(imageElement, outputPath, GENERATOR_IMAGE_QUALITY, outputPath);
            }

        } catch (IOException e) {
            GAlerter.lab(getClass().getName() + " generator forward realtionContent Image occured IOException.e:", e);
            outputPath = "";
        } catch (IM4JavaException e) {
            GAlerter.lab(getClass().getName() + " generator forward realtionContent Image occured IM4JavaException.e:", e);
            outputPath = "";
        } catch (InterruptedException e) {
            GAlerter.lab(getClass().getName() + " generator forward realtionContent Image occured InterruptedException.e:", e);
            outputPath = "";
        }

        //删除
        for (String tempImg : removeImageList) {
            try {
                FileUtils.forceDelete(new File(tempImg));
            } catch (Exception e) {
                GAlerter.lab("delete tempImg failed.img is:" + tempImg + " Exception: ", e);
            }
        }

        return outputPath;
    }

    private List<String> getSyncSubjectStrings(String subject) {
        List<String> list = new ArrayList<String>();

        String contentSubject = subject.length() > SUBJECT_LINE_SIZE ? subject.substring(0, SUBJECT_LINE_SIZE - 1) + "…" : subject;
        if (StringUtil.isEmpty(contentSubject)) {
            return list;
        }

        int preLineSize = SUBJECT_LINE_SIZE / 2;
        if (contentSubject.length() > preLineSize) {
            list.add(contentSubject.substring(0, preLineSize));
            list.add(contentSubject.substring(preLineSize));
        } else {
            list.add(contentSubject);
        }

        return list;
    }

    private List<String> getSyncContentStrings(String contentText) {
        List<String> contentList = new ArrayList<String>();
        for (int i = 0; i < contentText.length(); i += CONTENT_LINE_SIZE) {
            int endIdx = i + CONTENT_LINE_SIZE;
            endIdx = endIdx < contentText.length() ? endIdx : contentText.length();
            String segment = contentText.substring(i, endIdx);
            contentList.add(segment);
        }
        return contentList;
    }

    private ThumbImageElement genSyncTempImage(Image image, String src, String outPutPath) {
        String tempSrc = "";

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (image.getWidth(null) > THUMBIMG_WIDTH) {
            height = THUMBIMG_WIDTH * height / width;
            width = THUMBIMG_WIDTH;
        }
        try {
            tempSrc = ImageGenerator.resizeImage(src, width, height, COPY_IMAGE_QUALITY, outPutPath);
            tempSrc = ImageGenerator.addBorder(IMAGE_BORDER, tempSrc, COPY_IMAGE_QUALITY, outPutPath);
        } catch (IOException e) {
            GAlerter.lab(getClass().getName() + "genSyncTempImage occured IOException.e:", e);
            tempSrc = "";
        } catch (IM4JavaException e) {
            GAlerter.lab(getClass().getName() + "genSyncTempImage occured IOException.e:", e);
            tempSrc = "";
        } catch (InterruptedException e) {
            GAlerter.lab(getClass().getName() + "genSyncTempImage occured IOException.e:", e);
            tempSrc = "";
        }
        if (StringUtil.isEmpty(tempSrc)) {
            return null;
        }
        return new ThumbImageElement(width, height, tempSrc);
    }

    private class ThumbImageElement {
        private int width;
        private int height;
        private String src;

        private ThumbImageElement(int width, int height, String src) {
            this.width = width;
            this.height = height;
            this.src = src;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getSrc() {
            return src;
        }

    }


    public static void main(String[] args) {
        try {
            Content content = ContentServiceSngl.get().getContentById("0MMdV0pJh01rCZQyeQ_Qu6");

            new SyncImageGenerator().generatorRelationImage(content, "d:\\app\\", "d:\\app\\msyh.ttf", "d:\\app\\msyh.ttf");

            System.out.println(new SyncContextProcessor().processor(content, ""));
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
