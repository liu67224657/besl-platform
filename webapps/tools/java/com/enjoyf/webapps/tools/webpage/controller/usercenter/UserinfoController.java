package com.enjoyf.webapps.tools.webpage.controller.usercenter;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.ProfileField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.tools.weblogic.dto.GameDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengxu on 2017/4/5.
 */
@Controller
@RequestMapping(value = "/userinfo")
public class UserinfoController {

    @RequestMapping(value = "/querylist")
    public ModelAndView queryList(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                  @RequestParam(value = "maxPageItems", required = false, defaultValue = "10") int pageSize,
                                  @RequestParam(value = "nick", required = false) String nick,
                                  @RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "starttime", required = false) String startTime,
                                  @RequestParam(value = "endtime", required = false) String endTime) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(nick)) {
                mapMessage.put("nick", nick);
                queryExpress.add(QueryCriterions.eq(ProfileField.NICK, nick));
            }

            if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime)) {
                Date startDate = DateUtil.formatStringToDate(startTime, "yyyy-MM-dd HH:mm:ss");
                Date endDate = DateUtil.formatStringToDate(endTime, "yyyy-MM-dd HH:mm:ss");

                queryExpress.add(QueryCriterions.geq(ProfileField.CREATETIME, startDate));
                queryExpress.add(QueryCriterions.leq(ProfileField.CREATETIME, endDate));
                mapMessage.put("starttime", startTime);
                mapMessage.put("endtime", endTime);
            }
            if (StringUtil.isEmpty(nick) && StringUtil.isEmpty(status) && StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
                //参数都为空的情况下默认查询30天的数据
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                queryExpress.add(QueryCriterions.geq(ProfileField.CREATETIME, c.getTime()));
                queryExpress.add(QueryCriterions.leq(ProfileField.CREATETIME, new Date()));
            }
            String inOrNot = "";
            String profileNos = "";
            if (!StringUtil.isEmpty(status)) {
                mapMessage.put("status", status);
                Set<String> pids = AskServiceSngl.get().queryBlackListPid();
                if (CollectionUtil.isEmpty(pids) && "1".equals(status)) {
                    return new ModelAndView("/usercenter/querylist", mapMessage);
                } else if (CollectionUtil.isEmpty(pids) || "0".equals(status)) {
                    //默认查询30天的数据
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, -1);
                    queryExpress.add(QueryCriterions.geq(ProfileField.CREATETIME, c.getTime()));
                    queryExpress.add(QueryCriterions.leq(ProfileField.CREATETIME, new Date()));
                }

                if (!CollectionUtil.isEmpty(pids)) {
                    if ("1".equals(status)) {
                       // queryExpress.add(QueryCriterions.in(ProfileField.PROFILEID, pids.toArray()));
                        inOrNot ="in";
                    } else {
                       // queryExpress.add(QueryCriterions.notIn(ProfileField.PROFILEID, pids.toArray()));
                        inOrNot = "not";
                    }
                    //todo 微服务改造
                    for (String pid : pids) {
                        profileNos=pid+",";
                    }
                    profileNos = profileNos.substring(0,profileNos.length()-1);
                }
            }

            queryExpress.add(QuerySort.add(ProfileField.CREATETIME, QuerySortOrder.DESC));
            //PageRows<Profile> pageRows = UserCenterServiceSngl.get().queryProfileByPage(queryExpress, page);
            //todo 微服务改造
            PageRows<Profile> pageRows = UserCenterServiceSngl.get().searchProfiles(nick,inOrNot,profileNos,startTime,endTime,page);

            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                Set<String> profileIds = new HashSet<String>();
                for (Profile profile : pageRows.getRows()) {
                    profileIds.add(profile.getProfileId());
                }

                Map<String, BlackListHistory> blackMap = AskServiceSngl.get().queryBlackHisotryById(profileIds);
                mapMessage.put("blackMap", blackMap);
            }
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("list", pageRows.getRows());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        return new ModelAndView("/usercenter/querylist", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "pid", required = false) String pid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
            mapMessage.put("profile", profile);
            List<Region> regionList = MiscServiceSngl.get().getAllRegions();
            Map<Integer, Region> regionMap = new HashMap<Integer, Region>();
            for (Region region : regionList) {
                regionMap.put(region.getRegionId(), region);
            }
            mapMessage.put("regionMap", regionMap);


            List<UserFollowGame> list = AskServiceSngl.get().queryFollowGame(new QueryExpress().add(QueryCriterions.eq(UserFollowGameField.PROFILEID, pid))
                    .add(QueryCriterions.eq(UserFollowGameField.VALID_STATUS, ValidStatus.VALID.getCode())));
            List<GameDTO> returnList = new ArrayList<GameDTO>();
            if (!CollectionUtil.isEmpty(list)) {
                Map<Long, UserFollowGame> map = new HashMap<Long, UserFollowGame>();
                Set<Long> gameIds = new LinkedHashSet<Long>();

                for (UserFollowGame userFollowGame : list) {
                    gameIds.add(userFollowGame.getGameId());
                    map.put(userFollowGame.getGameId(), userFollowGame);
                }

                Map<Long, GameDB> gameDBMap = GameResourceServiceSngl.get().queryGameDBSet(gameIds);
                for (Long id : gameIds) {
                    returnList.add(buildGameDTO(gameDBMap.get(id), map.get(id)));
                }
            }

            mapMessage.put("followlist", returnList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }


        return new ModelAndView("/usercenter/detail", mapMessage);
    }

    @RequestMapping(value = "/black/info")
    public ModelAndView blackInfo(@RequestParam(value = "pid", required = false) String pid,
                                  @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                                  @RequestParam(value = "maxPageItems", required = false, defaultValue = "10") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination page = new Pagination(pageSize * curPage, curPage, pageSize);

            BlackListHistory blackListHistory = AskServiceSngl.get().getBlackListHistory(pid);
            mapMessage.put("info", blackListHistory);
            mapMessage.put("pid", pid);
            PageRows<BlackListHistory> blackListHistoryPageRows = AskServiceSngl.get().queryBlackListHistoryList(pid, page);
            mapMessage.put("list", blackListHistoryPageRows.getRows());
            mapMessage.put("page", blackListHistoryPageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("/usercenter/blackinfo", mapMessage);
    }

    @RequestMapping(value = "/black/add")
    public ModelAndView addBlack(
            @RequestParam(value = "pid", required = false) String pid,
            @RequestParam(value = "starttime", required = false) String startime,
            @RequestParam(value = "endtime", required = false) String endtime,
            @RequestParam(value = "reason", required = false) String reason) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            Date startDate = DateUtil.StringTodate(startime, "yyyy-MM-dd HH:mm:ss");
            Date endDate = DateUtil.StringTodate(endtime, "yyyy-MM-dd HH:mm:ss");

            BlackListHistory blackListHistory = new BlackListHistory();
            blackListHistory.setStartTime(startDate);
            blackListHistory.setEndTime(endDate);
            blackListHistory.setProfileId(pid);
            blackListHistory.setReason(reason);
            AskServiceSngl.get().addBlackListHisotry(blackListHistory);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/userinfo/black/info?pid=" + pid);
    }

    @RequestMapping(value = "/black/remove")
    public ModelAndView addBlack(@RequestParam(value = "pid", required = false) String pid) {
        try {
            AskServiceSngl.get().removeBlackListByPid(pid);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        return new ModelAndView("redirect:/userinfo/black/info?pid=" + pid);

    }

    private GameDTO buildGameDTO(GameDB gameDB, UserFollowGame userFollowGame) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameName(gameDB == null ? "" : gameDB.getGameName());
        gameDTO.setIcon(gameDB == null ? "" : gameDB.getGameIcon());
        gameDTO.setCreateTime(userFollowGame.getModifyTime());
        return gameDTO;
    }
}
