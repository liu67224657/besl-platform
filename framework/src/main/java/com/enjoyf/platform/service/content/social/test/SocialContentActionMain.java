package com.enjoyf.platform.service.content.social.test;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContentAction;
import com.enjoyf.platform.service.content.social.SocialContentActionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;

import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午6:28
 * Description:
 */
public class SocialContentActionMain {

    public static void main(String[] args) {
//        NextPageRows<SocialContentAction> pageRows = query(119);
//
//        for (SocialContentAction action : pageRows.getRows()) {
//            System.out.println(action);
//        }
        post();
    }

    private static void post() {
        SocialContentAction action = new SocialContentAction();

        action.setContentId(23);
        action.setContentUno("506bac64-ff59-43e1-b445-5c4b4af1edcf");
        action.setType(SocialContentActionType.AGREE);
        action.setUno("aed9e387-8784-4c7e-a39c-53e47a07929b");

        for (int i = 0; i < 10; i++) {
            action.setCreateTime(new Date());
            try {
                ContentServiceSngl.get().createSocialContentAction(action);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    private static NextPageRows query(long id) {
        try {
            return ContentServiceSngl.get().querySocialContentAction(23, SocialContentActionType.AGREE, new NextPagination(id, 5,false));
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
