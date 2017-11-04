package com.enjoyf.platform.webapps.common.util;

import com.enjoyf.platform.crypto.Base64Encoder;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/17
 * Description:
 */
public class YoukuCookieUtil {

    public static final String YOUKU_APPKEY = "08pkvrWvx5ArJNvhYf19kN";

    public static final String COOKIEKEY_YKDOMAIN = WebappConfig.get().getURL_YKCOOKIEDOMAIN();

    public static YoukuCookie praseYoukuCookieYKTK(String cookie) {
        //step1 urldecode
        try {
            cookie = URLDecoder.decode(cookie, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        String[] cookieArray = cookie.split("\\|");
        if (cookieArray.length == 0) {
            return null;
        }

        String base64Code = cookieArray[3];
        String yktkString = new Base64Encoder().decode(base64Code);

        YoukuCookie youkuCookie = new YoukuCookie();
        String[] keyValues = yktkString.split(",");
        for (String keyValue : keyValues) {
            String[] entrys = keyValue.split(":");
            if (entrys.length == 2) {
                if (entrys[0].equals("ytid")) {
                    youkuCookie.setYtid(entrys[1]);
                } else if (entrys[0].equals("nn")) {
                    try {
                        youkuCookie.setNick(new String(entrys[1].getBytes("ISO-8859-1"), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }

        if (StringUtil.isEmpty(youkuCookie.getYtid())) {
            return null;
        }

        return youkuCookie;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new String("è\u008F è\u0090\u009Dè\u0080\u0081å\u0085¬".getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println(YoukuCookieUtil.praseYoukuCookieYKTK("1%7C1435824956%7C15%7CaWQ6Mzc5NDQ3NTQ1LG5uOmFudG9ueTUxNix2aXA6ZmFsc2UseXRpZDozNzk0NDc1NDUsdGlkOjA%3D%7C66eb55e7c6871b581685ede004778eca%7C0e16ffa7f9e9a71441d108c073479811c2d228da%7C1")
//        );

        try {
            System.out.println(URLDecoder.decode("%E8%8F%A0%E8%90%9D%E8%80%81%E5%85%AC", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//
        System.out.println(YoukuCookieUtil.praseYoukuCookieYKTK("1%7C1436176656%7C15%7CaWQ6NzgwMzEyNDU3LG5uOuiPoOiQneiAgeWFrCx2aXA6ZmFsc2UseXRpZDo3ODAzMTI0NTcsdGlkOjA%3D%7C3586b84ddb17788472f0b18a28e745a7%7C97867c874555a531fb092edb8a34cda127f80624%7C1"));
    }


    //方法不对外仅用于测试
    private static String genYKTK(String nick, long ykid) throws UnsupportedEncodingException {
        String s = "id:" + ykid + ",nn:" + nick + ",vip:false,ytid:" + ykid + ",tid:0";

        s = new Base64Encoder().encode(s);

        s = "1|1434426667|15|" + s;

        s = URLEncoder.encode(s, "UTF-8");

        System.out.println(s);

        return s;
    }
}
