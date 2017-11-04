/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.search;

import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * search 操作action
 *
 * @author yongmingxu
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController extends BaseRestSpringController {
//    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    private static final String RESULT_LIST_SEARCH_CONTENT = "/views/jsp/search/search-content";
    private static final String RESULT_LIST_SEARCH_BOARD = "/views/jsp/search/search-group";
    private static final String RESULT_LIST_SEARCH_GAME = "/views/jsp/search/search-game";
    private static final String RESULT_LIST_SEARCH_BLOG = "/views/jsp/search/search-profile";

//    private static final int SEARCH_PROFILE_SIZE = 5;
//    private static final int SEARCH_GAME_RIGHT_SIZE = 3;
//    private static final int SEARCH_GROUP_RIGHT_SIZE = 4;

    private static final Pattern PATTERN_SEARCH_KEYWORD_SPLITE = Pattern.compile("[+\\-\\[\\](){}*]");
    /**
     * 博文搜索服务
     */
//    @Resource(name = "searchWebLogic")
//    private SearchWebLogic searchWebLogic;

    /**
     * 搜索小组
     *
     * @param key
     * @param pageNo
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/group/{key}")
    public ModelAndView searchGroup(@PathVariable("key") String key,
                                    @RequestParam(value = "p", required = false) Integer pageNo,
                                    HttpServletRequest request) throws Exception {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("encodeKey", key);
        String decodeKey = URLDecoder.decode(key, "UTF-8");
        mapMessage.put("key", decodeKey);//返回页面显示

//        String view = RESULT_LIST_SEARCH_BOARD;

//        UserSession userSession = this.getUserBySession(request);
//        String uno = userSession == null ? "" : userSession.getBlogwebsite().getUno();
//
//        Set<String> keySet = new HashSet<String>();
//        keySet.add(RegexUtil.replace(decodeKey, PATTERN_SEARCH_KEYWORD_SPLITE, " ", -1));

//        //search group
//        pageNo = pageNo == null ? 0 : pageNo;
//        Pagination boardPage = new Pagination(pageNo * WebappConfig.get().getSearchGroupPageSize(), pageNo, WebappConfig.get().getSearchGroupPageSize());
//        PageRows<GameResource> groupResourceRows = searchWebLogic.searchGroup(decodeKey, boardPage);
//
//        //search profile
//        Pagination profilePage = new Pagination(1 * SEARCH_PROFILE_SIZE, 1, SEARCH_PROFILE_SIZE);
//        PageRows<SocialProfile> profileRows = searchWebLogic.searchProfile(uno, decodeKey, profilePage);
//
//        //search game
//        Pagination gamePage = new Pagination(1 * SEARCH_GAME_RIGHT_SIZE, 1, SEARCH_GAME_RIGHT_SIZE);
//        PageRows<GameResource> gamePageRows = searchWebLogic.searchGame(decodeKey, gamePage);
//
//        List<BoardClientDTO> groupDTOList = searchWebLogic.parseGroup(uno, groupResourceRows.getRows());

//        mapMessage.put("gameList", gamePageRows.getRows());
//        mapMessage.put("profileList", profileRows.getRows());
//        mapMessage.put("groupList", groupDTOList);
//        mapMessage.put("page", groupResourceRows.getPage());
//
//        //sent search event
//        sendOutSearchUserEvent(uno, decodeKey, new Date(), getIp(request), null, UserEventType.USER_ACCOUNT_SEARCH);

        return new ModelAndView(RESULT_LIST_SEARCH_BOARD, mapMessage);
    }


    @RequestMapping(value = "/game/{key}")
    public ModelAndView searchGame(@PathVariable("key") String key,
                                   @RequestParam(value = "p", required = false) Integer pageNo,
                                   HttpServletRequest request) throws Exception {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

//        mapMessage.put("encodeKey", key);
//        String decodeKey = URLDecoder.decode(key, "UTF-8");
//        mapMessage.put("key", decodeKey);//返回页面显示
//
//        String view = RESULT_LIST_SEARCH_GAME;
//
//        UserSession userSession = this.getUserBySession(request);
//        String uno = userSession == null ? "" : userSession.getBlogwebsite().getUno();
//
//        Set<String> keySet = new HashSet<String>();
//        keySet.add(RegexUtil.replace(decodeKey, PATTERN_SEARCH_KEYWORD_SPLITE, " ", -1));
//
//        //search group
//        pageNo = pageNo == null ? 0 : pageNo;
//        Pagination gamePage = new Pagination(pageNo * WebappConfig.get().getSearchGroupPageSize(), pageNo, WebappConfig.get().getSearchGroupPageSize());
//        PageRows<GameResource> gameResourceRows = searchWebLogic.searchGame(decodeKey, gamePage);
//
//        //right
//        //search profile
//        Pagination profilePage = new Pagination(1 * SEARCH_PROFILE_SIZE, 1, SEARCH_PROFILE_SIZE);
//        PageRows<SocialProfile> profileRows = searchWebLogic.searchProfile(uno, decodeKey, profilePage);
//
//        //search game
//        Pagination groupPage = new Pagination(1 * SEARCH_GROUP_RIGHT_SIZE, 1, SEARCH_GROUP_RIGHT_SIZE);
//        PageRows<GameResource> groupPageRows = searchWebLogic.searchGroup(decodeKey, groupPage);
//
//        mapMessage.put("gameList", gameResourceRows.getRows());
//        mapMessage.put("page", gameResourceRows.getPage());
//
//        mapMessage.put("profileList", profileRows.getRows());
//        mapMessage.put("groupList", groupPageRows.getRows());


        //sent search event
//        sendOutSearchUserEvent(uno, decodeKey, new Date(), getIp(request), null, UserEventType.USER_ACCOUNT_SEARCH);

        return new ModelAndView(RESULT_LIST_SEARCH_GAME, mapMessage);
    }

    /**
     * 搜索博文内容
     *
     * @param key
     * @param pageNo
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/content/{key}")
    public ModelAndView searchBlogContent(@PathVariable("key") String key,
                                          @RequestParam(value = "p", required = false) Integer pageNo,
                                          HttpServletRequest request) throws Exception {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String reView = RESULT_LIST_SEARCH_CONTENT;

        mapMessage.put("encodeKey", key);
        String decodeKey = URLDecoder.decode(key, "UTF-8");
        mapMessage.put("key", decodeKey);//返回页面显示

        Set<String> keySet = new HashSet<String>();
        keySet.add(RegexUtil.replace(decodeKey, PATTERN_SEARCH_KEYWORD_SPLITE, " ", -1));

        //
//        UserSession userSession = this.getUserBySession(request);
//        String uno = userSession == null ? "" : userSession.getBlogwebsite().getUno();

        //search content
//        pageNo = pageNo == null ? 0 : pageNo;
//        Pagination p = new Pagination(pageNo * WebappConfig.get().getSearchcontentPageSize(), pageNo, WebappConfig.get().getSearchcontentPageSize());
//        PageRows<BlogContent> blogContentList = searchWebLogic.searchContent(uno, keySet, null, ContentPublishType.ORIGINAL, p);
//
//        //search game
//        Pagination gamePage = new Pagination(1 * SEARCH_GAME_RIGHT_SIZE, 1, SEARCH_GAME_RIGHT_SIZE);
//        PageRows<GameResource> gamePageRows = searchWebLogic.searchGame(decodeKey, gamePage);
//
//        //search board
//        Pagination boardPage = new Pagination(1 * SEARCH_GROUP_RIGHT_SIZE, 1, SEARCH_GROUP_RIGHT_SIZE);
//        PageRows<GameResource> groupPageRows = searchWebLogic.searchGroup(decodeKey, boardPage);
//
//        //search profile
//        Pagination profilePage = new Pagination(1 * SEARCH_PROFILE_SIZE, 1, SEARCH_PROFILE_SIZE);
//        PageRows<SocialProfile> profileRows = searchWebLogic.searchProfile(uno, decodeKey, profilePage);

//        mapMessage.put("blogList", blogContentList.getRows());
//        mapMessage.put("page", blogContentList.getPage());
//
//        mapMessage.put("gameList", gamePageRows.getRows());
//        mapMessage.put("profileList", profileRows.getRows());
//        mapMessage.put("groupList", groupPageRows.getRows());

//        ContentTag searchTag = new ContentTag(decodeKey);
//        sendOutSearchContentEvent("", uno, searchTag, new Date(), getIp(request), blogContentList.getRows().size());
        //sent search event
//        sendOutSearchUserEvent(uno, decodeKey, new Date(), getIp(request), null, UserEventType.USER_ACCOUNT_SEARCH);
        return new ModelAndView(reView, mapMessage);
    }


    /**
     * 搜索博主
     *
     * @param key 关键字
     */
    @RequestMapping(value = "/profile/{key}")
    public ModelAndView searchContent(@PathVariable("key") String key,
                                      @RequestParam(value = "p", required = false) Integer pageNo,
                                      HttpServletRequest request) throws Exception {
        //接收参数
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        key = URLDecoder.decode(key, "UTF-8");

        mapMessage.put("key", key);//返回页面显示

//        UserSession userSession = this.getUserBySession(request);
//        String uno = userSession == null ? "" : userSession.getBlogwebsite().getUno();
//        String reView = RESULT_LIST_SEARCH_BLOG;

        //是否已收藏过
//        if (userSession != null && userSession.getTagMap().containsKey(key)) {
//            mapMessage.put("userTag", userSession.getTagMap().get(key));
//        }

        //search profile
//        pageNo = pageNo == null ? 1 : pageNo;
//        Pagination page = new Pagination(pageNo * WebappConfig.get().getSearchcontentPageSize(), pageNo, WebappConfig.get().getSearchcontentPageSize());
//        PageRows<SocialProfile> profilePageRows = searchWebLogic.searchProfile(uno, key, page);
//
//        //search game
//        Pagination gamePage = new Pagination(1 * SEARCH_GAME_RIGHT_SIZE, 1, SEARCH_GAME_RIGHT_SIZE);
//        PageRows<GameResource> gamePageRows = searchWebLogic.searchGame(key, gamePage);
//
//        //search board
//        Pagination boardPage = new Pagination(1 * SEARCH_GROUP_RIGHT_SIZE, 1, SEARCH_GROUP_RIGHT_SIZE);
//        PageRows<GameResource> boardPageRows = searchWebLogic.searchGroup(key, boardPage);

        //sent search event
//        sendOutSearchUserEvent(uno, key, new Date(), getIp(request), profilePageRows.getRows().size(), UserEventType.USER_ACCOUNT_SEARCH);
//
//        mapMessage.put("list", profilePageRows.getRows());
//        mapMessage.put("page", profilePageRows.getPage());
//
//        mapMessage.put("gameList", gamePageRows.getRows());
//        mapMessage.put("groupList", boardPageRows.getRows());

        return new ModelAndView(RESULT_LIST_SEARCH_BLOG, mapMessage);
    }

    @Deprecated
    @RequestMapping(value = "/sblog/{key}")
    public ModelAndView gotoSearchProfile(@PathVariable("key") String key,
                                          @RequestParam(value = "p", required = false) Integer pageNo,
                                          HttpServletRequest request) {
        StringBuilder reder = null;
        try {
            reder = new StringBuilder("/search/profile/").append(URLEncoder.encode(key, "UTF-8"));

            if (pageNo != null) {
                reder.append("?p=" + pageNo);
            }
            RedirectView view = new RedirectView(reder.toString());
            view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return new ModelAndView(view);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Deprecated
    @RequestMapping(value = "/s/{key}")
    public ModelAndView gotoSearchContent(@PathVariable("key") String key,
                                          @RequestParam(value = "p", required = false) Integer pageNo,
                                          HttpServletRequest request) {
        StringBuilder reder = null;
        try {
            reder = new StringBuilder("/search/content/").append(URLEncoder.encode(key, "UTF-8"));

            if (pageNo != null) {
                reder.append("?p=" + pageNo);
            }
            RedirectView view = new RedirectView(reder.toString());
            view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return new ModelAndView(view);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Deprecated
    @RequestMapping(value = "/board/{key}")
    public ModelAndView gotoSearchGroup(@PathVariable("key") String key,
                                        @RequestParam(value = "p", required = false) Integer pageNo,
                                        HttpServletRequest request) {
        StringBuilder reder = null;
        try {
            reder = new StringBuilder("/search/group/").append(URLEncoder.encode(key, "UTF-8"));

            if (pageNo != null) {
                reder.append("?p=" + pageNo);
            }
            RedirectView view = new RedirectView(reder.toString());
            view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return new ModelAndView(view);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


//    private void sendOutSearchUserEvent(String uno, String keyWord, Date date, String ip, Integer rows, UserEventType userEventType) {
//        //post event
//        UserEvent userEvent = new UserEvent(uno);
//
//        userEvent.setEventType(userEventType);
//        userEvent.setCount(1l);
//        userEvent.setEventDate(date);
//        userEvent.setEventIp(ip);
//        userEvent.setDescription(keyWord);
//        if (rows != null) {
//            userEvent.setCount(rows);
//        }
//
//        try {
//            EventDispatchServiceSngl.get().dispatch(userEvent);
//        } catch (Exception e) {
//            logger.error("sendOutSearchUserEvent error.", e);
//        }
//    }


}