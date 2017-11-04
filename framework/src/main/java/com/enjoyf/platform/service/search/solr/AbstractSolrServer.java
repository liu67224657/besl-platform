package com.enjoyf.platform.service.search.solr;

import com.enjoyf.platform.util.Pagination;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-26
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSolrServer {

    protected QueryResponse solrRequest(SolrField solrField, SolrOrder solrOrder, Pagination pagination, String analysisUri, HttpSolrServer solrServer) throws IOException, SolrServerException {
        //查询前分词预处理
        String queryStr = analysis(solrField, analysisUri, solrServer);
        //查询
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(solrField.getFieldName() + ":" + queryStr);
        //设置起始位置与返回结果数
        if (pagination != null) {
            solrQuery.setStart(pagination.getStartRowIdx());
            solrQuery.setRows(pagination.getPageSize());
        }
        //设置排序
        if (solrOrder != null) {
            solrQuery.addSort(solrOrder.getFieldName(), solrOrder.getOrder());
        }

        QueryResponse queryResponse = solrServer.query(solrQuery);
        return queryResponse;
    }

    protected String analysis(SolrField solrField, String analysisUri, HttpSolrServer solrServer) throws IOException, SolrServerException {
        FieldAnalysisRequest request = new FieldAnalysisRequest(analysisUri);
        request.addFieldType(solrField.getFieldType());
        request.addFieldName(solrField.getFieldName());
        request.setFieldValue(solrField.getFieldValue());
        request.setQuery(solrField.getQuery());
        FieldAnalysisResponse response = request.process(solrServer);
        Iterator iterator = response.getFieldNameAnalysis(solrField.getFieldName()).getQueryPhases().iterator();

        String queryStr = "";
        while (iterator.hasNext()) {
            AnalysisResponseBase.AnalysisPhase phase = (AnalysisResponseBase.AnalysisPhase) iterator.next();
            if (phase.getClassName().equals("org.wltea.analyzer.lucene.IKTokenizer")) {
                List<AnalysisResponseBase.TokenInfo> list = phase.getTokens();
                for (AnalysisResponseBase.TokenInfo info : list) {
                    queryStr += info.getText() + " ";
                }
            }
        }
        return queryStr;
    }

    protected void add(SolrCore solrCore, String serverUri, SolrInputDocument doc) throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer(serverUri + "/" + solrCore.getCore());
        solrServer.add(doc);
        //对索引进行优化
        solrServer.optimize();
        //提交
        solrServer.commit();
    }

    protected void add(SolrCore solrCore, String serverUri, Collection<SolrInputDocument> docs) throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer(serverUri + "/" + solrCore.getCore());
        solrServer.add(docs);
        //对索引进行优化
        solrServer.optimize();
        //提交
        solrServer.commit();
    }
}
