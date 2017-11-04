package com.enjoyf.platform.tools.ask;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.QiniuUploadHandler;
import com.enjoyf.util.DateUtil;
import com.enjoyf.util.MD5Util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2016/10/20
 */
public class loadExcelUtil {


    /**
     * 分隔符
     */
    private final static String SEPARATOR = "|";

    static Workbook loadExcel(String xlsxPath) throws IOException {
        return new XSSFWorkbook(new FileInputStream(new File(xlsxPath)));
    }

    static Sheet getSheet(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    /**
     * 由指定的Sheet导出至List
     *
     * @param workbook
     * @param sheet
     * @return
     * @throws IOException
     */
    static List<String> exportListFromExcel(Sheet sheet, Workbook workbook) {


        // 解析公式结果
        FormulaEvaluator evaluator = workbook.getCreationHelper()
                .createFormulaEvaluator();

        List<String> list = new ArrayList<String>();

        int minRowIx = sheet.getFirstRowNum();
        int maxRowIx = sheet.getLastRowNum();
        for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {
            Row row = sheet.getRow(rowIx);
            StringBuilder sb = new StringBuilder();

            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
                Cell cell = row.getCell(new Integer(colIx));
                CellValue cellValue = evaluator.evaluate(cell);

                // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了
                // 其余数据类型，根据官方文档，完全可以忽略http://poi.apache.org/spreadsheet/eval.html
                if (colIx>minColIx) {
                    sb.append(SEPARATOR);
                }
                if (cellValue == null) {
                    continue;
                }
                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        sb.append(cellValue.getBooleanValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        // 这里的日期类型会被转换为数字类型，需要判别后区分处理
                        if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                            sb.append(cell.getDateCellValue());
                        } else {
                            sb.append(cellValue.getNumberValue());
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        sb.append(cellValue.getStringValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;
                    default:
                        break;
                }
            }
            list.add(sb.toString());
        }
        return list;
    }


    private static QiniuUploadHandler qiniuUploadHandler = new QiniuUploadHandler();

    //http://joymepic.joyme.com/qiniu/gamepictorial/2016/10/20/i1476942178095.jpg
    static Map<String, String> uploadImage(String dir) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles();
        if(files==null){
            return map;
        }
        for (File file : files) {
            String contentType="";
            String extFile="";
            if(file.getAbsolutePath().endsWith("amr")){
                contentType="audio/amr";
                extFile=".amr";
            }else if(file.getAbsolutePath().endsWith("jpg")){
                contentType="image/jpeg";
                extFile=".jpg";
            }
            String s = qiniuUploadHandler.upload("/gamepictorial/" + DateUtil.formatDateToString(new Date(), "yyyy/MM/dd") + "/" + MD5Util.Md5(UUID.randomUUID().toString())+extFile, BucketInfo.BUCKET_INFO_JOYMEPIC_QN.getBucket(), file.getAbsolutePath(), contentType, 0);
           if(file.getName().indexOf(".")>0){
               map.put(file.getName().substring(0,file.getName().indexOf(".")), "http://joymepic.joyme.com/"+s);
           }else{
               map.put(file.getName(),"http://joymepic.joyme.com/"+s);
           }
        }

        return map;
    }
}
