/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.service.search.solr.ProfileSolrjBean;
import com.enjoyf.platform.service.search.solr.SolrCore;
import com.enjoyf.platform.service.search.solr.SolrField;
import com.enjoyf.platform.service.search.solr.SolrOrder;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.aop.ThrowsAdvice;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>,zx
 * Create time: 11-8-16 下午10:52
 * Description:
 */
public interface SearchService extends EventReceiver {

    public PageRows<SearchGiftResultEntry> searchGiftByText(SearchGiftCriteria criteria, Pagination page) throws ServiceException;



}
