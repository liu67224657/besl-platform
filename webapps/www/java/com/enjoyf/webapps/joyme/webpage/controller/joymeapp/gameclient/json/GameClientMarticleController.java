package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheatFiled;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheatType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.TagDedearchiveCheatDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli
 * Date: 2015/01/24
 * Time: 13:52
 */
@Controller
@RequestMapping(value = "/joymeapp/gameclient/webview/marticle")
public class GameClientMarticleController extends AbstractGameClientBaseController {

    private static String TYPE = "yk";

    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;


    @ResponseBody
    @RequestMapping(value = "/getcheat")
    public String getcheat(HttpServletRequest request) {

        ResultObjectMsg msg = new ResultObjectMsg();
        msg.setRs(ResultObjectMsg.CODE_S);
        msg.setMsg("SUCCESS");
        msg.setResult("");
        try {
            String archiveid = HTTPUtil.getParam(request, "archiveid");
            if (StringUtil.isEmpty(archiveid)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("archiveid.is.null");
            }

            //优酷首页批量获取阅读数
            String ykindex = HTTPUtil.getParam(request, "ykindex");
            if (!StringUtil.isEmpty(ykindex)) {
                msg.setResult(getYkIndexCheat(archiveid));
                return "getcheatcallback([" + JsonBinder.buildNormalBinder().toJson(msg) + "])";
            }

            //发送任务
            Map<String, String> JParam = HTTPUtil.getJParam(request.getParameter("JParam"));
            String appkey = StringUtil.isEmpty(HTTPUtil.getParam(request, "appkey")) ? JParam.get("appkey") : HTTPUtil.getParam(request, "appkey");
            String uid = StringUtil.isEmpty(HTTPUtil.getParam(request, "uid")) ? JParam.get("uid") : HTTPUtil.getParam(request, "uid");
            String platform = StringUtil.isEmpty(HTTPUtil.getParam(request, "platform")) ? JParam.get("platform") : HTTPUtil.getParam(request, "platform");
            String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? JParam.get("clientid") : HTTPUtil.getParam(request, "clientid");
            if (StringUtil.isEmpty(clientId)) {
                clientId = request.getHeader("User-Agent");
            }

            if (!StringUtil.isEmpty(appkey) && !StringUtil.isEmpty(uid) && !StringUtil.isEmpty(platform)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
                if (profile != null) {
                    //浏览3篇话题文章
                    gameClientWebLogic.sendEventTask(TaskAction.VIEW_ARTICLE, getIp(request), platform, appkey, profile, archiveid, clientId);
                }
            }

            Long arch = Long.valueOf(archiveid);
            TagDedearchiveCheat cheat = JoymeAppServiceSngl.get().getTagDedearchiveCheat(arch);
            if (cheat != null) {
                msg.setResult(cheat);
            }

            String type = HTTPUtil.getParam(request, "type");

            //阅读数+1
            UpdateExpress up = new UpdateExpress();

            //如果是优酷，更新下type
            if (TYPE.equals(type)) {
                up.set(TagDedearchiveCheatFiled.ARCHIVE_TYPE, TagDedearchiveCheatType.YOUKU.getCode());
            }
            up.increase(TagDedearchiveCheatFiled.READ_NUM, 2 + new Random().nextInt(6));
            up.increase(TagDedearchiveCheatFiled.REAL_READ_NUM, 1);
            JoymeAppServiceSngl.get().modifyTagDedearchiveCheat(arch, up);

        } catch (Exception e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return "getcheatcallback([" + JsonBinder.buildNormalBinder().toJson(msg) + "])";
    }

    @ResponseBody
    @RequestMapping(value = "/agreecheat")
    public String agreecheat(HttpServletRequest request) {
        ResultObjectMsg msg = new ResultObjectMsg();
        msg.setRs(ResultObjectMsg.CODE_S);
        msg.setMsg("SUCCESS");
        msg.setResult("");
        try {
            String archiveid = HTTPUtil.getParam(request, "archiveid");
            if (StringUtil.isEmpty(archiveid)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("archiveid.is.null");
            }


            //发送任务
            Map<String, String> JParam = HTTPUtil.getJParam(request.getParameter("JParam"));
            String appkey = StringUtil.isEmpty(HTTPUtil.getParam(request, "appkey")) ? JParam.get("appkey") : HTTPUtil.getParam(request, "appkey");
            String uid = StringUtil.isEmpty(HTTPUtil.getParam(request, "uid")) ? JParam.get("uid") : HTTPUtil.getParam(request, "uid");
            String platform = StringUtil.isEmpty(HTTPUtil.getParam(request, "platform")) ? JParam.get("platform") : HTTPUtil.getParam(request, "platform");
            String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? JParam.get("clientid") : HTTPUtil.getParam(request, "clientid");
            if (StringUtil.isEmpty(clientId)) {
                clientId = request.getHeader("User-Agent");
            }

            if (!StringUtil.isEmpty(appkey) && !StringUtil.isEmpty(uid) && !StringUtil.isEmpty(platform)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.valueOf(uid));
                if (profile != null) {
                    //赞1篇文章
                    gameClientWebLogic.sendEventTask(TaskAction.LIKE_ARTICLE, getIp(request), platform, appkey, profile, archiveid, clientId);
                    //赞3篇喜欢的文章
//					gameClientWebLogic.sendEventTask(getLikearticleDailytasks(AppPlatform.getByCode(Integer.valueOf(platform))), getIp(request), platform, appkey, profile);
                }
            }

            Long arch = Long.valueOf(archiveid);
            UpdateExpress up = new UpdateExpress();
            up.increase(TagDedearchiveCheatFiled.AGREE_NUM, 2 + new Random().nextInt(6));
            up.increase(TagDedearchiveCheatFiled.REAL_AGREE_NUM, 1);
            boolean bval = JoymeAppServiceSngl.get().modifyTagDedearchiveCheat(arch, up);
            if (bval == false) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("read.fail");
            }
        } catch (Exception e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return "agreecheatcallback([" + JsonBinder.buildNormalBinder().toJson(msg) + "])";
    }


    private List<TagDedearchiveCheatDTO> getYkIndexCheat(String archiveid) {
        String archiveidArr[] = archiveid.split(",");
        Set<Long> dedeIds = new HashSet<Long>();
        for (String id : archiveidArr) {
            if (!StringUtil.isEmpty(id)) {
                dedeIds.add(Long.valueOf(id));
            }
        }
        Map<Long, TagDedearchiveCheat> cheatMap = null;
        List<TagDedearchiveCheatDTO> list = new ArrayList<TagDedearchiveCheatDTO>();
        try {
            cheatMap = JoymeAppServiceSngl.get().getMapTagDedearchiveCheat(dedeIds);
            for (Long id : cheatMap.keySet()) {
                TagDedearchiveCheat cheat = cheatMap.get(id);
                if (cheat == null) {
                    continue;
                }
                TagDedearchiveCheatDTO dto = new TagDedearchiveCheatDTO();
                dto.setArchivesid(String.valueOf(cheat.getDede_archives_id()));
                dto.setReadnum(String.valueOf(cheat.getRead_num()));
                list.add(dto);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }

}
