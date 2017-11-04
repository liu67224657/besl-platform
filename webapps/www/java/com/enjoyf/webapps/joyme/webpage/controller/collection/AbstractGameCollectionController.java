package com.enjoyf.webapps.joyme.webpage.controller.collection;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ActivityCalEventTimeEvent;
import com.enjoyf.platform.service.event.system.GameIncrCollectionCacheEvent;
import com.enjoyf.platform.service.event.system.JoymeClientLineModifyEvent;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.ClientItemDomain;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-2-18
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGameCollectionController extends BaseRestSpringController {
    protected String rotateImage(String pic) {
        if (pic.startsWith("http://joymepic.qiniudn.com") || pic.startsWith("https://joymepic.qiniudn.com") || pic.startsWith("http://joymepic.joyme.com") || pic.startsWith("https://joymepic.joyme.com")) {
            pic += "?imageMogr2/rotate/270";
        }
        return pic;
    }
    
    /**
     * 事件
     *
     */
    public void sendOutEvent(String code,GameDB gameDB) {
        GameIncrCollectionCacheEvent gameIncrCollectionCacheEvent=new GameIncrCollectionCacheEvent();
        gameIncrCollectionCacheEvent.setCode(code);
        gameIncrCollectionCacheEvent.setGameDbId(gameDB.getGameDbId());
        try {
            EventDispatchServiceSngl.get().dispatch(gameIncrCollectionCacheEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
        
        ClientLineItem clientLineItem = new ClientLineItem();
        clientLineItem.setDirectId(String.valueOf(gameDB.getGameDbId()));
        clientLineItem.setItemDomain(ClientItemDomain.GAME);
        clientLineItem.setPicUrl(gameDB.getGameIcon());
        clientLineItem.setTitle(gameDB.getGameName());
        clientLineItem.setValidStatus(ValidStatus.VALID);
        clientLineItem.setDisplayOrder(gameDB.getPvSum());
        clientLineItem.setItemCreateDate(new Date());

        //todo 建议改一个有意义的名字.(insert or update?)
        JoymeClientLineModifyEvent joymeClientLineModifyEvent=new JoymeClientLineModifyEvent();
        joymeClientLineModifyEvent.setCode(code);
        joymeClientLineModifyEvent.setClientLineItem(clientLineItem);
        joymeClientLineModifyEvent.setGameDbId(String.valueOf(gameDB.getGameDbId()));
        
        try {
            EventDispatchServiceSngl.get().dispatch(joymeClientLineModifyEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
    }
}
