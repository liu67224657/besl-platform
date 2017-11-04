package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-26
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public class SolrServer extends AbstractSolrServer {
    private static final Logger logger = LoggerFactory.getLogger(SolrServer.class);

    protected String serverUri;
    protected String analysisUri;

    public SolrServer(String serverUri, String analysisUri) {
        this.serverUri = serverUri;
        this.analysisUri = analysisUri;
    }

    public void add(SolrCore solrCore, Collection<SolrInputDocument> docs) {
        try {
            super.add(solrCore, serverUri, docs);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }

    public void add(SolrCore solrCore, SolrInputDocument doc) {
        try {
            super.add(solrCore, serverUri, doc);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }
}
