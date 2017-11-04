package com.enjoyf.platform.service.event.system;


/**
 * 游戏库热门缓存数据增加
 * @author huazhang
 *
 */
public class GameIncrCollectionCacheEvent extends SystemEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;//缓存键
	
	private long gameDbId;//缓存游戏id

	public GameIncrCollectionCacheEvent(){
		super(SystemEventType.GAME_INCR_COLLECTION_LIST_CACHE);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getGameDbId() {
		return gameDbId;
	}

	public void setGameDbId(long gameDbId) {
		this.gameDbId = gameDbId;
	}

}
