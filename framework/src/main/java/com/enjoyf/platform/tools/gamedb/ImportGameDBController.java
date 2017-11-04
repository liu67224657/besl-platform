package com.enjoyf.platform.tools.gamedb;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameDBHandler;
import com.enjoyf.platform.serv.gameres.GameResourceRedis;
import com.enjoyf.platform.serv.gameres.GroupPrivilegeCache;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jxl.*;
import net.sf.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhitaoshi on 2016/3/14.
 */
public class ImportGameDBController {

    private static GameDBHandler gameDBHandler = null;
    private static RedisManager redisManager = null;
    private static GameResourceRedis gameResourceRedis = null;
    private static GroupPrivilegeCache groupPrivilegeCache = null;

    public static void main(String[] args) {
        FiveProps fiveProps = Props.instance().getServProps();
        try {
            gameDBHandler = new GameDBHandler("gamedb", fiveProps);
            redisManager = new RedisManager(fiveProps);
            gameResourceRedis = new GameResourceRedis(fiveProps);
            groupPrivilegeCache = new GroupPrivilegeCache(new MemCachedConfig(fiveProps));
        } catch (DbException e) {
            e.printStackTrace();
        }
//        importGameDB();
//        importWWWParamCache();
//        importGameDBCategory();
//        importGameDBCache();
//        importExcel();
//        importExcel2();
//        importExcel3();
    }

