/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlog;
import com.enjoyf.platform.service.profile.socialclient.SocialProfileBlogField;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.profile.ProfileDomainGenerator;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class ProfileHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ProfileDetailAccessor profileDetailAccessor;
    private ProfileSumAccessor profileSumAccessor;

    private ProfileSettingAccessor profileSettingAccessor;
    private ProfileBlogAccessor profileBlogAccessor;

    private ProfileExperienceAccessor profileExperienceAccessor;

    private ProfileMobileDeviceAccessor profileMobileDeviceAccessor;
    private ProfileDeveloperAccessor profileDeveloperAccessor;

    private ProfileClientMobileDeviceAccessor profileClientMobileDeviceAccessor;

    private SocialProfileBlogAccessor socialProfileBlogAccessor;

    public ProfileHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        profileDetailAccessor = TableAccessorFactory.get().factoryAccessor(ProfileDetailAccessor.class, dataBaseType);
        profileSumAccessor = TableAccessorFactory.get().factoryAccessor(ProfileSumAccessor.class, dataBaseType);

        profileSettingAccessor = TableAccessorFactory.get().factoryAccessor(ProfileSettingAccessor.class, dataBaseType);
        profileBlogAccessor = TableAccessorFactory.get().factoryAccessor(ProfileBlogAccessor.class, dataBaseType);

        profileExperienceAccessor = TableAccessorFactory.get().factoryAccessor(ProfileExperienceAccessor.class, dataBaseType);

        profileMobileDeviceAccessor = TableAccessorFactory.get().factoryAccessor(ProfileMobileDeviceAccessor.class, dataBaseType);

        profileDeveloperAccessor = TableAccessorFactory.get().factoryAccessor(ProfileDeveloperAccessor.class, dataBaseType);

        profileClientMobileDeviceAccessor = TableAccessorFactory.get().factoryAccessor(ProfileClientMobileDeviceAccessor.class, dataBaseType);

        socialProfileBlogAccessor = TableAccessorFactory.get().factoryAccessor(SocialProfileBlogAccessor.class, dataBaseType);
    }

    /**
     *
     */
    public ProfileDetail createProfileDetail(ProfileDetail entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDetailAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /**
     *
     */
    public boolean updateProfileDetail(ProfileDetail entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDetailAccessor.update(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileDetail(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDetailAccessor.update(uno, keyValues, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileDetail(String uno, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDetailAccessor.update(updateExpress, uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ProfileDetail getProfileDetail(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDetailAccessor.get(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /*------------------ ProfileSumAccessor  -------------       ---------------*/

    public ProfileSum createProfileSum(ProfileSum entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean increaseProfileSum(String uno, Map<ObjectField, Object> keyDeltas) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.increase(uno, keyDeltas, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean increaseProfileSum(String uno, ObjectField field, Integer value) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.increase(uno, field, value, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileSum(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.update(uno, keyValues, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileSum getProfileSum(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSumAccessor.get(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    /*------------------ ProfileBlogAccessor  -------------       ---------------*/
    public ProfileBlog createProfileBlog(ProfileBlog entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            String domain = entity.getDomain();
            if (StringUtil.isEmpty(domain)) {
//                ProfileBlog profileBlog = null;

                int tryTimes = 0;
                do {
                    domain = ProfileDomainGenerator.generateProfileDomain(System.currentTimeMillis(), ProfileDomainGenerator.KEY_DOMAIN_LEN);
//                    entity.setDomain();
//                    try {
//                        profileBlog = profileBlogAccessor.insert(entity, conn);
//                    } catch (DbException e) {
//                    }
//
//                    if (profileBlog != null) {
//                        break;
//                    }
                    if (!StringUtil.isEmpty(domain)) {
                        break;
                    }
                    tryTimes++;
                } while (tryTimes < 5);

//                return profileBlog;
            }
            entity.setDomain(domain);
            if (StringUtil.isEmpty(entity.getScreenName())) {
                entity.setScreenName("joyme_" + domain);
            }


//            else {
//                return profileBlogAccessor.insert(entity, conn);
//            }

            return profileBlogAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

//    public ProfileBlog createProfileBlogGenDomain(ProfileBlog entity) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//
//
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }


    public ProfileBlog getProfileBlogByUno(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.getByUno(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileBlog getProfileBlogByScreenName(String screenName) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.getByScreenName(screenName, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, ProfileBlog> queryProfileBlogsByLikeScreenName(String screenName) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.queryProfileBlogsByLikeScreenName(screenName, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, ProfileBlog> queryProfileBlogByScreenNamesMap(Set<String> screenNames) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.queryByScreenNameMaps(screenNames, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileBlog getProfileBlogByDomain(String domain) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.getByDomain(domain, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ProfileBlog> queryProfileBlogsByDateStep(Date startDate, Date endDate, Pagination page) throws DbException {
        PageRows<ProfileBlog> returnValue = new PageRows<ProfileBlog>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(profileBlogAccessor.queryBlogByDateStep(startDate, endDate, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<ProfileBlog> searchProfileBlog(String keyWord, Pagination page) throws DbException {
        PageRows<ProfileBlog> returnValue = new PageRows<ProfileBlog>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(profileBlogAccessor.search(keyWord, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public boolean updateProfileBlog(ProfileBlog profileBlog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.update(profileBlog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileBlog(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.update(uno, keyValues, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileBlog(String uno, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(ProfileBlogField.UNO, uno)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

/*------------------ ProfileSettingAccessor  -------------       ---------------*/

    public ProfileSetting createProfileSetting(ProfileSetting entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSettingAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileSetting getProfileSettingByUno(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSettingAccessor.getByUno(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileSetting(ProfileSetting profileSetting) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSettingAccessor.update(profileSetting, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateProfileSetting(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileSettingAccessor.update(uno, keyValues, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    // profile experience ------------- accessor -----------
    public List<ProfileExperience> createProfileExperience(List<ProfileExperience> list) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileExperienceAccessor.insert(list, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean removeProfileExperience(String uno, ProfileExperienceType experienceType) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileExperienceAccessor.remove(uno, experienceType, conn);
        } finally {

            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ProfileExperience> queryProfileExperienceByUno(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileExperienceAccessor.queryProfileExperienceByUno(uno, conn);
        } finally {

            DataBaseUtil.closeConnection(conn);
        }
    }

    //------------   common update-----------------
    public int updateBlog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileBlogAccessor.update(updateExpress, queryExpress, conn);
        } finally {

            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ProfileBlog> query(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<ProfileBlog> returnValue = new PageRows<ProfileBlog>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(profileBlogAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {

            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    /*------------------ ProfileMobileDeviceAccessor  -------------       ---------------*/

    //insert
    public ProfileMobileDevice increaseProfileMobileDevice(ProfileMobileDevice entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.MDSERIAL, entry.getMdSerial()));

            Rangination rangination = new Rangination(0, 1000, 1000);

            List<ProfileMobileDevice> profileMobileDeviceList = profileMobileDeviceAccessor.query(queryExpress, rangination, conn);

            if (profileMobileDeviceList.size() > 0) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(ProfileMobileDeviceField.VALIDSTATUS, ValidStatus.INVALID.getCode());

                int value = profileMobileDeviceAccessor.update(updateExpress, queryExpress, conn);

                if (value > 0) {
                    if (!StringUtil.isEmpty(entry.getUno())) {
                        queryExpress.add(QueryCriterions.eq(ProfileMobileDeviceField.UNO, entry.getUno()));
                    }

                    profileMobileDeviceList = profileMobileDeviceAccessor.query(queryExpress, rangination, conn);

                    if (profileMobileDeviceList.size() > 0) {
                        UpdateExpress updateExpressUno = new UpdateExpress();
                        updateExpressUno.set(ProfileMobileDeviceField.VALIDSTATUS, ValidStatus.VALID.getCode());
                        updateExpressUno.set(ProfileMobileDeviceField.PUSHSTATUS, entry.getPushStatus().getCode());

                        value = profileMobileDeviceAccessor.update(updateExpressUno, queryExpress, conn);

                        if (value > 0) {
                            return entry;
                        }
                    } else {
                        return profileMobileDeviceAccessor.insert(entry, conn);
                    }
                }

                return null;
            } else {
                return profileMobileDeviceAccessor.insert(entry, conn);
            }

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by id
    public ProfileMobileDevice getProfileMobileDevice(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileMobileDeviceAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //query  ProfileMobileDevice
    public PageRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<ProfileMobileDevice> returnValue = new PageRows<ProfileMobileDevice>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(profileMobileDeviceAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public RangeRows<ProfileMobileDevice> queryProfileMobileDevice(QueryExpress queryExpress, Rangination range) throws DbException {
        RangeRows<ProfileMobileDevice> returnValue = new RangeRows<ProfileMobileDevice>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(profileMobileDeviceAccessor.query(queryExpress, range, conn));
            returnValue.setRange(range);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int updateProfileMobileDevice(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileMobileDeviceAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileDeveloper createProfileDeveloper(ProfileDeveloper profileDeveloper) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileDeveloperAccessor.insert(profileDeveloper, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileDeveloper getProfileDeveloper(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDeveloperAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ProfileDeveloper> queryProfileDeveloper(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDeveloperAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ProfileDeveloper> queryProfileDeveloperByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ProfileDeveloper> pageRows = new PageRows<ProfileDeveloper>();
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<ProfileDeveloper> list = profileDeveloperAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyProfileDeveloper(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileDeveloperAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileBlog getProfileBlogByPhone(String phone, ActStatus actStatus) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileBlogAccessor.get(new QueryExpress().add(QueryCriterions.eq(ProfileBlogField.PHONENUM, phone))
                    .add(QueryCriterions.eq(ProfileBlogField.PHONEVERIFYSTATUS, actStatus.getCode())), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileClientMobileDevice createProfileClientMobileDevice(ProfileClientMobileDevice mobileDevice) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileClientMobileDeviceAccessor.insert(mobileDevice, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ProfileClientMobileDevice getProfileClientMobileDevice(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileClientMobileDeviceAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ProfileClientMobileDevice> queryProfileClientMobileByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ProfileClientMobileDevice> pageRows = new PageRows<ProfileClientMobileDevice>();
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<ProfileClientMobileDevice> list = profileClientMobileDeviceAccessor.query(queryExpress, pagination, conn);
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ProfileClientMobileDevice> queryProfileClientMobileByList(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return profileClientMobileDeviceAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyProfileClientMobile(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return profileClientMobileDeviceAccessor.modify(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    /*------------------ SocialProfileBlogAccessor  -------------       ---------------*/
    public SocialProfileBlog createSocialProfileBlog(SocialProfileBlog entity) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            String domain = entity.getDomain();
            if (StringUtil.isEmpty(domain)) {
                int tryTimes = 0;
                do {
                    domain = ProfileDomainGenerator.generateProfileDomain(System.currentTimeMillis(), ProfileDomainGenerator.KEY_DOMAIN_LEN);
                    if (!StringUtil.isEmpty(domain)) {
                        break;
                    }
                    tryTimes++;
                } while (tryTimes < 5);
            }
            entity.setDomain(domain);
            if (StringUtil.isEmpty(entity.getScreenName())) {
                entity.setScreenName("joyme_" + domain);
            }

            return socialProfileBlogAccessor.insert(entity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialProfileBlog getSocialProfileBlogByUno(String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialProfileBlogAccessor.getByUno(uno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialProfileBlog getSocialProfileBlogByScreenName(String screenName) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialProfileBlogAccessor.getByScreenName(screenName, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, SocialProfileBlog> querySocialProfileBlogsByLikeScreenName(String screenName) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialProfileBlogAccessor.queryProfileBlogsByLikeScreenName(screenName, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Map<String, SocialProfileBlog> querySocialProfileBlogByScreenNamesMap(Set<String> screenNames) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialProfileBlogAccessor.queryByScreenNameMaps(screenNames, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialProfileBlog getSocialProfileBlogByDomain(String domain) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialProfileBlogAccessor.getByDomain(domain, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialProfileBlog> querySocialProfileBlogsByDateStep(Date startDate, Date endDate, Pagination page) throws DbException {
        PageRows<SocialProfileBlog> returnValue = new PageRows<SocialProfileBlog>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialProfileBlogAccessor.queryBlogByDateStep(startDate, endDate, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<SocialProfileBlog> searchSocialProfileBlog(String keyWord, Pagination page) throws DbException {
        PageRows<SocialProfileBlog> returnValue = new PageRows<SocialProfileBlog>();

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialProfileBlogAccessor.search(keyWord, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }


    public boolean updateSocialBlog(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialProfileBlogAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {

            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialProfileBlog> querySocialProfileBlogByPage(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<SocialProfileBlog> returnValue = new PageRows<SocialProfileBlog>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(socialProfileBlogAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {

            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public SocialProfileBlog getSocialProfileBlogByPhone(String phone, ActStatus actStatus) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return socialProfileBlogAccessor.get(new QueryExpress().add(QueryCriterions.eq(SocialProfileBlogField.PHONENUM, phone))
                    .add(QueryCriterions.eq(SocialProfileBlogField.PHONEVERIFYSTATUS, actStatus.getCode())), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    /*------------------ SocialProfileBlogAccessor  -------------       ---------------*/

}
