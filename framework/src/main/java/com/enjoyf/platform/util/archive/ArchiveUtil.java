package com.enjoyf.platform.util.archive;

import com.enjoyf.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-4 下午2:45
 * Description:
 */
public class ArchiveUtil {

    private static final Pattern ARCHIVE_NUMBER_SUFFIX=Pattern.compile("([/\\d]+)\\.html");

	public static String getArchiveId(String url) {
		String[] urls = url.split("/");
        String archiveId = "";
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

			if (StringUtil.isNumeric(item)) {
				number += item;
			}
		}

		if (number.length() > 8 && number.startsWith("20")) {
			archiveId = number.substring(8, number.length());
		}

		return archiveId;
	}


    public static String formatUrl(String url) {
//        String s="http://localhost:8080/syhb2/pingguoduan/2/0141/0/315/64/05.html";
        Matcher matcher = ARCHIVE_NUMBER_SUFFIX.matcher(url);

        StringBuffer sb=new StringBuffer();
        if(matcher.find()){
            String numberString=matcher.group(1);

            numberString=numberString.replace("/","");
            String id= String.valueOf(getArchiveId(numberString));

            int position=numberString.lastIndexOf(id);
            String prefix=numberString.substring(0,position);

            String urlSuffix="/"+prefix+"/"+id+".html";

            matcher.appendReplacement(sb,urlSuffix);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}
