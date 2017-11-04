package com.enjoyf.webapps.joyme.webpage.controller.point.webview;

import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.point.ActionHistoryDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengxu on 2016/12/7.
 */
@Deprecated//todo 用戶激励体系删除
@Controller
@RequestMapping(value = "/usercenter/mypoint")
public class MyPointController extends BaseRestSpringController {

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;


    @RequestMapping(value = "/list")
    public ModelAndView myPointList(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                                    @RequestParam(value = "psize", required = false, defaultValue = "10") Integer psize,
                                    @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
            mapMessage.put("isnotlogin", "true");
            return new ModelAndView("/views/jsp/point/mypoint", mapMessage);
        }
        try {
            String profiileId = userSession.getProfileId();
            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profiileId);

            mapMessage.put("userPoint", userPoint);
            mapMessage.put("monthHistoryList", "");
            mapMessage.put("allHistoryList", "");
            mapMessage.put("page", "");

            Pagination pagination = new Pagination(p * psize, p, psize);
            PageRows<PointActionHistory> pointActionHistoryPageRows = null;
//            if (StringUtil.isEmpty(flag)) {
//                QueryExpress queryExpress = new QueryExpress();
//                queryExpress.add(QueryCriterions.eq(PointActionHistoryField.PROFILEID, userSession.getProfileId()));
//                queryExpress.add(QueryCriterions.eq(PointActionHistoryField.POINTKEY, userPoint.getPointKey()));
//                queryExpress.add(QueryCriterions.eq(PointActionHistoryField.HISTORYACTIONTYPE, HistoryActionType.POINT.getCode()));
//                if (StringUtil.isEmpty(type)) {
//                    queryExpress.add(QueryCriterions.gt(PointActionHistoryField.POINTVALUE, 0));
//                } else {
//                    queryExpress.add(QueryCriterions.lt(PointActionHistoryField.POINTVALUE, 0));
//                }
//                mapMessage.put("type", type);
//
//                queryExpress.add(QuerySort.add(PointActionHistoryField.ACTIONDATE, QuerySortOrder.DESC));
//
//                pointActionHistoryPageRows = PointServiceSngl.get().queryPointActionHistoryByPage(queryExpress, pagination);
//            } else {
            String flag = "increase";
            if (!StringUtil.isEmpty(type)) {
                flag = "reduce";
            }
            mapMessage.put("type", type);
            pointActionHistoryPageRows = PointServiceSngl.get().queryMyPointByCache(profiileId, flag, userPoint.getPointKey(), pagination);
//            }
            if (pointActionHistoryPageRows != null && !CollectionUtil.isEmpty(pointActionHistoryPageRows.getRows())) {
                Map<String, List<ActionHistoryDTO>> map = pointWebLogic.buildActionHistoryDTO(pointActionHistoryPageRows.getRows());
                mapMessage.put("monthHistoryList", map.get("monthHistoryList"));
                mapMessage.put("allHistoryList", map.get("allHistoryList"));
            }
            mapMessage.put("page", pointActionHistoryPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        return new ModelAndView("/views/jsp/point/mypoint", mapMessage);
    }
}
