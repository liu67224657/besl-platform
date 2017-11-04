package com.enjoyf.webapps.joyme.webpage.controller.youku.api;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.youku.YoukuAppDTO;
import com.enjoyf.webapps.joyme.weblogic.youku.YoukuGameWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.youku.AbstractYoukuController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/18
 * Description:
 */
@Controller
@RequestMapping("/youku/api/app")
public class YoukuApiAppController extends AbstractYoukuController {


    @Resource(name = "youkuGameDbWebLogic")
    private YoukuGameWebLogic youkuAppWebLogic;

    /**
     * 通过积分墙的appid获取app
     * amount = 20;
     * guid = 84223b379879bb1a205b1925792df5a2;
     * idfa = "866D639E-1DFE-4684-B014-5BEAB4C9C7B7";
     * network = WIFI;
     * ouid = 2d996d459f7e9ce7a8afae4dbe4c435bfdadfca9;
     * pid = ebbe0b1509bb6cd9;
     * product_id = 2;
     * subpage = allchannel;
     * vdid = "799B212A-7D71-4861-9EAB-C2D899D71765";
     * ver = "4.2";
     * <p/>
     * response json
     * <p/>
     * {
     * "api.expired_at" = 1434515135;
     * games =     (
     * {
     * appname = "Temple Run 2";
     * charge = 0;
     * desc = "Temple Run\U62e5\U6709\U8d85\U8fc7170\U4e07\U7684\U4e0b\U8f7d\U91cf\Uff0c\U91cd\U65b0\U5b9a\U4e49\U4e86\U79fb\U52a8\U6e38\U620f\U3002\U5728Temple Run 2\U4e2d\Uff0c\U4f60\U53ef\U8fdb\U884c\U66f4\U591a\U4ee4\U4eba\U632f\U594b\U7684\U5954\U8dd1\U3001\U8df3\U8dc3\U3001\U8f6c\U5f2f\U548c\U6ed1\U52a8\Uff0c\U968f\U4f60\U6240\U7231\Uff01\n\n\U5f53\U4f60\U8bd5\U56fe\U9003\U8131\U8bc5\U5492\U7684\U795e\U50cf\U65f6\Uff0c\U4f60\U4f1a\U9014\U5f84\U5371\U9669\U7684\U60ac\U5d16\U3001\U62c9\U94fe\U7ebf\U3001\U77ff\U5c71\U548c\U68ee\U6797\U3002\U770b\U770b\U4f60\U80fd\U8dd1\U591a\U8fdc\U5427\Uff01\n\U7279\U5f81\Uff1a\n\n*\U7f8e\U4e3d\U7684\U65b0\U56fe\U5f62\n*\U534e\U4e3d\U7684\U65b0\U578b\U6709\U673a\U73af\U5883\n*\U65b0\U969c\U788d\n*\U66f4\U591a\U80fd\U91cf\U805a\U96c6\n*\U66f4\U591a\U6210\U7ee9\n*\U6bcf\U4e2a\U89d2\U8272\U90fd\U6709\U7279\U522b\U6743\U529b\n*\U66f4\U5927\U7684\U7334\U5b50\Uff01";
     * id = 197;
     * "init_downloads" = 456;
     * "ios_idfa_switch" = 0;
     * itunesid = 572395608;
     * logo = "http://r4.ykimg.com/05100000536B412F6714C06E3302A0F3";
     * "new_vids" =             (
     * );
     * platform = 1;
     * price = 0;
     * "rec_tags" = "";
     * "rec_words" = "";
     * "recommend_type" = 1;
     * "redirect_type" = 1;
     * "redirect_url" = "";
     * score = "0.0";
     * scroller = "http://r4.ykimg.com/05100000541A88536714C0469F0D5F56";
     * size = "56.5M";
     * "total_downloads" = 456;
     * "upload_time" = "2014-09-18";
     * url = "https://itunes.apple.com/cn/app/temple-run-2/id572395608?mt=8";
     * version = "1.8";
     * vids = "";
     * }
     * );
     * "games_count" = 1;
     * status = success;
     * }
     *
     * @return
     */
    @RequestMapping("/rec")
    @ResponseBody
    public String rec(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        try {
            String gameid = request.getParameter("gameid");

            if (gameid == null) {
                jsonObject.put("status", "error");
                return jsonObject.toString();
            }
            PointwallWallApp pointwallWallApp = PointServiceSngl.get()
                    .getPointwallWallApp(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APP_ID, Long.valueOf(gameid)))
                            .add(QueryCriterions.eq(PointwallWallAppField.APPKEY, APPKEY)
                            ));

            jsonObject.put("api.expired_at", System.currentTimeMillis());
            jsonObject.put("status", "success");
            if (pointwallWallApp != null) {

                YoukuAppDTO gameDTO = youkuAppWebLogic.builAppDTO(pointwallWallApp);
                List<YoukuAppDTO> gameDTOs = new ArrayList<YoukuAppDTO>();
                gameDTOs.add(gameDTO);
                jsonObject.put("games", gameDTO);
            }
            jsonObject.put("games_count", pointwallWallApp != null ? 1 : 0);
            return jsonObject.toString();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            jsonObject.put("status", "error");
        }


        return jsonObject.toString();
    }

    //http://test.ios.gamex.mobile.youku.com/app/ios/recommend/
    @RequestMapping("/recommend")
    @ResponseBody
    public String recommend(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        //todo cache
        Pagination pagination = new Pagination(1 * 10, 1, 10);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(PointwallWallAppField.APPKEY, APPKEY));
        queryExpress.add(QueryCriterions.eq(PointwallWallAppField.STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QueryCriterions.eq(PointwallWallAppField.PLATFORM, AppPlatform.IOS.getCode()));
        queryExpress.add(new QuerySort(PointwallWallAppField.DISPLAY_ORDER, QuerySortOrder.ASC));
        try {
            PageRows<PointwallWallApp> pageRows = PointServiceSngl.get().queryPointwallWallAppByPage(queryExpress, pagination);


            jsonObject.put("api.expired_at", System.currentTimeMillis());
            jsonObject.put("status", "success");
            List<YoukuAppDTO> gameDTOList = youkuAppWebLogic.queryYoukuGameDTO(pageRows.getRows());
            jsonObject.put("games", gameDTOList);
            jsonObject.put("games_count", pageRows.getPage().getTotalRows());
            return jsonObject.toString();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:,", e);
            jsonObject.put("status", "error");
        }

        return jsonObject.toString();
    }
}
