package com.enjoyf.platform.cloudfile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/8/17
 * Description:
 */
public class BucketInfo {
    public static final String QINIU = "qiniu";
    public static final String ALIYUN = "aliyun";

    private static Map<String, BucketInfo> map = new HashMap<String, BucketInfo>();

    public static BucketInfo BUCKET_INFO_JOYMEPIC_QN = new BucketInfo("joymepic", QINIU, "http://joymepic.joyme.com");
    public static BucketInfo BUCKET_INFO_JOYMEPIC_ALIYUN = new BucketInfo("aliypicpro", ALIYUN, "http://aliypic.joyme.com");

    private String bucket;
    private String thirdKey;
    private String host;

    private BucketInfo(String bucket, String thirdKey, String host) {
        this.bucket = bucket;
        this.thirdKey = thirdKey;
        this.host = host;
        map.put(this.bucket, this);
    }

    @Override
    public int hashCode() {
        return bucket.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BucketInfo that = (BucketInfo) o;

        if (bucket != null ? !bucket.equals(that.bucket) : that.bucket != null) return false;

        return true;
    }

    public static BucketInfo getByCode(String code) {
        return map.get(code);
    }

    public String getBucket() {
        return bucket;
    }

    public String getThirdKey() {
        return thirdKey;
    }

    public String getHost() {
        return host;
    }
}
