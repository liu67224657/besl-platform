package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.profile.ProfileHandler;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialActivityField;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.tools.profile.AddProfileUserId;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-7-19
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class UpdateSocialActivityUseSum {

    private static final Logger logger = LoggerFactory.getLogger(AddProfileUserId.class);

    private static ContentHandler contentHandler;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            contentHandler = new ContentHandler("content", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("init gameHandler error.");
        }
        try {
            ContentServiceSngl.get().modifySocialActivity(22l, new UpdateExpress().set(SocialActivityField.USE_SUM, 17));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
