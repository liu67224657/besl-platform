package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ResourceDomainHotdeployConfig;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Resource server monitor
 */
public class ResourceServerMonitor {
    //
    private static final Logger logger = LoggerFactory.getLogger(ResourceServerMonitor.class);

    //
    private static volatile ResourceServerMonitor instance;

    private static final int MAX_TRY_TIMES = 3;

    //
    private NamingServiceMulti namingServiceMulti;

    //
    private Refresher refresher;
    private ResourceDomainHotdeployConfig config;

    private ChooseServerStragy chooseServerStragy;

    public static ResourceServerMonitor get() {
        if (instance == null) {
            synchronized (ResourceServerMonitor.class) {
                instance = new ResourceServerMonitor();
            }
        }

        return instance;
    }

    public void init() {
        //
        NamingServiceAddress namingServiceAddress = new NamingServiceAddress();
        namingServiceAddress.setFromProp(EnvConfig.get().getNamingHostName(), EnvConfig.get().getNamingPort());

        //
        Set<String> serviceTypeSet = new HashSet<String>();
        config = HotdeployConfigFactory.get().getConfig(ResourceDomainHotdeployConfig.class);

        for (int i = 1; i <= config.getTotalNum(); i++) {
            serviceTypeSet.add(config.getDomainPrefixServiceType() + StringUtil.appendZore(i, config.getNumLength()));
        }

        //
        namingServiceMulti = new NamingServiceMulti(namingServiceAddress, serviceTypeSet);

        //
        reload();

        //
        refresher = new Refresher(1000 * 60);

        chooseServerStragy = new ChooseServerStragy() {
            @Override
            public String choose(int dirNumber) {

                //得到down掉的服务器
                Set<Integer> downResourceDomainNumSet = new HashSet<Integer>();
                for (int i = 1; i <= config.getTotalNum(); i++) {
                    if (!namingServiceMulti.isLive(config.getDomainPrefixServiceType() + StringUtil.appendZore(i, config.getNumLength()))) {
                        downResourceDomainNumSet.add(i);
                    }
                }

                //todo 如果该服务器down，向下查找一个目录结构 有死循环的危险
                if (downResourceDomainNumSet.contains(dirNumber)) {
                    int i = 0;
                    while (i < config.getCopyNum()) {
                        if (dirNumber < config.getTotalNum()) {
                            dirNumber = dirNumber + 1;
                        } else {
                            dirNumber = 1;
                        }

                        //

                        if (!downResourceDomainNumSet.contains(dirNumber)) {
                            break;
                        }
                    }
                }
                return config.getDomainPrefixResource() + StringUtil.appendZore(dirNumber, config.getNumLength());
            }
        };
    }

    private ResourceServerMonitor() {
        init();
    }

    public synchronized void reload() {
        //reload the hotdeploy
        for (int i = 1; i <= config.getTotalNum(); i++) {
            namingServiceMulti.addServiceType(config.getDomainPrefixServiceType() + StringUtil.appendZore(i, config.getNumLength()));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("ResourceServerMonitor reload from the resource domain configure file.");
        }
    }


    public String getRandomUploadDomainPrefix() {
        int tryTimes = 0;

        //get a random one.
        int num = config.getResourceDomainRandomUploadPrefixNum();

        //check the live status
        while (config.getUploadNum() > 0 && tryTimes < MAX_TRY_TIMES && !namingServiceMulti.isLive(config.getDomainPrefixServiceType() + StringUtil.appendZore(num, config.getNumLength()))) {
            //if the uplaod is dead.
            num++;
            if (num > config.getTotalNum()) {
                num = 1;
            }

            if (!config.isExcludeUpload(num)) {
                tryTimes++;
            }
        }

        return config.getDomainPrefixUpload() + StringUtil.appendZore(num, config.getNumLength());
    }

    public Map<String, BackupResourceDomain> getDownResourceDomainMap() {
        //define the returnValue.
        Map<String, BackupResourceDomain> returnValue = new HashMap<String, BackupResourceDomain>();

        if (refresher.shouldRefresh()) {
            reload();
        }

        //get the down server number set.
        Set<Integer> downResourceDomainNumSet = new HashSet<Integer>();
        for (int i = 1; i <= config.getTotalNum(); i++) {
            if (!namingServiceMulti.isLive(config.getDomainPrefixServiceType() + StringUtil.appendZore(i, config.getNumLength()))) {
                downResourceDomainNumSet.add(i);
            }
        }

        //get the backup server for each down domains
        for (Integer num : downResourceDomainNumSet) {
            String domainPrefixSource = config.getDomainPrefixResource() + StringUtil.appendZore(num, config.getNumLength());

            int backResourceNum = num;
            int tempNum;
            for (int i = 1; i <= config.getCopyNum(); i++) {
                //
                tempNum = num + i;
                if (tempNum > config.getTotalNum()) {
                    tempNum = 1;
                }

                //if the backup domain num is down also, get next.
                //if is live, select this domain.
                if (!downResourceDomainNumSet.contains(tempNum)) {
                    backResourceNum = tempNum;
                    break;
                }
            }

            returnValue.put(domainPrefixSource, new BackupResourceDomain(domainPrefixSource, config.getDomainPrefixResource() + StringUtil.appendZore(backResourceNum, config.getNumLength())));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("The resource server monitor getDownResourceDomainMap result is " + returnValue.size());
        }

        //
        return returnValue;
    }

    public String getResourceDomainByImageDir(String imageDir) {
        if(StringUtil.isEmpty(imageDir)){
            return imageDir;
        }

        //得到当前的目录代表的res server的id
        Integer dirNumber = null;
        try {
            dirNumber = Integer.parseInt(imageDir.substring(1));
        } catch (NumberFormatException e) {
        }
        if(dirNumber==null){
            return imageDir;
        }
        //
        return chooseServerStragy.choose(dirNumber);
    }

    /**
     * get the thumbnail resource
     *
     * @param imgPath
     * @return
     */
    public String getThumbnailResourceDomain(String imgPath) {
        // /r001/image/2011/12/47/A3312FD0D6C5A683252EE3692D38BE46_SS.jpg
        return config.getDomainPrefixUpload() + imgPath.substring(2, 5);
    }

    interface ChooseServerStragy {
        public String choose(int dirNumber);
    }

}
