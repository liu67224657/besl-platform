package com.enjoyf.webapps.tools.weblogic.customerservice;

import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.service.misc.IpForbiddenField;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterion;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-10
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "ipManagementWebLogic")
public class IpManagementWebLogic {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PageRows<IpForbidden> queryIpList(IpForbidden ipForbidden, Pagination pagination){
        if(logger.isDebugEnabled()){
            logger.debug("call method queryIpList, param ipForbidden : " + ipForbidden.toString());
        }

        QueryExpress queryExpress = new QueryExpress();
        if(ipForbidden.getStartIP() != 0){
            //处于IP段之间的（>=起始IP，<=结束IP）
            queryExpress.add(QueryCriterions.leq(IpForbiddenField.STARTIP, ipForbidden.getStartIP()))
                    .add(QueryCriterions.geq(IpForbiddenField.ENDIP, ipForbidden.getStartIP()));
        }
        if(ipForbidden.getCreateDate() != null){
            //
            queryExpress.add(QueryCriterions.geq(IpForbiddenField.CREATEDATE, ipForbidden.getCreateDate()))
                    .add(QueryCriterions.lt(IpForbiddenField.CREATEDATE, new Date(ipForbidden.getCreateDate().getTime() + 24 * 60 * 60 * 1000)));
        }
        if(ipForbidden.getStatus() != null){
            //
            queryExpress.add(QueryCriterions.eq(IpForbiddenField.VALIDSTATUS, ipForbidden.getStatus().getCode()));
        }

        PageRows<IpForbidden> pageRows = new PageRows<IpForbidden>();
        try {
            pageRows = MiscServiceSngl.get().queryIpForbiddens(queryExpress, pagination);
        } catch (ServiceException e) {
            GAlerter.lab("invoke method  queryIpList occurred an Exception :" + e);
        }

        return pageRows;
    }
}
