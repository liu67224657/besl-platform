package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;

/**
 * Created by zhimingli on 2016-12-13 0013.
 */
public class AbstractWanbaController extends ToolsBaseController {
    //1对1
    protected static String ONE_VIRTUAL_ASKPID = "one_virtual_askpid";//虚拟用户
    protected static String ONE_VIRTUAL_TAGID = "one_virtual_tagid";//达人标签
    protected static String ONE_VIRTUAL_INVITEPID = "one_virtual_invitepid";//邀请的达人


    //显示提问
    protected static String TIME_VIRTUAL_ASKPID = "time_virtual_askpid";//虚拟用户
    protected static String TIME_VIRTUAL_TAGID = "time_virtual_tagid";//达人标签
    protected static String TIME_VIRTUAL_TIMELIMIT = "time_virtual_timelimit";//设置时间

    public boolean remote(String wikikey, long wikiGameresId) {
        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            String domain = WebappConfig.get().getDomain();
            if (domain.equals("joyme.dev") || domain.equals("joyme.test")) {
                domain = "joyme.alpha";
            }
            HttpResult result = httpClientManager.post("http://hezuo." + domain + "/wiki/index.php", new HttpParameter[]{
                    new HttpParameter("c", "wiki"),
                    new HttpParameter("a", "getwikibykey"),
                    new HttpParameter("wikikey", wikikey)
            }, null);
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            if (result.getReponseCode() != 200) {
                return false;
            }
            int i = jsonObject.getInt("rs");
            if (i == 1) {
                JSONObject resultJsonObject = jsonObject.getJSONObject("result");
                if (resultJsonObject != null && i > 0) {
                    String wikiIcon = resultJsonObject.getString("wiki_icon");
                    if (!StringUtil.isEmpty(wikiIcon)) {
                        AskServiceSngl.get().modifyWikiGameres(wikiGameresId, new UpdateExpress().set(WikiGameresField.HEADPIC, wikiIcon));
                    }
                }
            }
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }

    public boolean modifyWikiStatus(Long gameId, String status) {
        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            String domain = WebappConfig.get().getDomain();
            if (domain.equals("joyme.dev") || domain.equals("joyme.test")) {
                domain = "joyme.alpha";
            }
            HttpResult result = httpClientManager.post("http://hezuo." + domain + "/wiki/index.php", new HttpParameter[]{
                    new HttpParameter("c", "wiki"),
                    new HttpParameter("a", "ewstatusbygid"),
                    new HttpParameter("gameid", gameId),
                    new HttpParameter("status", status)
            }, null);
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            if (result.getReponseCode() != 200) {
                return false;
            }

            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }
}
