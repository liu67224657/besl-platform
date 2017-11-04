package com.enjoyf.webapps.joyme.dto.giftmarket;

import com.enjoyf.platform.service.viewline.ViewLineItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-29 下午2:24
 * Description:
 */
public class GiftPageDTO implements Serializable{
    private List<ViewLineItem> headGiftList;
    private String addLogsHtml;

    public List<ViewLineItem> getHeadGiftList() {
        return headGiftList;
    }

    public void setHeadGiftList(List<ViewLineItem> headGiftList) {
        this.headGiftList = headGiftList;
    }

    public String getAddLogsHtml() {
        return addLogsHtml;
    }

    public void setAddLogsHtml(String addLogsHtml) {
        this.addLogsHtml = addLogsHtml;
    }
}
