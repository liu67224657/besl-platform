/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp.clientline;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.archive.ArchiveUtil;
import com.enjoyf.platform.util.log.GAlerter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-7 下午2:28
 * Description:
 */
public class ClientLineCMSWebDataProcessor implements ClientLineWebDataProcessor {


	private static final Pattern PATTERN_REG_CMSARCHIVEID = Pattern.compile("/(\\d+).html");

	private static final String PREFIX_MARTICLE_HOST = "marticle.";
	private static final String PREFIX_WWW_HOST = "www.";

	private static final String REPLACE_STR = "marticle.joyme.com/marticle";

	private static final String PREFIX_WWW_HOST_30 = "172.16.75.30";

	@Override
	public ClientLineItem generateAddLineItem(Map<String, String> lineItemKeyValues, Map<String, String> errorMsgMap, Map<String, Object> msgMap) {
		String lineId = lineItemKeyValues.get("lineid");
		String itemType = lineItemKeyValues.get("itemtype");
		String itemDomain = lineItemKeyValues.get("itemdomain");
		String picurl = lineItemKeyValues.get("picurl");
		String directid = lineItemKeyValues.get("directid");
		String url = lineItemKeyValues.get("linkaddress");
		String itemname = lineItemKeyValues.get("itemname");
		String desc = lineItemKeyValues.get("desc");
		String redirecttype = lineItemKeyValues.get("redirecttype");
		String displayType = lineItemKeyValues.get("displaytype");
		String author = lineItemKeyValues.get("author");


		try {
            url = ArchiveUtil.formatUrl(url);

			URL urlObj = new URL(url);
			String host = urlObj.getHost();

			if (!host.contains(PREFIX_MARTICLE_HOST) && !host.contains(PREFIX_WWW_HOST) && !host.contains(PREFIX_WWW_HOST_30)) {
				errorMsgMap.put("urlerror", "article.link.invalid");
				return null;
			}

			if (url.contains(PREFIX_WWW_HOST)) {
				url = url.replace(urlObj.getHost(), REPLACE_STR);
			}
		} catch (MalformedURLException e) {
			errorMsgMap.put("urlerror", "article.link.invalid");
			return null;
		}


		AppRedirectType redirectType = AppRedirectType.getByCode(Integer.parseInt(redirecttype));
		if (AppRedirectType.CMSARTICLE.equals(redirectType)) {
			url = url.replace("marticle/", "json/");
		}


		//
		Archive archive = null;
		try {
			int archiveId = getArchiveId(url);
			if (archiveId <= 0) {
				errorMsgMap.put("urlerror", "article.link.invalid");
				GAlerter.lab(this.getClass().getName() + " occured error archiveid<=0.url: " + url);
				return null;
			} else {
				archive = JoymeAppServiceSngl.get().getArchiveById(archiveId);
			}


		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
			errorMsgMap.put("urlerror", "article.link.invalid");
			return null;
		}

		if (archive == null) {
			errorMsgMap.put("urlerror", "article.null");
			return null;
		}

		ClientLineItem clientLineItem = new ClientLineItem();
		clientLineItem.setDesc(StringUtil.isEmpty(desc) || desc == "" ? archive.getDesc() : desc);
		clientLineItem.setLineId(Integer.parseInt(lineId));
		clientLineItem.setDirectId(String.valueOf(archive.getArchiveId()));
		clientLineItem.setItemDomain(ClientItemDomain.getByCode(Integer.parseInt(itemDomain)));
		clientLineItem.setPicUrl(StringUtil.isEmpty(picurl) ? archive.getIcon() : picurl);
		clientLineItem.setItemType(ClientItemType.getByCode(Integer.parseInt(itemType)));
		clientLineItem.setTitle(StringUtil.isEmpty(itemname) || itemname == "" ? archive.getTitle() : itemname);
		clientLineItem.setUrl(url);
		clientLineItem.setRedirectType(AppRedirectType.getByCode(Integer.parseInt(redirecttype)));
		clientLineItem.setValidStatus(ValidStatus.VALID);
		clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
		clientLineItem.setItemCreateDate(new Date());
		clientLineItem.setAppDisplayType(AppDisplayType.getByCode(Integer.parseInt(displayType)));
		clientLineItem.setTypeName(StringUtil.isEmpty(archive.getTypeName()) ? "" : archive.getTypeName());
		clientLineItem.setTypeColor(StringUtil.isEmpty(archive.getTypeColor()) ? "" : archive.getTypeColor());
		clientLineItem.setAuthor(StringUtil.isEmpty(author) || author == "" ? archive.getAuthor() : author);
		return clientLineItem;
	}


	@Override
	public ClientLineItemDTO buildViewLineItemDTOs(ClientLineItem item) throws Exception {
		Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.parseInt(item.getDirectId()));
		if (archive == null) {
			return null;
		}


		ClientLineItemDTO dto = new ClientLineItemDTO();

		dto.setTitle(item.getTitle() == null ? archive.getTitle() : item.getTitle());
		dto.setDesc(item.getDesc() == null ? archive.getDesc() : item.getDesc());
		dto.setTagList(archive.getArchiveTagList());
		if (item.getParam() != null) {
			dto.setBgColor(item.getParam().getBgcolor());
		}
		dto.setId(item.getItemId());
		dto.setLid(item.getLineId());
		dto.setCreateDate(item.getItemCreateDate());
		dto.setDomain(item.getItemDomain().getCode());
		dto.setDid(item.getDirectId());
		dto.setLink(item.getUrl());
		dto.setOrder(item.getDisplayOrder());
		dto.setPic(item.getPicUrl());
		dto.setrType(item.getRedirectType().getCode());
		dto.setType(item.getItemType().getCode());
		dto.setStatus(item.getValidStatus().getCode());
		dto.setTypeName(archive.getTypeName() == null ? "" : archive.getTypeName());
		dto.setTypeColor(archive.getTypeColor() == null ? "" : archive.getTypeColor());
		return dto;
	}

	public int getArchiveId(String url) {
		String[] urls = url.split("/");
		int archiveId = 0;
		String number = "";
		for (int i = 0; i < urls.length; i++) {
			String item = urls[i];
			if (item.endsWith(".html")) {
				item = item.replaceAll(".html", "");
				int position = item.indexOf("_");
				if (position >= 0) {
					item = item.substring(0, position);
				}
			}

			if (com.enjoyf.util.StringUtil.isNumeric(item)) {
				number += item;
			}
		}

		if (number.length() > 8 && number.startsWith("20")) {
			archiveId = Integer.parseInt(number.substring(8, number.length()));
		}

		return archiveId;
	}


	public static void main(String[] args) {
		System.out.println(new ClientLineCMSWebDataProcessor().getArchiveId("http://www.joyme.com/news/gamenews/201312/1220777.html"));
	}
}
