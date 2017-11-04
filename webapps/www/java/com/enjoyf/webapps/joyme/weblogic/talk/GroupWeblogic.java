package com.enjoyf.webapps.joyme.weblogic.talk;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.service.gameres.GroupUserField;
import com.enjoyf.platform.service.gameres.GroupValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.dto.index.ViewLineItemElementDTO;
import org.springframework.stereotype.Service;

import javax.management.QueryExp;
import java.util.*;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-14
 * Description:
 */
@Service(value = "groupWebLogic")
public class GroupWeblogic {

    public List<ViewLineItemElementDTO> queryGroupRelation(String srcUno, List<ViewLineItemElementDTO> groupList) throws ServiceException, CloneNotSupportedException {

        List<ViewLineItemElementDTO> returnObj = new ArrayList<ViewLineItemElementDTO>();

        if (StringUtil.isEmpty(srcUno) || CollectionUtil.isEmpty(groupList)) {
            returnObj.addAll(groupList);
            return groupList;
        } else {
            Set<Long> destIds = new HashSet<Long>();

            for (ViewLineItemElementDTO viewLineItemElementDTO : groupList) {
                try {
                    destIds.add(Long.valueOf(viewLineItemElementDTO.getElementId()));
                } catch (NumberFormatException e) {
                }
            }

//            Map<String, SocialRelation> relationMap = SocialServiceSngl.get().checkRelationsByDestUnos(srcUno, RelationType.BOARD_FOLLOW, destIds);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, srcUno));
            queryExpress.add(QueryCriterions.in(GroupUserField.GROUP_ID, destIds.toArray()));
            PageRows<GroupUser> groupUserPageRows = GameResourceServiceSngl.get().queryGroupUser(queryExpress, new Pagination(destIds.size(), 1, destIds.size()));
            if (groupUserPageRows == null || CollectionUtil.isEmpty(groupUserPageRows.getRows())) {
                return groupList;
            }

            Map<String, GroupUser> map = new HashMap<String, GroupUser>();
            for (GroupUser groupUser : groupUserPageRows.getRows()) {
                map.put(String.valueOf(groupUser.getGroupId()), groupUser);
            }

            for (ViewLineItemElementDTO viewLineItemElementDTO : groupList) {

                ViewLineItemElementDTO coloneElement = (ViewLineItemElementDTO) viewLineItemElementDTO.clone();

                GroupUser groupUser = map.get(viewLineItemElementDTO.getElementId());
                if (groupUser != null) {
                    if (groupUser.getValidStatus().equals(GroupValidStatus.VALID)) {
                        coloneElement.setRelationFlag("1");
                    } else {
                        coloneElement.setRelationFlag("0");
                    }
                } else {
                    coloneElement.setRelationFlag("0");
                }

                returnObj.add(coloneElement);
            }
        }

        return returnObj;
    }


    public JoymeResultMsg applyGroup(String uno, long groupId, String ip, Date createTime, String reason) throws ServiceException {
        GroupUser groupUser = GameResourceServiceSngl.get().getGroupUser(uno, groupId);

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        if (groupUser == null) {
            groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUno(uno);
            groupUser.setCreateTime(createTime);
            groupUser.setValidStatus(GroupValidStatus.INIT);
            groupUser.setCreateIp(ip);
            groupUser.setCreateReason(reason);
            GameResourceServiceSngl.get().createGroupUser(groupUser);

            resultMsg.setMsg("user.apply.success");
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);

        } else if (groupUser.getValidStatus().equals(GroupValidStatus.VALID)) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg("user.had.joingroup");
            return resultMsg;
        } else if (groupUser.getValidStatus().equals(GroupValidStatus.INIT)) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg("user.had.applygroup");

        } else if (groupUser.getValidStatus().equals(GroupValidStatus.REMOVE)) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg("user.had.applygroup");

        } else {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(GroupUserField.VALID_STATUS, GroupValidStatus.INIT.getCode());
            updateExpress.set(GroupUserField.CREATE_TIME, createTime);
            if (!StringUtil.isEmpty(reason)) {
                updateExpress.set(GroupUserField.CREATE_REASON, reason);
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
            queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, groupId));

            GameResourceServiceSngl.get().modifyGroupUser(updateExpress, queryExpress);

            resultMsg.setMsg("user.apply.success");
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
        }

        return resultMsg;
    }


    public JoymeResultMsg removeGroup(String uno, long groupId) throws ServiceException {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(GroupUserField.VALID_STATUS, GroupValidStatus.INVALID.getCode());

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, uno));
        queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, groupId));

        GameResourceServiceSngl.get().modifyGroupUser(updateExpress, queryExpress);

        resultMsg.setStatus_code(JoymeResultMsg.CODE_S);


        return resultMsg;
    }
}
