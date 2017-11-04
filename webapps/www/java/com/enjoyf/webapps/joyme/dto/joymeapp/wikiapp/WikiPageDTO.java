package com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/20
 * Description:
 */
public class WikiPageDTO {
    private Long wikiPageId;
    private String title;
    private String wikiKey;

    public WikiPageDTO() {
    }

    public Long getWikiPageId() {
        return wikiPageId;
    }

    public void setWikiPageId(Long wikiPageId) {
        this.wikiPageId = wikiPageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWikiKey() {
        return wikiKey;
    }

    public void setWikiKey(String wikiKey) {
        this.wikiKey = wikiKey;
    }

    @Override
    public String toString() {
        return "WikiPageDTO{" +
                "wikiPageId=" + wikiPageId +
                ", title='" + title + '\'' +
                ", wikiKey='" + wikiKey + '\'' +
                '}';
    }
}
