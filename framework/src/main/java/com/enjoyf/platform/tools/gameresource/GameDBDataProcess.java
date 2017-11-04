package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameDBHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBCover;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.mongodb.BasicDBObject;

import java.util.List;

/**
 * Created by tonydiao on 2015/3/23.  用于把新游开测的ios和android的clientline中的精确时间还是自定义值、自定义字符串、开测时间     等三个字段
 * 同步到mongodb的game库的game_db表
 */
public class GameDBDataProcess {

    private static GameDBHandler gameDBHandler = null;
    private static JoymeAppHandler joymeAppHandler = null;

    public static void main(String[] args) {
        FiveProps fiveProps = Props.instance().getServProps();

        try {
            gameDBHandler = new GameDBHandler("gamedb", fiveProps);
            joymeAppHandler = new JoymeAppHandler("joymeapp", fiveProps);

            Long lineIdIos = 0L, lineIdAndroid = 0L;
            //  gc_newgame
            QueryExpress queryExpressIos = new QueryExpress();
            queryExpressIos.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.GAME.getCode()));
            queryExpressIos.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpressIos.add(QueryCriterions.like(ClientLineField.CODE, "gc_newgame_0"));

            List<ClientLine> clientLineListIos = joymeAppHandler.queryClientLine(queryExpressIos);

            QueryExpress queryExpressAndroid = new QueryExpress();
            queryExpressAndroid.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.GAME.getCode()));
            queryExpressAndroid.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpressAndroid.add(QueryCriterions.like(ClientLineField.CODE, "gc_newgame_1"));

            List<ClientLine> clientLineListAndroid = joymeAppHandler.queryClientLine(queryExpressAndroid);

            lineIdIos = clientLineListIos.get(0).getLineId();
            lineIdAndroid = clientLineListAndroid.get(0).getLineId();

            process(lineIdIos, 0);
            process(lineIdAndroid, 1);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private static void process(Long lineId, int type) throws DbException {

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
        queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));

        List<ClientLineItem> itemListIos = joymeAppHandler.queryClientLineItem(queryExpress);
        String posterShowTypeIos = "", posterShowContentIos = "", posterGamePublicTimeIos = "";
        String posterShowTypeAndroid = "";
        String posterShowContentAndroid = "";
        String posterGamePublicTimeAndroid = "";
        long gameDbIdLong;
        ClientLineItem item;
        for (int i = 0; i < itemListIos.size(); i += 1) {

            item = itemListIos.get(i);
            if (type == 0) {
                posterShowTypeIos = item.getRate();
                posterShowContentIos = item.getAuthor();
                posterGamePublicTimeIos = String.valueOf(item.getItemCreateDate().getTime());
            } else if (type == 1) {
                posterShowTypeAndroid = item.getRate();
                posterShowContentAndroid = item.getAuthor();
                posterGamePublicTimeAndroid = String.valueOf(item.getItemCreateDate().getTime());

            }
            gameDbIdLong = Long.valueOf(item.getDirectId());


            GameDB gameDB = gameDBHandler.get(new BasicDBObject().append("_id", gameDbIdLong));

            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameDbIdLong);
            BasicDBObject updateDBObject = new BasicDBObject();
            GameDBCover cover = gameDB.getGameDBCover();
            if (type == 0) {
                cover.setPosterShowTypeIos(posterShowTypeIos);
                cover.setPosterShowContentIos(posterShowContentIos);
                cover.setPosterGamePublicTimeIos(posterGamePublicTimeIos); //数据库中存放long值
            } else if (type == 1) {
                cover.setPosterShowTypeAndroid(posterShowTypeAndroid);
                cover.setPosterShowContentAndroid(posterShowContentAndroid);
                cover.setPosterGamePublicTimeAndroid(posterGamePublicTimeAndroid); //数据库中存放long值
            }

            updateDBObject.put(GameDBField.GAMEDB_COVER.getColumn(), cover.toJson());
            gameDBHandler.update(queryDBObject, updateDBObject);
        }


    }


}
