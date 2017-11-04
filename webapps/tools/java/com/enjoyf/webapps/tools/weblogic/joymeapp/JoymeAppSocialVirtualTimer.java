package com.enjoyf.webapps.tools.weblogic.joymeapp;

import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-21
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppSocialVirtualTimer extends Thread {


	private int timerSize;
	private long timeInterval;
	private String type; //agree reply focus
	private SocialContent socialContent;
	private List<String> unos;


	private List<String> bodys;

	private String uno;
	private int action;

	public JoymeAppSocialVirtualTimer(String type, List<String> unos, String uno, int action, long timerConstant) {
		this.timerSize = unos.size();
		this.type = type;
		this.unos = unos;
		this.uno = uno;
		this.action = action;
		this.timeInterval = timerConstant;
	}

	public JoymeAppSocialVirtualTimer(String type, SocialContent socialContent, List<String> unos, long timerConstant) {
		this.timerSize = unos.size();
		this.type = type;
		this.socialContent = socialContent;
		this.unos = unos;
		this.timeInterval = timerConstant;
	}

	public JoymeAppSocialVirtualTimer(String type, SocialContent socialContent, List<String> unos, List<String> bodys, long timerConstant) {
		this.timerSize = unos.size();
		this.type = type;
		this.socialContent = socialContent;
		this.unos = unos;
		this.bodys = bodys;
		this.timeInterval = timerConstant;
	}


	@Override
	public void run() {

		if (type.equals("agree")) {
			for (int i = 0; i < getTimerSize(); i++) {
				agree(socialContent, unos.get(i));
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i==getTimerSize()){
					break;
				}
			}
		} else if (type.equals("reply")) {
			for (int i = 0; i < getTimerSize(); i++) {
				reply(socialContent, unos.get(i), bodys.get(i));
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i==getTimerSize()){
					break;
				}
			}
		} else if (type.equals("focus")) {
			for (int i = 0; i < getTimerSize(); i++) {
//				focus(uno, unos.get(i), action);
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(i==getTimerSize()){
					break;
				}
			}
		}

	}

//	private void focus(String srcUno, String uno2, int action) {
//		try {
//			if (action == 0) {//用户关注
////				focus(srcUno, uno2, "", RelationType.SFOCUS);
//			} else if (action == 1) {//用户被关注
////				focus(uno2, srcUno, "", RelationType.SFOCUS);
//			} else if (action == 3) {//取消用户关注
////				SocialServiceSngl.get().breakRelation(srcUno, uno2, RelationType.SFOCUS);
//			} else if (action == 4) {//取消用户被关注
////				SocialServiceSngl.get().breakRelation(uno2, srcUno, RelationType.SFOCUS);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			GAlerter.lab(this.getClass().getName() + " focus occured ServiceException.e: ", e);
//		}
//	}

	private void agree(SocialContent sc, String uno) {
		SocialContentAction socialContentAction = new SocialContentAction();
		socialContentAction.setSocialContentPlatformDomain(SocialContentPlatformDomain.IOS);
		socialContentAction.setLat(0);
		socialContentAction.setLon(0);
		socialContentAction.setType(SocialContentActionType.AGREE);
		socialContentAction.setContentId(sc.getContentId());
		socialContentAction.setContentUno(sc.getUno());
		socialContentAction.setUno(uno);
		try {
			ContentServiceSngl.get().createSocialContentAction(socialContentAction);
		} catch (ServiceException e) {
			e.printStackTrace();
			GAlerter.lab(this.getClass().getName() + " agree occured ServiceException.e: ", e);
		}
	}

	private void reply(SocialContent sc, String uno, String body) {
		SocialContentReply scr = new SocialContentReply();
		scr.setReplyUno(uno);
		scr.setContentId(sc.getContentId());
		scr.setContentUno(sc.getUno());
		scr.setCreateTime(new Date());
		scr.setCreateIp("127.0.0.1");
		scr.setBody(body);
		scr.setSocialContentPlatformDomain(SocialContentPlatformDomain.IOS);
		try {
			ContentServiceSngl.get().postSocialContentReply(scr);
		} catch (ServiceException e) {
			e.printStackTrace();
			GAlerter.lab(this.getClass().getName() + " reply occured ServiceException.e: ", e);
		}
	}




	public int getTimerSize() {
		return timerSize;
	}

	public void setTimerSize(int timerSize) {
		this.timerSize = timerSize;
	}


}
