package com.enjoyf.platform.tools.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.misc.MiscHandler;
import com.enjoyf.platform.service.misc.GamePropDb;
import com.enjoyf.platform.service.misc.GamePropValueType;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-15
 * Time: 上午9:37
 * To change this template use File | Settings | File Templates.
 */
public class ImportGamePropDbByExcel {

    private static final String EXCEL_PATH = "C:\\Users\\ericliu.EF\\Desktop\\MT_db.xlsx";


    public static void main(String[] args) throws FileNotFoundException {
        long now = System.currentTimeMillis();

        FiveProps servProps = Props.instance().getServProps();
        MiscHandler handler = null;
        try {
            handler = new MiscHandler("writeable", servProps);
        } catch (DbException e) {
            System.exit(0);
        }

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(EXCEL_PATH));

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> keyRowIterator = sheet.rowIterator();


            Map<String, Integer> keyMap = getKeyMap(keyRowIterator.next(), 0);

            while (keyRowIterator.hasNext()) {
                Row row = keyRowIterator.next();
                List<GamePropDb> gamePropDbList = genRowGamePropDb(row, keyMap, 1, handler);
                handler.batchInsertGamePropDb("mt", gamePropDbList);
            }

            System.out.println("===========finish:========================spend:" + ((System.currentTimeMillis() - now) / 1000));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static Map<String, Integer> getKeyMap(Row keyRow, int keyIdx) {
        Map<String, Integer> keyMap = new LinkedHashMap<String, Integer>();

        Iterator<Cell> cellIterator = keyRow.cellIterator();

        int idx = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            keyMap.put(cell.getStringCellValue(), idx);
            idx++;
        }

        return keyMap;
    }

    private static List<GamePropDb> genRowGamePropDb(Row row, Map<String, Integer> keyMap, int propDbType, MiscHandler handler) throws DbException {
        List<GamePropDb> gamePropDbList = new ArrayList<GamePropDb>();

        long keyid = handler.getGamePropDbSeqNo();
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            GamePropDb gamePropDb = new GamePropDb();

            gamePropDb.setKey_id(keyid);
            gamePropDb.setKey_name(entry.getKey());
            gamePropDb.setType(propDbType);

            Cell cell = row.getCell(entry.getValue());

            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    gamePropDb.setDate_value(cell.getDateCellValue());
                    gamePropDb.setValueType(GamePropValueType.DATE_VALUE);
                } else {
                    Float intValue = Float.parseFloat(String.valueOf(cell.getNumericCellValue()));
                    gamePropDb.setNum_value(intValue);
                    gamePropDb.setValueType(GamePropValueType.NUM_VALUE);
                }
            } else {
                String value = null;
                try {
                    value = cell.getStringCellValue();
                    gamePropDb.setString_value(value);
                    gamePropDb.setValueType(GamePropValueType.STRING_VALUE);
                } catch (Exception e) {
                    System.out.println(row);
                    System.out.println(entry.getKey());
                }
                try {
                    Float intValue = Float.parseFloat(value);
                    gamePropDb.setNum_value(intValue);
                    gamePropDb.setValueType(GamePropValueType.NUM_VALUE);
                } catch (NumberFormatException e) {

                }
            }
            gamePropDbList.add(gamePropDb);
        }

        return gamePropDbList;
    }
}
