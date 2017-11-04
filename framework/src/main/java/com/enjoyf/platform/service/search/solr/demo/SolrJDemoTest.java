package com.enjoyf.platform.service.search.solr.demo;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.FieldAnalysisRequest;
import org.apache.solr.client.solrj.response.AnalysisResponseBase;
import org.apache.solr.client.solrj.response.FieldAnalysisResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-23
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
public class SolrJDemoTest {

    public static void main(String[] args) {
        try {
            //链接服务器tomcat solr
            HttpSolrServer solrServer = new HttpSolrServer("http://172.16.75.30:38000/solr/pages");
            //基本设置
            solrServer.setConnectionTimeout(100);
            solrServer.setDefaultMaxConnectionsPerHost(100);
            solrServer.setMaxTotalConnections(100);

            //添加索引
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", "10");
            doc.addField("iktext", "古老的东方有一条龙");
            doc.addField("smarttext", "古老的东方有一条龙");
            docs.add(doc);

            SolrInputDocument doc2 = new SolrInputDocument();
            doc2.addField("id", "20");
            doc2.addField("iktext", "东方不败是中国的武侠小说中的角色");
            doc2.addField("smarttext", "东方不败是中国的武侠小说中的角色");
            docs.add(doc2);

            SolrInputDocument doc3 = new SolrInputDocument();
            doc3.addField("id", "30");
            doc3.addField("iktext", "东方生气的太阳阿萨德积分卡拉斯代表");
            doc3.addField("smarttext", "东方生气的太阳阿萨德积分卡拉斯代表");
            docs.add(doc3);

            SolrInputDocument doc4 = new SolrInputDocument();
            doc4.addField("id", "40");
            doc4.addField("iktext", "啊在德国看电视的方式和中国不同");
            doc4.addField("smarttext", "啊在德国看电视的方式和中国不同");
            docs.add(doc4);

            solrServer.add(docs);
            solrServer.deleteById("10");
            //对索引进行优化
            solrServer.optimize();
            //提交
            solrServer.commit();

            //查询前分词预处理
            FieldAnalysisRequest request = new FieldAnalysisRequest("/analysis/field");
            request.addFieldType("textIKAnalyze");
            request.addFieldName("iktext");
            request.setFieldValue("text");
            request.setQuery("中国东方");
            FieldAnalysisResponse response = request.process(solrServer);
            Iterator iterator = response.getFieldNameAnalysis("iktext").getQueryPhases().iterator();

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
            System.out.println(queryStr);
            //查询
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("iktext:" + queryStr);
            //设置起始位置与返回结果数
            solrQuery.setStart(0);
            solrQuery.setRows(5);

            //设置排序
//            solrQuery.addSort("age", SolrQuery.ORDER.desc);

            //设置高亮
//            if (null != hightlight) {
//                query.setHighlight(true); // 开启高亮组件
//                query.addHighlightField("title");// 高亮字段
//                query.setHighlightSimplePre("<font color="red">");// 标记
//                query.setHighlightSimplePost("</font>");
//                query.setHighlightSnippets(1);//结果分片数，默认为1
//                query.setHighlightFragsize(1000);//每个分片的最大长度，默认为100
//            }
            QueryResponse queryResponse = solrServer.query(solrQuery);   //queryResponse.getResults().get(0).getFieldValue("uno")

//            Iterator responseIterator = queryResponse.getResults().iterator();
//            List<ProfileSolrjBean> list = queryResponse.getBeans(ProfileSolrjBean.class);

            System.out.println(queryResponse.getResults());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
