package com.enjoyf.platform.service.joymeapp.test;

import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.SocialContent;
import com.enjoyf.platform.service.content.social.SocialContentAction;
import com.enjoyf.platform.service.content.social.SocialContentActionType;
import com.enjoyf.platform.service.content.social.SocialContentField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.openjpa.kernel.exps.QueryExpressions;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午7:43
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        try {
            List<TagDedearchives> list = JoymeAppServiceSngl.get().queryTagDedearchives(new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, -2l)));
            System.out.println(list.size());
            Set<String> commentIdSet = new HashSet<String>();

            for (TagDedearchives tag : list) {
                commentIdSet.add(tag.getDede_archives_id());
            }
            Map<String, CommentBean> map = CommentServiceSngl.get().queryCommentBeanByIds(commentIdSet);

            int i = 0;
            for (TagDedearchives tag : list) {
                CommentBean commentBean = map.get(tag.getDede_archives_id());
                if (commentBean != null) {
                    QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, tag.getId()));
                    UpdateExpress updateExpress = new UpdateExpress().set(TagDedearchivesFiled.PROFILE_ID, commentBean.getUri());
                    JoymeAppServiceSngl.get().modifyTagDedearchives(tag.getTagid(), tag.getDede_archives_id(), queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
                }
            }
            System.out.println(i);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


//        Timer timer = new Timer();
//
//      //  System.out.print(new Date().getHours(),100);
//
//        timer.schedule(new Time(), new Date().getHours(), 3000);
//    }
//
//    static class Time extends java.util.TimerTask {
//
//        public void run() {
//            System.out.println(new Date().getHours());
//        }
//    }
//        List<AppTag> appTagList = new ArrayList<AppTag>();
//        AppTag appTag = new AppTag();
//        appTag.setTagName("heheHE");
//        appTagList.add(appTag);
//        try {
//            JoymeAppServiceSngl.get().createAppTagRelation(10180L, appTagList);
//        } catch (ServiceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

//        Pagination page = new Pagination(500, 1, 500);
//        do{
//            try {
//                PageRows<SocialContent> pageRows = ContentServiceSngl.get().querySocialContentByPage(new QueryExpress()
//                        .add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, ActStatus.UNACT.getCode())), page);
//                if(pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())){
//                    for(SocialContent content:pageRows.getRows()){
//                        System.out.println("--------------------------------"+ content.getContentId() + "--------------------------------------");
//                        NextPageRows<SocialContentAction> actionList = ContentServiceSngl.get().querySocialContentAction(content.getContentId(), SocialContentActionType.AGREE, new NextPagination(0, 100, true));
//                        if(actionList != null && !CollectionUtil.isEmpty(actionList.getRows())){
//                            for(SocialContentAction action:actionList.getRows()){
//                                ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(action.getContentUno());
//                                if(blog == null){
//                                    ContentServiceSngl.get().removeSocialContentAction(content.getContentId(), action.getUno(), SocialContentActionType.AGREE);
//                                    ContentServiceSngl.get().removeSocialContent(content.getUno(), content.getContentId());
//                                }
//                                 ProfileBlog blogs = ProfileServiceSngl.get().getProfileBlogByUno(action.getUno());
//                                if(blogs == null){
//                                    ContentServiceSngl.get().removeSocialContent(content.getUno(), content.getContentId());
//                                }
//                            }
//                        }
//                        System.out.println("--------------------------------end--------------------------------------");
//                    }
//                }
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }while (!page.isLastPage());
//            System.out.println(TableUtil.getTableNumSuffix("03c62f37-b313-436f-afb5-2a627797c7ac".hashCode(), 100));
    }
}

