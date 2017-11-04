package com.enjoyf.platform.cloud;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.ScoreRange;
import com.enjoyf.platform.util.redis.ScoreRangeRows;
import com.squareup.okhttp.Response;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by zhimingli on 2017/6/16.
 */
public class PageRowsUtil {

    /**
     * @param response，使用okhttp，获取数据，返回response
     * @param curPage                           页码
     * @param pageSize                          页大小
     * @return
     * @throws IOException
     */
    public static PageRows getPage(Response response, int curPage, int pageSize, Class t) throws IOException {
        PageRows returnPageRow = new PageRows();
        if (!response.isSuccessful()) {
            return returnPageRow;
        }
        //分页信息
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        String totleSize = response.header("X-Total-Count");
        if (StringUtil.isEmpty(totleSize)) {
            return returnPageRow;
        }
        pagination.setTotalRows(Integer.valueOf(totleSize));
        returnPageRow.setPage(pagination);

        //数据
        returnPageRow.setRows(JSON.parseArray(response.body().string(), t));
        return returnPageRow;
    }


    public static ScoreRangeRows geRangeRows(Response response, Class t) throws IOException {
        ScoreRangeRows result = new ScoreRangeRows();

        if (!response.isSuccessful()) {
            return result;
        }
        String returnStr = null;
        try {
            returnStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!StringUtil.isEmpty(returnStr)) {
            JSONObject jsonObject = JSONObject.fromObject(returnStr);
            String range = jsonObject.getString("range");
            String rows = jsonObject.getString("rows");

            if (!StringUtil.isEmpty(range)) {
                JSONObject rangeObj = JSONObject.fromObject(range);
                ScoreRange scoreRange = JSON.parseObject(range, ScoreRange.class);
                scoreRange.setHasnext(rangeObj.getBoolean("hasnext"));
                scoreRange.setIsFirstPage(rangeObj.getBoolean("firstPage"));
                result.setRange(scoreRange);
            }
            if (!StringUtil.isEmpty(rows)) {
                result.setRows(JSON.parseArray(rows, t));
            }
        }
        return result;
    }

}
