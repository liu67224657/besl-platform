/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.tools.contenttrans.OldContentHandler;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.PrivacyType;
import com.enjoyf.platform.service.content.AudioContent;
import com.enjoyf.platform.service.content.AudioContentSet;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.content.VideoContent;
import com.enjoyf.platform.service.content.VideoContentSet;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.Utility;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-30 上午11:38
 * Description: 转移旧的博文。
 */
public class ContentTransfer {
    private static final Logger logger = LoggerFactory.getLogger(ContentTransfer.class);

    private static final String REGEX_IMAGE = "<img\\s+src=\"([^>]+)\"\\s*+/>";
    private static final Pattern REGEX_PATTERN_IMAGE = Pattern.compile(REGEX_IMAGE);

    public static void main(String[] args) {
        //param, the table name, the src column, the dest column.
        FiveProps servProps = Props.instance().getServProps();

        String oldDataSourceName = args[0];
        String newDataSourceName = args[1];

        Pagination page = new Pagination(1000, 1, 100);

        try {
            OldContentHandler oldHandler = new OldContentHandler(oldDataSourceName, servProps);
            ContentHandler newHandler = new ContentHandler(newDataSourceName, servProps);

            PageRows<OldContent> pageRows;

            do {
                pageRows = oldHandler.query(page);

                //
                for (OldContent oldC : pageRows.getRows()) {
                    System.out.println("SSSSSSSSSSSSS" + oldC);

                    //todo, do the transfer.
                    Content newC = new Content();

                    copy(oldC, newC);

                    newC = newHandler.insertContent(newC);

                    System.out.println("RRRRRRRRRRRRRR" + newC);
                }

                Utility.sleep(2000);
                //
                if (page.isLastPage()) {
                    break;
                } else {
                    page.setCurPage(page.getCurPage() + 1);
                }
            } while (true);
        } catch (DbException e) {
            e.printStackTrace();
        }

        System.out.println("XXXXXXXXXXXXXXXXX" + page);
    }

    private static void copy(OldContent oldC, Content newC) {

        newC.setContentId(String.valueOf(oldC.getBlogid()));

        newC.setSubject(oldC.getBlogsubject());
        newC.setContent(oldC.getContent());
        newC.setContentTag(ContentTag.parse(oldC.getTag()));
        newC.setThumbImgLink(oldC.getThumbimglink());

        newC.setRootContentId(String.valueOf(oldC.getOrginblogid()));
        newC.setRootContentUno(oldC.getOrginuno());

        newC.setUno(oldC.getUno());

        if ("C".equalsIgnoreCase(oldC.getBlogrange())) {
            newC.setPrivacy(PrivacyType.FRIENDS);
        } else if ("N".equalsIgnoreCase(oldC.getBlogrange())) {
            newC.setPrivacy(PrivacyType.PRIVATE);
        } else {
            newC.setPrivacy(PrivacyType.PUBLIC);
        }

        newC.setFavorTimes(oldC.getLovenum());
        newC.setForwardTimes(oldC.getReblognum());
        newC.setReplyTimes(oldC.getFeedbacknum());

        newC.setPublishDate(oldC.getPublishdate());

        if ("T".equalsIgnoreCase(oldC.getIsreblog())) {
            newC.setContentType(ContentType.getByValue(1));
            newC.setPublishType(ContentPublishType.FORWARD);
        } else {
            if (oldC.getBlogtype().equalsIgnoreCase("chat")) {
                newC.setContentType(ContentType.getByValue(1));

            } else if (oldC.getBlogtype().equalsIgnoreCase("text")) {
                convertText(oldC, newC);

                if (newC.getImages() == null) {
                    newC.setContentType(ContentType.getByValue(16));
                } else {
                    newC.setContentType(ContentType.getByValue(18));
                }

            } else if (oldC.getBlogtype().equalsIgnoreCase("photo")) {
                newC.setContentType(ContentType.getByValue(3));

                convertImages(oldC, newC);
            } else if (oldC.getBlogtype().equalsIgnoreCase("audio")) {
                newC.setContentType(ContentType.getByValue(5));

                //
                AudioContent ac = new AudioContent();
                ac.setDesc(oldC.getContent());
                ac.setTitle(oldC.getBlogsubject());
                ac.setFlashUrl(oldC.getUrllink());

                if (!Strings.isNullOrEmpty(oldC.getThumbimglink())) {
                    ac.setUrl(getTypeImageUrl(oldC.getThumbimglink(), ""));
                    ac.setSs(getTypeImageUrl(oldC.getThumbimglink(), "_1"));
                    ac.setS(getTypeImageUrl(oldC.getThumbimglink(), "_3"));
                    ac.setM(getTypeImageUrl(oldC.getThumbimglink(), "_2"));
                }
                //
                newC.setAudios(new AudioContentSet());
                newC.getAudios().add(ac);

                newC.setThumbImgLink(ac.getM());

            } else if (oldC.getBlogtype().equalsIgnoreCase("video")) {
                newC.setContentType(ContentType.getByValue(9));

                //
                VideoContent vc = new VideoContent();
                vc.setDesc(oldC.getContent());
                vc.setTitle(oldC.getBlogsubject());

                vc.setFlashUrl(oldC.getUrllink());
                vc.setUrl(oldC.getThumbimglink());

                //
                newC.setVideos(new VideoContentSet());
                newC.getVideos().add(vc);

                newC.setThumbImgLink(vc.getM());
            }
        }
    }

