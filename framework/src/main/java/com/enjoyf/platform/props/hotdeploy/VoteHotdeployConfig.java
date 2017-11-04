package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.vote.VoteDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class VoteHotdeployConfig extends HotdeployConfig {
    private static final Logger logger = LoggerFactory.getLogger(VoteHotdeployConfig.class);
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String KEY_VOTE_SUPPORT_DOMAIN = "vote.support.domain";

    private ReadCache readCache;

    public VoteHotdeployConfig() {
        super(EnvConfig.get().getVoteHotDeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        List<VoteDomain> voteDomainList = new ArrayList<VoteDomain>();

        List<String> domainCodes = getList(KEY_VOTE_SUPPORT_DOMAIN);
        for (String domainCode : domainCodes) {
            VoteDomain domain = VoteDomain.getByCode(domainCode);
            if (domain == null) {
                continue;
            }

        }

        //
        ReadCache tmpCache = new ReadCache();
        tmpCache.setVoteDomainList(voteDomainList);

        //
        this.readCache = tmpCache;

        logger.info("Event Props init finished.");
    }

    private class ReadCache {
        private List<VoteDomain> voteDomainList = new ArrayList<VoteDomain>();

        public List<VoteDomain> getVoteDomainList() {
            return voteDomainList;
        }

        public void setVoteDomainList(List<VoteDomain> voteDomainList) {
            this.voteDomainList = voteDomainList;
        }

    }

    public List<VoteDomain> getVoteDomainList() {
        return readCache.getVoteDomainList();
    }
}
