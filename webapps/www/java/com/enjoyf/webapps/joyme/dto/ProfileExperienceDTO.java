package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.profile.AllowType;
import com.enjoyf.platform.service.profile.ProfileExperience;
import com.enjoyf.platform.service.profile.ProfileExperienceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-19
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public class ProfileExperienceDTO {
    private List<ProfileExperience> schoolExp = new ArrayList<ProfileExperience>();
    private List<ProfileExperience> companyExp = new ArrayList<ProfileExperience>();
    private boolean showExpSchool = true;
    private boolean showExpComp = true;
    private AllowType expSchAllowType = AllowType.A_ALLOW;
    private AllowType expCompAllowType = AllowType.A_ALLOW;
    //constructor
    public ProfileExperienceDTO() {
    }

    // the getter and setter

    public List<ProfileExperience> getSchoolExp() {
        return schoolExp;
    }

    public void setSchoolExp(List<ProfileExperience> schoolExp) {
        this.schoolExp = schoolExp;
    }

    public List<ProfileExperience> getCompanyExp() {
        return companyExp;
    }

    public void setCompanyExp(List<ProfileExperience> companyExp) {
        this.companyExp = companyExp;
    }

    public boolean isShowExpSchool() {
        return showExpSchool;
    }

    public void setShowExpSchool(boolean showExpSchool) {
        this.showExpSchool = showExpSchool;
    }

    public boolean isShowExpComp() {
        return showExpComp;
    }

    public void setShowExpComp(boolean showExpComp) {
        this.showExpComp = showExpComp;
    }

    public AllowType getExpSchAllowType() {
        return expSchAllowType;
    }

    public void setExpSchAllowType(AllowType expSchAllowType) {
        this.expSchAllowType = expSchAllowType;
    }

    public AllowType getExpCompAllowType() {
        return expCompAllowType;
    }

    public void setExpCompAllowType(AllowType expCompAllowType) {
        this.expCompAllowType = expCompAllowType;
    }

    public void addAllExp(List<ProfileExperience> list) {
        for (ProfileExperience experience : list) {
            if (experience.getExperienceType().equals(ProfileExperienceType.SCHOOL)) {
                schoolExp.add(experience);
            }
            if (experience.getExperienceType().equals(ProfileExperienceType.COMPANY)) {
                companyExp.add(experience);
            }
        }
    }
}
