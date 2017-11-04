package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-26
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSolrServer extends SolrServer {
    private static final Logger logger = LoggerFactory.getLogger(ProfileSolrServer.class);

    public ProfileSolrServer(String serverUri, String analysisUri) {
        super(serverUri, analysisUri);
    }

    public PageRows<ProfileSolrjBean> query(SolrCore solrCore, SolrField solrField, SolrOrder solrOrder, Pagination pagination) {
        try {
            HttpSolrServer solrServer = new HttpSolrServer(serverUri + "/" + solrCore.getCore());

            QueryResponse response = solrRequest(solrField, solrOrder, pagination, analysisUri, solrServer);

            List<ProfileSolrjBean> beanList = response.getBeans(ProfileSolrjBean.class);
            pagination = new Pagination((int) response.getResults().getNumFound(), pagination.getCurPage(), pagination.getPageSize());
            PageRows<ProfileSolrjBean> pageRows = new PageRows<ProfileSolrjBean>();
            pageRows.setRows(beanList);
            pageRows.setPage(pagination);
            return pageRows;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception", e);
        }
        return null;
    }

}
