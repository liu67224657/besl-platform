/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline.test;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.util.Utility;

import java.util.Date;
import java.util.List;

/**
 * taijunli
 */
public class Main {
    public static void main(String[] args) {

        createCategory();

        Utility.sleep(10000);
    }

    public static void createCategory() {
        ViewCategory category = new ViewCategory();

        category.setCategoryName("xxxxxxx");
        category.setCreateDate(new Date());
        category.setCreateUserid("xxx");
        category.setCategoryAspect(ViewCategoryAspect.CONTENT_GAME);

        category.setValidStatus(ValidStatus.VALID);


        try {
            category = ViewLineServiceSngl.get().createCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewCategory category2 = new ViewCategory();

        category2.setCategoryName("yyyy");
        category2.setCreateDate(new Date());
        category2.setCreateUserid("yy");
        category2.setCategoryAspect(ViewCategoryAspect.CONTENT_GAME);

        category2.setValidStatus(ValidStatus.VALID);
        category2.setParentCategoryId(category.getCategoryId());

        try {
            category2 = ViewLineServiceSngl.get().createCategory(category2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<ViewCategory> list = ViewLineServiceSngl.get().queryCategoryTreeByAspectParent(ViewCategoryAspect.CONTENT_GAME, 0);

            for (ViewCategory cate : list) {
                System.out.println(cate.getCategoryId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
