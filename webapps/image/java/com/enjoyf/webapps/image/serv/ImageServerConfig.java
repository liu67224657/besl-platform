/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.image.serv;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-11 下午7:23
 * Description:
 */
public class ImageServerConfig {
    //
    private static ImageServerConfig instance;

    //
    private FiveProps props;

    //
    private String serviceType = "image01";
    private String serviceName = "image01_01";
    private int servicePort = 8091;

    private String uploadResouceName = "r001";

    private int queueThreadNum = 8;

    //
    private static final String PREFIX_KEY_SERVICE = "imageserver";

    private static final String SUFFIX_KEY_SERVICE_TYPE = ".TYPE";
    private static final String SUFFIX_KEY_SERVICE_NAME = ".NAME";
    private static final String SUFFIX_KEY_SERVICE_PORT = ".PORT";

    private static final String KEY_UPLOAD_RESOURCE_NAME = "upload.resource.name";

    private ImageServerConfig() {
        props = Props.instance().getServProps();

        init();
    }

    //
    public static ImageServerConfig get() {
        if (instance == null) {
            instance = new ImageServerConfig();
        }

        return instance;
    }

    private void init() {
        serviceType = props.get(PREFIX_KEY_SERVICE + SUFFIX_KEY_SERVICE_TYPE, serviceType);
        serviceName = props.get(PREFIX_KEY_SERVICE + SUFFIX_KEY_SERVICE_NAME, serviceName);
        servicePort = props.getInt(PREFIX_KEY_SERVICE + SUFFIX_KEY_SERVICE_PORT, servicePort);

        uploadResouceName = props.get(KEY_UPLOAD_RESOURCE_NAME, uploadResouceName);
    }

    public FiveProps getProps() {
        return props;
    }

    public String getServicePrefix() {
        return PREFIX_KEY_SERVICE;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUploadResouceName() {
        return uploadResouceName;
    }

    public int getQueueThreadNum() {
        return queueThreadNum;
    }

    public String getQiNiuUploadResourceName() {
        return "qiniu";
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
