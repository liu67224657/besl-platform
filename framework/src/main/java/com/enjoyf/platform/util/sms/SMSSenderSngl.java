package com.enjoyf.platform.util.sms;

import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午1:25
 * Description:
 */
public class SMSSenderSngl {
    private static Map<String, SMSsender> senderMap = new HashMap<String, SMSsender>();


    public static final String CODE_DEFAULT = "default";
    public static final String CODE_PC = "pc";
    public static final String CODE_WANBA = "wanba";

    static {
        senderMap.put(CODE_DEFAULT, new BFLTSMSsender());
        senderMap.put(CODE_PC, new BFLTSMSsender());//todo
        senderMap.put(CODE_WANBA, new WanbaBFLTSMSsender());
    }


    //
    private static SMSsender instance;

    /**
     * @param code pc-PC default or null---www
     * @return
     */
    public static SMSsender getByCode(String code) {
        SMSsender smSsender = senderMap.get(code);
        if (smSsender == null) {
            GAlerter.lab(SMSSenderSngl.class.getName() + " SMSSenderSngl getByCode null return default. code:" + code);
            smSsender = senderMap.get("default");
        }

        return smSsender;
    }
}
