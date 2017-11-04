package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.crypto.Base64Encoder;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.account.discuz.DiscuzMember;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UUIDUtil;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-13 下午4:49
 * Description:
 */
public class DiscuzUtil {

    public static final String DEFUALT_PWD = MD5Util.Md5("joyme");
    private static final String KEY_SIGN = "jfwmt&jjnngtginjm#2fnn*&^%fjgnaa=";

    private static Logger logger = LoggerFactory.getLogger(DiscuzUtil.class);


    /**
     * 注册
     *
     * @param userid
     * @param pwd
     * @param registerTime
     * @param registerIp
     * @param request
     * @param response
     */
    public static void register(Profile profile, String userid, String pwd, String registerIp, long registerTime, LoginDomain loginDomain, int point, HttpServletRequest request, HttpServletResponse response) {
        DiscuzUser discuzUser = insertDiscuzMember(profile, userid, pwd, registerIp, registerTime, loginDomain, point);

        if (discuzUser != null && discuzUser.getUserId() > 0) {
            setDiscuzCookie(request, response, discuzUser.getPwd(), String.valueOf(discuzUser.getUserId()));
        }
    }

    /**
     * 退出
     *
     * @param request
     * @param response
     */
    public static void logOutDiscuz(HttpServletRequest request, HttpServletResponse response) {
        DiscuzUtil.delDiscuzCookie(request, response);
    }

    /**
     * @param field
     * @param value
     * @param uid
     */
    public static void modifyProfile(DiscuzModifyField field, String value, long uid) {
        String api = WebappConfig.get().getDiscuzApiHost() + "/api/joyme/modifyprofile.php";

        logger.info("modifyProfile:" + api + ",upfield:" + field.getCode() + ",val:" + value + ",uno:" + uid);
        HttpParameter[] params = new HttpParameter[]{
                new HttpParameter("upfield", field.getCode()),
                new HttpParameter("val", value),
                new HttpParameter("uno", uid),
                new HttpParameter("time", getTimeString(System.currentTimeMillis()))
        };

        getResultByPost(api, sign(params));
    }

    private static DiscuzUser insertDiscuzMember(Profile profile, String userid, String pwd, String registerIp, long registerTime, LoginDomain accountDomain, int point) {
        DiscuzUser discuzUser;
        if (accountDomain.equals(LoginDomain.EMAIL)) {
            discuzUser = callRegisterApi(profile, userid, pwd, registerIp, registerTime, point);
        } else {
            userid = UUIDUtil.getShortUUID() + "@joyme.com";
            pwd = DEFUALT_PWD;
            discuzUser = callRegisterApi(profile, userid, pwd, registerIp, registerTime, point);
        }
        return discuzUser;
    }

    private static DiscuzUser callRegisterApi(Profile profile, String userid, String pwd, String registerIp, long registerTime, int point) {
        String api = WebappConfig.get().getDiscuzApiHost() + "/api/joyme/register.php";

        pwd = pwd.toLowerCase();
        HttpParameter[] params;
        registerIp = StringUtil.isEmpty(registerIp) ? "127.0.0.1" : registerIp;
        if (com.enjoyf.util.StringUtil.isEmpty(profile.getIcon())) {
            params = new HttpParameter[]{
                    new HttpParameter("userid", userid),
                    new HttpParameter("userpwd", pwd),
                    new HttpParameter("screenname", profile.getNick()),
                    new HttpParameter("registerip", registerIp),
                    new HttpParameter("accountuno", profile.getUid()),
                    new HttpParameter("uno", profile.getUid()),
                    new HttpParameter("point", point),
                    new HttpParameter("domain", profile.getDomain()),
                    new HttpParameter("time", getTimeString(registerTime)),
            };
        } else {
            params = new HttpParameter[]{
                    new HttpParameter("userid", userid),
                    new HttpParameter("userpwd", pwd),
                    new HttpParameter("screenname", profile.getNick()),
                    new HttpParameter("registerip", registerIp),
                    new HttpParameter("accountuno", profile.getUid()),
                    new HttpParameter("uno", profile.getUid()),
                    new HttpParameter("point", point),
                    new HttpParameter("domain", profile.getDomain()),
                    new HttpParameter("headicon", profile.getIcon()),
                    new HttpParameter("time", getTimeString(registerTime)),
            };
        }

        HttpResult result = getResultByPost(api, sign(params));

        long uidResponse = -1l;
        if (result.getReponseCode() == 200) {
            GAlerter.lan("DiscuzUtil.callRegisterApi Parameter.userid=" + userid + ",userpwd=" + pwd + ",screenname=" + profile.getNick()
                    + ",registerip=" + registerIp + ",profileUid=" + profile.getUid() + ",uno=" + profile.getUno() + ",point=" + point + ",domain=" + profile.getDomain());
            GAlerter.lab("DiscuzUtil.callRegisterApi result.getResult()===" + result.getResult() + ",api=" + api);
            JSONObject object = JSONObject.fromObject(result.getResult());
            int resultCode = object.getInt("code");

            if (resultCode == 0) {
                uidResponse = object.getLong("msg");
            } else {
                GAlerter.lan(DiscuzUtil.class + " callRegisterApi return error: profile: " + profile + " returnCode:" + object.toString());
            }
        }

        DiscuzUser user = new DiscuzUser();
        if (uidResponse > 0) {
            user.setUserId(uidResponse);
            user.setPwd(pwd);
        }
        return user;
    }

    private static HttpParameter[] sign(HttpParameter[] params) {
        StringBuilder sb = new StringBuilder();
        for (HttpParameter param : params) {
            sb.append(param.getName()).append("=").append(param.getValue()).append("&");
        }
        sb.append("key=").append(KEY_SIGN);
        if (logger.isDebugEnabled()) {
            logger.debug("discuz sign org param:" + sb.toString());
        }
        String signValue = MD5Util.Md5(sb.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("discuz sign result:" + signValue);
        }
        params = Arrays.copyOf(params, params.length + 1);
        params[params.length - 1] = new HttpParameter("sign", signValue);
        return params;
    }


    private static void setDiscuzCookie(HttpServletRequest request, HttpServletResponse response, String password, String uid) {
        String cookieKey = password.toLowerCase() + "\t" + uid;
        CookieUtil.setCookie(request, response, WebappConfig.get().getDiscuzAuthCookieKey(), new Base64Encoder().encode(cookieKey.getBytes()), -1);
        response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
    }


    private static void delDiscuzCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, WebappConfig.get().getDiscuzAuthCookieKey(), null, 0);
        response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
    }

    private static String getTimeString(long time) {
        return String.valueOf(time).substring(0, 10);
    }

    private static HttpResult getResultByPost(String url, HttpParameter[] params) {
        HttpClientManager httpClient = new HttpClientManager();
        int responseCode;
        int retryTimes = 0;
        HttpResult result = null;
        do {
            result = httpClient.post(url, params, null);

            responseCode = result.getReponseCode();
            retryTimes++;
        } while (responseCode != 200 && retryTimes < 3);
        return result;
    }


    private static class DiscuzUser {
        private long userId;
        private String pwd;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
