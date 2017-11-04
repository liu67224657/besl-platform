package com.enjoyf.platform.service.joymeapp.anime;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2016/9/26 0026.
 */
public class WanbaActivity implements Serializable {
    private String pic; //头图

    private String corner;//角标文本

    private String askwho;//向谁提问


    private List<String> guestList;//嘉宾,用逗号分隔

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAskwho() {
        return askwho;
    }

    public void setAskwho(String askwho) {
        this.askwho = askwho;
    }

    public List<String> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<String> guestList) {
        this.guestList = guestList;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public static WanbaActivity toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, WanbaActivity.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static void main(String[] args) {
        WanbaActivity wanbaActivity = new WanbaActivity();
        wanbaActivity.setPic("pic");
        wanbaActivity.setAskwho("askwho");
        List<String> guestList = new ArrayList<String>();
        guestList.add("a");
        guestList.add("b");
        wanbaActivity.setGuestList(guestList);
        System.out.println(wanbaActivity.toJson());

        String str = "{\"pic\":\"pic\",\"askwho\":\"askwho\",\"guestList\":[\"a\",\"b\"]}";
        System.out.println(WanbaActivity.toObject(str));
    }
}
