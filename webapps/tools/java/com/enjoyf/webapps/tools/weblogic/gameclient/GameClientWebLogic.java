package com.enjoyf.webapps.tools.weblogic.gameclient;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.GameDBModifyTimeEvent;
import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Service;

/**
 * Created by zhimingli on 2015/2/4 0004.
 */
@Service(value = "gameClientWebLogic")
public class GameClientWebLogic {

	public void sendGameDBModifyTimeEvent(long gamedbFileId, GameDBModifyTimeFieldJson modifyTimeFieldJson) {
		GameDBModifyTimeEvent gameDBModifyTimeEvent = new GameDBModifyTimeEvent();
		gameDBModifyTimeEvent.setGamedbFileId(gamedbFileId);
		gameDBModifyTimeEvent.setModifyTimeFieldJson(modifyTimeFieldJson);
		try {
			GAlerter.lab("sendGameDBModifyTimeEvent:gamedbFileId=" + gamedbFileId + ",GameDBModifyTimeFieldJson=" + modifyTimeFieldJson);
			EventDispatchServiceSngl.get().dispatch(gameDBModifyTimeEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
