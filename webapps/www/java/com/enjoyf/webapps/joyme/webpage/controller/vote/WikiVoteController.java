package com.enjoyf.webapps.joyme.webpage.controller.vote;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVote;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVoteSet;
import com.enjoyf.webapps.joyme.dto.vote.WikiVoteDTO;
import com.enjoyf.webapps.joyme.weblogic.vote.VoteWebLogic;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午1:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/wiki/vote")
public class WikiVoteController {

    @Resource(name = "voteWebLogic")
    private VoteWebLogic voteWebLogic;

    @ResponseBody
    @RequestMapping("/get")
    public String getVote(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "url", required = false) String url,
                          @RequestParam(value = "pic", required = false) String pic,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "num", required = false) String num,
                          @RequestParam(value = "sum", required = false) String sum) {
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);
        if (StringUtil.isEmpty(url)) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("param.curl.is.empty");
            return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
        }

        try {
            Pattern pattern = Pattern.compile("[0-9]+");
            int voteSum = 0;
            if (StringUtil.isEmpty(sum) || (!pattern.matcher(sum).matches())) {
                voteSum = 0;
            } else {
                voteSum = Integer.parseInt(sum);
            }
            url = URLDecoder.decode(url, "UTF-8");
            if (!url.startsWith("http://")) {
                url = "http://" + url;
            }
            if (url.endsWith(".shtml")) {
                Long articleId = Long.parseLong(url.substring(url.lastIndexOf("/") + 1, url.indexOf(".shtml")));
                String keyWords = "";
                if (url.indexOf(Constant.DOMAIN) > 0) {
                    if (url.startsWith("http://www.")) {
                        keyWords = url.substring(url.replaceAll("/" + articleId, "").lastIndexOf("/") + 1, url.indexOf("/" + articleId));
                    } else {
                        keyWords = url.substring(url.indexOf("http://"), url.indexOf("." + Constant.DOMAIN)).replaceAll("http://", "");
                    }
                }

                WikiVoteDTO wikiVote = voteWebLogic.getWikiVote(url, pic, name, num, articleId, keyWords, voteSum);
                resultObjectMsg.setRs(ResultObjectMsg.CODE_S);
                resultObjectMsg.setMsg("success");
                resultObjectMsg.setResult(wikiVote);
                return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
            } else {

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
        }
        return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
    }

    @ResponseBody
    @RequestMapping("/inc")
    public String incVote(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "url", required = false) String url
    ) {
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);
        if (StringUtil.isEmpty(url)) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("param.curl.is.empty");
            return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
        }

        try {
            url = URLDecoder.decode(url, "UTF-8");
            if (!url.startsWith("http://")) {
                url = "http://" + url;
            }
            if (url.endsWith(".shtml")) {
                Long articleId = Long.parseLong(url.substring(url.lastIndexOf("/") + 1, url.indexOf(".shtml")));

                voteWebLogic.incWikiVote(url, articleId);
                resultObjectMsg.setRs(ResultObjectMsg.CODE_S);
                resultObjectMsg.setMsg("success");
                resultObjectMsg.setResult(true);
                return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
            } else {

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
        }
        return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
    }

    @ResponseBody
    @RequestMapping("/query")
    public String queryVotes(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "url", required = false) String pageurl,
                             @RequestParam(value = "jsonarr", required = false) String jsonarr) {
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_E);

        try {
            if (!StringUtil.isEmpty(pageurl)) {
                pageurl = URLDecoder.decode(pageurl, "UTF-8");
                String keyWords = "";
                if (pageurl.indexOf(Constant.DOMAIN) > 0) {
                    if (pageurl.startsWith("http://www.")) {
                        String subStr = pageurl.substring(pageurl.lastIndexOf("/"), pageurl.length());
                        String str = pageurl.replaceAll(subStr, "");
                        keyWords = str.substring(str.lastIndexOf("/") + 1, str.length());
                    } else {
                        keyWords = pageurl.substring(pageurl.indexOf("http://"), pageurl.indexOf("." + Constant.DOMAIN)).replaceAll("http://", "");
                    }
                }

                List<WikiVoteDTO> wikiVoteList = voteWebLogic.queryWikiVoteByKey(keyWords);
                resultObjectMsg.setRs(ResultObjectMsg.CODE_S);
                resultObjectMsg.setMsg("success");
                resultObjectMsg.setResult(wikiVoteList);
                return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);

            } else if (!StringUtil.isEmpty(jsonarr)) {
                JSONWikiVoteSet voteSet = JSONWikiVoteSet.parse(jsonarr);
                if (CollectionUtil.isEmpty(voteSet.getVoteSet())) {
                    resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                    resultObjectMsg.setMsg("param.error");
                    return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
                }

                Set<Long> idSet = new HashSet<Long>();
                for (JSONWikiVote vote : voteSet.getVoteSet()) {
                    String url = URLDecoder.decode(vote.getUrl(), "UTF-8");
                    if (url.endsWith(".shtml")) {
                        Long articleId = Long.parseLong(url.substring(url.lastIndexOf("/") + 1, url.indexOf(".shtml")));
                        if (articleId != null && articleId > 0l) {
                            idSet.add(articleId);
                        }
                    }
                }

                if (!CollectionUtil.isEmpty(idSet)) {
                    List<WikiVoteDTO> wikiVoteList = voteWebLogic.queryWikiVotes(idSet);
                    resultObjectMsg.setRs(ResultObjectMsg.CODE_S);
                    resultObjectMsg.setMsg("success");
                    resultObjectMsg.setResult(wikiVoteList);
                    return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg("system.error");
            return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
        }
        return JsonBinder.buildNonDefaultBinder().toJson(resultObjectMsg);
    }

}
