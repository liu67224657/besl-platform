package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.Question;
import com.enjoyf.platform.service.ask.search.WanbaSearchService;
import com.enjoyf.platform.service.ask.search.WanbaSearchType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.joyme.dto.Wanba.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
@Service
public class WanbaSearchWebLogic extends AbstractWanbaWebLogic {
    private static Logger logger = LoggerFactory.getLogger(WanbaSearchWebLogic.class);

    private WanbaSearchService searchService = new WanbaSearchService();

    public PageRows<QuestionDTO> searchQuestionByText(String text, Pagination page, String profileId) throws ServiceException {
        PageRows<String> questionIds = searchService.searchByType(WanbaSearchType.QUESION, page.getCurPage(), page.getPageSize(), text);

        List<Long> queryIdList = new ArrayList<Long>();
        for (String idStr : questionIds.getRows()) {
            try {
                queryIdList.add(Long.valueOf(idStr));
            } catch (NumberFormatException ignored) {
            }
        }

        Map<Long, Question> questions = AskServiceSngl.get().queryQuestionByIds(queryIdList);
        List<QuestionDTO> returnList = queryQuetionDtoListByQuestionList(questions.values(), profileId, false);
        PageRows<QuestionDTO> rows = new PageRows<QuestionDTO>();
        rows.setPage(questionIds.getPage());
        rows.setRows(returnList);

        return rows;
    }
}
