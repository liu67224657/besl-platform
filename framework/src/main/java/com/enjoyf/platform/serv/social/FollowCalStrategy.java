package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.*;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-8-30
 * Description:
 */
public class FollowCalStrategy implements RecommendCalStrategy {

    private SocialLogic socialLogic;
    private SocialConfig socialConfig;

    public FollowCalStrategy(SocialLogic socialLogic, SocialConfig socialConfig) {
        this.socialLogic = socialLogic;
        this.socialConfig = socialConfig;
    }

    @Override
    public SocialRecommend calculate(String srcUno) {
        QueryExpress updateQueryExpress = new QueryExpress().add(QueryCriterions.eq(SocialRecommendField.SRCUNO, srcUno))
                .add(QueryCriterions.eq(SocialRecommendField.RECOMMENDTYPE, RecommendType.FOLLOW_EACHOTHER.getCode()));
        Date now = new Date();

        SocialRecommend socialRecommend = null;
        try {

            socialRecommend = socialLogic.getSocialRecommendFromDB(updateQueryExpress);

            if (socialRecommend != null) {
                socialLogic.updateSocialRecommend(new UpdateExpress().set(SocialRecommendField.CALSTATUS, ActStatus.ACTING.getCode())
                        , updateQueryExpress);
            }

            //查找该用户关注和互相关注的用户
            Set<String> srcFollowSet = new HashSet<String>();
            Set<String> srcEachOtherUnoSet = new HashSet<String>();
            QueryExpress followExpress = new QueryExpress()
                    .add(QueryCriterions.eq(SocialRelationField.SRCUNO, srcUno))
                    .add(QueryCriterions.eq(SocialRelationField.SRCSTATUS, ActStatus.ACTED.getCode()));
            List<SocialRelation> srcFollowRelationList = socialLogic.querySocialRelation(srcUno, RelationType.FOCUS, followExpress);
            for (SocialRelation socialRelation : srcFollowRelationList) {
                srcFollowSet.add(socialRelation.getDestUno());
                if (socialRelation.getDestStatus().equals(ActStatus.ACTED)) {
                    srcEachOtherUnoSet.add(socialRelation.getDestUno());
                }
            }

            Map<String, RecommendBlack> recommencBlackMap = new HashMap<String, RecommendBlack>();
            if (socialRecommend != null && socialRecommend.getBlackSet() != null) {
                for (RecommendBlack recommendBlack : socialRecommend.getBlackSet().getRecommendBlaskSet()) {
                    recommencBlackMap.put(recommendBlack.getUno(), recommendBlack);
                }
            }

            Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
            for (String destUno : srcEachOtherUnoSet) {
                //查找相互关注的用户的相互关注的用户
                List<SocialRelation> destEachOtherRelationList = socialLogic.querySocialRelation(destUno, RelationType.FOCUS, new QueryExpress()
                        .add(QueryCriterions.eq(SocialRelationField.SRCUNO, destUno))
                        .add(QueryCriterions.eq(SocialRelationField.SRCSTATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.eq(SocialRelationField.DESTSTATUS, ActStatus.ACTED.getCode())));

                for (SocialRelation socialRelation : destEachOtherRelationList) {
                    if (srcFollowSet.contains(socialRelation.getDestUno())
                            || srcUno.equals(socialRelation.getDestUno())) {
                        //该共同好友已经被关注过
                        continue;
                    }

                    //在不感兴趣的名单中
                    RecommendBlack black = recommencBlackMap.get(socialRelation.getDestUno());
                    if (black != null) {
                        if (black.getDate() == null) {
                            continue;
                        }

                        Calendar balckDateCal = Calendar.getInstance();
                        balckDateCal.setTime(black.getDate());
                        balckDateCal.add(Calendar.SECOND, socialConfig.getCalRecommendBalckListInterval());

                        if (balckDateCal.getTime().after(now)) {
                            continue;
                        } else {
                            recommencBlackMap.remove(socialRelation.getDestUno());
                        }
                    }

                    //共同好友
                    if (!resultMap.containsKey(socialRelation.getDestUno())) {
                        resultMap.put(socialRelation.getDestUno(), new ArrayList<String>());
                    }

                    resultMap.get(socialRelation.getDestUno()).add(socialRelation.getSrcUno());
                }
            }

            //共同好友>2
            List<Map.Entry<String, List<String>>> resultList = new ArrayList<Map.Entry<String, List<String>>>();
            for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    resultList.add(entry);
                }
            }

            //排序从大到小
            Collections.sort(resultList, new Comparator<Map.Entry<String, List<String>>>() {
                @Override
                public int compare(Map.Entry<String, List<String>> o1, Map.Entry<String, List<String>> o2) {
                    return o1.getValue().size() > o2.getValue().size() ? -1 : 1;
                }
            });

            Map<String, RecommendDetail> recommendDetailList = new LinkedHashMap<String, RecommendDetail>();
            for (Map.Entry<String, List<String>> entry : resultList) {
                RecommendDetail recommendDetail = new RecommendDetail();
                recommendDetail.setUno(entry.getKey());
                recommendDetail.setDestUnos(entry.getValue());
                recommendDetail.setRecommendReason(RecommendReason.FOLLOW);
                recommendDetailList.put(entry.getKey(), recommendDetail);
            }


            if (socialRecommend != null) {
                socialRecommend.setCalStatus(ActStatus.ACTED);
                socialRecommend.setCalDate(now);
                socialRecommend.setRecommendDetailSet(new RecommendDetailSet(new ArrayList<RecommendDetail>(recommendDetailList.values())));
                UpdateExpress updateExpress = new UpdateExpress().set(SocialRecommendField.DETAIL, socialRecommend.getRecommendDetailSet().toJsonStr())
                        .set(SocialRecommendField.CALDATE, socialRecommend.getCalDate())
                        .set(SocialRecommendField.CALSTATUS, socialRecommend.getCalStatus().getCode());
                socialLogic.updateSocialRecommend(updateExpress, updateQueryExpress);
            } else {
                socialRecommend = new SocialRecommend();
                socialRecommend.setRecommendType(RecommendType.FOLLOW_EACHOTHER);
                socialRecommend.setSrcUno(srcUno);
                socialRecommend.setRecommendDetailSet(new RecommendDetailSet(new ArrayList<RecommendDetail>(recommendDetailList.values())));
                socialRecommend.setCalDate(new Date());
                socialRecommend.setCalStatus(ActStatus.ACTED);
                socialLogic.createSocialRecommend(socialRecommend);
            }

            //计算系统推荐用户 并更新缓存
            addSystemRecommendUno(socialRecommend, srcUno, srcFollowSet, recommendDetailList, recommencBlackMap, now);

            //修改黑名单
            UpdateExpress updateExpress = new UpdateExpress().set(SocialRecommendField.BLACKDETAIL, CollectionUtil.isEmpty(recommencBlackMap) ? null : new RecommendBlackSet().add(recommencBlackMap.values()).toJsonStr());
            socialLogic.updateSocialRecommend(updateExpress, new QueryExpress().add(QueryCriterions.eq(SocialRecommendField.SRCUNO, srcUno))
                    .add(QueryCriterions.eq(SocialRecommendField.RECOMMENDTYPE, RecommendType.FOLLOW_EACHOTHER.getCode())));

            //更新缓存
            return socialRecommend;
        } catch (ServiceException e) {
            GAlerter.lab(getClass().getName() + " occured ServiceException.", e);
        }

