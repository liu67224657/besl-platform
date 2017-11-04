package com.enjoyf.platform.webapps.common.dto.index;

import com.enjoyf.platform.service.viewline.ViewLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexPageDTO {
    //缓存首页页面
    private String gameHtml;  //找游戏
    private String assessment;//游戏评测
    private String handbook;//最新攻略
    private String hotNews;
    private String indexTopic;
    private String group;
    private String recommend;
    private String wiki;
    private String progress;  //着迷在进步

    private String wikiModule;

    //缓存数据
    private List<ViewLineItemElementDTO> talentProfileList;
    private List<ViewLineItemElementDTO> famousList;
    private Map<Integer, String> findGameMap = new HashMap<Integer, String>();

    private ViewLine recommendLine;
    private List<ViewLineItemElementDTO> recommendList;

    private ViewLine hotLine;
    private List<ViewLineItemElementDTO> hotList;

    private List<ViewLineItemElementDTO> wikiList;

    private ViewLine assessmentLine;
    private List<ViewLineItemElementDTO> assessmentList;

    private ViewLine handbookLine;
    private List<ViewLineItemElementDTO> handbookList;

    private ViewLine topicLine;
    private List<ViewLineItemElementDTO> topicList;

    public Map<Integer, String> getFindGameMap() {
        return findGameMap;
    }

    public void setFindGameMap(Map<Integer, String> findGameMap) {
        this.findGameMap = findGameMap;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getHandbook() {
        return handbook;
    }

    public void setHandbook(String handbook) {
        this.handbook = handbook;
    }

    public String getIndexTopic() {
        return indexTopic;
    }

    public void setIndexTopic(String indexTopic) {
        this.indexTopic = indexTopic;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getWikiModule() {
        return wikiModule;
    }

    public void setWikiModule(String wikiModule) {
        this.wikiModule = wikiModule;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }


    public String getHotNews() {
        return hotNews;
    }

    public void setHotNews(String hotNews) {
        this.hotNews = hotNews;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public List<ViewLineItemElementDTO> getTalentProfileList() {
        return talentProfileList;
    }

    public void setTalentProfileList(List<ViewLineItemElementDTO> talentProfileList) {
        this.talentProfileList = talentProfileList;
    }

    public List<ViewLineItemElementDTO> getFamousList() {
        return famousList;
    }

    public void setFamousList(List<ViewLineItemElementDTO> famousList) {
        this.famousList = famousList;
    }

    public String getGameHtml() {
        return gameHtml;
    }

    public void setGameHtml(String gameHtml) {
        this.gameHtml = gameHtml;
    }

    public List<ViewLineItemElementDTO> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<ViewLineItemElementDTO> recommendList) {
        this.recommendList = recommendList;
    }

    public List<ViewLineItemElementDTO> getHotList() {
        return hotList;
    }

    public void setHotList(List<ViewLineItemElementDTO> hotList) {
        this.hotList = hotList;
    }

    public List<ViewLineItemElementDTO> getWikiList() {
        return wikiList;
    }

    public void setWikiList(List<ViewLineItemElementDTO> wikiList) {
        this.wikiList = wikiList;
    }

    public List<ViewLineItemElementDTO> getAssessmentList() {
        return assessmentList;
    }

    public void setAssessmentList(List<ViewLineItemElementDTO> assessmentList) {
        this.assessmentList = assessmentList;
    }

    public ViewLine getRecommendLine() {
        return recommendLine;
    }

    public void setRecommendLine(ViewLine recommendLine) {
        this.recommendLine = recommendLine;
    }

    public List<ViewLineItemElementDTO> getHandbookList() {
        return handbookList;
    }

    public void setHandbookList(List<ViewLineItemElementDTO> handbookList) {
        this.handbookList = handbookList;
    }

    public ViewLine getHotLine() {
        return hotLine;
    }

    public void setHotLine(ViewLine hotLine) {
        this.hotLine = hotLine;
    }

    public ViewLine getAssessmentLine() {
        return assessmentLine;
    }

    public void setAssessmentLine(ViewLine assessmentLine) {
        this.assessmentLine = assessmentLine;
    }

    public ViewLine getHandbookLine() {
        return handbookLine;
    }

    public void setHandbookLine(ViewLine handbookLine) {
        this.handbookLine = handbookLine;
    }

    public ViewLine getTopicLine() {
        return topicLine;
    }

    public void setTopicLine(ViewLine topicLine) {
        this.topicLine = topicLine;
    }

    public List<ViewLineItemElementDTO> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<ViewLineItemElementDTO> topicList) {
        this.topicList = topicList;
    }
}
