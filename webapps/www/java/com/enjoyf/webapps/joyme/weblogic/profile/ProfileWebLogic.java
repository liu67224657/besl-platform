package com.enjoyf.webapps.joyme.weblogic.profile;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileExperience;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.service.social.SocialServiceException;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.MobileProfileClientDTO;
import com.enjoyf.webapps.joyme.dto.MobileProfileDTO;
import com.enjoyf.webapps.joyme.dto.MobileProfileDetail;
import com.enjoyf.webapps.joyme.dto.MobileProfileSum;
import com.enjoyf.webapps.joyme.dto.ProfileExperienceDTO;
import com.enjoyf.webapps.joyme.dto.MobileProfileMiniDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The profile logic is used by webpage and webapi.
 */

@Service(value = "profileWebLogic")
public class ProfileWebLogic {
    //the logger
    private Logger logger = LoggerFactory.getLogger(ProfileWebLogic.class);


    //get the profile mini for client.
    public MobileProfileMiniDTO getProfileMiniForClient(String uno) {
        MobileProfileMiniDTO returnValue = null;

        try {
            Profile profile = ProfileServiceSngl.get().getProfileByUno(uno);

            if (profile != null) {
                returnValue = new MobileProfileMiniDTO();

                returnValue.setUno(uno);
                returnValue.setName(profile.getBlog().getScreenName());
                returnValue.setIcon(profile.getBlog().getHeadIcon());
            }
        } catch (Exception e) {
            //
            GAlerter.lab("ProfileWebLogic getProfileMiniForClient call ProfileServiceSngl to getProfileByUno error.", e);
        }


        return returnValue;
    }

    public MobileProfileClientDTO getProfileForClientNotSignin(String uno) {
        MobileProfileClientDTO returnValue = null;

        try {
            Profile profile = ProfileServiceSngl.get().getProfileByUno(uno);

            if (profile != null) {
                returnValue = new MobileProfileClientDTO();

                MobileProfileDTO pc = new MobileProfileDTO();
                pc.setUno(profile.getUno());
                pc.setIcon(ImageResolveUtil.genImageByTemplate(profile.getBlog().getHeadIcon(), ImageSize.IMAGE_SIZE_S));
                pc.setName(profile.getBlog().getScreenName());
                pc.setDesc(profile.getBlog().getDescription());

                MobileProfileDetail detail = new MobileProfileDetail();
                detail.setSex(profile.getDetail().getSex());
                if(!StringUtil.isEmpty(profile.getDetail().getVerifyDesc())){
                    detail.setVdesc(profile.getDetail().getVerifyDesc());
                }

                MobileProfileSum sum = new MobileProfileSum();
                sum.setBlog(profile.getSum().getBlogSum());
                sum.setFans(profile.getSum().getFansSum());
                sum.setFavor(profile.getSum().getFavorSum());
                sum.setFocus(profile.getSum().getFocusSum());
                sum.setFwd(profile.getSum().getForwardSum());

                returnValue.setPc(pc);
                returnValue.setDetail(detail);
                returnValue.setSum(sum);

            }
        } catch (Exception e) {
            //
            GAlerter.lab("ProfileWebLogic getProfileForClient call ProfileServiceSngl to getProfileByUno error.", e);
        }


        return returnValue;
    }

     //get the profile for client.
    public MobileProfileClientDTO getProfileForClient(String srcUno, String uno) {
        MobileProfileClientDTO returnValue = null;

        try {
            Profile profile = ProfileServiceSngl.get().getProfileByUno(uno);

            if (profile != null) {
                returnValue = new MobileProfileClientDTO();

                MobileProfileDTO pc = new MobileProfileDTO();
                pc.setUno(profile.getUno());
                pc.setIcon(ImageResolveUtil.genImageByTemplate(profile.getBlog().getHeadIcon(), ImageSize.IMAGE_SIZE_S));
                pc.setName(profile.getBlog().getScreenName());
                pc.setDesc(profile.getBlog().getDescription());


                MobileProfileDetail detail = new MobileProfileDetail();
                detail.setSex(profile.getDetail().getSex());
                if(!StringUtil.isEmpty(profile.getDetail().getVerifyDesc())){
                    detail.setVdesc(profile.getDetail().getVerifyDesc());
                }

                MobileProfileSum sum = new MobileProfileSum();
                sum.setBlog(profile.getSum().getBlogSum());
                sum.setFans(profile.getSum().getFansSum());
                sum.setFavor(profile.getSum().getFavorSum());
                sum.setFocus(profile.getSum().getFocusSum());
                sum.setFwd(profile.getSum().getForwardSum());

                returnValue.setPc(pc);
                returnValue.setDetail(detail);
                returnValue.setSum(sum);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("ProfileWebLogic getProfileForClient call ProfileServiceSngl to getProfileByUno error.", e);
        }


        return returnValue;
    }

    public ProfileExperienceDTO getProfileExperience(String expUno){
        ProfileExperienceDTO profileExperienceDTO = new ProfileExperienceDTO();
        try {
            List<ProfileExperience> list =  ProfileServiceSngl.get().queryProfileExperienceByUno(expUno);
            profileExperienceDTO.addAllExp(list);
        } catch (ServiceException e) {
            GAlerter.lab("ProfileWebLogic getProfileExperience call ProfileServiceSngl to queryProfileExperienceByUno error.", e);
        }
        return profileExperienceDTO;
    }


}
