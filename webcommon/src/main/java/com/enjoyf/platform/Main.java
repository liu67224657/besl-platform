package com.enjoyf.platform;

public class Main {

    public static void main(String[] args) {
        String s="时津风\n" +
                "丛云\n" +
                "新奥尔良\n" +
                "尼克尼克尼\n" +
                "Nicho\n" +
                "冲田总悟大魔王\n" +
                "秘银\n" +
                "火系大卡尔\n" +
                "雪音\n" +
                "joyme红喵酱\n" +
                "托奇\n" +
                "东方小柠檬\n" +
                "冰影风\n" +
                "日坂菜乃\n" +
                "剑心清明\n" +
                "遗失的裤衩\n" +
                "葵千月\n" +
                "Joymeadmin\n" +
                "宝强不哭";

        s="'"+s+"'";

        s=s.replaceAll("\n","','");
        System.out.println(s);

        String  uid="5456826\n" +
                "5187024\n" +
                "5153697\n" +
                "5153681\n" +
                "5153620\n" +
                "5153413\n" +
                "5153105\n" +
                "5039815\n" +
                "5029872\n" +
                "4967312\n" +
                "4468929\n" +
                "4265539\n" +
                "3985156\n" +
                "3403834\n" +
                "1658489\n" +
                "1543751\n" +
                "1534111\n" +
                "1397956";
        System.out.println(uid.replaceAll("\n",","));

        String wiki="blhx cq dxcb2 dq10 pvp fzjh hzsg starve ro mjj sdmht tenka f7 ny\n" +
                "yl\n" +
                "bh3\n" +
                "oni\n" +
                "compass\n" +
                "yuyuyui\n" +
                "kpgs\n" +
                "gundam\n" +
                "Xxm\n" +
                "mfjsml\n" +
                "nwny\n" +
                "mkly\n" +
                "djqy\n" +
                "sdorica\n" +
                "es\n" +
                "szqy\n" +
                "xnsc\n" +
                "snsc trove";
        System.out.println(wiki.replaceAll("\n"," "));
    }
}
