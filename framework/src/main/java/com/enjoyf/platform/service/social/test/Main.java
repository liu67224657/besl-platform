package com.enjoyf.platform.service.social.test;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午7:43
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {


        ObjectRelation objectRelation = new ObjectRelation();
        objectRelation.setStatus(IntValidStatus.VALID);
        objectRelation.setProfileId("0568523f5331d8ddb70713327c27ccdd");
        objectRelation.setObjectId("894585");
        objectRelation.setProfileKey("www");
        objectRelation.setType(ObjectRelationType.GAME);

        try {
            SocialServiceSngl.get().saveObjectRelation(objectRelation);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

//        try {
//            SocialServiceSngl.get().removeObjectRelation("0568523f5331d8ddb70713327c27ccdd", "894585", ObjectRelationType.GAME, "www");
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }

        while (true) {
        }
    }

}
