package com.enjoyf.platform.tools.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.db.profile.ProfileHandler;
import com.enjoyf.platform.db.tools.contenttrans.OldContentHandler;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileBlogField;
import com.enjoyf.platform.tools.contenttrans.OldContent;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-10 下午5:56
 * Description:
 */
public class AddProfileUserId {
    private static final Logger logger = LoggerFactory.getLogger(AddProfileUserId.class);

    private static ProfileHandler profileHandler;

    private static long seqUserId = 10000l;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            profileHandler = new ProfileHandler("writeable", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("init gameHandler error.");
        }

        queryProfile();

        System.out.println("======================finish:==============================" + seqUserId);
        System.exit(0);
    }


    private static void queryProfile() {
        Pagination page = new Pagination(1000, 1, 100);

        try {

            PageRows<ProfileBlog> pageRows;

            do {
                pageRows = profileHandler.query(new QueryExpress().add(QuerySort.add(ProfileBlogField.CREATEDATE, QuerySortOrder.DESC)), page);

                //
                for (ProfileBlog profileBlog : pageRows.getRows()) {
                    System.out.println("SSSSSSSSSSSSS" + profileBlog);

                    profileHandler.updateBlog(new UpdateExpress().set(ProfileBlogField.USERID, seqUserId++)
                            , new QueryExpress().add(QueryCriterions.eq(ProfileBlogField.UNO, profileBlog.getUno())));

                    System.out.println("RRRRRRRRRRRRRR" + profileBlog);
                }

                Utility.sleep(2000);
                //
                if (page.isLastPage()) {
                    break;
                } else {
                    page.setCurPage(page.getCurPage() + 1);
                }
            } while (true);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
