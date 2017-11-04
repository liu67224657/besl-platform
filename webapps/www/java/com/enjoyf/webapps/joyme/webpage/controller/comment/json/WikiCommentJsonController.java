package com.enjoyf.webapps.joyme.webpage.controller.comment.json;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReplyField;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.comment.LiveCommentDTO;
import com.enjoyf.webapps.joyme.webpage.controller.comment.AbstractCommentController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/wikicomment")
public class WikiCommentJsonController extends AbstractCommentController {

    private static final CommentDomain UGCWIKI_DOMAIN = CommentDomain.UGCWIKI_COMMENT;

    @ResponseBody
    @RequestMapping("/user/commentsum")
    public String post(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "wikikey", required = false) String wikiKey,
                      @RequestParam(value = "uids", required = false) String uids,
                      @RequestParam(value = "startdate", required = false) String start,
                      @RequestParam(value = "enddate", required = false) String end
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
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
//todo 新版修改
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.in(ProfileField.UID, uidSet.toArray()));
//            List<Profile> profiles = UserCenterServiceSngl.get().queryProfile(queryExpress);
            List<Profile> profiles = UserCenterServiceSngl.get().listProfilesByIds(uidSet);

            Map<String, Integer> map = new HashMap<String, Integer>();
            if(!CollectionUtil.isEmpty(profiles)){
                for(Profile profile:profiles){
                    int sum = CommentServiceSngl.get().countCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.SUBKEY, wikiKey))
                            .add(QueryCriterions.eq(CommentReplyField.DOMAIN, UGCWIKI_DOMAIN.getCode()))
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
        return ResultCodeConstants.SUCCESS.getJsonString(callback);
    }
}
