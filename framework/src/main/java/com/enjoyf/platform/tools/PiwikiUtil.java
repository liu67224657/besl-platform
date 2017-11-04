package com.enjoyf.platform.tools;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericliu on 2017/8/22.
 */
public class PiwikiUtil {
    private static final String insert_template = "INSERT INTO piwik_site\n" +
            "            (piwik_site.name,\n" +
            "             main_url,\n" +
            "             ts_created,\n" +
            "             ecommerce,\n" +
            "             sitesearch,\n" +
            "             sitesearch_keyword_parameters,\n" +
            "             sitesearch_category_parameters,\n" +
            "             timezone,\n" +
            "             currency,\n" +
            "             excluded_ips,\n" +
            "             excluded_parameters,\n" +
            "             excluded_user_agents,\n" +
            "             piwik_site.group,\n" +
            "             piwik_site.type,\n" +
            "             keep_url_fragment)\n" +
            "VALUES ('@2',\n" +
            "        '@3',\n" +
            "        NOW(),\n" +
            "        0,\n" +
            "        1,\n" +
            "        '',\n" +
            "        '',\n" +
            "        'Asia/Shanghai',  \n" +
            "        'USD',\n" +
            "        '',\n" +
            "        '',\n" +
            "        '',\n" +
            "        '游戏WIKI',\n" +
            "        'website',\n" +
            "        0);";

    public static void main(String[] args) {

        String s = "787\t灰烬战姬\thjzj\n" +
                "788\t刀剑契约WIKI\tdjqy\n" +
                "789\t万象物语\tsdorica\n" +
                "790\t勇者斗恶龙汇总WIKI\tdq\n" +
                "791\t偶像梦幻祭WIKI\tes\n" +
                "792\t舰姬WIKI\tjianji\n" +
                "793\t食之契约WIKI\tszqy\n" +
                "794\t星娘收藏WIKI\txnsc\n" +
                "795\t恋与制作人WIK\tlyzz\n" +
                "796\t少年jump手游WIKI\tsnsc\n" +
                "797\t江湖风云录WIKI\tjhfyl\n" +
                "798\t宝藏世界WIKI\ttrove\n" +
                "799\t乱斗英雄WIKI\tld\n" +
                "800\t元素守护者WIKI\tysshz\n" +
                "801\t结城友奈是勇者手游wiki\tyuyuyui\n" +
                "802\t荒野乱斗WIKI\tbs";

        List<String> lineList = new ArrayList<String>();
        for (String line : s.split("\n")) {
            lineList.add(line);
        }

        StringBuffer sb = new StringBuffer();
        for (String line : lineList) {
            String[] lineObj = line.split("\t");
            String sql = insert_template.replaceAll("@2", lineObj[1])
                    .replaceAll("@3", "http://wiki.joyme.com/"+lineObj[2]);
            sb.append(sql).append("\n");
        }

        System.out.println(sb);

    }
}
