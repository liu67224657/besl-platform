package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagPicParentParam implements Serializable {
	private String pic1;
	private String pic2;
	public AnimeTagPicParentParam() {

	}

	public AnimeTagPicParentParam(String pic1, String pic2) {
		this.pic1 = pic1;
		this.pic2 = pic2;
	}

	public static String toJson(AnimeTagPicParentParam paramTextJson) {
		return JsonBinder.buildNonDefaultBinder().toJson(paramTextJson);
	}

	public static AnimeTagPicParentParam fromJson(String jsonString) {
		AnimeTagPicParentParam param = new AnimeTagPicParentParam();
		try {
			if (!StringUtil.isEmpty(jsonString)) {
				param = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<AnimeTagPicParentParam>() {
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

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
}
