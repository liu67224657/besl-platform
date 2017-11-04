package com.enjoyf.webapps.joyme.dto.mobilegame;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-12
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
public class MobilegameIndexDTO implements Serializable {
	private long lineid;
	private String linename;
	private String linedesc;
	private String indexpic;
	private List<MobileGameDTO> gamedtolist;
	private List<MobileGameUserDTO> gaglist;

	private int maxPage;
	private int curPage;


	public List<MobileGameDTO> getGamedtolist() {
		return gamedtolist;
	}

	public void setGamedtolist(List<MobileGameDTO> gamedtolist) {
		this.gamedtolist = gamedtolist;
	}

	public String getIndexpic() {
		return indexpic;
	}

	public void setIndexpic(String indexpic) {
		this.indexpic = indexpic;
	}

	public long getLineid() {
		return lineid;
	}

	public void setLineid(long lineid) {
		this.lineid = lineid;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getLinedesc() {
		return linedesc;
	}

	public void setLinedesc(String linedesc) {
		this.linedesc = linedesc;
	}

	public List<MobileGameUserDTO> getGaglist() {
		return gaglist;
	}

	public void setGaglist(List<MobileGameUserDTO> gaglist) {
		this.gaglist = gaglist;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
