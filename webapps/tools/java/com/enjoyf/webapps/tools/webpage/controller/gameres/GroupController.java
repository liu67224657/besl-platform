package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.dto.game.GroupUserDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.GroupWebLogic;
import com.enjoyf.webapps.tools.weblogic.viewline.ViewLineWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-23 下午5:08
 * Description:
 */
@Deprecated
@Controller
@RequestMapping("/group")
public class GroupController extends ToolsBaseController {


    @Resource(name = "viewLineWebLogic")
    private ViewLineWebLogic viewLineWebLogic;

    @Resource(name = "groupWebLogic")
    private GroupWebLogic groupWebLogic;

    @RequestMapping(value = "/userlistpage")
    public ModelAndView userpage(
            @RequestParam(value = "resid", required = true) Long resId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            GameResource groupReosurce = GameResourceServiceSngl.get().getGameResource(
                    new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, resId)));
            //
            GameRelationSet gameRelationSet = groupReosurce.getGameRelationSet();
            if (gameRelationSet == null || gameRelationSet.getBoardRelation() == null) {
                return new ModelAndView("/");
            }

            ViewCategory boardCategory = null;
            if (gameRelationSet != null && gameRelationSet.getBoardRelation() != null) {
                try {
                    int boardId = Integer.parseInt(gameRelationSet.getBoardRelation().getRelationValue());
                    boardCategory = ViewLineServiceSngl.get().getCategoryByCategoryIdFromCache(boardId);
                } catch (NumberFormatException e) {
                }
            }
            if (boardCategory == null) {
            }
            ViewCategory talkBoardCategory = null;
            for (ViewCategory category : boardCategory.getChildrenCategories()) {
                if (LocationCode.TALK_BOARD.getCode().equals(category.getLocationCode())) {
                    talkBoardCategory = category;
                    break;
                }
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, talkBoardCategory.getCategoryId()));
            queryExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));

            ViewLine viewLine = ViewLineServiceSngl.get().getViewLine(queryExpress);

            map.put("line", viewLine);
            map.put("lineId", viewLine.getLineId());

        } catch (ServiceException e) {
            e.printStackTrace();
        }


        return new ModelAndView("/gameresource/listuserlineitem", map);
    }

    @RequestMapping(value = "/userlist")
    public ModelAndView userlist(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "domain", required = false) String domain,
            @RequestParam(value = "uno", required = false) String uno,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lineId", lineId);
        map.put("screenName", screenName);
        map.put("domain", domain);
        map.put("uno", uno);
        map.put("pager.offset", pageStartIndex);
        map.put("maxPageItems", pageSize);

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(curPage * pageSize, curPage, pageSize);

        if (StringUtil.isEmpty(screenName) && StringUtil.isEmpty(domain) & StringUtil.isEmpty(uno)) {
            return new ModelAndView("/gameresource/listuserlineitem", map);
        }
        ProfileBlog profileBlog;
        try {
            if (!StringUtil.isEmpty(uno)) {
                profileBlog = ProfileServiceSngl.get().getProfileBlogByUno(uno);
            } else if (!StringUtil.isEmpty(screenName)) {
                profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);
            } else {
                profileBlog = ProfileServiceSngl.get().getProfileBlogByDomain(domain);
            }

            //
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId))
                    .add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, profileBlog.getUno()))
                    .add(QuerySort.add(ViewLineItemField.DISPLAYORDER));
            PageRows<ViewLineItem> pageRows = viewLineWebLogic.queryLineItems(queryExpress, page);

            ViewLine line = viewLineWebLogic.getLineById(lineId);

            map.put("lineItems", viewLineWebLogic.buildLineItemDTOs(line.getItemType(), pageRows.getRows()));
            map.put("page", pageRows.getPage());
            map.put("lineId", lineId);
            map.put("profileBlog", profileBlog);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("/gameresource/listuserlineitem", map);
    }

    @RequestMapping(value = "/removeitembyuser")
    public ModelAndView removeLineItem(
            @RequestParam(value = "lineId", required = true) int lineId,
            @RequestParam(value = "uno", required = true) String uno) {


        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, lineId));
        queryExpress.add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, uno));

        try {
            ViewLineServiceSngl.get().removeLineItem(queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/group/userlist?lineId=" + lineId + "&uno=" + uno);
    }


    @RequestMapping(value = "/validuserlist")
    public ModelAndView validuserlist(
            @RequestParam(value = "resid", required = true) Long resourceId,
            @RequestParam(value = "screenName", required = false) String screenName,
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusList", GroupValidStatus.getAll());
        map.put("queryStatus", status);
        map.put("resid", resourceId);
        map.put("screenName", screenName);
        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination page = new Pagination(curPage * pageSize, curPage, pageSize);
        try {


            PageRows<GroupUserDTO> dtoPageRows = groupWebLogic.queryByCondition(screenName, resourceId, status, page);

            map.put("groupUserList", dtoPageRows.getRows());
            map.put("page", dtoPageRows.getPage());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            map.put("page", page);
        }

        return new ModelAndView("/gameresource/group/validuserlist", map);
    }

    @RequestMapping(value = "/joingroup")
    public ModelAndView join(
            @RequestParam(value = "resid", required = true) Long resourceId,
            @RequestParam(value = "uno", required = true) String uno,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {

        try {

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(GroupUserField.VALID_STATUS, GroupValidStatus.VALID.getCode());
            updateExpress.set(GroupUserField.VALID_TIME, new Date());
            updateExpress.set(GroupUserField.VALID_USERID, getCurrentUser().getUserid());

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
            queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, resourceId));
            GameResourceServiceSngl.get().modifyGroupUser(updateExpress, queryExpress);


            GroupProfile groupProfile = new GroupProfile();
            groupProfile.setGroupId(resourceId);
            groupProfile.setRoleLevel(RoleLevel.MEMBER);
            groupProfile.setUno(uno);
            groupProfile.setStatus(GroupProfileStatus.DEFAULT);
            groupProfile.setCreateDate(new Date());
            groupProfile.setCreateIp(getIp());
            groupProfile.setCreateUno(getCurrentUser().getUserid());

            GroupProfile grouProfile = GameResourceServiceSngl.get().getGroupProfile(new QueryExpress().add(QueryCriterions.eq(GroupProfileField.UNO, uno))
                    .add(QueryCriterions.eq(GroupProfileField.GROUP_ID, resourceId)));

            Role role = GameResourceServiceSngl.get().getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.GROUP_ID, resourceId))
                    .add(QueryCriterions.eq(RoleField.ROLE_LEVEL, RoleLevel.MEMBER.getCode())));

            if (grouProfile != null) {
                GameResourceServiceSngl.get().createGroupProfile(groupProfile);
                //把该用户写入角色与用户关联表
                if (role != null) {
                    GroupProfilePrivilege groupProfilePrivilege = new GroupProfilePrivilege();
                    groupProfilePrivilege.setDestId(role.getRoleId());
                    groupProfilePrivilege.setPrivilegeCategory(PrivilegeCategory.ROLE);
                    groupProfilePrivilege.setUno(uno);
                    groupProfilePrivilege.setGroupId(role.getGroupId());
                    groupProfilePrivilege.setActStatus(ActStatus.UNACT);
                    groupProfilePrivilege.setCreateIp(getIp());
                    groupProfilePrivilege.setCreateUno(getCurrentUser().getUserid());
                    groupProfilePrivilege.setCreateDate(new Date());
                    GameResourceServiceSngl.get().createGroupProfilePrivilege(groupProfilePrivilege);
                }
            } else {
                updateExpress = new UpdateExpress();
                updateExpress.set(GroupProfileField.STATUS, GroupProfileStatus.DEFAULT.getCode());
                GameResourceServiceSngl.get().modifyGroupProfile(new QueryExpress().
                        add(QueryCriterions.eq(GroupProfileField.UNO, uno))
                        .add(QueryCriterions.eq(GroupProfileField.GROUP_ID, resourceId)), updateExpress);
            }
            //通过通知
            joinGroupNotice(uno, resourceId);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/group/validuserlist?resid=" + resourceId + "&uno=" + uno + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&status=" + status);
    }

    /**
     * 允许加入小组后的系统通知
     */
    private void joinGroupNotice(String uno, long groupId) {
        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        Map<String, String> paramMap = new HashMap<String, String>();
        try {
            GameResource groupResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress()
                    .add(QueryCriterions.eq(GameResourceField.RESOURCEID, groupId)));
            paramMap.put("groupname", groupResource.getResourceName());
            String messageBody = NamedTemplate.parse(templateConfig.getJoinUserGroup()).format(paramMap);

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(uno);
            message.setRecieverUno(uno);
            message.setSendDate(new Date());
            MessageServiceSngl.get().postMessage(uno, message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcetpion.e", e);
        }
    }

    @RequestMapping(value = "/removegroup")
    public ModelAndView remove(
            @RequestParam(value = "resid", required = true) Long resourceId,
            @RequestParam(value = "uno", required = true) String uno,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "status", required = false, defaultValue = "0") Integer status) {

        try {
            if (status == 0) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(GroupUserField.VALID_STATUS, GroupValidStatus.INVALID.getCode());
                updateExpress.set(GroupUserField.VALID_TIME, null);

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
                queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, resourceId));
                GameResourceServiceSngl.get().modifyGroupUser(updateExpress, queryExpress);
                rejectedGroupNotice(uno, resourceId);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(GroupUserField.VALID_STATUS, GroupValidStatus.REMOVE.getCode());
                updateExpress.set(GroupUserField.VALID_TIME, null);

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
                queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, resourceId));
                GameResourceServiceSngl.get().modifyGroupUser(updateExpress, queryExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        return new ModelAndView("redirect:/group/validuserlist?resid=" + resourceId + "&uno=" + uno + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&status=" + status);
    }

    //拒绝加入小组后发送的系统通知
    private void rejectedGroupNotice(String uno, long groupId) {
        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        Map<String, String> paramMap = new HashMap<String, String>();
        try {
            GameResource groupResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress()
                    .add(QueryCriterions.eq(GameResourceField.RESOURCEID, groupId)));
            paramMap.put("groupname", groupResource.getResourceName());
            paramMap.put("rejectedreason", "");
            String messageBody = NamedTemplate.parse(templateConfig.getRejectedJoinUserGroup()).format(paramMap);

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(uno);
            message.setRecieverUno(uno);
            message.setSendDate(new Date());
            MessageServiceSngl.get().postMessage(uno, message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcetpion.e", e);
        }
    }
}
