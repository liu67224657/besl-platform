package com.enjoyf.platform.util.oauth.weibo4j.model;

import com.enjoyf.platform.util.oauth.weibo4j.Weibo;
import com.enjoyf.platform.util.oauth.weibo4j.http.Response;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONArray;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sinaWeibo
 * 
 */
public class Tag extends WeiboResponse implements java.io.Serializable {

	private static final long serialVersionUID = 2177657076940291492L;

	private String id;           //标签id

	private String value;        //标签value

	public Tag(JSONObject json) throws WeiboException, JSONException {
		if (!json.getString("id").isEmpty()) {
			id = json.getString("id"); 
		}
		if(!json.getString("value").isEmpty()) {
			value = json.getString("value");
		}else {
			Iterator<String> keys = json.sortedKeys();
			if (keys.hasNext()) {
				id = keys.next();
				value = json.getString(id);	
			}
		}
	}
	public Tag(JSONObject json , Weibo weibo) throws WeiboException,JSONException {
		System.out.println(json);
		id = json.getString("id");
		value = json.getString("count");
	}


	public static List<Tag> constructTags(Response res) throws WeiboException {
		try {
			JSONArray list = res.asJSONArray();
			int size = list.length();
			List<Tag> tags = new ArrayList<Tag>(size);
			for (int i = 0; i < size; i++) {
				tags.add(new Tag(list.getJSONObject(i)));
			}
			return tags;
		} catch (JSONException jsone) {
			throw new WeiboException(jsone);
		} catch (WeiboException te) {
			throw te;
		}
	}
	public static List<FavoritesTag> constructTag(Response res) throws WeiboException {
		try {
			JSONArray list = res.asJSONObject().getJSONArray("tags");
			int size = list.length();
			List<FavoritesTag> tags = new ArrayList<FavoritesTag>(size);
			for (int i = 0; i < size; i++) {
				tags.add(new FavoritesTag(list.getJSONObject(i)));
			}
			return tags;
		} catch (JSONException jsone) {
			throw new WeiboException(jsone);
		} catch (WeiboException te) {
			throw te;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public String toString() {

		return "tags{" + id + "," + value +'}';

	}

}
