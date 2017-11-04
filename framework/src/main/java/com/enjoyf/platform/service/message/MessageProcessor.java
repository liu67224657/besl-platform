package com.enjoyf.platform.service.message;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
public interface MessageProcessor {

      public boolean processSendMessage(Map<String, Object> paramMap);

}
