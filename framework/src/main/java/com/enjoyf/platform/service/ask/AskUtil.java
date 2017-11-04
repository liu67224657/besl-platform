package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.search.WanbaSearchType;
import com.enjoyf.platform.service.ask.search.WikiappSearchType;
import com.enjoyf.platform.service.ask.wiki.AdvertiseDomain;
import com.enjoyf.platform.service.ask.wiki.ContentLineOwn;
import com.enjoyf.platform.service.ask.wiki.ContentLineType;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.util.MD5Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskUtil {
    public static final String KEY_ALLGAME = "all";

    private static final Pattern IMAGE = Pattern.compile("<jmwbimg:([^>]+)>");
    private static final Pattern IMG = Pattern.compile("<img src=\"([^\"]+)\"[^>]+>");
    private static final Pattern SPACE = Pattern.compile("(?:\n)+");

    //    private static final Pattern VOICE = Pattern.compile("<jmwbvoice:([^:]+):jmwbvt:([^>]+)>");
    private static final Pattern TAG = Pattern.compile("<jm[^>]+>");


    private static final Pattern P_TAG = Pattern.compile("</p><p>");
    private static final Pattern BR_TAG = Pattern.compile("<br/>");


    public static final String SEARCH_SUGGEST_KEY = AskConstants.SERVICE_PREFIX + "_search_suggest_key";

    public static final String JOYMEWIKI_SEARCH_SUGGEST_KEY = AskConstants.SERVICE_PREFIX + "_joymewiki_search_suggest_key";


    private static HttpClientManager httpClient = new HttpClientManager();

    public static String getAskLineKey(String ownProfileId, WanbaItemDomain itemDomain) {
        return MD5Util.Md5(ownProfileId + itemDomain.getCode());
    }


    public static String getAskItemId(String destId, String lineKey, ItemType type) {
        return MD5Util.Md5(destId + lineKey + type.getCode());
    }

    //profileId+destId+askType+actionType
    public static String getAskUserActionId(String profileId, long destId, ItemType itemType, AskUserActionType userActionType) {
        return MD5Util.Md5(profileId + destId + itemType.getCode() + userActionType.getCode());
    }

    public static String getWanbaSearchEntryId(String id, WanbaSearchType type) {
        return MD5Util.Md5(id + type.getCode());
    }


    public static String getWanbaProfileClassifyId(String profileid, WanbaProfileClassifyType type) {
        return MD5Util.Md5(profileid + type.getCode());
    }


    public static String getAskReportId(String id, ItemType itemType) {
        return MD5Util.Md5(id + itemType.getCode());
    }


    public static AskBody getQuestionBody(String richText) {
        List<Map<String, String>> images = RegexUtil.fetch(richText, IMAGE, null);
        richText = RegexUtil.replace(richText, SPACE, " ", -1);

        String s = RegexUtil.replace(richText, IMAGE, "[图片]", -1);

        List<String> pic = new ArrayList<String>();
        for (Map<String, String> imageEntry : images) {
            pic.add(imageEntry.get("1"));
        }

        AskBody questionBody = new AskBody();
        questionBody.setText(s.length() > 150 ? s.substring(0, 150) : s);
        questionBody.setPic(pic);
        return questionBody;
    }

    /**
     * @param richText
     * @return
     */
    public static String getHtmlRichBody(String richText) {
        //tarn \n <p></p>

        StringBuffer sb = new StringBuffer("<p>").append(richText).append("</p>");

        richText = RegexUtil.replace(sb.toString(), SPACE, "</p><p>", -1);

        return RegexUtil.replace(richText, IMAGE, "<cite><img src=\"$1?imageView2/2/w/750\"></cite>", -1);
    }

    public static String replaceImportRouce(String richText) {
        StringBuffer sb = new StringBuffer("<p>").append(richText).append("</p>");

        richText = RegexUtil.replace(sb.toString(), SPACE, "</p><p>", -1);
        return richText;
    }

    public static String textReplace(String richText) {
        richText = RegexUtil.replace(richText, IMG, "<jmwbimg:$1>", -1);
        richText = RegexUtil.replace(richText, P_TAG, "\n", -1);
        richText = RegexUtil.replace(richText, BR_TAG, "\n", -1);
        richText = richText.replace("<p>", "").replace("</p>", "");
        return richText;
    }


    /**
     * 限时提问
     *
     * @return
     */
    public static String postTimeQuestion(String title, String text, String askpid, String timelimit, String tagid) {
        HttpResult httpResult = httpClient.post("http://api." + WebappConfig.get().getDomain() + "/wanba/api/ask/question/post/timelimit", new HttpParameter[]{
                new HttpParameter("title", title),
                new HttpParameter("text", AskUtil.textReplace(text)),
                new HttpParameter("pid", askpid),
                new HttpParameter("tagid", tagid),
                new HttpParameter("timelimit", timelimit),
                new HttpParameter("appkey", "3iiv7VWfx84pmHgCUqRwun"),
        }, null);
        return httpResult.getResult();
    }


    public static String textRelaceHtmlTag(String richText) {

        return richText;
    }


    public static String getContentLineKey(ContentLineOwn contentLineOwn, String gameid) {
        //今天的文章line
        if (contentLineOwn.getCode() == ContentLineOwn.TODAY_ALL_ARCHIVE.getCode()) {
            return MD5Util.Md5(contentLineOwn.getCode() + DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT) + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
        } else if (contentLineOwn.getCode() == ContentLineOwn.GAME_ALL_ARCHIVE.getCode()) { //游戏对应的文章lien
            return MD5Util.Md5(contentLineOwn.getCode() + gameid + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
        }
        return MD5Util.Md5(contentLineOwn.getCode() + "" + ContentLineType.CONTENTLINE_ARCHIVE.getCode());
    }


    public static String getWikiappSearchEntryId(String id, WikiappSearchType type) {
        return MD5Util.Md5(id + type.getCode());
    }


    public static String getAdvertiseLinekey(String appkey, Integer platform, AdvertiseDomain advertiseDomain) {
        return MD5Util.Md5(appkey + platform + advertiseDomain.getCode());
    }


    //功能：wiki链接需要加皮肤
    // 包括锚点和问号参数
    public static String handleUrl(String target) {
        if (target.contains("http")) {
            if (target.contains("?")) {
                if(!target.contains("useskin=MediaWikiBootstrap2")){
                    target = target + "&useskin=MediaWikiBootstrap2";
                }
            } else {
                target = target + "?useskin=MediaWikiBootstrap2";
            }
        }
        return target;
    }


    public static void main(String[] args) {
//        String s = "晴明：首先我个人晴明是一个纯粹的辅助型阴阳师(一式的爆符伤害就不要拿出来丢人现眼了!老老实实被动输出把!)不管是星，灭，生，守都是很有用的辅助技能，尤其是守，在开启状态下，可以帮你躲过很多的DEBUFF状态(没错!我就是说的那三只猪!就是他们!我剧情模式的一血就是他们夺走的!)而且因为晴明作为初始，技能在前期相比于其他比较容易升级(大概没有满技能的晴明都是卡在了困难18把，反正我是，不管，你们也都是!)所以前期剧情模式选择大多数都是选晴明(我只是说三个御灵强化同等级的情况)而后期，某些特殊剧情(没错!还是那三只猪!)晴明的守能带来的收益比其他阴阳师的收益高，所以在剧情模式我还是比较推荐晴明。\n推荐指数：★★★★★\n推荐技能：1.守 2.生or灭or星(优先高等级，优先：生，星。因为剧情BOSS也有不吃buff的，如大天狗)\n神乐：这个我个人定义为是半辅助半输出的阴阳师，在剧情模式下，我认为一般不带神乐，除非是你的单一式神强度相比其他2个出战式神高出很多的情况，靠着神乐的疾风来拯救世界的那种，不然就技能的选择上很难，因为她比晴明解锁时间晚，技能点数加的慢，但是假如你的伞和被动以及疾风在很早的时候就满级或者高级了，那剧情模式是可以带的(我就不明白了，为什么同样的攻击，被动伤害加成也是一样的，神乐的伤害怎么就比晴明高辣么多!)，比如那三只猪(没错!我就是针对他们)，在三只猪的剧情下，你来一只高速兔子(比猪和神乐快就好)，带一只被动满级的打火机(不知道的萌新在这里为你们科普一下，打火机就是座敷童子!不要再问打火机是什么了!也不要问我为什么座敷童子叫打火机!)，然后你的高强度AOE输出(比神乐快就好)，然后进场兔子拉一手，输出一个AOE，神乐拉一手，输出再一个AOE，对面那些猪就等着死把!西八!而除了这种单点AOE输出高的情况，一般不推荐带神乐。\n推荐指数：★★★★★推荐技能：1.疾风2.续命or伞or鱼\n源博雅：我对源博雅的定义是一个输出型的阴阳师，虽然他很帅，但是，他是个妹控,还对我们的神乐妹妹有所企图!我不能忍啊!而作为我的狗粮大队长，他刷狗粮确实是一把好手，但是在剧情模式里面，因为解锁时间晚，所以技能点最少的就是这货了!尤其是这货最后一个技能点最然要60级!60级!60级!我也不知道我为什么到60级了还有剧情没过，需要这只狗粮队长来拯救我!所以基本剧情模式我们不选这货。但是!也有例外!我们继续拿那三只猪说事(我告诉你们这几只猪，咱的梁子算是结下了!)在你的源博雅分身和三连都解锁的早和技能等级不低的情况或者下，同时你的输出又不够给力的情况下，源博雅是你不错的选择，因为虽然他的影分身技能继承血量，但其实，那个技能真的只是召唤一个影子啊!这个影子不吃伤害，不会被选为攻击目标，不吃控制,这也就意味着这三只猪控制不住咱的源博雅的影子，你可以靠着影子的输出，慢慢弄死他们(这只是一种假想，反正我没靠源博雅过猪)，还有，因为源博雅是一个输出，所以在后期的剧情中(没错，我就是说的黑晴!)你可以靠着源博雅来补输出，达到各个击破的目的。但是终究我还是不推荐在剧情模式下带源博雅的。\n推荐指数：★\n推荐技能：1.分身2.三连";

//        String s = "<p>4324324<img src=\"http://joymepic.contentLineType.com/wanbaask/201610/171479363336008311.jpeg\" width=\"100\" height=\"100\"/></p>";
//        s = imgReplaceJmwbimg(s);
//
//        System.out.println(s);
//        String linekey = AskUtil.getAskLineKey("110", WanbaItemDomain.RECOMMEND);
//        System.out.println(linekey);
        String text = "<p><jmwbimg:http://joymepic.joyme.com/wanbaask/201610/181479434685113672.png></p><p><br/></p><p>图片回答</p>";

//        AskBody askBody = getQuestionBody(text);
//        System.out.println(askBody);
//
//        System.out.println(askBody.toString());
//
//
//        Gson gson = new GsonBuilder().setPrettyPrinting()
//                .disableHtmlEscaping()
//                .create();
//        String datas = gson.toJson(askBody);
//        System.out.println(datas);

        String tex = textRelaceHtmlTag(text);
        System.out.println(tex);
        AskBody askBody = getQuestionBody(tex);
        System.out.println(askBody);
    }

}
