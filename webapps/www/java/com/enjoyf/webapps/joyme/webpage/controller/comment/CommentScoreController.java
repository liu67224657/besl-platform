package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentReplyField;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ScoreDTO;
import com.enjoyf.webapps.joyme.dto.comment.ScoreEntity;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-17
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/comment/score")
public class CommentScoreController {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "unikey", required = false) String uniKey,
                             @RequestParam(value = "domain", required = false) String domain,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                             @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                mapMessage.put("message", "param.unikey.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("uniKey", uniKey);
            String[] keyArr = uniKey.split("\\|");
            if (!CollectionUtil.isEmpty(keyArr)) {
                String key = keyArr[0];
                mapMessage.put("key", key);
            }

            if (StringUtil.isEmpty(domain)) {
                mapMessage.put("message", "param.domain.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("domain", domain);

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(MD5Util.Md5(uniKey + domain));
            if (commentBean == null) {
                mapMessage.put("message", "comment.bean.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("uri", commentBean.getUri());

            ScoreEntity scoreEntity = commentWebLogic.buildScoreEntity(commentBean);

            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryReplyByOrderField(commentBean, pageNum, pageSize, CommentReplyField.AGREE_SUM, QuerySortOrder.DESC);
            ScoreDTO returnDTO = new ScoreDTO();
            returnDTO.setMainreplys(mainReplyRows);
            returnDTO.setScore(scoreEntity);

            mapMessage.put("dto", returnDTO);
        } catch (Exception e) {
            GAlerter.lab("CommentScoreController occur Exception.e:", e);
            mapMessage.put("errorMessage", "forign.content.null");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/comment/comment-score-wap", mapMessage);
    }

}
