package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;

/**
 * 
 * @author huazhang
 *
 */
public class JoymeClientLineModifyEvent extends SystemEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	private String gameDbId;
	
	private ClientLineItem clientLineItem;

	public JoymeClientLineModifyEvent(){
		super(SystemEventType.JOYMEAPP_CLIENT_LINE_ITEM);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ClientLineItem getClientLineItem() {
		return clientLineItem;
	}

	public void setClientLineItem(ClientLineItem clientLineItem) {
		this.clientLineItem = clientLineItem;
	}

	public String getGameDbId() {
		return gameDbId;
	}

	public void setGameDbId(String gameDbId) {
		this.gameDbId = gameDbId;
	}

}
