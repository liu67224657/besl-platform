package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.profile.ProfileExperience;
import com.enjoyf.platform.service.profile.ProfileExperienceType;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileExperienceAccessor extends SequenceAccessor {
    //
    public List<ProfileExperience> insert(List<ProfileExperience> list, Connection conn) throws DbException;
    //
    public boolean remove(String uno, ProfileExperienceType experienceType, Connection conn) throws DbException;
    //
    public List<ProfileExperience> queryProfileExperienceByUno(String uno, Connection conn) throws DbException;

}