    private static void convertImages(OldContent oldC, Content newC) {
        OldImageContentSet oldSet = OldImageContentSet.parse(oldC.getUrllink());

        if (oldSet.getPhoto().size() > 0) {
            ImageContentSet newSet = new ImageContentSet();
            String thumbUrl = null;

            int i = 0;
            for (OldImageContent oldIc : oldSet.getPhoto()) {
                ImageContent newIc = new ImageContent();

                newIc.setId(i);
                newIc.setDesc(oldIc.getDesc());
                newIc.setM(oldIc.getM());
                newIc.setS(oldIc.getS());
                newIc.setSs(oldIc.getSs());
                newIc.setUrl(oldIc.getB());

                newSet.add(newIc);

                if (thumbUrl == null) {
                    thumbUrl = Strings.isNullOrEmpty(oldIc.getSs()) ? oldIc.getM() : oldIc.getSs();
                }
                i++;
            }

            newC.setThumbImgLink(thumbUrl);
            newC.setImages(newSet);
        }
    }

    private static void convertText(OldContent oldC, Content newC) {
        if (Strings.isNullOrEmpty(oldC.getContent())) {
            return;
        }

        Matcher m = REGEX_PATTERN_IMAGE.matcher(oldC.getContent());

        //
        int idx = 0;
        Map<Integer, ImageContent> imageMap = new LinkedHashMap<Integer, ImageContent>();

        //
        while (m.find()) {
            String imageTag = m.group();
            String url = m.group(1);

            if (url.startsWith("http://r001.joyme.dev")) {
                newC.setContent(newC.getContent().replace(imageTag, "[image:" + idx + "]"));
                url = url.replace("http://r001.joyme.dev", "/r001");

                ImageContent imageContent = new ImageContent();

                imageContent.setId(idx);
                imageContent.setDesc("");
                imageContent.setUrl(getTypeImageUrl(url, ""));
                imageContent.setM(getTypeImageUrl(url, "_M"));
                imageContent.setS(getTypeImageUrl(url, "_S"));
                imageContent.setSs(getTypeImageUrl(url, "_SS"));

                imageMap.put(idx, imageContent);
                idx++;
            }
        }

        if (imageMap.size() > 0) {
            newC.setThumbImgLink(imageMap.get(0).getSs());
            newC.setImages(new ImageContentSet());

            for (ImageContent ic : imageMap.values()) {
                newC.getImages().add(ic);
            }
        }
    }

    private static String getTypeImageUrl(String url, String typeKey) {
        String returnValue = null;

        int dotIdx = url.lastIndexOf(".");
        int dashIdx = url.lastIndexOf("_");

        String fileSuffix = null;
        if (dotIdx > 0) {
            fileSuffix = url.substring(dotIdx);
        }

        String fileOrigPrefix = null;
        if (dashIdx > 0) {
            fileOrigPrefix = url.substring(0, dashIdx);
        } else {
            fileOrigPrefix = url.substring(0, dotIdx);
        }

        returnValue = fileOrigPrefix + typeKey + fileSuffix;

        return returnValue;
    }
}
