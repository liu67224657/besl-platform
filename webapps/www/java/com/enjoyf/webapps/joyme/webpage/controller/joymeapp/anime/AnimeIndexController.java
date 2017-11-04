package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.anime;


import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.JoymeAppHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AnimeIndex;
import com.enjoyf.platform.service.joymeapp.AnimeIndexField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeConfigDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.anime.AnimeIndexDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/anime")
public class AnimeIndexController extends AbstractAnimeBaseController {

    private JoymeAppHotdeployConfig config = HotdeployConfigFactory.get().getConfig(JoymeAppHotdeployConfig.class);

    @ResponseBody
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response

    ) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);
            String appkey = joymeAppClientConstant.getAppkey();
            int platform = joymeAppClientConstant.getPlatform();
            String flag = getFlag();
            if (StringUtil.isEmpty(appkey)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("appkey.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            MongoQueryExpress mongoQueryExpress = new MongoQueryExpress();
            if ("true".equals(flag)) {
                Set<String> setValid = new HashSet<String>();
                setValid.add(ValidStatus.INVALID.getCode());
                setValid.add(ValidStatus.VALID.getCode());

                mongoQueryExpress.add(MongoQueryCriterions.in(AnimeIndexField.VALID_STATUS, setValid.toArray()));
            } else {
                mongoQueryExpress.add(MongoQueryCriterions.eq(AnimeIndexField.VALID_STATUS, ValidStatus.VALID.getCode()));
            }
            mongoQueryExpress.add(MongoQueryCriterions.eq(AnimeIndexField.PLATFORM, platform));
            mongoQueryExpress.add(MongoQueryCriterions.eq(AnimeIndexField.APPKEY, appkey));
            List<AnimeIndex> list = JoymeAppServiceSngl.get().queryAnimeIndex(mongoQueryExpress);
            if (CollectionUtil.isEmpty(list)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("anime.onepiece.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            List<AnimeIndexDTO> animeIndexDTOList = new ArrayList<AnimeIndexDTO>();
            for (int i = 0; i < list.size(); i++) {
                for (AnimeIndex animeIndex : list) {
                    if (Integer.parseInt(animeIndex.getCode().split("_")[1]) != i) {
                        continue;
                    }
                    AnimeIndexDTO animeIndexDTO = new AnimeIndexDTO();
                    animeIndexDTO.setDesc(StringUtil.isEmpty(animeIndex.getDesc()) ? "" : animeIndex.getDesc());
                    animeIndexDTO.setJi(StringUtil.isEmpty(animeIndex.getLinkUrl()) ? "" : animeIndex.getLinkUrl());
                    animeIndexDTO.setJt(animeIndex.getAnimeRedirectType().getCode() + "");
                    animeIndexDTO.setTips(StringUtil.isEmpty(animeIndex.getSuperScript()) ? "" : animeIndex.getSuperScript());
                    animeIndexDTO.setTitle(StringUtil.isEmpty(animeIndex.getTitle()) ? "" : animeIndex.getTitle());
                    animeIndexDTO.setNavtitle(StringUtil.isEmpty(animeIndex.getLine_name()) ? "" : animeIndex.getLine_name());
                    animeIndexDTO.setWikinum(StringUtil.isEmpty(animeIndex.getWikiPageNum()) ? "" : animeIndex.getWikiPageNum());
                    animeIndexDTO.setPicurl(StringUtil.isEmpty(animeIndex.getPic_url()) ? "" : URLUtils.getJoymeDnUrl(animeIndex.getPic_url()));
                    animeIndexDTOList.add(animeIndexDTO);
                }
            }


            //保证:事典、追番、海湾顺序
            Collections.sort(animeIndexDTOList, new Comparator<AnimeIndexDTO>() {
                @Override
                public int compare(AnimeIndexDTO o1, AnimeIndexDTO o2) {
                    int jt1 = Integer.valueOf(o1.getJt());
                    int jt2 = Integer.valueOf(o2.getJt());
                    return jt1 < jt2 ? 1 : (o1 == o2 ? 0 : -1);
                }
            });


            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("rows", animeIndexDTOList);
            msg.setMsg("success");
            msg.setResult(returnMap);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    //客户端加载配置文件
    @ResponseBody
    @RequestMapping(value = "/rule")
    public String rule(HttpServletRequest request) {
        //String ruleUrl_dev = "http://wikidev.enjoyf.com/images/vlr_piece.json";
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            //String ruleUrl_json = config.getJoymeAppConfig().getAnimeRuleText();
            //Map<String, String> map = new HashMap<String, String>();
            // map.put("rows", ruleUrl_json);
            //  msg.setResult(ruleUrl_json);

            Map<String, Object> map = new HashMap<String, Object>();

            //优酷
            Map<String, Object> youku = new HashMap<String, Object>();
            List<String> youkuList = new ArrayList<String>();
            youkuList.add("var _m3u8 = document.getElementById(\"youku-html5player-video\").getAttribute('src');if(_m3u8){var _newM3u8 =_m3u8.replace(\\/vid\\/([\\\\d]+)\\/ , \"vid\\/\"+ videoId);document.getElementById(\"youku-html5player-video\").setAttribute('src', _newM3u8);");

            youku.put("agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11A501");
            youku.put("commands", youkuList);
            youku.put("commands", youkuList);
            map.put("http://v.youku.com", youku);


            //搜狐
            Map<String, Object> sohu = new HashMap<String, Object>();
            List<String> sohuList = new ArrayList<String>();
            sohuList.add("var doc = document.getElementsByTagName('head')[0];var script = document.createElement('script');script.type = 'text/javascript';var funstr = \"function myFunction() {var htmstr = document.getElementsByTagName('body')[0].innerHTML; if (htmstr.substr(0,6) == 'jsonp1'){htmstr = htmstr.replace('jsonp1', '');htmstr = htmstr.replace('\"; funstr+='\"highVid\":'; funstr+=\"', '<video ><source src=');\";funstr+=\"htmstr =htmstr.replace('\";funstr+=',\"ip\":';funstr+=\"', '/></video>');\";funstr+=\"document.getElementsByTagName('body')[0].innerHTML = htmstr; }else{ var h = ''+(new Date).getTime();\";funstr+=\"var sig=h._shift_en([23, 12, 131, 1321]);\";var sohuUrl = 'http://pad.tv.sohu.com/playinfo?callback=jsonp1';funstr+=\"sohuUrl +='&sig='+ sig;\";funstr+=\"sohuUrl += '&vid=' + vid;\";funstr+=\"var vidstr = ''+vid;\";funstr+=\"sohuUrl += '&key=' + vidstr._shift_en([23, 12, 131, 1321]);\";funstr+=\"sohuUrl +='&playlistid=' + playlistId;\";funstr+=\"window.location.href=sohuUrl;}\";funstr+=\"}\"; script.text =  funstr;doc.appendChild(script);");
            sohuList.add("myFunction();");
            sohu.put("agent", "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53");
            sohu.put("commands", sohuList);
            map.put("http://pad.tv.sohu.com", sohu);


            Map<String, Object> msohu = new HashMap<String, Object>();
            msohu.put("agent", "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53");
            msohu.put("commands", sohuList);
            map.put("http://m.tv.sohu.com", msohu);

            //乐视
            Map<String, Object> letv = new HashMap<String, Object>();
            letv.put("agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11A501");
            List<String> letvList = new ArrayList<String>();
            letvList.add("window.__PLAYER__.play();");
            letv.put("commands", letvList);
            map.put("http://m.letv.com", letv);

            //土豆
            Map<String, Object> tudou = new HashMap<String, Object>();
            tudou.put("agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11A501");
            List<String> tudouList = new ArrayList<String>();
            tudouList.add("$('#playBtn').togglePlay();");
            tudou.put("commands", tudouList);
            map.put("http://www.tudou.com", tudou);


            //PPTV
            Map<String, Object> pptv = new HashMap<String, Object>();
            pptv.put("agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11A501");
            List<String> pptvList = new ArrayList<String>();
            pptv.put("commands", pptvList);
            map.put("http://m.pptv.com", pptv);

            //pps
            Map<String, Object> pps = new HashMap<String, Object>();
            pps.put("agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11A501");
            List<String> ppsList = new ArrayList<String>();
            ppsList.add("window.__PLAYER__.play();");
            pps.put("commands", ppsList);
            map.put("http://m.pps.tv", pps);

            Map<String, Object> ruleMap = new HashMap<String, Object>();
            ruleMap.put("rule", map);
            ruleMap.put("tvversion", "2");
            msg.setResult(ruleMap);
            msg.setMsg("success");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }


    //客户端加载配置文件
    @ResponseBody
    @RequestMapping(value = "/config")
    public String config(HttpServletRequest request) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            AnimeConfigDTO dto = new AnimeConfigDTO();
            dto.setDownload(config.getJoymeAppConfig().getAnimeDownLoad());
            dto.setOpenad(config.getJoymeAppConfig().getAnimeOpenAd());
            dto.setTvversion(config.getJoymeAppConfig().getAnimeRuleVersion());

            Map<String, AnimeConfigDTO> map = new HashMap<String, AnimeConfigDTO>();
            map.put("rows", dto);
            msg.setResult(map);
            msg.setMsg("success");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }
}
