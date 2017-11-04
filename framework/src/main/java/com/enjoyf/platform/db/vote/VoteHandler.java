/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.vote.Vote;
import com.enjoyf.platform.service.vote.VoteDomain;
import com.enjoyf.platform.service.vote.VoteOption;
import com.enjoyf.platform.service.vote.VoteOptionField;
import com.enjoyf.platform.service.vote.VoteRecord;
import com.enjoyf.platform.service.vote.VoteRecordSet;
import com.enjoyf.platform.service.vote.VoteSubject;
import com.enjoyf.platform.service.vote.VoteSubjectField;
import com.enjoyf.platform.service.vote.VoteSubjectRecord;
import com.enjoyf.platform.service.vote.VoteUserRecord;
import com.enjoyf.platform.service.vote.VoteUserRecordField;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-21
 * Time: 上午10:03
 * To change this template use File | Settings | File Templates.
 */
public class VoteHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private VoteSubjectAccessor voteSubjectAccessor;
    private VoteOptionAccessor voteOptionAccessor;
    private VoteSubjectRecordAccessor voteSubjectRecordAccessor;
    private VoteUserRecordAccessor voteUserRecordAccessor;

    public VoteHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the data source
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        voteSubjectAccessor = VoteAccessorFactory.factoryVoteSubjectAccessor(dataBaseType);
        voteOptionAccessor = VoteAccessorFactory.factoryVoteOptionAccessor(dataBaseType);
        voteSubjectRecordAccessor = VoteAccessorFactory.factoryVoteSubjectRecordAccessor(dataBaseType);
        voteUserRecordAccessor = VoteAccessorFactory.factoryVoteUserRecordAccessor(dataBaseType);
    }

    /**
     * 发起投票
     *
     * @param vote
     * @return
     * @throws DbException
     */
    public Vote postVote(Vote vote) throws DbException {
        Vote returnValue = null;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            Set<VoteOption> voteOptionSet = voteOptionAccessor.insertBatch(vote.getVoteSubject().getSubjectId(), new LinkedHashSet<VoteOption>(vote.getVoteOptionMap().values()), conn);
            VoteSubject voteSubject = voteSubjectAccessor.insert(vote.getVoteSubject(), conn);

            //
            if (!CollectionUtil.isEmpty(voteOptionSet) && voteSubject != null) {
                Map<Long, VoteOption> voteOptionMap = new LinkedHashMap<Long, VoteOption>();
                for (VoteOption voteOption : voteOptionSet) {
                    voteOptionMap.put(voteOption.getOptionId(), voteOption);
                }

                vote.setVoteOptionMap(voteOptionMap);
                vote.setVoteSubject(voteSubject);
                returnValue = vote;
            }
            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(DbException.GENERIC, e.getMessage());
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    /**
     * 获取投票主体信息
     *
     * @param subjectId
     * @return
     * @throws DbException
     */
    public Vote getVote(String subjectId) throws DbException {
        Vote returnValue = null;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            VoteSubject voteSubject = voteSubjectAccessor.get(new QueryExpress().add(QueryCriterions.eq(VoteSubjectField.SUBJECTID, subjectId)), conn);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(VoteOptionField.SUBJECTID, subjectId));
            queryExpress.add(QuerySort.add(VoteOptionField.OPTIONID, QuerySortOrder.ASC));

            List<VoteOption> voteOptionList = voteOptionAccessor.queryOptions(subjectId, queryExpress, conn);

            returnValue = new Vote();
            returnValue.setVoteSubject(voteSubject);

            Map<Long, VoteOption> voteOptionMap = new LinkedHashMap<Long, VoteOption>();
            int voteNum = 0;
            for (VoteOption voteOption : voteOptionList) {
                voteNum = voteNum + voteOption.getOptionNum();
                voteOptionMap.put(voteOption.getOptionId(), voteOption);
            }
            returnValue.setVoteNum(voteNum);

            returnValue.setVoteOptionMap(voteOptionMap);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    /**
     * 参与投票
     *
     * @param subjectId
     * @param voteUno
     * @param voteIp
     * @param voteDomain
     * @param recordSet
     * @return
     * @throws DbException
     */
    public boolean partVote(String subjectId, String voteUno, String voteIp, VoteDomain voteDomain, VoteRecordSet recordSet) throws DbException {
        boolean returnValue = false;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            //记录投票详细(参与人)
            VoteUserRecord voteUserRecord = new VoteUserRecord();
            voteUserRecord.setVoteUno(voteUno);
            voteUserRecord.setVoteIp(voteIp);
            voteUserRecord.setVoteDate(new Date());
            voteUserRecord.setVoteDomain(voteDomain);
            voteUserRecord.setSubjectId(subjectId);
            voteUserRecord.setRecordSet(recordSet);

            voteUserRecordAccessor.insert(voteUserRecord, conn);

            //记录投票详细(主题)
            Set<VoteSubjectRecord> subjectRecordSet = new HashSet<VoteSubjectRecord>();
            for (VoteRecord voteRecord : recordSet.getRecords()) {
                VoteSubjectRecord subjectRecord = new VoteSubjectRecord();
                subjectRecord.setSubjectId(subjectId);
                subjectRecord.setVoteDate(new Date());
                subjectRecord.setVoteDomain(voteDomain);
                subjectRecord.setVoteIp(voteIp);
                subjectRecord.setVoteOption(voteRecord.getOptionId());
                subjectRecord.setVoteUno(voteUno);

                subjectRecordSet.add(subjectRecord);
            }

            voteSubjectRecordAccessor.insertBatch(subjectId, subjectRecordSet, conn);

            //更新投票总数
            boolean increaseSubjectNum = voteSubjectAccessor.increase(subjectId, VoteSubjectField.VOTENUM, 1, conn);

            //更新选项数
            boolean increaseOptionNum = voteOptionAccessor.increaseBatch(subjectId, recordSet, VoteOptionField.OPTIONNUM, 1, conn);

            if (increaseOptionNum && increaseSubjectNum) {
                conn.commit();
                returnValue = true;
            }

        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            throw new DbException(DbException.GENERIC, e.getMessage());
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    /**
     * 根据uno and subjectId 查询投票结果
     *
     * @param voteUno
     * @param subjectId
     * @return
     * @throws DbException
     */
    public VoteUserRecord getVoteUserRecord(String voteUno, String subjectId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(VoteUserRecordField.VOTEUNO, voteUno));
            queryExpress.add(QueryCriterions.eq(VoteUserRecordField.SUBJECTID, subjectId));

            return voteUserRecordAccessor.getVoteUserRecord(voteUno, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<VoteSubjectRecord> queryVoteSubjectRecords(String subjectId, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return voteSubjectRecordAccessor.queryByExpress(subjectId, queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public VoteUserRecord insertVoteUserRecord(VoteUserRecord voteUserRecord)throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return voteUserRecordAccessor.insert(voteUserRecord, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
