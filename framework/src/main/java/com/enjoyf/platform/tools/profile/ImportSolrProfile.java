package com.enjoyf.platform.tools.profile;

import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-26
 * Time: 下午2:03
 * To change this template use File | Settings | File Templates.
 */
public class ImportSolrProfile {

    public static void main(String[] args) {
        HttpSolrServer solrServer = new HttpSolrServer("http://172.16.75.30:38000/solr/users");

        try {
            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            Pagination page = new Pagination(200, 1, 200);
            do {
                PageRows<SocialProfile> pageRows = ProfileServiceSngl.get().queryNewestSocialProfile(new QueryExpress(), page);
                page = pageRows.getPage();
                for (SocialProfile profile : pageRows.getRows()) {
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("uno", profile.getUno());
                    doc.addField("appkey", "0VsYSLLsN8CrbBSMUOlLNx");
//                    doc.addField("age", 26);
                    doc.addField("birthday", profile.getDetail().getBirthday());
                    doc.addField("searchtext", profile.getBlog().getScreenName() + " " + profile.getBlog().getDescription());
                    doc.addField("sex", profile.getDetail().getSex());
                    docs.add(doc);
                }

                solrServer.add(docs);
                //对索引进行优化
                solrServer.optimize();
                //提交
                solrServer.commit();
            } while (!page.isLastPage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
