package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameDBHandler;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;

import java.util.List;

//import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/2
 * Description:
 */
public class GameDBRelationMain {

    private static GameDBHandler handler = null;

    public static void main(String[] args) {
        FiveProps fiveProps = Props.instance().getServProps();

        try {
            handler = new GameDBHandler("writeable", fiveProps);

            List<GameDB> gameDBList = handler.query(new MongoQueryExpress().add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, ValidStatus.VALID.getCode())));


            for (GameDB gameDb : gameDBList) {
                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gameDb.getGameDbId()))
                        .add(QueryCriterions
                                .eq(GameDBRelationField.TYPE, GameDBRelationType.DETAIL.getCode()));
                GameDBRelation gameDBRelation = handler.getGameDbRelation(queryExpress);
                if (gameDBRelation == null) {
                    createGameDBRelation(gameDb.getGameDbId());

                    System.out.println("create game db:" + gameDb.getGameName());
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private static void createGameDBRelation(long gamedbId) throws DbException {

        GameDBRelation relation = new GameDBRelation();
        relation.setModifyUserid("sysadmin");
        relation.setModifyIp("127.0.0.1");
        relation.setTitle("详情");
        relation.setUri(String.valueOf(gamedbId));
        relation.setType(GameDBRelationType.DETAIL);
        relation.setGamedbId(gamedbId);
        relation.setDisplayOrder((int) ((Integer.MAX_VALUE - System.currentTimeMillis()) / 1000));

        handler.insertGameDbRelation(relation);
//        GameResourceServiceSngl.get().createGameDbRelation(relation);

    }
}
