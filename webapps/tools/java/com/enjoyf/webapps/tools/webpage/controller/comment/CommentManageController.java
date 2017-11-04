package com.enjoyf.webapps.tools.webpage.controller.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentPermission;
import com.enjoyf.platform.service.comment.CommentPermissionType;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author huazhang
 *
 */
@Controller
@RequestMapping(value = "/comment/manage")
public class CommentManageController extends ToolsBaseController {

	@ResponseBody
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {
			
			List<CommentPermission> list = CommentServiceSngl.get().queryCommentPermissionList();
			if (!CollectionUtil.isEmpty(list)) {
				for (CommentPermission commentPermission : list) {
					Profile profileId = UserCenterServiceSngl.get().getProfileByProfileId(
							commentPermission.getProfileId());
					if (profileId != null) {
						commentPermission.setNick(profileId.getNick());
					}
				}
			}

			mapMessage.put("list", list);
			return new ModelAndView("/comment/manage/list",mapMessage);
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
			mapMessage.put("errorMsg", "system.error");
			return new ModelAndView("/comment/manage/list",mapMessage);
		}
	}

	@RequestMapping("/delete")
	public ModelAndView delete(HttpServletRequest request,
			@RequestParam(value = "id", required = false) String permissionId) {
		Map<String, Object> mapMessage = new HashMap<String, Object>();
		try {

			boolean isDeleted=CommentServiceSngl.get().deleteCommentPermission(permissionId);
			if (isDeleted) {
				return new ModelAndView("redirect:/comment/manage/list");
			}else {
				mapMessage.put("errorMsg", "删除失败");
				return new ModelAndView("redirect:/comment/manage/list",mapMessage);
			}
			
		} catch (Exception e) {
			mapMessage.put("errorMsg", "系统异常");
			GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
			return new ModelAndView("redirect:/comment/manage/list",mapMessage);
		}
	}
	
    @ResponseBody
    @RequestMapping("/createpage")
    public ModelAndView bindProfilePermission(HttpServletRequest request) {
         return new ModelAndView("/comment/manage/create");
    }
    
    @ResponseBody
    @RequestMapping("/create")
    public ModelAndView bindProfilePermission(HttpServletRequest request,
                                        @RequestParam(value = "nick", required = false) String nick
    ) {
    	Map<String, Object> model=new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(nick)) {
            	model.put("errorMsg", "nick null error");
            	return new ModelAndView("/comment/manage/create",model);
            }

            nick=URLDecoder.decode(nick, "utf8");
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);
            if (null == profile) {
            	model.put("errorMsg", "nick null error");
            	return new ModelAndView("/comment/manage/create",model);
            }
            String commentPermissionId=getPermissionId(profile.getProfileId(),CommentPermissionType.LIVE_COMMENT);
            CommentPermission permission = CommentServiceSngl.get().createCommentPermission(commentPermissionId, profile.getProfileId(), CommentPermissionType.LIVE_COMMENT, 0);
            if (null == permission) {
            	model.put("errorMsg", "create failed");
                return new ModelAndView("/comment/manage/create",model);
            }
            return new ModelAndView("redirect:/comment/manage/list");

        } catch (Exception e) {
        	model.put("errorMsg", "sys exception");
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
            return new ModelAndView("redirect:/comment/manage/create",model);
        }
    }
    
    private String getPermissionId(String profileId,CommentPermissionType permissionType){
    	return MD5Util.Md5(profileId+permissionType.getCode());
    }
}
