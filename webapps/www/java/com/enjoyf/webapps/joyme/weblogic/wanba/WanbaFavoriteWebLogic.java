package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaFavoriteDTO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhimingli on 2016/10/10 0010.
 */
@Service(value = "wanbaFavoriteWebLogic")
public class WanbaFavoriteWebLogic extends AbstractWanbaWebLogic {

    public PageRows<WanbaFavoriteDTO> queryFavoriteAskUserActionList(String pid, Pagination page) {
        PageRows<WanbaFavoriteDTO> returnObj = new PageRows<WanbaFavoriteDTO>();
        try {
            PageRows<AskUserAction> pageRows = AskServiceSngl.get().queryAskUserAction(pid, ItemType.ANSWER, AskUserActionType.FAVORITE, page);

            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                returnObj.setPage(page);
                page.setTotalRows(0);
                return returnObj;
            }


            Set<Long> questionIdSet = new HashSet<Long>();
            Set<Long> answerIdSet = new HashSet<Long>();
            for (AskUserAction askUserAction : pageRows.getRows()) {
                //
                answerIdSet.add(askUserAction.getDestId());
                //
                questionIdSet.add(Long.valueOf(askUserAction.getValue()));
            }

            Map<Long, Question> questionMap = AskServiceSngl.get().queryQuestionByIds(questionIdSet);
            Map<Long, Answer> answerMap = AskServiceSngl.get().queryAnswerByAnswerIds(answerIdSet);

            Set<String> profileIdSet = new HashSet<String>();
            for (Long answerId : answerMap.keySet()) {
                profileIdSet.add(answerMap.get(answerId).getAnswerProfileId());
            }

            Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
            Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);

            List<WanbaFavoriteDTO> wanbaFavoriteDTOList = new ArrayList<WanbaFavoriteDTO>();
            for (AskUserAction askUserAction : pageRows.getRows()) {
                WanbaFavoriteDTO dto = new WanbaFavoriteDTO();

                Question question = questionMap.get(Long.valueOf(askUserAction.getValue()));

                //如果问题不存在，从我的收藏里面删除
                if (question == null) {
                    AskServiceSngl.get().removeAskUserAction(askUserAction);
                    continue;
                }
                dto.setQuestion(questionToDetailDTO(question, null, new HashSet<Long>()));

                Answer answer = answerMap.get(Long.valueOf(askUserAction.getDestId()));
                //askUserAction.get
                if (answer == null) {
                    AskServiceSngl.get().removeAskUserAction(askUserAction);
                    continue;
                }


                Profile profile = profileMap.get(answer.getAnswerProfileId());
                VerifyProfile wanbaProfile = wanbaProfileMap.get(answer.getAnswerProfileId());
                if (profile != null) {
                    dto.setProfile(wanbaProfileDTO(profile, wanbaProfile));
                }
                dto.setAnswer(answerToDetailDTO(answer, null, null));

                wanbaFavoriteDTOList.add(dto);
            }
            returnObj.setRows(wanbaFavoriteDTOList);
           // pageRows.getPage().setTotalRows(wanbaFavoriteDTOList.size());
            returnObj.setPage(pageRows.getPage());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return returnObj;
    }

}
