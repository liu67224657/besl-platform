package com.enjoyf.webapps.tools.webpage.controller.operate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-4 下午1:47
 * Description:
 */
@Controller
@Deprecated
@RequestMapping(value = "/operate")
public class OperateController extends BaseOperateController {

    @RequestMapping("/page")
    public ModelAndView operatePage() {
        return new ModelAndView("/operate/operatepage");
    }

    @RequestMapping("/dorefreshindex")
    public ModelAndView refreshIndex() {
        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            doRefreshWebCacheIndex();
//
//            String indexUrl = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getFetcherIndexUrl();
//            String html = IndexFetcher.get().fetcherIndex(indexUrl);
//
//            if (!StringUtil.isEmpty(html)) {
//                IndexCache indexCache = new IndexCache();
//                indexCache.setHtml(html);
//                indexCache.setIndexCacheType(IndexCacheType.PC);
//                MiscServiceSngl.get().createIndexCache(indexCache);
//                map.put("indexHtml", html);
//            } else {
//                GAlerter.lab(this.getClass().getName() + " fetch index error");
//                map.put("indexHtml", "fetch index error");
//            }
//
//            String indexMobileUrl = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getFetcherIndexUrl();
//            String mobile = IndexFetcher.get().fetcherIndex(indexMobileUrl);
//
//            if (!StringUtil.isEmpty(mobile)) {
//                IndexCache mobileCache = new IndexCache();
//                mobileCache.setHtml(mobile);
//                mobileCache.setIndexCacheType(IndexCacheType.MOBILE);
//                MiscServiceSngl.get().createIndexCache(mobileCache);
//                map.put("mobileHtml", html);
//            } else {
//                GAlerter.lab(this.getClass().getName() + " fetch index error");
//                map.put("mobileHtml", "fetch mobileindex error");
//            }
//
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " ocured ServiceException.e:", e);
//        }

        return new ModelAndView("/operate/refreshindex", map);
    }

//    private void doRefreshWebCacheIndex() {
//        String url = "http://webcache." + WebappConfig.get().getDomain() + "/refresh/index.do";
//        GAlerter.lan("==============================request webcache refresh:"+url);
//        HttpClientManager clientManager = new HttpClientManager();
//        HttpResult result = clientManager.post(url, new HttpParameter[]{}, null);
//        GAlerter.lan("==============================request webcache refresh:" + result.toString());
//    }

    //todo notuse
    //        Map<String, Object> map = new HashMap<String, Object>();
//
//        JoymeOperate joymeOperate = new JoymeOperate();
//        joymeOperate.setContent("");
//        joymeOperate.setCreateTime(new Date());
//        joymeOperate.setCreateUserId(getCurrentUser().getUsername());
//        joymeOperate.setOperateType(JoymeOperateType.REFRESH_INDEX);
//
//        FiveProps servProps = Props.instance().getServProps();
//        if (servProps == null || servProps.get("webapps.NAME") == null || servProps.get("webapps.NAME").length() == 0) {
//            joymeOperate.setServerId("tools");
//        } else {
//            joymeOperate.setServerId(servProps.get("webapps.NAME"));
//        }
//
//        try {
//            MiscServiceSngl.get().createOperate(joymeOperate);
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " ocured ServiceException.e:", e);
//            map.put("key", "operate.success." + JoymeOperateType.REFRESH_INDEX.getCode());
//            return new ModelAndView("/operate/operateresult",putErrorMessage(map,"system.error"));
//        }
//
//        map.put("key", "operate.success." + JoymeOperateType.REFRESH_INDEX.getCode());

}
