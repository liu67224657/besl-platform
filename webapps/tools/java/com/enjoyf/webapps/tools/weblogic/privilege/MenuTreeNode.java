package com.enjoyf.webapps.tools.weblogic.privilege;

import java.io.Serializable;

public class MenuTreeNode implements Serializable{
	private String id = null;

	private String parentId = null;

	private String name = null;

	private String url = "";

	private String description = "";

	private String target = "";

	private String iconUrl = "";

	public MenuTreeNode(String id, String parentId, String name) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
