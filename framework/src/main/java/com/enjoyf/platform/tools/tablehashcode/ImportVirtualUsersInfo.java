package com.enjoyf.platform.tools.tablehashcode;

import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONArray;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class ImportVirtualUsersInfo {
    public static UserCenterHandler userCenterHandler = null;
    private static String APPKEY = "www";

    static Map<String, String> userMap = new HashMap<String, String>();

    static {
        userMap.put("NERV总部", "http://joymepic.qiniudn.com/qiniu/original/2015/07/18/375f8f7b02d5304d4e0b94b0d52149594d76.jpg");
        userMap.put("zoey fan", "http://joymepic.qiniudn.com/qiniu/original/2015/07/72/0c35cfeb08f61048cc0a9a4032209a9908c2.jpg");
        userMap.put("冰糖狸猫酱", "http://joymepic.qiniudn.com/qiniu/original/2015/07/65/dad59f5c004db046910b1f401ac9ea94f7bb.jpg");
        userMap.put("黑叔叔", "http://joymepic.qiniudn.com/qiniu/original/2015/07/61/990beabb091fb0467f0a7ef0b9f6693a723c.jpg");
        userMap.put("黄金姬腿饱", "http://joymepic.qiniudn.com/qiniu/original/2015/07/8/647e41130d94a04a57089290ef96d217a989.jpg");
        userMap.put("金鱼草控", "http://joymepic.qiniudn.com/qiniu/original/2015/07/84/d862aed40a89804c420ad770f6d8f8725c1f.png");
        userMap.put("筋肉人兄贵", "http://joymepic.qiniudn.com/qiniu/original/2015/07/36/4d7d30a80787a04bd608e320f58a4f6746a6.jpg");
        userMap.put("具大的狗头果", "http://joymepic.qiniudn.com/qiniu/original/2015/07/2/09750e710e55604ec50993708b5f7bb51a1a.jpg");
        userMap.put("里番宝库", "http://joymepic.qiniudn.com/qiniu/original/2015/07/29/a0fc02130f0aa0479f0a76503cf6d6399068.jpg");
        userMap.put("马猴烧酒酱", "http://joymepic.qiniudn.com/qiniu/original/2015/07/38/9808380d07c7e04b050a6f70cbe7ff70c6b2.jpg");
        userMap.put("猫头抽脸机", "http://joymepic.qiniudn.com/qiniu/original/2015/07/53/420f363f02515046e708706003c62bad1baf.jpg");
        userMap.put("肉肉仔", "http://joymepic.qiniudn.com/qiniu/original/2015/07/59/eb03566b0684c04126096490a82a5c4fb115.jpg");
        userMap.put("四月一日侑子", "http://joymepic.qiniudn.com/qiniu/original/2015/07/93/4e9383ed0495f04b850972509bd50c7218c5.jpg");
        userMap.put("糖兔子", "http://joymepic.qiniudn.com/qiniu/original/2015/07/67/52c7222f029b8045f50811d0b4cb8bc8478d.jpg");
        userMap.put("文春泡泡", "http://joymepic.qiniudn.com/qiniu/original/2015/07/95/8a8e88d90a30504dee0a2c20c71c95aff186.jpg");
        userMap.put("小公举", "http://joymepic.qiniudn.com/qiniu/original/2015/07/84/ddeb0f0c026c90450608e07055df91328712.png");
        userMap.put("宅腐姬", "http://joymepic.qiniudn.com/qiniu/original/2015/07/32/d464be920746104ef50950a0f0d9957873b4.jpg");
    }

    public static void main(String[] args) {

        try {
            FiveProps fiveProps = Props.instance().getServProps();
            RedisManager manager = new RedisManager(fiveProps);
            userCenterHandler = new UserCenterHandler("usercenter", fiveProps);

            Set<String> keySet = userMap.keySet();
            for (String key : keySet) {
                String value = userMap.get(key);
                importUser(key, value, userCenterHandler);
                System.out.println("success=================================================");
            }

        } catch (ServiceException e) {
            System.out.println("error=================================================");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void importUser(String nick, String icon, UserCenterHandler userCenterHandler) throws ServiceException {
        Date now = new Date();

        String uno = UUID.randomUUID().toString();
        UserAccount userAccount = new UserAccount();
        userAccount.setUno(uno);
        userAccount.setCreateTime(now);
        userAccount.setCreateIp("127.0.0.1");
        userAccount.setAccountFlag(new AccountFlag().has(0));
        userCenterHandler.insertUserAccount(userAccount);

        String profieId = UserCenterUtil.getProfileId(userAccount.getUno(), APPKEY);

        Profile profile = new Profile();
        profile.setProfileId(profieId);
        profile.setUno(userAccount.getUno());
        profile.setProfileKey(APPKEY);
        profile.setCreateIp("127.0.0.1");
        profile.setCreateTime(now);
        profile.setFlag(new ProfileFlag().has(ProfileFlag.FLAG_EXPLORE));
        profile.setIcon(icon);
        profile.setNick(nick);

        Icons icons = new Icons();
        icons.getIconList().add(new Icon(0, icon));
        profile.setIcons(icons);
        profile = userCenterHandler.createProfile(profile);
        //新用户
        if (profile != null) {
            profile.setFreshUser(true);
        }
        Token token = new Token();
        token.setTokenType(TokenType.DEFAULT);
        token.setUno(profile.getUno());
        token.setProfileKey(profile.getProfileKey());
        token.setUid(profile.getUid());
        token.setProfileId(profile.getProfileId());
        token = userCenterHandler.createToken(token);

    }
}
