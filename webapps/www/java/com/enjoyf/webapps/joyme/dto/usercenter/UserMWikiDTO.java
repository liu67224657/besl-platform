package com.enjoyf.webapps.joyme.dto.usercenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxu on 2017/1/1.
 */
public class UserMWikiDTO {
    private String count;   //管理的WIKI总数
    private List<MWikiInfoDTO> mWikiInfo = new ArrayList<MWikiInfoDTO>();  //管理的WIKI信息

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<MWikiInfoDTO> getmWikiInfo() {
        return mWikiInfo;
    }

    public void setmWikiInfo(List<MWikiInfoDTO> mWikiInfo) {
        this.mWikiInfo = mWikiInfo;
    }
}
