package com.enjoyf.webapps.tools.weblogic.gameres;

import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.service.gameres.GroupUserField;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.tools.weblogic.dto.game.GroupUserDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 下午7:46
 * Description:
 */
@Service(value="groupWebLogic")
public class GroupWebLogic {

    public PageRows<GroupUserDTO> queryByCondition(String screenName, long groupId, int status, Pagination page) throws ServiceException {
       PageRows<GroupUserDTO> returnObj=new PageRows<GroupUserDTO>();
        
        ProfileBlog profileBlog = null;
        if (!StringUtil.isEmpty(screenName)) {
            profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);
        }

        QueryExpress queryExpress = new QueryExpress();
        if (profileBlog != null) {
            queryExpress.add(QueryCriterions.eq(GroupUserField.UNO, profileBlog.getUno()));
        }
        queryExpress.add(QueryCriterions.eq(GroupUserField.GROUP_ID, groupId));
        queryExpress.add(QueryCriterions.eq(GroupUserField.VALID_STATUS, status));

        PageRows<GroupUser> groupUserRows = GameResourceServiceSngl.get().queryGroupUser(queryExpress, page);

        Set<String> unoSet = new HashSet<String>();
        for (GroupUser groupUser : groupUserRows.getRows()) {
            unoSet.add(groupUser.getUno());
        }

        List<GroupUserDTO> dtoList=new ArrayList<GroupUserDTO>();
        Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSet);
        for (GroupUser groupUser : groupUserRows.getRows()) {
            if (profileMap.containsKey(groupUser.getUno())) {
                GroupUserDTO groupUserDTO = new GroupUserDTO();
                groupUserDTO.setScreenName(profileMap.get(groupUser.getUno()).getBlog().getScreenName());
                groupUserDTO.setGroupUser(groupUser);
                dtoList.add(groupUserDTO); 
            }
        }
        
        returnObj.setRows(dtoList);
        returnObj.setPage(groupUserRows.getPage());

        return returnObj;
    }
}
