package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * Created by zhimingli on 2017/5/26.
 * Created
 * CreatedCreated
 */
@Controller
@RequestMapping("/comment/api")
public class ApiCommentController {


    @RequestMapping("/commentbean")
    @ResponseBody
    public String commentbean(HttpServletRequest request,
                              @RequestParam(value = "commentids", required = false) String commentids) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(commentids)) {
                return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Set<String> commentIdSet = new HashSet<String>();
            for (String commentid : commentids.split(",")) {
                commentIdSet.add(commentid);
            }

            Map<String, CommentBean> map = CommentServiceSngl.get().queryCommentBeanByIds(commentIdSet);


            List<CommentBeanDTO> returnList = new ArrayList<CommentBeanDTO>();
            for (String commentId : map.keySet()) {
                CommentBean bean = map.get(commentId);
                if (bean == null) {
                    continue;
                }
                returnList.add(new CommentBeanDTO(commentId, bean.getTotalRows()));
            }


            jsonObject.put("result", returnList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    public class CommentBeanDTO implements Serializable {
        private String commentid;
        private int totalRows;

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }

        public CommentBeanDTO(String commentid, int totalRows) {
            this.commentid = commentid;
            this.totalRows = totalRows;
        }

        public CommentBeanDTO() {
        }
    }

}
