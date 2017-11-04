package com.enjoyf.platform.service.content;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:
 * </p>
 * 
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class MoodTemplate {
	
//	String str =null;
//
//	public MoodTemplate(String templage) {
//		prase(templage);
//	}
//
//	private void prase(String template) {
//		str = template;
//	}
//
//
//	public String format() {
//		str = replace(str);
//		return str;
//	}
//
//	public static void main(String[] args) {
//		//Pattern p = Pattern.compile("\\[(.*?)\\]");
//		String str = "[哈哈][猪头][馋嘴][[[[--[aaa";
//		str = replace(str);
//		System.out.println("str:"+str);
//	}
//
//	/*
//	 * 重写moodTemplate类
//	 * 2011-7-15 by zx
//	 * */
//	public static String replace(String str){
//		if(str == null || "".equals(str)){
//			System.out.println("replace  str null:  "+str);
//			return null;
//		}
//
//	     String regex="\\[(.*?)\\]";
//	     Pattern pattern=Pattern.compile(regex);
//	     Matcher matcher=pattern.matcher(str);
//	    if(matcher.find()){
//	    	String sUrl = getHref(matcher.group(1));
//	    	if(sUrl!=null){
//	    		str=matcher.replaceFirst(sUrl);
//	    		return replace(str);
//	    	}
//	    	  return str;
//	     }
//	    else {
//	        return str;
//	     }
//	}
//	public static String getHref(String keyName){
//		Mood mood = MoodPropsMgr.get().getImageUrlByCode(keyName);
//		if(mood == null || "".equals(mood)){
//			return null;
//		}
//	    return "<img src='"+ mood.getImgUrl() +"'/>";
//	}

	
}
