package com.enjoyf.webapps.joyme.weblogic.profile;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.SMSLogType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.RandomRange;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sms.SMSSenderSngl;
import com.enjoyf.webapps.joyme.dto.profile.MobileCodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-24 上午11:54
 * Description:
 */
@Service(value = "mobileVerifyWebLogic")
public class MobileVerifyWebLogic {

    Logger logger = LoggerFactory.getLogger(MobileVerifyWebLogic.class);

    public static final int VERIFY_REG_TIMES = -1;
    public static final int VERIFYSMS_TIMES = 5;
    public static final int PC_VERIFYSMS_TIMES = 5;

    private RandomRange randomRange = new RandomRange(10000, 99999);
    private static final String MOBILE_KEY = "mobile_verify";
    private static final String MOBILE_MOBEILE_KEY = "mobile_verify_mobile";

    public MobileCodeDTO generatorCode(String profileId, String phone, String template, int times) throws ServiceException {

        MobileCodeDTO returnObj = new MobileCodeDTO();

        UserPropKey key = new UserPropKey(UserPropDomain.DEFAULT, profileId, MOBILE_KEY + "_" + DateUtil.formatDateToString(new Date(), DateUtil.DATE_FORMAT));

        UserProperty userProperty = UserPropsServiceSngl.get().getUserProperty(key);
        if (times > 0 && userProperty.getIntValue() >= times) {
            //out times
            returnObj.setRs(MobileCodeDTO.RS_ERROR_OUT_LIMIT);
            return returnObj;
        }

        UserPropsServiceSngl.get().increaseUserProperty(key, 1);
        String returnString = String.valueOf(randomRange.nextInt());
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("validCode", returnString);
        GAlerter.lan("this profile:" + profileId + " send code is " + returnString);

        try {
            boolean bool = MiscServiceSngl.get().sendSMS(phone, NamedTemplate.parse(template).format(paramMap), SMSLogType.VERIFY_CODE.getCode(), SMSSenderSngl.CODE_DEFAULT);
            if (bool) {
                returnObj.setRs(MobileCodeDTO.RS_SUCCESS);
                returnObj.setCode(returnString);
            } else {
                GAlerter.lab(this.getClass().getName() + " sendSMS error:");
                returnObj.setRs(MobileCodeDTO.RS_ERROR_SEND);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendSMS error.e:", e);
            returnObj.setRs(MobileCodeDTO.RS_ERROR_SEND);
        }
        return returnObj;
    }


    public MobileCodeDTO sendCode(String phone, String template, int times, String smssendersnglcode) throws ServiceException {

        MobileCodeDTO returnObj = new MobileCodeDTO();

        if (!MiscServiceSngl.get().checkPhoneLimit(phone)) {
            logger.info("checkPhoneLimit out limit phone:" + phone);
            returnObj.setRs(MobileCodeDTO.RS_ERROR_OUT_LIMIT);
            return returnObj;
        }

        UserPropKey key = new UserPropKey(UserPropDomain.DEFAULT, phone, MOBILE_KEY + "_" + DateUtil.formatDateToString(new Date(), DateUtil.DATE_FORMAT));

        UserProperty userProperty = UserPropsServiceSngl.get().getUserProperty(key);
        if (times > 0 && userProperty.getIntValue() >= times) {
            //out times
            returnObj.setRs(MobileCodeDTO.RS_ERROR_OUT_LIMIT);
            return returnObj;
        }

        UserPropsServiceSngl.get().increaseUserProperty(key, 1);
        String returnString = String.valueOf(randomRange.nextInt());
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("validCode", returnString);
        GAlerter.lan("this profile:" + phone + " send code is " + returnString);

        try {
            boolean bool = MiscServiceSngl.get().sendSMS(phone, NamedTemplate.parse(template).format(paramMap), SMSLogType.VERIFY_CODE.getCode(), smssendersnglcode);
            if (bool) {
                returnObj.setRs(MobileCodeDTO.RS_SUCCESS);
                returnObj.setCode(returnString);
            } else {
                GAlerter.lab(this.getClass().getName() + " sendSMS error:");
                returnObj.setRs(MobileCodeDTO.RS_ERROR_SEND);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendSMS error.e:", e);
            returnObj.setRs(MobileCodeDTO.RS_ERROR_SEND);
        }
        return returnObj;
    }


    public String modifyMobileByReg(String profileId, String phone, String ip) throws ServiceException {
        boolean bVal = unbindMobile(profileId, ip);
        if (bVal) {
            bVal = UserCenterServiceSngl.get().bindMobile(phone, profileId, ip);
        }

        return bVal ? PhoneVerifyError.SUCCESS : PhoneVerifyError.ERROR_VERIFY_NULL;
    }

    public String modifyMobileByReg(String profileId, String phone, String profileKey, String ip) throws ServiceException {
        //查询注册手机号有没绑定过
        String result = verifyMobileHasBinded(phone, profileKey);
        boolean bVal = true;

        if (!result.equals(PhoneVerifyError.SUCCESS)) {
            bVal = unbind(phone, profileKey);
        }

        if (bVal) {
            bVal = UserCenterServiceSngl.get().bindMobile(phone, profileId, ip);
        }

        return bVal ? PhoneVerifyError.SUCCESS : PhoneVerifyError.ERROR_VERIFY_NULL;

    }


    /**
     * @param profileId
     * @param phone
     * @param code
     * @param sessionCode
     * @param ip
     * @return
     * @throws ServiceException PHONE_HAS_BIND
     */
    public String verifyMobile(String profileId, String phone, String code, String sessionCode, String ip) throws ServiceException {
        if (!sessionCode.equals(code)) {
            return PhoneVerifyError.ERROR_VERIFY;
        }

        UserCenterServiceSngl.get().bindMobile(phone, profileId, ip);

        return PhoneVerifyError.SUCCESS;
    }

    public String modifyMobile(String profileId, String phone, String code, String sessionCode, String ip) throws ServiceException {
        if (!sessionCode.equals(code)) {
            return PhoneVerifyError.ERROR_VERIFY;
        }

        boolean bVal = unbindMobile(profileId, ip);
        if (bVal) {
            bVal = UserCenterServiceSngl.get().bindMobile(phone, profileId, ip);
        }

        return bVal ? PhoneVerifyError.SUCCESS : PhoneVerifyError.ERROR_VERIFY_NULL;
    }

    public boolean unbindMobile(String profileId, String ip) throws ServiceException {
        return UserCenterServiceSngl.get().unbindMobile(profileId, ip);
    }

    public boolean unbind(String phone, String profileKey) throws ServiceException {
        String id = UserCenterUtil.getProfileMobileId(phone, profileKey);
        return UserCenterServiceSngl.get().unbindMobile(id);

    }

    public String verifyMobileHasBinded(String phone, String profileKey) throws ServiceException {
        if (UserCenterServiceSngl.get().checkMobileIsBinded(phone, profileKey)) {
            return PhoneVerifyError.ERROR_PHONE_HAS_BIND;
        }

        return PhoneVerifyError.SUCCESS;
    }

}
