/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:51
 * Description:
 */
public interface SocialService extends EventReceiver {

    //recieve the player event
    @RPC
    public boolean receiveEvent(Event event) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RPC
    public boolean reportInviteImportInfos(List<InviteImportInfo> inviteImportInfos) throws ServiceException;


    /**
     * 喜欢话题、游戏
     * 先判断是否存在如果不存在insert，
     * 如果存在状态为unvalied，修改状态，
     * 失败返回null
     *
     * @param relation
     * @return
     * @throws ServiceException
     */
    @RPC
    public ObjectRelation saveObjectRelation(ObjectRelation relation) throws ServiceException;


    @RPC
    public List<ObjectRelation> queryObjectRelations(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public ObjectRelation getObjectRelation(String relationId) throws ServiceException;

    /**
     * 取消喜欢话题、游戏
     *
     * @param profileId
     * @param objectId
     * @return
     * @throws ServiceException
     */
    @RPC
    public boolean removeObjectRelation(String profileId, String objectId, ObjectRelationType type, String profileKey) throws ServiceException;

    /**
     * like人
     * 判断src---dest是否存在，如果存在update srcstatus，如果不存在insert
     * 判断dest---src是否存在，如果存在update deststatus，否则返回
     * 失败返回null
     *
     * @param relation
     * @return
     * @throws ServiceException
     */
    @RPC
    public ProfileRelation saveProfileRelation(ProfileRelation relation) throws ServiceException;

    /**
     * remove人的关系
     *
     * @param srcProfileId
     * @param destProfileId
     * @param removeProfileRelation
     * @return
     * @throws ServiceException
     */
    @RPC
    public boolean removeProfileRelation(String srcProfileId, String destProfileId, ObjectRelationType removeProfileRelation, String profileKey) throws ServiceException;

    /**
     * 查询我喜欢的人列表
     *
     * @param srcProfileId
     * @param relationType
     * @param pagination   @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<ProfileRelation> querySrcRelation(String srcProfileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException;


    /**
     * 查询喜欢我的人列表
     *
     * @param srcProfileId
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<ProfileRelation> queryDestRelation(String srcProfileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException;

    /**
     * 查询objectRelation列表
     *
     * @param profileId
     * @param type
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<ObjectRelation> queryObjectRelation(String profileId, ObjectRelationType type, Pagination pagination) throws ServiceException;

    @RPC
    public Map<String, ObjectRelation> checkObjectRelation(String profileId, ObjectRelationType type, Set<String> destId) throws ServiceException;

    @RPC
    public List<String> queryObjectRelationObjectIdList(String profileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException;

    @RPC
    public PageRows<String> queryFocusProfileId(String profileId, ObjectRelationType relationType, Pagination page) throws ServiceException;


    //build the relation.
    @RPC
    public UserRelation buildUserRelation(UserRelation relation) throws ServiceException;

    //break the relation
    @RPC
    public boolean removeUserRelation(String srcUno, String destUno, ObjectRelationType type, String modifyIp) throws ServiceException;

    /**
     * 查询关注的人列表
     *
     * @param profileid
     * @param relationType
     * @param pagination   @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<UserRelation> queryFans(String profileid, ObjectRelationType relationType, Pagination pagination) throws ServiceException;

    /**
     * 查询喜欢的人列表
     *
     * @param profileid
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<UserRelation> queryFollowUser(String profileid, ObjectRelationType relationType, Pagination pagination) throws ServiceException;

    //get the relation with someone
    @RPC
    public UserRelation getRelation(String srcUno, String destUno, ObjectRelationType type) throws ServiceException;

    @RPC
    PageRows<String> queryFansProfileid(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException;

    @RPC
    PageRows<String> queryFollowProfileid(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException;

    //取交集,返回的是关注
    @RPC
    Set<String> checkFollowStatus(String profileId, Collection<String> pids) throws ServiceException;

    @RPC
    Set<String> checkFansStatus(String profileId, Collection<String> pids) throws ServiceException;
}

