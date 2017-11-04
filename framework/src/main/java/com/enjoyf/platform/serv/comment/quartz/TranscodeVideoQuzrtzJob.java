package com.enjoyf.platform.serv.comment.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.serv.comment.CommentLogic;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.quartz.FivewhQuartzJob;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.lowagie.tools.concat_pdf;

import org.apache.commons.httpclient.HttpClient;
import org.hibernate.mapping.Array;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时检查转码视频状态并将转码成功的视频url更新到数据库
 * @author huazhang
 *
 */
public class TranscodeVideoQuzrtzJob extends FivewhQuartzJob {
	
	private static final String DOMAIN ="http://joymepic.joyme.com/";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        CommentLogic commentLogic = (CommentLogic) jobExecutionContext.getJobDetail().getJobDataMap().get(CommentLogic.class.getName());
        Map<String, String> videoMap=commentLogic.getTranscodeVideoMap();
        System.out.println("======================transcode status check================");
        if (!CollectionUtil.isEmpty(videoMap)) {
        	HttpClientManager httpClientManager=new HttpClientManager();
        	String url="http://api.qiniu.com/status/get/prefop";
			for (String commentId : videoMap.keySet()) {
				String persistentIds=videoMap.get(commentId);
				if (!StringUtil.isEmpty(persistentIds)) {
					List<String> videoUrList=new ArrayList<String>();
					String[] pids=persistentIds.split(",");
					try {
						for (int i = 0; i < pids.length; i++) {
							HttpParameter[] httpParameter = { new HttpParameter("id", pids[i].replaceAll(" ", "")) };
							HttpResult httpResult = httpClientManager.get(url, httpParameter);
							String ret = httpResult.getResult();
							if (!StringUtil.isEmpty(ret)) {

								JSONObject jsonObject = JSONObject.parseObject(ret);
								int code = jsonObject.getIntValue("code");
								if (code == 0) {// 转码成功
									JSONArray items = jsonObject.getJSONArray("items");
									JSONObject itemJsonObject = items.getJSONObject(0);
									String key = DOMAIN + itemJsonObject.getString("key");
									if(!StringUtil.isEmpty(key) && !videoUrList.contains(key)){
										videoUrList.add(key);
									}
								} else if (code == 3) {// 转码失败
									String msg = jsonObject.getString("error");
									GAlerter.lab("transcode failed, msg:" + msg);
								}

							} else {
								GAlerter.lab("http check transcode ret null error");
								continue;
							}
						}
						
						if (!CollectionUtil.isEmpty(videoUrList)) {
							commentLogic.removeTranscodeVide(commentId);
							CommentBean commentBean = commentLogic.getCommentBeanById(commentId);
							String expandString = commentBean.getExpandstr();
							JSONObject expendJsonObject = JSONObject.parseObject(expandString);
							expendJsonObject.put("videoUrl", videoUrList.toArray());
							UpdateExpress updateExpress = new UpdateExpress();
							updateExpress.set(CommentBeanField.EXPANDSTR, expendJsonObject.toJSONString());
							commentLogic.modifyCommentBeanById(commentId, updateExpress);
						} else {
							GAlerter.lab("getcommentbean expandstr null error");
							continue;
						}
					} catch (Exception e) {
						GAlerter.lab("ret parsejson exception:" + e.getMessage());
						continue;
					}
				}
			}
		}
    }
}
