package com.enjoyf.webapps.joyme.webpage.controller.activity;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.event.*;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.activity.EventActivityDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-6
 * Time: 上午10:08
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/json/activity/")
public class JsonActivityController {

    /**
     *
     * @param request   aid
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public String getActivity(HttpServletRequest request){
        String aid=request.getParameter("aid");
        long activityId=0;
        if(aid!=null){
            try {
                activityId=Long.parseLong(aid);
            } catch (NumberFormatException e) {
            }
        }

        ResultObjectMsg msg=new ResultObjectMsg(ResultObjectMsg.CODE_S);

        if(activityId==0){
            msg.setRs(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getCode());
            msg.setMsg(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getMsg());
            return JsonBinder.buildNormalBinder().toJson(msg);
        }

        try {
            Activity activity= ActivityServiceSngl.get().getActivity(activityId);
            if(activity==null){
                msg.setRs(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getCode());
                msg.setMsg(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getMsg());
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            EventActivityDTO dto=new EventActivityDTO();

            dto.setAid(activity.getActivityId());
            dto.setName(activity.getName());
            dto.setCount(activity.getCount());
            dto.setRestamount(activity.getRestamount());

            msg.setResult(dto);
            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured ServiceException.e: ",e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
    }

    /**
     *
     * @param request  aid uno ip
     * @return
     */
    @RequestMapping("/award")
    @ResponseBody
    public String award(HttpServletRequest request){
        String aid=request.getParameter("aid");
        String profileId = request.getParameter("pid");
        String appkey=request.getParameter("appKey");
        long activityId=0;
        Activity activity=null;
        if(aid!=null){
            try {
                activityId=Long.parseLong(aid);
                activity=ActivityServiceSngl.get().getActivity(activityId);
            } catch (NumberFormatException e) {
            } catch (ServiceException e) {
               GAlerter.lab(this.getClass().getName()+" occured error.e: ",e);
            }
        }

        ResultObjectMsg msg=new ResultObjectMsg(ResultObjectMsg.CODE_S);
        if(activity==null){
            msg.setRs(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getCode());
            msg.setMsg(ResultCodeConstants.ACTIVITY_ACTIVITY_NOTEXISTS.getMsg());
            return JsonBinder.buildNormalBinder().toJson(msg);
        }

        String uno=request.getParameter("uno");
        Profile profile= null;
        if(uno!=null){
            try {
                profile=ProfileServiceSngl.get().getProfileByUno(uno);
            } catch (ServiceException e) {
               GAlerter.lab(this.getClass().getName()+" get profile occured error.e: ",e);
            }
        }
        if(profile==null){
            msg.setRs(ResultCodeConstants.AWARD_USER_NOT_EXISTS.getCode());
            msg.setMsg(ResultCodeConstants.AWARD_USER_NOT_EXISTS.getMsg());
            return JsonBinder.buildNormalBinder().toJson(msg);
        }

        String ip=request.getParameter("ip");

        try {
            AwardResult result=ActivityAwardFactory.get().sendAwardByAwardType(activity,uno,profileId,appkey,new Date(),ip);

            if(result!=null){
                msg.setResult(result);
            }

            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured ServiceException.e: ",e);
            if(e.equals(ActivityServiceException.USER_HAS_AWARD)){
                msg.setRs(ResultCodeConstants.AWARD_USER_HAS_AWARD.getCode());
                msg.setMsg(ResultCodeConstants.AWARD_USER_HAS_AWARD.getMsg());
            }else if(e.equals(ActivityServiceException.AWARD_NOT_ENOUGH)){
                msg.setRs(ResultCodeConstants.AWARD_NOT_ENOUGH.getCode());
                msg.setMsg(ResultCodeConstants.AWARD_NOT_ENOUGH.getMsg());
            }else if(e.equals(ActivityServiceException.AWARD_GET_FAILED)){
                msg.setRs(ResultCodeConstants.AWARD_USER_GET_FAILED.getCode());
                msg.setMsg(ResultCodeConstants.AWARD_USER_GET_FAILED.getMsg());
            }else{
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("system.error");
            }


            return JsonBinder.buildNormalBinder().toJson(msg);
        }
    }
}
