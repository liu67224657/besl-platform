package com.enjoyf.platform.webapps.common.dto.content;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ContentPageDTO {
    //缓存首页页面
    private String hotNewsHtml;  //找游戏

    private String recommendHtml;

    public String getHotNewsHtml() {
        return hotNewsHtml;
    }

    public void setHotNewsHtml(String hotNewsHtml) {
        this.hotNewsHtml = hotNewsHtml;
    }

    public String getRecommendHtml() {
        return recommendHtml;
    }

    public void setRecommendHtml(String recommendHtml) {
        this.recommendHtml = recommendHtml;
    }
}
