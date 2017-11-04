package com.enjoyf.webapps.joyme.webpage.controller.activity;

import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReplyField;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameOrdered;
import com.enjoyf.platform.service.gameres.gamedb.GameOrderedField;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhimingli on 2015/6/18.
 * 动态调用的controller
 */
@Controller
public class JsonDynamicActivityController extends BaseRestSpringController {

    private static final long ZHENGTU_GAMEID = 100703l;

    /**
     * 一层目录
     */
    @ResponseBody
    @RequestMapping(value = "/activity/json/{path}")
    public String aboutOne(@PathVariable(value = "path") String path, HttpServletRequest request, HttpServletResponse response) {
        //征途手机版用户预约活动
        String callback = request.getParameter("callback");
        if(path.equals("gameordered")){
            String name = request.getParameter("name");
            String weiXin = request.getParameter("weixin");
            String qq = request.getParameter("qq");
            String mobile = request.getParameter("mobile");
            if(StringUtil.isEmpty(name)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                jsonObject.put("msg", "姓名不能为空");
                return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
            }
            if(StringUtil.isEmpty(weiXin)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                jsonObject.put("msg", "微信不能为空");
                return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
            }
            if(StringUtil.isEmpty(qq)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                jsonObject.put("msg", "QQ不能为空");
                return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
            }
            if(StringUtil.isEmpty(mobile)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                jsonObject.put("msg", "电话不能为空");
                return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
            }

            BasicDBObject query = new BasicDBObject();
            BasicDBList orList = new BasicDBList();
            orList.add(new BasicDBObject(GameOrderedField.QQ.getColumn(), qq));
            orList.add(new BasicDBObject(GameOrderedField.WEIXIN.getColumn(), weiXin));
            orList.add(new BasicDBObject(GameOrderedField.MOBILE.getColumn(), mobile));
            query.put("$or", orList);
            try {
                GameOrdered gameOrdered = GameResourceServiceSngl.get().getGameOrdered(query);
                if(gameOrdered != null){
                    if(weiXin.equals(gameOrdered.getWeiXin())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                        jsonObject.put("msg", "该微信号已经预约过了");
                        return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
                    }
                    if(qq.equals(gameOrdered.getQq())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                        jsonObject.put("msg", "该QQ号已经预约过了");
                        return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
                    }
                    if(mobile.equals(gameOrdered.getMobile())){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
                        jsonObject.put("msg", "该电话号已经预约过了");
                        return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
                    }
                }
                GameOrdered newGameOrdered = new GameOrdered();
                newGameOrdered.setMobile(mobile);
                newGameOrdered.setQq(qq);
                newGameOrdered.setWeiXin(weiXin);
                newGameOrdered.setName(name);
                newGameOrdered.setGameId(ZHENGTU_GAMEID);
                GameResourceServiceSngl.get().createGameOrdered(newGameOrdered);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.ERROR.getCode()));
                jsonObject.put("msg", "系统错误！");
                return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
        jsonObject.put("msg", "提交成功");
        return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
    }

    /**
     * 二层目录
     */
    @ResponseBody
    @RequestMapping(value = "/activity/json/{path}/{path2}")
    public String aboutTwo(@PathVariable(value = "path") String sPath, @PathVariable(value = "path2") String sPath2, HttpServletRequest request, HttpServletResponse response) {
        String callback = HTTPUtil.getParam(request, "callback");
        if(sPath.equals("wikicomment") && sPath2.equals("usercomment")){
            String wikiKey = request.getParameter("wikikey");
            String uids = request.getParameter("uids");
            String start = request.getParameter("startdate");
            String end = request.getParameter("enddate");

            if (StringUtil.isEmpty(wikiKey) || StringUtil.isEmpty(uids) || StringUtil.isEmpty(start) || StringUtil.isEmpty(end)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString(callback);
            }

            start += " 00:00:00";
            end += " 23:59:59";

            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = df.parse(start);
                Date endDate = df.parse(end);

                Set<Long> uidSet = new HashSet<Long>();
                if(uids.indexOf(",") > 0){
                    String[] uidArr = uids.split(",");
                    for(String uidStr:uidArr){
                        uidSet.add(Long.valueOf(uidStr));
                    }
                }else{
                    uidSet.add(Long.valueOf(uids));
                }
                //todo 微服务版修改

//                QueryExpress queryExpress = new QueryExpress();
//                queryExpress.add(QueryCriterions.in(ProfileField.UID, uidSet.toArray()));
//                List<Profile> profiles = UserCenterServiceSngl.get().queryProfile(queryExpress);
                List<Profile> profiles = UserCenterServiceSngl.get().listProfilesByIds(uidSet);

                Map<String, Integer> map = new HashMap<String, Integer>();
                if(!CollectionUtil.isEmpty(profiles)){
                    for(Profile profile:profiles){
                        int sum = CommentServiceSngl.get().countCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.SUBKEY, wikiKey))
                                .add(QueryCriterions.eq(CommentReplyField.DOMAIN, CommentDomain.UGCWIKI_COMMENT.getCode()))
                                .add(QueryCriterions.eq(CommentReplyField.REPLY_PROFILEID, profile.getProfileId()))
                                .add(QueryCriterions.leq(CommentReplyField.CREATE_TIME, endDate))
                                .add(QueryCriterions.geq(CommentReplyField.CREATE_TIME, startDate)));
                        map.put(String.valueOf(profile.getUid()), sum);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                    jsonObject.put("result", map);
                    return ResultCodeConstants.resultCheckCallback(jsonObject, callback);
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
                return ResultCodeConstants.ERROR.getJsonString(callback);
            }

        }
        return ResultCodeConstants.SUCCESS.getJsonString(callback);
    }
}
