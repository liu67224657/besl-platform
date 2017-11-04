package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.weixin.BasicButton;
import com.enjoyf.platform.service.weixin.CommonButton;
import com.enjoyf.platform.service.weixin.ComplexButton;
import com.enjoyf.platform.service.weixin.WeixinMenu;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.weixin.WeixinUtil;

/**
 * Created by xupeng on 16/2/2.
 */
public class WeixinMenuMain {

    /**
     * @param args
     */
    private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static final String APPID = "wxedaaf0b0315d44e7";   //订阅号
    private static final String SECRET = "afde3c1cd927e508f663b92e6a084b6b ";   //订阅号

    public static void main(String[] args) {
        try {
            //获取token
            String accessToken = MiscServiceSngl.get().getAccessTokenCache(APPID, SECRET);
            if (!StringUtil.isEmpty(accessToken)) {
                //生成菜单  菜单生成规则参考微信文档 http://mp.weixin.qq.com/wiki/13/43de8269be54a0a6f64413e4dfa94f39.html
                int i = WeixinUtil.createMenu(getWeixinMenu(), accessToken);
                System.out.println(i);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private static WeixinMenu getWeixinMenu() {
        // CommonButton为单个菜单类
        // ComplexButton为组合菜单 里面可放多个CommonButton 微信最多支持5个子菜单

        CommonButton commonButton21 = new CommonButton();
        commonButton21.setName("领礼包");
        commonButton21.setKey("giftmarket");
        commonButton21.setType("click");

        CommonButton commonButton133 = new CommonButton();
        commonButton133.setName("看WIKI");
        commonButton133
                .setUrl("http://www.joyme.com/wechat/strategy/index.html");
        commonButton133.setType("view");

//        CommonButton commonButton22 = new CommonButton();
//        commonButton22.setName("礼包许愿");
//        commonButton22
//                .setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx758f0b2d30620771&redirect_uri=http://www.joyme.com/giftmarket/wap/reserve&response_type=code&scope=snsapi_base#wechat_redirect");
//        commonButton22.setType("view");

        CommonButton commonButton23 = new CommonButton();
        commonButton23.setName("天天活动");
        commonButton23.setUrl("http://www.joyme.com/wechat/bbs/ac");
        commonButton23.setType("view");

        CommonButton commonButton30 = new CommonButton();
        commonButton30.setName("着迷上市");
        commonButton30.setType("view");
        commonButton30.setUrl("http://m.joyme.com/gopublic/index.html");

        CommonButton commonButton31 = new CommonButton();
        commonButton31.setName("着迷玩霸");
        commonButton31.setType("view");
        commonButton31.setUrl("http://huabao.joyme.com/wap.html");

        CommonButton commonButton32 = new CommonButton();
        commonButton32.setName("商务合作");
        commonButton32.setType("view");
        commonButton32.setUrl("http://t.cn/RPDb0OK");

        CommonButton commonButton34 = new CommonButton();
        commonButton34.setName("着迷招聘");
        commonButton34.setType("view");
        commonButton34
                .setUrl("http://www.joyme.com/wechat/joymerecruit/index.html");

        ComplexButton complexButton2 = new ComplexButton();
        complexButton2
                .setSub_button(new BasicButton[]{commonButton30,
                        commonButton31, commonButton23, commonButton32,
                        commonButton34});
        complexButton2.setName("玩转着迷");

        //生成的结果页 放入对应的类
        WeixinMenu weixinMenu = new WeixinMenu();
        weixinMenu.setButton(new BasicButton[]{commonButton21,
                commonButton133, complexButton2});

        return weixinMenu;
    }

}
