package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-9-9
 * Time: 下午5:29
 * To change this template use File | Settings | File Templates.
 */
public class AccountVirtualHeadIcon implements Serializable {
	private String pic;
	private String pic_M;
	private String pic_S;
	private String pic_SS;


	public static String toJson(AccountVirtualHeadIcon accountVirtualHeadIcon) {
		return JsonBinder.buildNonDefaultBinder().toJson(accountVirtualHeadIcon);
	}

	public static AccountVirtualHeadIcon fromJson(String jsonString) {
		AccountVirtualHeadIcon param = new AccountVirtualHeadIcon();
		try {
			if (!StringUtil.isEmpty(jsonString)) {
				param = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<AccountVirtualHeadIcon>() {
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return param;
	}

	public String toJson() {
		return JsonBinder.buildNonNullBinder().toJson(this);
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic_M() {
		return pic_M;
	}

	public void setPic_M(String pic_M) {
		this.pic_M = pic_M;
	}

	public String getPic_S() {
		return pic_S;
	}

	public void setPic_S(String pic_S) {
		this.pic_S = pic_S;
	}

	public String getPic_SS() {
		return pic_SS;
	}

	public void setPic_SS(String pic_SS) {
		this.pic_SS = pic_SS;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