    private static void importExcel3() {
        System.out.println("====start");
        Workbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //读取文件
            InputStream in = new FileInputStream("C:\\Users\\zhitaoshi\\Desktop\\主机游戏.xls");
            if (in == null) {
                System.out.println("====文件不存在");
            }
            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.getWorkbook(in, workbookSettings);
            //表单
            Sheet sheet = workbook.getSheet(0);
            int columns = sheet.getColumns();
            int rows = sheet.getRows();
            for (int j = 13; j < rows; j++) {//行
                GameDB gameDB = new GameDB();
                Set<GamePlatformType> platformTypeSet = new HashSet<GamePlatformType>();
                Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
                for (int i = 0; i < columns; i++) {//列
                    //j行i列  的   单元格
                    Cell cell = sheet.getCell(i, j);
                    //通用的获取cell值的方式,返回字符串
                    String content = cell.getContents();
                    //获得cell具体类型值的方式
                    if (cell.getType() == CellType.LABEL) {
                        LabelCell labelCell = (LabelCell) cell;
                        content = labelCell.getString();
                    }
                    //excel 类型为时间类型处理;
                    if (cell.getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) cell;
                        content = sdf.format(dateCell.getDate());

                    }
                    //excel 类型为数值类型处理;
                    if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
                        NumberCell numberCell = (NumberCell) cell;
                        content = String.valueOf(numberCell.getValue());
                    }
                    //每一列对应的 字段 （双方约定规范，不得随意更改）
                    if (i == 0) {
                        gameDB.setGameName(content);
                    } else if (i == 1) {
                        gameDB.setAnotherName(content);
                    } else if (i == 2) {
                        HttpClientManager httpClientManager = new HttpClientManager();
                        HttpResult httpResult = httpClientManager.get("http://up001.joyme.test/json/upload/figureurl?url=" + content + "&at=joymeplatform", new HttpParameter[]{}, "UTF-8");
                        if (httpResult != null && httpResult.getResult() != null) {
                            JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
                            if (jsonObject.containsKey("url")) {
                                gameDB.setGameIcon(jsonObject.getString("url"));
                            }
                        }
                    } else if (i == 3) {
                        if(!StringUtil.isEmpty(content)){
                            try {
                                gameDB.setGamePublicTime(sdf.parse(content));
                            } catch (ParseException e) {
                            }
                        }
                    } else if (i == 4) {
                        Set<GameLanguageType> languageTypeSet = new HashSet<GameLanguageType>();
                        if (content.indexOf(",") > 0) {
                            String[] arr = content.split(",");
                            for (String str : arr) {
                                for (GameLanguageType languageType : GameLanguageType.getAll()) {
                                    if (languageType.getName().equals(str)) {
                                        languageTypeSet.add(languageType);
                                    }
                                }
                            }
                        } else {
                            for (GameLanguageType languageType : GameLanguageType.getAll()) {
                                if (languageType.getName().equals(content)) {
                                    languageTypeSet.add(languageType);
                                }
                            }
                        }
                        gameDB.setLanguageTypeSet(languageTypeSet);
                    } else if (i == 5) {
                        if(!StringUtil.isEmpty(content)){
                            if(content.indexOf(",") > 0){
                                String[] arr = content.split(",");
                                for(String str:arr){
                                    for (PSPPlatform pspPlatform : PSPPlatform.getAll()) {
                                        if (pspPlatform.getDesc().toLowerCase().equals(str.toLowerCase())) {
                                            platformTypeSet.add(GamePlatformType.PSP);
                                            if(!platformMap.containsKey(String.valueOf(GamePlatformType.PSP.getCode()))){
                                                platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), new HashSet<GamePlatform>());
                                            }
                                            platformMap.get(String.valueOf(GamePlatformType.PSP.getCode())).add(pspPlatform);
                                        }
                                    }
                                    for (TVPlatform tvPlatform : TVPlatform.getAll()) {
                                        if (tvPlatform.getDesc().toLowerCase().equals(str.toLowerCase())) {
                                            platformTypeSet.add(GamePlatformType.TV);
                                            if(!platformMap.containsKey(String.valueOf(GamePlatformType.TV.getCode()))){
                                                platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), new HashSet<GamePlatform>());
                                            }
                                            platformMap.get(String.valueOf(GamePlatformType.TV.getCode())).add(tvPlatform);
                                        }
                                    }
                                }
                            }else {
                                for (PSPPlatform pspPlatform : PSPPlatform.getAll()) {
                                    if (pspPlatform.getDesc().toLowerCase().equals(content.toLowerCase())) {
                                        platformTypeSet.add(GamePlatformType.PSP);
                                        if(!platformMap.containsKey(String.valueOf(GamePlatformType.PSP.getCode()))){
                                            platformMap.put(String.valueOf(GamePlatformType.PSP.getCode()), new HashSet<GamePlatform>());
                                        }
                                        platformMap.get(String.valueOf(GamePlatformType.PSP.getCode())).add(pspPlatform);
                                    }
                                }
                                for (TVPlatform tvPlatform : TVPlatform.getAll()) {
                                    if (tvPlatform.getDesc().toLowerCase().equals(content.toLowerCase())) {
                                        platformTypeSet.add(GamePlatformType.TV);
                                        if(!platformMap.containsKey(String.valueOf(GamePlatformType.TV.getCode()))){
                                            platformMap.put(String.valueOf(GamePlatformType.TV.getCode()), new HashSet<GamePlatform>());
                                        }
                                        platformMap.get(String.valueOf(GamePlatformType.TV.getCode())).add(tvPlatform);
                                    }
                                }
                            }

                        }
                    } else if (i == 6) {
                        Set<GameCategoryType> gameCategoryTypeSet = new HashSet<GameCategoryType>();
                        if (content.indexOf(",") > 0) {
                            String[] arr = content.split(",");
                            for (String str : arr) {
                                for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                    if (categoryType.getValue().equals(str)) {
                                        gameCategoryTypeSet.add(categoryType);
                                    }
                                }
                            }
                        } else {
                            for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                if (categoryType.getValue().equals(content)) {
                                    gameCategoryTypeSet.add(categoryType);
                                }
                            }
                        }
                        gameDB.setCategoryTypeSet(gameCategoryTypeSet);
                    } else if (i == 7) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setGameProfile(content);
                        }

                    } else if (i == 8) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setGameDeveloper(content);
                        }

                    } else if (i == 9) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setGamePublishers(content);
                        }
                    } else if (i == 10) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setOfficialWebsite(content);
                        }
                    }
                }
                gameDB.setPlatformTypeSet(platformTypeSet);
                gameDB.setPlatformMap(platformMap);
                gameDB.setValidStatus(GameDbStatus.VALID);
                gameDB.setCreateDate(new Date());
                GameDB gameDB2 = gameDBHandler.get(new BasicDBObject(GameDBField.GAMENAME.getColumn(), gameDB.getGameName()));
                if (gameDB2 == null) {
                    gameDB = gameDBHandler.insertGameDB(gameDB);
                }
                System.out.println(j + ":" + gameDB.getGameDbId() + ":" + gameDB.getGameName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        System.out.println("====end");
    }

    private static void importExcel2() {
        System.out.println("====start");
        Workbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //读取文件
            InputStream in = new FileInputStream("C:\\Users\\zhitaoshi\\Desktop\\电脑游戏.xls");
            if (in == null) {
                System.out.println("====文件不存在");
            }
            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.getWorkbook(in, workbookSettings);
            //表单
            Sheet sheet = workbook.getSheet(0);
            int columns = sheet.getColumns();
            int rows = sheet.getRows();
            for (int j = 1; j < rows; j++) {//行
                GameDB gameDB = new GameDB();
                Set<GamePlatformType> platformTypeSet = new HashSet<GamePlatformType>();
                platformTypeSet.add(GamePlatformType.PC);
                gameDB.setPlatformTypeSet(platformTypeSet);

                Set<GamePlatform> platformSet = new HashSet<GamePlatform>();
                Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
                for (int i = 0; i < columns; i++) {//列
                    //j行i列  的   单元格
                    Cell cell = sheet.getCell(i, j);
                    //通用的获取cell值的方式,返回字符串
                    String content = cell.getContents();
                    //获得cell具体类型值的方式
                    if (cell.getType() == CellType.LABEL) {
                        LabelCell labelCell = (LabelCell) cell;
                        content = labelCell.getString();
                    }
                    //excel 类型为时间类型处理;
                    if (cell.getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) cell;
                        content = sdf.format(dateCell.getDate());

                    }
                    //excel 类型为数值类型处理;
                    if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
                        NumberCell numberCell = (NumberCell) cell;
                        content = String.valueOf(numberCell.getValue());
                    }
                    //每一列对应的 字段 （双方约定规范，不得随意更改）
                    if (i == 0) {
                        gameDB.setGameName(content);
                    } else if (i == 1) {
                        gameDB.setAnotherName(content);
                    } else if (i == 2) {
                        HttpClientManager httpClientManager = new HttpClientManager();
                        HttpResult httpResult = httpClientManager.get("http://up001.joyme.test/json/upload/figureurl?url=" + content + "&at=joymeplatform", new HttpParameter[]{}, "UTF-8");
                        if (httpResult != null && httpResult.getResult() != null) {
                            JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
                            if (jsonObject.containsKey("url")) {
                                gameDB.setGameIcon(jsonObject.getString("url"));
                            }
                        }
                    } else if (i == 3) {
                        if(!StringUtil.isEmpty(content)){
                            for (PCPlatform pcPlatform : PCPlatform.getAll()) {
                                if (pcPlatform.getDesc().toLowerCase().equals(content.toLowerCase())) {
                                    platformSet.add(pcPlatform);
                                }
                            }
                        }
                    } else if (i == 4) {
                        if(!StringUtil.isEmpty(content)){
                            gameDB.setGamePublicTime(sdf.parse(content));
                        }
                    } else if (i == 5) {
                        Set<GameCategoryType> gameCategoryTypeSet = new HashSet<GameCategoryType>();
                        if (content.indexOf(",") > 0) {
                            String[] arr = content.split(",");
                            for (String str : arr) {
                                for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                    if (categoryType.getValue().equals(str)) {
                                        gameCategoryTypeSet.add(categoryType);
                                    }
                                }
                            }
                        } else {
                            for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                if (categoryType.getValue().equals(content)) {
                                    gameCategoryTypeSet.add(categoryType);
                                }
                            }
                        }
                        gameDB.setCategoryTypeSet(gameCategoryTypeSet);
                    } else if (i == 6) {
                        Set<GameLanguageType> languageTypeSet = new HashSet<GameLanguageType>();
                        if (content.indexOf(",") > 0) {
                            String[] arr = content.split(",");
                            for (String str : arr) {
                                for (GameLanguageType languageType : GameLanguageType.getAll()) {
                                    if (languageType.getName().equals(str)) {
                                        languageTypeSet.add(languageType);
                                    }
                                }
                            }
                        } else {
                            for (GameLanguageType languageType : GameLanguageType.getAll()) {
                                if (languageType.getName().equals(content)) {
                                    languageTypeSet.add(languageType);
                                }
                            }
                        }
                        gameDB.setLanguageTypeSet(languageTypeSet);
                    } else if (i == 7) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setGameDeveloper(content);
                        }

                    } else if (i == 8) {
                        if (!StringUtil.isEmpty(content)) {
                            gameDB.setGamePublishers(content);
                        }
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.PC.getCode()), platformSet);
                gameDB.setPlatformMap(platformMap);
                gameDB.setValidStatus(GameDbStatus.VALID);
                gameDB.setCreateDate(new Date());
                GameDB gameDB2 = gameDBHandler.get(new BasicDBObject(GameDBField.GAMENAME.getColumn(), gameDB.getGameName()));
                if (gameDB2 == null) {
                    gameDB = gameDBHandler.insertGameDB(gameDB);
                }
                System.out.println(j + ":" + gameDB.getGameDbId() + ":" + gameDB.getGameName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        System.out.println("====end");
    }

    private static void importExcel() {
        System.out.println("====start");
        Workbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //读取文件
            InputStream in = new FileInputStream("C:\\Users\\zhitaoshi\\Desktop\\手机游戏.xls");
            if (in == null) {
                System.out.println("====文件不存在");
            }
            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.getWorkbook(in, workbookSettings);
            //表单
            Sheet sheet = workbook.getSheet(0);
            int columns = sheet.getColumns();
            int rows = sheet.getRows();
            for (int j = 3035; j < rows; j++) {//行
                GameDB gameDB = new GameDB();
                Set<GamePlatformType> platformTypeSet = new HashSet<GamePlatformType>();
                platformTypeSet.add(GamePlatformType.MOBILE);
                gameDB.setPlatformTypeSet(platformTypeSet);

                Set<GamePlatform> platformSet = new HashSet<GamePlatform>();
                Map<String, Set<GamePlatform>> platformMap = new HashMap<String, Set<GamePlatform>>();
                for (int i = 0; i < columns; i++) {//列
                    //j行i列  的   单元格
                    Cell cell = sheet.getCell(i, j);
                    //通用的获取cell值的方式,返回字符串
                    String content = cell.getContents();
                    //获得cell具体类型值的方式
                    if (cell.getType() == CellType.LABEL) {
                        LabelCell labelCell = (LabelCell) cell;
                        content = labelCell.getString();
                    }
                    //excel 类型为时间类型处理;
                    if (cell.getType() == CellType.DATE) {
                        DateCell dateCell = (DateCell) cell;
                        content = sdf.format(dateCell.getDate());

                    }
                    //excel 类型为数值类型处理;
                    if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
                        NumberCell numberCell = (NumberCell) cell;
                        content = String.valueOf(numberCell.getValue());
                    }
                    //每一列对应的 字段 （双方约定规范，不得随意更改）
                    if (i == 0) {
                        gameDB.setGameName(content);
                    } else if (i == 1) {
                        gameDB.setAnotherName(content);
                    } else if (i == 2) {
                        HttpClientManager httpClientManager = new HttpClientManager();
                        HttpResult httpResult = httpClientManager.get("http://up001.joyme.test/json/upload/figureurl?url=" + content + "&at=joymeplatform", new HttpParameter[]{}, "UTF-8");
                        if (httpResult != null && httpResult.getResult() != null) {
                            JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
                            if (jsonObject.containsKey("url")) {
                                gameDB.setGameIcon(jsonObject.getString("url"));
                            }
                        }
                    } else if (i == 3) {
                        if(!StringUtil.isEmpty(content)){
                            gameDB.setGameRate(Double.valueOf(content));
                        }
                    } else if (i == 4) {
                        Set<GameCategoryType> gameCategoryTypeSet = new HashSet<GameCategoryType>();
                        if (content.indexOf(",") > 0) {
                            String[] arr = content.split(",");
                            for (String str : arr) {
                                for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                    if (categoryType.getValue().equals(str)) {
                                        gameCategoryTypeSet.add(categoryType);
                                    }
                                }
                            }
                        } else {
                            for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                if (categoryType.getValue().equals(content)) {
                                    gameCategoryTypeSet.add(categoryType);
                                }
                            }
                        }
                        gameDB.setCategoryTypeSet(gameCategoryTypeSet);
                    } else if (i == 5) {
                        gameDB.setGameProfile(content);
                    } else if (i == 6) {
                        if (!StringUtil.isEmpty(content)) {
                            platformSet.add(MobilePlatform.IOS);
                            gameDB.setIosDownload(content);
                        }

                    } else if (i == 7) {
                        if (!StringUtil.isEmpty(content)) {
                            platformSet.add(MobilePlatform.ANDROID);
                            gameDB.setAndroidDownload(content);
                        }
                    }
                }
                platformMap.put(String.valueOf(GamePlatformType.MOBILE.getCode()), platformSet);
                gameDB.setPlatformMap(platformMap);
                gameDB.setValidStatus(GameDbStatus.VALID);
                gameDB.setCreateDate(new Date());
                GameDB gameDB2 = gameDBHandler.get(new BasicDBObject(GameDBField.GAMENAME.getColumn(), gameDB.getGameName()));
                if (gameDB2 == null) {
                    gameDB = gameDBHandler.insertGameDB(gameDB);
                }
                System.out.println(j + ":" + gameDB.getGameDbId() + ":" + gameDB.getGameName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        System.out.println("====end");
    }

    private static void importGameDBCategory() {
        try {
            System.out.println("====start");
            int cp = 0;
            Pagination page;
            do {
                cp += 1;
                System.out.println("====cp" + cp);
                page = new Pagination(2000 * cp, cp, 2000);
                PageRows<GameDB> pageRows = gameDBHandler.queryGameDbByPage(new MongoQueryExpress().add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode())), page);
                page = pageRows.getPage();
                for (GameDB gameDB : pageRows.getRows()) {
                    System.out.println("====id:" + gameDB.getGameDbId());
                    if (!CollectionUtil.isEmpty(gameDB.getCategoryTypeSet())) {
                        BasicDBObject queryDBObject = new BasicDBObject();
                        queryDBObject.put(GameDBField.ID.getColumn(), gameDB.getGameDbId());

                        BasicDBObject updateDBObject = new BasicDBObject();
                        updateDBObject.append("$set", new BasicDBObject());
                        updateDBObject.append("$unset", new BasicDBObject());
//                        for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
//                            if (categoryType.equals(GameCategoryType.YIZHI)) {//益智归入休闲
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.XIUXIAN.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.YIZHI.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.YANGCHENG)) {//养成归入休闲
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.XIUXIAN.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.YANGCHENG.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.SANXIAO)) {//三消归入休闲
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.XIUXIAN.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.SANXIAO.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.KONGBU)) {//恐怖归入动作
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.DONGZUO.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.KONGBU.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.SAICHE)) {//赛车归入竞速
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.JINGSU.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.SAICHE.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.PAOKU)) {//跑酷归入竞速
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.JINGSU.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.PAOKU.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.JINGYING)) {//经营归入模拟
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.MONI.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.JINGYING.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.TAFANG)) {//塔防归入策略
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.CELUE.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.TAFANG.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.TUOCHU)) {//脱出归入策略
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.CELUE.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.TUOCHU.getCode(), false);
//                            }
//                            if (categoryType.equals(GameCategoryType.DANMU)) {//弹幕归入射击
//                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.SHEJI.getCode(), true);
//                                ((BasicDBObject) updateDBObject.get("$unset")).append(GameDBField.GAME_CATEGORY_.getColumn() + GameCategoryType.DANMU.getCode(), false);
//                            }
//                        }
//                        gameDBHandler.update(queryDBObject, updateDBObject);
                        gameResourceRedis.removeGameCollectionDTO(gameDB.getGameDbId());
                        groupPrivilegeCache.removeGameDBCache(gameDB.getGameDbId());
                        groupPrivilegeCache.removeGameDBByAnotherName(gameDB.getAnotherName());
                    }
                }
            } while (!page.isLastPage());
            System.out.println("====end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importGameDBCache() {
        try {
            System.out.println("====start");
            int cp = 0;
            Pagination page;
            do {
                cp += 1;
                System.out.println("====cp" + cp);
                page = new Pagination(2000 * cp, cp, 2000);
                PageRows<GameDB> pageRows = gameDBHandler.queryGameDbByPage(new MongoQueryExpress().add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode())), page);
                page = pageRows.getPage();
                for (GameDB gameDB : pageRows.getRows()) {
                    System.out.println("====id:" + gameDB.getGameDbId());
                    if (!CollectionUtil.isEmpty(gameDB.getPlatformMap())) {
                        BasicDBObject queryDBObject = new BasicDBObject();
                        queryDBObject.put(GameDBField.ID.getColumn(), gameDB.getGameDbId());

                        BasicDBObject updateDBObject = new BasicDBObject();
                        for (String key : gameDB.getPlatformMap().keySet()) {
                            updateDBObject.put(GameDBField.PLATFORMTYPE_.getColumn() + key, true);
                            for (GamePlatform newPlatform : gameDB.getPlatformMap().get(key)) {
                                updateDBObject.put(GameDBField.GAME_PLATFORM_.getColumn() + key + "_" + newPlatform.getCode(), true);
                            }
                        }
                        gameDBHandler.update(queryDBObject, updateDBObject);
                        gameResourceRedis.removeGameCollectionDTO(gameDB.getGameDbId());
                        groupPrivilegeCache.removeGameDBCache(gameDB.getGameDbId());
                        groupPrivilegeCache.removeGameDBByAnotherName(gameDB.getAnotherName());
                    }
                }
            } while (!page.isLastPage());
            System.out.println("====end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importWWWParamCache() {
        try {
            System.out.println("====start");
            int cp = 0;
            Pagination page;
            do {
                cp += 1;
                System.out.println("====cp" + cp);
                page = new Pagination(2000 * cp, cp, 2000);
                
                PageRows<GameDB> pageRows = gameDBHandler.queryGameDbByPage(new MongoQueryExpress().add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode())).add(new MongoSort[]{new MongoSort(GameDBField.CREATE_DATE, MongoSortOrder.DESC)}), page);
                page = pageRows.getPage();
                for (GameDB gameDB : pageRows.getRows()) {
                    System.out.println("====id:" + gameDB.getGameDbId());
                    String idStr = String.valueOf(gameDB.getGameDbId());
                    Set<String> platformParamSet = new HashSet<String>();
                    for (String key : gameDB.getPlatformMap().keySet()) {
                        for (GamePlatform gamePlatform : gameDB.getPlatformMap().get(key)) {
                            String platformParam = idStr + "_" + key + "-" + gamePlatform.getCode();
                            platformParamSet.add(platformParam);
                        }
                    }
                    Set<String> netParamSet = new HashSet<String>();
                    for (String platformParam : platformParamSet) {
                        if(gameDB.getGameNetType() == null){
                            for(GameNetType netType:GameNetType.getAll()){
                                String netParam = platformParam + "_" + netType.getCode();
                                netParamSet.add(netParam);
                            }
                        }else {
                            String netParam = platformParam + "_" + gameDB.getGameNetType().getCode();
                            netParamSet.add(netParam);
                        }

                    }
                    Set<String> languageParamSet = new HashSet<String>();
                    for (String netParam : netParamSet) {
                        if(CollectionUtil.isEmpty(gameDB.getLanguageTypeSet())){
                            for(GameLanguageType languageType:GameLanguageType.getAll()){
                                String languageParam = netParam + "_" + languageType.getCode();
                                languageParamSet.add(languageParam);
                            }
                        }else {
                            for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                                String languageParam = netParam + "_" + languageType.getCode();
                                languageParamSet.add(languageParam);
                            }
                        }
                    }
                    Set<String> categoryParamSet = new HashSet<String>();
                    for (String languageParam : languageParamSet) {
                        if(CollectionUtil.isEmpty(gameDB.getCategoryTypeSet())){
                            for (GameCategoryType categoryType : GameCategoryType.getAll()) {
                                String categoryParam = languageParam + "_" + categoryType.getCode();
                                categoryParamSet.add(categoryParam);
                            }
                        }else {
                            for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                                String categoryParam = languageParam + "_" + categoryType.getCode();
                                categoryParamSet.add(categoryParam);
                            }
                        }
                    }
                    Set<String> themeParamSet = new HashSet<String>();
                    for (String categoryParam : categoryParamSet) {
                        if(CollectionUtil.isEmpty(gameDB.getThemeTypeSet())){
                            for (GameThemeType themeType : GameThemeType.getAll()
                                    ) {
                                String themeParam = categoryParam + "_" + themeType.getCode();
                                themeParamSet.add(themeParam);
                            }
                        }else {
                            for (GameThemeType themeType : gameDB.getThemeTypeSet()) {
                                String themeParam = categoryParam + "_" + themeType.getCode();
                                themeParamSet.add(themeParam);
                            }
                        }
                    }
                    for (String param : themeParamSet) {
                        redisManager.lpush("webappwww_game_collection_param_set", param);
                    }
                }
            } while (!page.isLastPage());
            System.out.println("====end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importGameDB() {
        try {
            System.out.println("====start");
            int cp = 0;
            Pagination page;
            do {
                cp += 1;
                System.out.println("====cp" + cp);
                page = new Pagination(2000 * cp, cp, 2000);
                PageRows<DBObject> pageRows = gameDBHandler.findAll(new BasicDBObject(), page);
                page = pageRows.getPage();
                for (DBObject dbObject : pageRows.getRows()) {
                    System.out.println("====id:" + dbObject.get("_id"));
                    BasicDBObject queryDBObject = new BasicDBObject();
                    queryDBObject.put(GameDBField.ID.getColumn(), (Long) dbObject.get("_id"));

                    BasicDBObject updateDBObject = new BasicDBObject();
                    updateDBObject.append("$set", new BasicDBObject());
                    updateDBObject.append("$unset", new BasicDBObject());

                    //((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.PLATFORMTYPE_.getColumn(), GamePlatformType.MOBILE.getCode());
                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.PUBLICTIPS.getColumn(), false);
                    if (dbObject.get("gamelanguage") != null) {
                        ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_LANGUAGE_.getColumn() + dbObject.get("gamelanguage"), true);
                    }
                    if (dbObject.get("gametheme") != null) {
                        ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_THEME_.getColumn() + dbObject.get("gametheme"), true);
                    }
                    if (dbObject.get("gamepic") != null) {
                        ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAMEPIC.getColumn(), ((String) dbObject.get("gamepic")).replaceAll("\\@", ","));
                    }
                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.NEWSSUM.getColumn(), 0);
                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.VIDEOSUM.getColumn(), 0);
                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GUIDESUM.getColumn(), 0);
                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.PVSUM.getColumn(), 0);
                    ((BasicDBObject) updateDBObject.get("$unset")).append("gamebrand", 0);
                    ((BasicDBObject) updateDBObject.get("$unset")).append("appclick", "");
                    ((BasicDBObject) updateDBObject.get("$unset")).append("gametheme", -1);
                    ((BasicDBObject) updateDBObject.get("$unset")).append("gamelanguage", -1);
                    ((BasicDBObject) updateDBObject.get("$unset")).append("uno", "");
                    ((BasicDBObject) updateDBObject.get("$unset")).append("platformwiki", "");
                    ((BasicDBObject) updateDBObject.get("$unset")).append("platformcms", "");
                    ((BasicDBObject) updateDBObject.get("$unset")).append("gamescore", 0);

                    ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAMERATE.getColumn(), dbObject.get("gamescore") == null ? 8.0 : Double.valueOf(String.valueOf(dbObject.get("gamescore"))));
                    for (String key : dbObject.keySet()) {
                        if (key.startsWith("platform_code_")) {
                            ((BasicDBObject) updateDBObject.get("$unset")).append(key, -1);
                        }
                        if (key.startsWith("platform_download_")) {
                            ((BasicDBObject) updateDBObject.get("$unset")).append(key, "");
                            String id = key.substring("platform_download_".length());
                            if (id.equals("0")) {
                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.IOSDOWNLOAD.getColumn(), String.valueOf(dbObject.get(key)));
                            } else if (id.equals("1")) {
                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.ANDROIDDOWNLOAD.getColumn(), String.valueOf(dbObject.get(key)));
                            }
                        }
                        if (key.startsWith("game_category_")) {
                            if (dbObject.get(key) == "" || !(Boolean) dbObject.get(key)) {
                                ((BasicDBObject) updateDBObject.get("$unset")).append(key, false);
                            }
                        }
                        if (key.startsWith("channel_id_device_")) {
                            ((BasicDBObject) updateDBObject.get("$unset")).append(key, false);
                            if (dbObject.get(key) != null && dbObject.get(key) != "" && (Boolean) dbObject.get(key)) {
                                String id = key.substring("channel_id_device_".length());
                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.CHANNEL_PLATFORM_.getColumn() + id, true);
                            }
                        }
                        if (key.startsWith("game_device_")) {
                            ((BasicDBObject) updateDBObject.get("$unset")).append(key, false);
                        }
                        if (key.startsWith("app_platform_")) {
                            ((BasicDBObject) updateDBObject.get("$unset")).append(key, -1);
                            if ((Boolean) dbObject.get(key)) {
                                String id = key.substring("app_platform_".length());
                                ((BasicDBObject) updateDBObject.get("$set")).append(GameDBField.GAME_PLATFORM_.getColumn() + GamePlatformType.MOBILE.getCode() + "_" + id, true);
                            }
                        }
                    }
                    gameDBHandler.update(queryDBObject, updateDBObject);
                }
            } while (!page.isLastPage());
            System.out.println("====end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
