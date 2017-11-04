package com.enjoyf.platform.tools.gamedb;


import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class ImportGameByExcel {

    static String SEPARATOR = "|";

    //private static String dir = "C:\\Users\\pengxu\\Desktop\\asktemplate";
    private static String filepath = "/home/ops/services/updategame/updategame.xlsx";


    //private static String testPath = "/Users/zhimingli/Documents/updategame.xlsx";

    public static void main(String[] args) throws Exception {
        System.out.println("start=====");
        List<Game> games = loadGame(filepath);
        for (Game game : games) {
            System.out.println(new Gson().toJson(game));

            updaetGame(game);
        }
        System.out.println("end==============");
    }

    private static void updaetGame(Game game) {
        try {
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", game.gameid), false);
            if (gameDB != null) {
                BasicDBObject queryDBObject = new BasicDBObject();
                BasicDBObject updateDBObject = new BasicDBObject();
                queryDBObject.put(GameDBField.ID.getColumn(), game.getGameid());

                updateDBObject.put(GameDBField.LEVELGAME.getColumn(), game.isLevelGame());
                updateDBObject.put(GameDBField.WIKIKEY.getColumn(), game.getWikikey());
                updateDBObject.put(GameDBField.OFFICIALWEBSITE.getColumn(), game.getWww_url());
                GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private static List<Game> loadGame(String filepath) {

        List<Game> returnList = new ArrayList<Game>();
        InputStream is = null;
        try {
            is = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    XSSFCell gameid = xssfRow.getCell(0);
                    XSSFCell name = xssfRow.getCell(1);
                    XSSFCell wikikey = xssfRow.getCell(2);
                    XSSFCell levalgame = xssfRow.getCell(3);
                    XSSFCell www_url = xssfRow.getCell(4);
                    if (StringUtil.isEmpty(getValue(gameid))) {
                        continue;
                    }


                    Game game = new Game();
                    game.setGameid(Double.valueOf(getValue(gameid)).longValue());
                    game.setWikikey(getValue(wikikey));
                    game.setLevelGame("是".equals(getValue(levalgame)) ? true : false);
                    game.setWww_url(getValue(www_url));
                    returnList.add(game);
                    System.out.println(getValue(gameid) + "<--1->" + getValue(name) + "<--2->" + getValue(wikikey) + "<--3->" + getValue(levalgame) + "<--4->" + getValue(www_url));
                }
            }
        }

        return returnList;
    }

    private static String getValue(XSSFCell xssfRow) {
        if (xssfRow == null) {
            return "";
        }
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    static class Game {
        private Long gameid;
        private String wikikey;
        private boolean levelGame;//是否关卡游戏
        private String www_url;

        public Long getGameid() {
            return gameid;
        }

        public void setGameid(Long gameid) {
            this.gameid = gameid;
        }

        public String getWikikey() {
            return wikikey;
        }

        public void setWikikey(String wikikey) {
            this.wikikey = wikikey;
        }

        public boolean isLevelGame() {
            return levelGame;
        }

        public void setLevelGame(boolean levelGame) {
            this.levelGame = levelGame;
        }

        public String getWww_url() {
            return www_url;
        }

        public void setWww_url(String www_url) {
            this.www_url = www_url;
        }
    }


}
