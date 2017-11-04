package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.dto.game.GroupProfileDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.GameResourceWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-23 下午5:08
 * Description:
 */
@Deprecated
@Controller
@RequestMapping("/group/profile")
public class GroupProfileController extends ToolsBaseController {
    @Resource(name = "gameResourceWebLogic")
    private GameResourceWebLogic gameResourceWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "groupid", required = false) Long groupId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            List<GameResource> groupList = gameResourceWebLogic.queryGroupResources(new QueryExpress()
                    .add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode()))
                    .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode())));

            if (!CollectionUtil.isEmpty(groupList)) {
                mapMessage.put("groupList", groupList);
            }
            if (groupId == null) {
                List list = new ArrayList<String>();
                mapMessage.put("list", list);
                return new ModelAndView("/gameresource/group/groupprofilelist", mapMessage);
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.or(QueryCriterions.eq(GroupProfileField.STATUS, GroupProfileStatus.DEFAULT.getCode()),
                            QueryCriterions.eq(GroupProfileField.STATUS, GroupProfileStatus.NO_SPEAK.getCode())));
            if (groupId != null && groupId > 0) {
                mapMessage.put("groupId", groupId);
                queryExpress.add(QueryCriterions.eq(RoleField.GROUP_ID, groupId));
            }
            PageRows<GroupProfile> pageRows = GameResourceServiceSngl.get().queryGroupProfileByPage(queryExpress, pagination);
            List<GroupProfileDTO> dtoList = new ArrayList<GroupProfileDTO>();
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                Set<String> unoSet = new HashSet<String>();
                for (GroupProfile groupProfile : pageRows.getRows()) {
                    unoSet.add(groupProfile.getUno());
                }
                Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSet);
                for (GroupProfile groupProfile : pageRows.getRows()) {
                    if (profileMap.containsKey(groupProfile.getUno())) {
                        GroupProfileDTO groupProfileDTO = new GroupProfileDTO();
                        groupProfileDTO.setScreenName(profileMap.get(groupProfile.getUno()).getBlog().getScreenName());
                        groupProfileDTO.setGroupProfile(groupProfile);
                        dtoList.add(groupProfileDTO);
                    }
                }
            }

            mapMessage.put("list", dtoList);
            mapMessage.put("page", pageRows.getPage());


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameresource/group/groupprofilelist", mapMessage);
    }

    @RequestMapping(value = "/createrole")
    public ModelAndView createRole(@RequestParam(value = "groupid", required = false) Long groupId,
                                   @RequestParam(value = "groupprofileid", required = false) Long groupProfileId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            Long[] ads = new Long[2];
            ads[0]=0L;
            if (groupId != null) {
                ads[1]=groupId;
            }
            List<Role> roleList = GameResourceServiceSngl.get().queryRole(new QueryExpress().add(QueryCriterions.in(RoleField.GROUP_ID, ads)));
            if (CollectionUtil.isEmpty(roleList)) {
                return new ModelAndView("/gameresource/group/createrolepage", mapMessage);
            }
            GroupProfile groupProfile = GameResourceServiceSngl.get().getGroupProfile(new QueryExpress().add(QueryCriterions.eq(GroupProfileField.GROUP_PROFILE_ID, groupProfileId)));

            Profile profile = ProfileServiceSngl.get().getProfileByUno(groupProfile.getUno());
            GameResource group = gameResourceWebLogic.getGameResourceById(groupId);
            if (group != null) {
                mapMessage.put("group", group);
            }

            mapMessage.put("screenName", profile.getBlog().getScreenName());
            mapMessage.put("groupProfile", groupProfile);
            mapMessage.put("list", roleList);
            mapMessage.put("groupid", groupId);
            mapMessage.put("uno", groupProfile.getUno());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameresource/group/createrolepage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "roleid", required = false) Long roleId,
                               @RequestParam(value = "groupprofileid", required = false) Long groupProfileId,
                               @RequestParam(value = "groupid", required = false) Long groupId,
                               @RequestParam(value = "uno", required = false) String uno) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<GroupProfilePrivilege> groupProfilePrivilegeList = GameResourceServiceSngl.get().queryGroupProfilePrivilege(new QueryExpress()
                    .add(QueryCriterions.eq(GroupProfilePrivilegeField.GROUP_ID, groupId))
                    .add(QueryCriterions.eq(GroupProfilePrivilegeField.UNO, uno)));
            if (CollectionUtil.isEmpty(groupProfilePrivilegeList)) {
                GroupProfilePrivilege groupProfilePrivilege = new GroupProfilePrivilege();
                groupProfilePrivilege.setActStatus(ActStatus.UNACT);
                groupProfilePrivilege.setUno(uno);
                groupProfilePrivilege.setGroupId(groupId);
                groupProfilePrivilege.setDestId(roleId);
                groupProfilePrivilege.setPrivilegeCategory(PrivilegeCategory.ROLE);
                groupProfilePrivilege.setCreateDate(new Date());
                groupProfilePrivilege.setCreateIp(getIp());
                groupProfilePrivilege.setCreateUno(getCurrentUser().getUserid());
                GameResourceServiceSngl.get().createGroupProfilePrivilege(groupProfilePrivilege);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(GroupProfilePrivilegeField.DEST_ID, roleId);
                GameResourceServiceSngl.get().modifyGroupProfilePrivilege(new QueryExpress().add(QueryCriterions.eq(GroupProfilePrivilegeField.PROFILE_PRIVILEGE_ID, groupProfilePrivilegeList.get(0).getProfilePrivilegeId())), updateExpress);
            }
            Role role = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.ROLE_ID, roleId)));
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(GroupProfileField.PROFILE_TYPE, role.getRoleLevel().getCode());
            GameResourceServiceSngl.get().modifyGroupProfile(new QueryExpress().add(QueryCriterions.eq(GroupProfileField.GROUP_PROFILE_ID, groupProfileId)), updateExpress);
            mapMessage.put("groupid", groupId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("forward:/group/profile/list", mapMessage);
    }


}
