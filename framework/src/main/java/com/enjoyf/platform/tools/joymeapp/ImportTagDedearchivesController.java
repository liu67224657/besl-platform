package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.joymeapp.ArchiveHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportTagDedearchivesController {

    private static final Logger logger = LoggerFactory.getLogger(ImportTagDedearchivesController.class);

    private static JoymeAppHandler joymeAppHandler;
    private static ArchiveHandler archiveHandler;

    private static Set<Integer> archiveIds = new HashSet<Integer>();

    static {
        archiveIds.add(51611);
        archiveIds.add(51608);
        archiveIds.add(51606);
        archiveIds.add(51577);
        archiveIds.add(51575);
        archiveIds.add(51570);
        archiveIds.add(51567);
        archiveIds.add(51564);
        archiveIds.add(51562);
        archiveIds.add(51557);
        archiveIds.add(51551);
        archiveIds.add(51543);
        archiveIds.add(51541);
        archiveIds.add(51539);
        archiveIds.add(51531);
        archiveIds.add(51516);
        archiveIds.add(51513);
        archiveIds.add(51484);
        archiveIds.add(51473);
        archiveIds.add(51469);
        archiveIds.add(51451);
        archiveIds.add(51444);
        archiveIds.add(51434);
        archiveIds.add(51433);
        archiveIds.add(51432);
        archiveIds.add(51430);
        archiveIds.add(51428);
        archiveIds.add(51426);
        archiveIds.add(51424);
        archiveIds.add(51422);
        archiveIds.add(51419);
        archiveIds.add(51416);
        archiveIds.add(51413);
        archiveIds.add(51410);
        archiveIds.add(51406);
        archiveIds.add(51403);
        archiveIds.add(51387);
        archiveIds.add(51373);
        archiveIds.add(51352);
        archiveIds.add(51347);
        archiveIds.add(51335);
        archiveIds.add(51333);
        archiveIds.add(51331);
        archiveIds.add(51326);
        archiveIds.add(51322);
        archiveIds.add(51319);
        archiveIds.add(51317);
        archiveIds.add(51315);
        archiveIds.add(51311);
        archiveIds.add(51303);
        archiveIds.add(51293);
        archiveIds.add(51287);
        archiveIds.add(51279);
        archiveIds.add(51262);
        archiveIds.add(51259);
        archiveIds.add(51237);
        archiveIds.add(51179);
        archiveIds.add(51176);
        archiveIds.add(51174);
        archiveIds.add(51172);
        archiveIds.add(51169);
        archiveIds.add(51167);
        archiveIds.add(51163);
        archiveIds.add(51144);
        archiveIds.add(51139);
        archiveIds.add(51131);
        archiveIds.add(51127);
        archiveIds.add(51126);
        archiveIds.add(51122);
        archiveIds.add(51119);
        archiveIds.add(51111);
        archiveIds.add(51110);
        archiveIds.add(51106);
        archiveIds.add(51102);
        archiveIds.add(51099);
        archiveIds.add(51094);
        archiveIds.add(51091);
        archiveIds.add(51089);
        archiveIds.add(51076);
        archiveIds.add(51074);
        archiveIds.add(51066);
        archiveIds.add(51060);
        archiveIds.add(51055);
        archiveIds.add(51049);
        archiveIds.add(51037);
        archiveIds.add(51035);
        archiveIds.add(51005);
        archiveIds.add(51000);
        archiveIds.add(50996);
        archiveIds.add(50988);
        archiveIds.add(50974);
        archiveIds.add(50967);
        archiveIds.add(50961);
        archiveIds.add(50955);
        archiveIds.add(50952);
        archiveIds.add(50951);
        archiveIds.add(50948);
        archiveIds.add(50936);
        archiveIds.add(50932);
        archiveIds.add(50925);
        archiveIds.add(50908);
        archiveIds.add(50859);
        archiveIds.add(50857);
        archiveIds.add(50851);
        archiveIds.add(50850);
        archiveIds.add(50844);
        archiveIds.add(50836);
        archiveIds.add(50834);
        archiveIds.add(50831);
        archiveIds.add(50825);
        archiveIds.add(50821);
        archiveIds.add(50819);
        archiveIds.add(50815);
        archiveIds.add(50812);
        archiveIds.add(50804);
        archiveIds.add(50796);
        archiveIds.add(50782);
        archiveIds.add(50750);
        archiveIds.add(50747);
        archiveIds.add(50744);
    }

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            joymeAppHandler = new JoymeAppHandler("joymeapp", servProps);
            archiveHandler = new ArchiveHandler("article_cms", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
//        importTagDedearchives();
    }

    private static void importTagDedearchives() {
        //TODO 这个方法有错误
//        try {
//            for (Integer aid : archiveIds) {
//                System.out.println("---------------"+aid+"---------------");
//                HttpClientManager clientManager = new HttpClientManager();
//                HttpResult result = clientManager.post("http://api.joyme.beta/joymeapp/gameclient/api/tagphp/updatearticle", new HttpParameter[]{
//                        new HttpParameter("archivesid", aid),
//                        new HttpParameter("tags", "-100"),
//                        new HttpParameter("displaytag", "0")
//                }, null);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
