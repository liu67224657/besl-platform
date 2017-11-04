package com.enjoyf.platform.webapps.common.multimedia.appFetcher;

import com.enjoyf.platform.service.gameres.GameDevice;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.webapps.common.picture.AppCardGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description: app数据获取类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AppFetcher {
    private static Logger logger = LoggerFactory.getLogger(IOSAppEntity.class);

    private static final Pattern PATTERN_LABLE = Pattern.compile("<span class=\"[^\"]*\">[^<]+</span>");
    private static final Pattern PATTERN_SPLITE_LABLE = Pattern.compile("(\\d+(?:\\.\\d)?)[^\\d]*[,|，]\\s*(\\d+|\\.)[^\\d]*");
    private static final Pattern PATTERN_GET_ID = Pattern.compile("http://itunes.apple.com/[^/]*/app/[^/]*/?id([^?]+)");


    private static final String DEVICE_IPAD = "iPad";
    private static final String DEVICE_IPOD = "iPad";
    private static final String DEVICE_IPHONE = "iPhone";

    public static IOSAppEntity fetchAppleByUrl(String url) throws IOException, Exception {
        IOSAppEntity returnAppEntity = null;
        try {
            Document doc = getApiDocument(url);

            Element titleElement = doc.getElementById("title");
            if (titleElement == null) {
                return null;
            }
            String appName = doc.getElementById("title").getElementsByTag("h1").text();

            Elements infoElements = doc.getElementsByAttributeValue("class", "lockup product application");
            if (infoElements == null || infoElements.size() == 0) {
                return null;
            }
            Element infoElement = infoElements.get(0);
            String appLogo = infoElement.getElementsByTag("img").get(0).attr("src");
            Elements infoLi = infoElement.getElementsByTag("ul").get(0).getElementsByTag("li");
            String price = infoLi.get(0).text();
            String appType = infoLi.get(1).getElementsByTag("a").get(0).text();
            String releaseDate = removeInfoLabel(infoLi.get(2).html());
            String vsersion = removeInfoLabel(infoLi.get(3).html());
            String size = removeInfoLabel(infoLi.get(4).html());
            String language = removeInfoLabel(infoLi.get(5).html());
            String provider = removeInfoLabel(infoLi.get(6).html());
            String systemRequired = removeInfoLabel(infoElement.getElementsByTag("p").get(0).html());
            if (StringUtil.isEmpty(systemRequired)) {
                systemRequired = systemRequired.toLowerCase();
            }
            Set<GameDevice> deviceNameSet = getDeviceByRequired(systemRequired);

            Element ratingElement = doc.getElementsByAttributeValue("class", "extra-list customer-ratings").get(0);

            //得到评分
            RatingInfo currentRatingInfo = null;
            RatingInfo allRatingInfo = null;
            if (ratingElement.getElementsByAttributeValue("class", "rating") != null && ratingElement.getElementsByAttributeValue("class", "rating").size() != 0) {
                String currentRatingStr = ratingElement.getElementsByAttributeValue("class", "rating").get(0).attr("aria-label");
                currentRatingInfo = getRatingByStr(currentRatingStr);

                //如果有倆个评分
                if (ratingElement.getElementsByAttributeValue("class", "rating").size() > 1) {
                    String allRatingStr = ratingElement.getElementsByAttributeValue("class", "rating").get(1).attr("aria-label");
                    allRatingInfo = getRatingByStr(allRatingStr);
                }

            }

            Set<String> moreAppSets = new HashSet<String>();
            Elements moreAppInfoElements = doc.getElementsByAttributeValue("class", "extra-list more-by");
            if (moreAppInfoElements != null && moreAppInfoElements.size() > 0) {
                Element moreAppInfo = moreAppInfoElements.get(0);
                Elements moreAppName = moreAppInfo.getElementsByAttributeValue("class", "name");
                moreAppSets = getMoreApp(moreAppName);
            }


            Elements screenShotEle = doc.getElementsByAttributeValue("class", "lockup");
            Set<String> screenShots = getScreenShots(screenShotEle);

            String desc = null;
            Elements descElems = doc.getElementsByAttributeValue("class", "product-review");
            if (descElems != null && descElems.size() > 0) {
                Elements descPTag = descElems.get(0).getElementsByTag("p");
                if (descPTag != null && descPTag.size() > 0) {
                    desc = descPTag.get(0).text();
                    desc = desc.length() > 300 ? desc.substring(0, 300) : desc;
                }
            }

            returnAppEntity = new IOSAppEntity();
            returnAppEntity.setAppid(getAppId(url));
            returnAppEntity.setAppName(appName);
            returnAppEntity.setIcon(appLogo);
            returnAppEntity.setPrice(price);
            returnAppEntity.setAppCategory(appType);
            returnAppEntity.setPublishDate(releaseDate);
            returnAppEntity.setAppVersion(vsersion);
            returnAppEntity.setFileSize(size);
            returnAppEntity.setLanguage(language);
            returnAppEntity.setDevelop(provider);
            returnAppEntity.setDeviceSet(deviceNameSet);

            if (!StringUtil.isEmpty(desc)) {
                returnAppEntity.setDesc(desc);
            }
            if (currentRatingInfo != null) {
                returnAppEntity.setCurrentRating(currentRatingInfo.getRating());
                returnAppEntity.setCurrentRatingCount(currentRatingInfo.getRatingCount());
            }
            if (allRatingInfo != null) {
                returnAppEntity.setTotalRating(allRatingInfo.getRating());
                returnAppEntity.setTotalRatingCount(allRatingInfo.getRatingCount());
            }
            returnAppEntity.setMoreAppSets(moreAppSets);
            returnAppEntity.setScreenShot(screenShots);
            returnAppEntity.setResourceUrl(url);
        } catch (IOException e) {
            logger.error("fetcher app occured IOException:", e);
            throw e;
        } catch (Exception e) {
            logger.error("fetcher app occured Exception:", e);
            throw e;
        }
        return returnAppEntity;
    }

    private static String getAppId(String url) {
        String id = "";
        List<Map<String, String>> fetchData = RegexUtil.fetch(url, PATTERN_GET_ID, null);
        if (!CollectionUtil.isEmpty(fetchData)) {
            id = fetchData.get(0).get("1");
        }
        return id;
    }

    //get api url
    private static Document getApiDocument(String url) throws IOException {
        return Jsoup.connect(url).timeout(6000).post();
    }

    private static String removeInfoLabel(String data) {
        if (StringUtil.isEmpty(data)) {
            return "";
        }

        return RegexUtil.replace(data, PATTERN_LABLE, "", -1);
    }

    private static Set<GameDevice> getDeviceByRequired(String systemRequired) {
        Set<GameDevice> returnSet = new HashSet<GameDevice>();
        systemRequired = systemRequired.toLowerCase();
        if (systemRequired.contains(DEVICE_IPAD.toLowerCase())) {
            GameDevice ipad = GameDevice.getByCode(DEVICE_IPAD);
            if (ipad != null) {
                returnSet.add(ipad);
            }
        }
        if (systemRequired.contains(DEVICE_IPOD.toLowerCase())) {
            GameDevice ipod = GameDevice.getByCode(DEVICE_IPOD);
            if (ipod != null) {
                returnSet.add(ipod);
            }
        }
        if (systemRequired.contains(DEVICE_IPHONE.toLowerCase())) {
            GameDevice iphone = GameDevice.getByCode(DEVICE_IPHONE);
            if (iphone != null) {
                returnSet.add(iphone);
            }
        }
        return returnSet;
    }

    private static RatingInfo getRatingByStr(String ratingStr) {
        RatingInfo ratingInfo = null;
        //3.5星, 27 份评分
        List<Map<String, String>> fetchData = RegexUtil.fetch(ratingStr, PATTERN_SPLITE_LABLE, null);
        if (!CollectionUtil.isEmpty(fetchData)) {
            ratingInfo = new RatingInfo();
            ratingInfo.setRating(Float.parseFloat(fetchData.get(0).get("1")));
            ratingInfo.setRatingCount(Integer.parseInt(fetchData.get(0).get("2")));
        }
        return ratingInfo;
    }

    private static Set<String> getMoreApp(Elements moreElements) {
        Set<String> returnSet = new HashSet<String>();
        for (Element ele : moreElements) {
            returnSet.add(ele.text());
        }
        return returnSet;
    }

    private static Set<String> getScreenShots(Elements screenShotEles) {
        Set<String> screenShot = new HashSet<String>();
        int count = 0;
        for (Element ele : screenShotEles) {
            if (count >= 5) {
                break;
            }
            if (ele.getElementsByTag("img") == null) {
                continue;
            }
            Element imgEle = ele.getElementsByTag("img").get(0);

            screenShot.add(imgEle.attr("src"));
            count++;
        }
        return screenShot;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        IOSAppEntity appEntity = null;
        try {
            appEntity = fetchAppleByUrl("http://itunes.apple.com/cn/app/alices-adventures-rabbit-hole/id331563961?mt=8");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        System.out.println(fetcherAppleByUrl("http://itunes.apple.com/cn/app//id394760598?mt=8"));
        AppCardGenerator cardGenerator = new AppCardGenerator();

        Map<AppCardGenerator.PictureElement, String> appParamMap = new HashMap<AppCardGenerator.PictureElement, String>();

        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_PRICE, new String(appEntity.getPrice().getBytes("UTF-8"), "GB18030"));
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_SIZE, appEntity.getFileSize());
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_RATING, String.valueOf(appEntity.getCurrentRating()));
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_CATEGORY, new String("游戏".getBytes("UTF-8"), "GB18030"));
        String logoUrl = appEntity.getIcon();
        appParamMap.put(AppCardGenerator.PictureElement.IOS_ELEMENT_NAME, new String(appEntity.getAppName().getBytes("UTF-8"), "GB18030"));

        Set<AppCardGenerator.AppDevicePic> devicePicTypes = new HashSet<AppCardGenerator.AppDevicePic>();

        try {
            devicePicTypes.add(AppCardGenerator.AppDevicePic.IPHONE);
            devicePicTypes.add(AppCardGenerator.AppDevicePic.IPAD);
            URL url = new URL(logoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
            String logoSrc = null;
            logoSrc = cardGenerator.generatorAppLogo(bufferedInputStream, "app\\", "applogonew", "d:\\", "D:\\workspace\\ericliu-laptop-joyme\\webapps\\image\\htdocs\\static\\");
            String cardSrc = cardGenerator.generatorImage("app\\", "\\result", "d:\\" + logoSrc, appParamMap, devicePicTypes, "d:\\", "D:\\workspace\\ericliu-laptop-joyme\\webapps\\image\\htdocs\\static\\");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.im4java.core.IM4JavaException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * <p/>
     * Description:评分信息
     * </p>
     *
     * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
     */
    public static class RatingInfo implements Serializable {
        private float rating;
        private int ratingCount;

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public int getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(int ratingCount) {
            this.ratingCount = ratingCount;
        }

        @Override
        public String toString() {
            return "RatingInfo{" +
                    "rating=" + rating +
                    ", ratingCount=" + ratingCount +
                    '}';
        }
    }
}