        return socialRecommend;
    }

    private void addSystemRecommendUno(SocialRecommend socialRecommend, String srcUno, Set<String> srcFollowSet, Map<String, RecommendDetail> recommendDetailMap, Map<String, RecommendBlack> recommencBlackMap, Date now) {
        List<String> sysRecommendList = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSocialRecommendUnoList();

        Set<String> queryUnos = new LinkedHashSet<String>();
        for (String sysCommendUno : sysRecommendList) {
            if (sysCommendUno.equals(srcUno) || srcFollowSet.contains(sysCommendUno) || recommendDetailMap.containsKey(sysCommendUno)) {
                continue;
            }

            //在不感兴趣的名单中
            //在不感兴趣的名单中
            RecommendBlack black = recommencBlackMap.get(sysCommendUno);
            if (black != null) {
                if (black.getDate() == null) {
                    continue;
                }

                Calendar balckDateCal = Calendar.getInstance();
                balckDateCal.setTime(black.getDate());
                balckDateCal.add(Calendar.SECOND, socialConfig.getCalRecommendBalckListInterval());

                if (balckDateCal.getTime().after(now)) {
                    continue;
                } else {
                    recommencBlackMap.remove(sysCommendUno);
                }
            }

            queryUnos.add(sysCommendUno);
        }

        try {
            Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(queryUnos);
            for (String uno : queryUnos) {
                Profile profile = profileMap.get(uno);
                if (profile == null) {
                    continue;
                }

                String verifyType = "";
                String desc = "";
                if (profile.getDetail() != null) {
                    verifyType = profile.getDetail().getVerifyType() == null ? "" : profile.getDetail().getVerifyType().getCode();
                    desc = StringUtil.isEmpty(profile.getDetail().getVerifyDesc()) ? profile.getBlog().getDescription() : profile.getDetail().getVerifyDesc();
                }

                RecommendDetail recommendDetail = new RecommendDetail();
                recommendDetail.setUno(uno);
                recommendDetail.setRecommendReason(RecommendReason.TALENT);
                recommendDetail.setDesc(desc);
                recommendDetail.setVerifyType(verifyType);
                socialRecommend.getRecommendDetailSet().add(recommendDetail);
            }
        } catch (ServiceException e) {
            GAlerter.lab(getClass().getName() + " calculate system user occured Exception.", e);
        }
    }
}
