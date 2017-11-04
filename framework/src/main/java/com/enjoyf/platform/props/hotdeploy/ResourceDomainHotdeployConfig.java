package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * <a href="mailto:yinpengyi@enjoyf.com">Yin Pengyi</a>
 */
public class ResourceDomainHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(ResourceDomainHotdeployConfig.class);

    /////////////////////////////////////////////////////////////////////////////
    //the keys
    private static final String KEY_RESDOMAIN_PREFIX_UPLOAD = "resource.domain.prefix.upload";
    private static final String KEY_RESDOMAIN_PREFIX_SERVICETYPE = "resource.domain.prefix.servicetype";
    private static final String KEY_RESDOMAIN_PREFIX_RESOURCE = "resource.domain.prefix.resource";

    private static final String KEY_RESDOMAIN_TOTAL_NUM = "resource.domain.total.num";
    private static final String KEY_RESDOMAIN_COPY_NUM = "resource.domain.copy.num";
    private static final String KEY_RESDOMAIN_NUM_LENGTH = "resource.domain.num.length";

    private static final String KEY_RESDOMAIN_NUM_UPLOAD_EXCLUDE_LIST = "resource.domain.num.upload.exclude.list";

    //
    private static final String SUFFIX_KEY_ODD = ".odd";

    //
    private Cached cached;

    //
    private Random random = new Random();

    public ResourceDomainHotdeployConfig() {
        super(EnvConfig.get().getResdomainHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    public synchronized void reload() {
        super.reload();

        //
        Cached tmpCache = new Cached();

        tmpCache.setTotalNum(getInt(KEY_RESDOMAIN_TOTAL_NUM, tmpCache.getTotalNum()));
        tmpCache.setCopyNum(getInt(KEY_RESDOMAIN_COPY_NUM, tmpCache.getCopyNum()));
        tmpCache.setNumLength(getInt(KEY_RESDOMAIN_NUM_LENGTH, tmpCache.getNumLength()));

        tmpCache.setDomainPrefixResource(getString(KEY_RESDOMAIN_PREFIX_RESOURCE, tmpCache.getDomainPrefixResource()));
        tmpCache.setDomainPrefixUpload(getString(KEY_RESDOMAIN_PREFIX_UPLOAD, tmpCache.getDomainPrefixUpload()));
        tmpCache.setDomainPrefixServiceType(getString(KEY_RESDOMAIN_PREFIX_SERVICETYPE, tmpCache.getDomainPrefixServiceType()));

        //
        for (int i = 1; i <= tmpCache.getTotalNum(); i++) {
            tmpCache.getUploadDomainOddMap().put(i, getInt(i + SUFFIX_KEY_ODD, 1));
        }

        //
        List<String> uploadExcludeNums = getList(KEY_RESDOMAIN_NUM_UPLOAD_EXCLUDE_LIST);
        for (String num : uploadExcludeNums) {
            tmpCache.getExcludeUploadDomainNumSet().add(Integer.valueOf(num));
        }

        //
        int maxOdd = 0;
        for (Map.Entry<Integer, Integer> entry : tmpCache.getUploadDomainOddMap().entrySet()) {
            if (!tmpCache.getExcludeUploadDomainNumSet().contains(entry.getKey())) {
                maxOdd += entry.getValue();
            }
        }
        tmpCache.setUploadMaxOdd(maxOdd);

        //
        this.cached = tmpCache;

        logger.info("ResourceDomain Props init finished, the props is " + getProps());
    }

    public int getResourceDomainRandomUploadPrefixNum() {
        int seed = random.nextInt(cached.getUploadMaxOdd()) + 1;

        int temp = 0;

        int returnValue = 1;
        for (Map.Entry<Integer, Integer> entry : cached.getUploadDomainOddMap().entrySet()) {
            if (cached.getExcludeUploadDomainNumSet().contains(entry.getKey())) {
                continue;
            }

            //
            if (seed > temp && seed <= (temp + entry.getValue())) {
                returnValue = entry.getKey();
                break;
            }

            temp += entry.getValue();
        }

        return returnValue;
    }

    public int getTotalNum() {
        return cached.getTotalNum();
    }

    public int getCopyNum() {
        return cached.getCopyNum();
    }

    public int getNumLength() {
        return cached.getNumLength();
    }

    public int getUploadMaxOdd() {
        return cached.getUploadMaxOdd();
    }

    public int getUploadNum() {
        return cached.getTotalNum() - cached.getExcludeUploadDomainNumSet().size();
    }

    public String getDomainPrefixUpload() {
        return cached.getDomainPrefixUpload();
    }

    public String getDomainPrefixResource() {
        return cached.getDomainPrefixResource();
    }

    public String getDomainPrefixServiceType() {
        return cached.getDomainPrefixServiceType();
    }

    public boolean isExcludeUpload(int n) {
        return cached.getExcludeUploadDomainNumSet().contains(n);
    }

    //
    private class Cached {
        //
        private String domainPrefixUpload = "up";
        private String domainPrefixResource = "r";
        private String domainPrefixServiceType = "image";

        //
        private int totalNum = 1;
        private int copyNum = 1;
        private int numLength = 3;

        //
        private int uploadMaxOdd = 1;

        //
        private Set<Integer> excludeUploadDomainNumSet = new HashSet<Integer>();
        private Map<Integer, Integer> uploadDomainOddMap = new HashMap<Integer, Integer>();

        public String getDomainPrefixUpload() {
            return domainPrefixUpload;
        }

        public void setDomainPrefixUpload(String domainPrefixUpload) {
            this.domainPrefixUpload = domainPrefixUpload;
        }

        public String getDomainPrefixResource() {
            return domainPrefixResource;
        }

        public void setDomainPrefixResource(String domainPrefixResource) {
            this.domainPrefixResource = domainPrefixResource;
        }

        public String getDomainPrefixServiceType() {
            return domainPrefixServiceType;
        }

        public void setDomainPrefixServiceType(String domainPrefixServiceType) {
            this.domainPrefixServiceType = domainPrefixServiceType;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getCopyNum() {
            return copyNum;
        }

        public void setCopyNum(int copyNum) {
            this.copyNum = copyNum;
        }

        public int getNumLength() {
            return numLength;
        }

        public void setNumLength(int numLength) {
            this.numLength = numLength;
        }

        public int getUploadMaxOdd() {
            return uploadMaxOdd;
        }

        public void setUploadMaxOdd(int uploadMaxOdd) {
            this.uploadMaxOdd = uploadMaxOdd;
        }

        public Set<Integer> getExcludeUploadDomainNumSet() {
            return excludeUploadDomainNumSet;
        }

        public Map<Integer, Integer> getUploadDomainOddMap() {
            return uploadDomainOddMap;
        }

        public String toString() {
            return ReflectUtil.fieldsToString(this);
        }
    }
}
