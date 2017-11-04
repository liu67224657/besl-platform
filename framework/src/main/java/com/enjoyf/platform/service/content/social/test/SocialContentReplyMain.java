package com.enjoyf.platform.service.content.social.test;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContentReply;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;

import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午6:28
 * Description:
 */
public class SocialContentReplyMain {

    public static void main(String[] args) {
//        insertReply();

        PageRows<SocialContentReply> pageRows=queryReply(1);

        for(SocialContentReply reply:pageRows.getRows()){
            System.out.println(reply);
        }
    }


    private static void insertReply() {
        SocialContentReply socialContentReply = new SocialContentReply();

        socialContentReply.setContentId(23);
        socialContentReply.setContentUno("506bac64-ff59-43e1-b445-5c4b4af1edcf");

        socialContentReply.setReplyUno("506bac64-ff59-43e1-b445-5c4b4af1edcf");

        for (int i = 0; i < 1000; i++) {
            socialContentReply.setBody("ceshi");
            socialContentReply.setCreateTime(new Date());
            try {
                ContentServiceSngl.get().postSocialContentReply(socialContentReply);
            } catch (ServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private static PageRows<SocialContentReply> queryReply(int page) {
        try {


            return ContentServiceSngl.get().querySocialContentReply(23, new Pagination(1000, page, 10));
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
