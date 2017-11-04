/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.search;

import com.enjoyf.platform.service.point.PointConstants;
import com.enjoyf.platform.service.search.SearchConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class SearchConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    private boolean taskOpen = true;
    private int taskIndexStep = 5 * 60 * 1000;
    private int indexContentSize = 500;

    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    private String searchContentPropsUno = "search-content-uno-001";
    private String searchContentPropsKey = "search-content-key-001";

    private String searchBlogPropsUno = "search-blog-uno-001";
    private String searchBlogPropsKey = "search-blog-key-001";

    private String searchIndexDir = "/opt/servicedata/index";
    private String searchServerName = "searchservice01";


    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    //queue
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    // configure file keys for
    private static final String KEY_TASK_OPEN = "task.open";
    private static final String KEY_TASK_INDEX_STEP = "task.index.step";
    private static final String KEY_SEARCH_SERVER_NAME = "searchserver.NAME";
    private static final String KEY_SEARCH_CONTENT_PROPS_UNO = "search.content.props.uno";
    private static final String KEY_SEARCH_CONTENT_PROPS_KEY = "search.content.props.key";
    private static final String KEY_SEARCH_BLOG_PROPS_UNO = "search.blog.props.uno";
    private static final String KEY_SEARCH_BLOG_PROPS_KEY = "search.blog.props.key";
    private static final String KEY_SEARCH_INDEX_DIR = "search.index.dir";

    private static final String KEY_TASK_INDEX_CONTENT_SIZE = "task.index.content.size";
    //solr
    private String solrServerUri;
    private String solrAnalysisUri;
    private static final String KEY_SOLR_SERVER_URI = "solr.server.uri";
    private static final String KEY_SOLR_ANALYSIS_URI = "solr.analysis.uri";
    //redis
    private String redisServerHost;
    private int redisServerPort;
    private static final String KEY_REDIS_SERVER_HOST = "redis.server.host";
    private static final String KEY_REDIS_SERVER_PORT = "redis.server.port";
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public SearchConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("MessageConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(SearchConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        //others
        taskOpen = props.getBoolean(KEY_TASK_OPEN, taskOpen);
        taskIndexStep = props.getInt(KEY_TASK_INDEX_STEP, taskIndexStep);
        indexContentSize = props.getInt(KEY_TASK_INDEX_CONTENT_SIZE, indexContentSize);
        searchContentPropsUno = props.get(KEY_SEARCH_CONTENT_PROPS_UNO, searchContentPropsUno);
        searchContentPropsKey = props.get(KEY_SEARCH_CONTENT_PROPS_KEY, searchContentPropsKey);

        searchBlogPropsUno = props.get(KEY_SEARCH_BLOG_PROPS_UNO, searchContentPropsUno);
        searchBlogPropsKey = props.get(KEY_SEARCH_BLOG_PROPS_KEY, searchContentPropsKey);

        searchIndexDir = props.get(KEY_SEARCH_INDEX_DIR, searchIndexDir);
        searchServerName = props.get(KEY_SEARCH_SERVER_NAME, searchServerName);
        //
        solrServerUri = props.get(KEY_SOLR_SERVER_URI);
        solrAnalysisUri = props.get(KEY_SOLR_ANALYSIS_URI);

        redisServerHost = props.get(KEY_REDIS_SERVER_HOST);
        redisServerPort = props.getInt(KEY_REDIS_SERVER_PORT);
    }

    public FiveProps getProps() {
        return props;
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }


    public boolean getTaskOPen() {
        return taskOpen;
    }

    public int getTaskIndexStep() {
        return taskIndexStep;
    }

    public String getSearchContentPropsUno() {
        return searchContentPropsUno;
    }

    public String getSearchContentPropsKey() {
        return searchContentPropsKey;
    }

    public String getSearchBlogPropsUno() {
        return searchBlogPropsUno;
    }

    public String getSearchBlogPropsKey() {
        return searchBlogPropsKey;
    }


    public int getIndexContentSize() {
        return indexContentSize;
    }

    public String getSearchServerName() {
        return searchServerName;
    }

    public String getContentIndexDIr() {
        return searchIndexDir + "/" + searchServerName + "/content/";
    }

    public String getProfileIndexDIr() {
        return searchIndexDir + "/" + searchServerName + "/profile/";
    }

    public String getGameIndexDir() {
        return searchIndexDir + "/" + searchServerName + "/game/";
    }

    public String getGroupIndexDir() {
        return searchIndexDir + "/" + searchServerName + "/group/";
    }

    public String getGameDbIndexDir() {
        return searchIndexDir + "/" + searchServerName + "/gamedb/";
    }

    public String getGiftIndexDir() {
        return searchIndexDir + "/" + searchServerName + "/activity/";
    }

    int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    void setEventQueueThreadNum(int eventQueueThreadNum) {
        this.eventQueueThreadNum = eventQueueThreadNum;
    }

    String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    void setQueueDiskStorePath(String queueDiskStorePath) {
        this.queueDiskStorePath = queueDiskStorePath;
    }

    public String getSolrServerUri() {
        return solrServerUri;
    }

    public String getSolrAnalysisUri() {
        return solrAnalysisUri;
    }

    String getRedisServerHost() {
        return redisServerHost;
    }

    int getRedisServerPort() {
        return redisServerPort;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }



}
