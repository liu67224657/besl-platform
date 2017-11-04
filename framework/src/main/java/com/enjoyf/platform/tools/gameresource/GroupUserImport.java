package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-23 上午9:46
 * Description:
 */
public class GroupUserImport {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GroupUserImport.class);

    private static GameResourceHandler handler;


    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();

        try {
            handler = new GameResourceHandler("gameres", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("init gameHandler error.");
        }
        int index = 0;
        try {
//            List<GameResource> groupList = gameResourceList(handler);
//            if (!CollectionUtil.isEmpty(groupList)) {
//                for (GameResource gameResource : groupList) {
//                    createRole(gameResource.getResourceId(), "创建者", gameResource.getResourceName() + "创建者", 1);
//                    createRole(gameResource.getResourceId(), "组长", gameResource.getResourceName() + "组长", 2);
//                    createRole(gameResource.getResourceId(), "小组长", gameResource.getResourceName() + "小组长", 3);
//                    createRole(gameResource.getResourceId(), "组员", gameResource.getResourceName() + "组员", 4);
//                }
//            }
            List<GroupUser> list = queryGroupUser(handler);

            logger.info("============================ group valid user is:" + list.size());

            Role role = handler.getRole(new QueryExpress().add(QueryCriterions.eq(RoleField.GROUP_ID, 0l))
                    .add(QueryCriterions.eq(RoleField.ROLE_LEVEL, RoleLevel.MEMBER.getCode())));

            for (GroupUser group : list) {
                //将临时表的数据导入到User成员表里
                GroupProfile groupProfile = new GroupProfile();
                groupProfile.setGroupId(group.getGroupId());
                groupProfile.setRoleLevel(RoleLevel.MEMBER);
                groupProfile.setUno(group.getUno());
                groupProfile.setStatus(GroupProfileStatus.DEFAULT);
                groupProfile.setCreateDate(new Date());
                groupProfile.setCreateIp(group.getCreateIp());
                groupProfile.setCreateUno(group.getValidUno());
                createGroupProfile(groupProfile);
                logger.info("============================ createGroupProfile is:" + groupProfile);


                //往用户和角色的关联表里写一条记录

                GroupProfilePrivilege groupProfilePrivilege = new GroupProfilePrivilege();
                groupProfilePrivilege.setDestId(role.getRoleId());
                groupProfilePrivilege.setPrivilegeCategory(PrivilegeCategory.ROLE);
                groupProfilePrivilege.setUno(group.getUno());
                groupProfilePrivilege.setGroupId(group.getGroupId());
                groupProfilePrivilege.setActStatus(ActStatus.UNACT);
                groupProfilePrivilege.setCreateIp(group.getCreateIp());
                groupProfilePrivilege.setCreateUno(group.getValidUno());
                groupProfilePrivilege.setCreateDate(new Date());
                createGroupProfilePrivilege(groupProfilePrivilege);
                logger.info("============================ createGroupProfilePrivilege is:" + groupProfilePrivilege);
                index = index + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//                }

        logger.info("================finished import data===================== conunt:" + index);
    }


    private static List<GroupUser> queryGroupUser(GameResourceHandler handler) throws DbException {
        return handler.queryGroupUserList(new QueryExpress()
                .add(QueryCriterions.eq(GroupUserField.VALID_STATUS, GroupValidStatus.VALID.getCode())));
    }

    private static GroupProfile createGroupProfile(GroupProfile groupProfile) throws DbException {
        return handler.insertGroupProfile(groupProfile);
    }

    private static GroupProfilePrivilege createGroupProfilePrivilege(GroupProfilePrivilege groupProfilePrivilege) throws DbException {
        return handler.insertGroupProfilePrivilege(groupProfilePrivilege);
    }

    private static List<GameResource> gameResourceList(GameResourceHandler handler) throws DbException {
        return handler.queryGameResources(new QueryExpress()
                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode())));
    }

    private static Role createRole(Long groupId, String roleName, String RoleDesc, int level) throws DbException {
        Role role = new Role();
        role.setGroupId(groupId);
        role.setRoleName(roleName);
        role.setRoleDesc(RoleDesc);
        role.setRoleLevel(RoleLevel.getByCode(level));
        role.setRoleType(RoleType.DEFAULT);
        role.setActStatus(ActStatus.UNACT);
        role.setCreateDate(new Date());
        return handler.insertRole(role);
    }


}
