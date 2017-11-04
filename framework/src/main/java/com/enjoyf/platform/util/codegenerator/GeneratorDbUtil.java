package com.enjoyf.platform.util.codegenerator;

import com.enjoyf.platform.service.ask.BlackListHistory;
import com.enjoyf.platform.service.ask.UserFollowGame;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.usercenter.Verify;
import com.enjoyf.platform.util.FileUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ericliu on 16/3/7.
 */
public class GeneratorDbUtil {


    private static Set<String> UPER_ARRAY = new HashSet<String>();

    static {
        UPER_ARRAY.add("A");
        UPER_ARRAY.add("B");
        UPER_ARRAY.add("C");
        UPER_ARRAY.add("D");
        UPER_ARRAY.add("E");
        UPER_ARRAY.add("F");
        UPER_ARRAY.add("G");
        UPER_ARRAY.add("H");
        UPER_ARRAY.add("I");
        UPER_ARRAY.add("J");
        UPER_ARRAY.add("K");
        UPER_ARRAY.add("L");
        UPER_ARRAY.add("M");
        UPER_ARRAY.add("N");
        UPER_ARRAY.add("O");
        UPER_ARRAY.add("P");
        UPER_ARRAY.add("Q");
        UPER_ARRAY.add("R");
        UPER_ARRAY.add("S");
        UPER_ARRAY.add("T");
        UPER_ARRAY.add("U");
        UPER_ARRAY.add("V");
        UPER_ARRAY.add("W");
        UPER_ARRAY.add("X");
        UPER_ARRAY.add("Y");
        UPER_ARRAY.add("Z");
    }

    public static void main(String[] args) throws FileNotFoundException {

        String outPutDir = "c:/Users/pengxu.EF/Desktop";

        if (!FileUtil.isFileOrDirExist(outPutDir)) {
            FileUtil.createDirectory(outPutDir);
        }

        Class objectClass = BlackListHistory.class;

        System.out.println("========DB Field ==========");
        System.out.println(getDBFieldClass(objectClass, outPutDir));
        System.out.println("========interface ==========");
        System.out.println(getInterfaceAccessor(objectClass, outPutDir));
        System.out.println("========abstract ==========");
        System.out.println(getAbstactAccessor(objectClass, outPutDir));
        System.out.println("========mysql ==========");
        System.out.println(getAccessor("MySql", objectClass, outPutDir));
        System.out.println("========sqlserver ==========");
        System.out.println(getAccessor("SqlServer", objectClass, outPutDir));
        System.out.println("========oracle ==========");
        System.out.println(getAccessor("Oracle", objectClass, outPutDir));
    }

    public static void writeFile(File file, String content) {
        if (file.exists()) {
            file.delete();
        }

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAccessor(String sqlType, Class objectClass, String outputDir) {
        String calssName = objectClass.getSimpleName();

        String template = replaceClassName(Template.TEMPLATE_ACCESSOR, calssName);
        template = replaceSqlType(template, sqlType);

        writeFile(new File(outputDir + "/" + calssName + "Accessor" + sqlType + ".java"), template);
        return template;
    }


    public static String getInterfaceAccessor(Class objectClass, String outputDir) {
        String calssName = objectClass.getSimpleName();
        String objectName = getTuofengFiledString(calssName);

        String template = replaceClassName(Template.TEMPLATE_INTERFACE_ACCESSOR, calssName);
        template = replaceObjectName(template, objectName);

        writeFile(new File(outputDir + "/" + calssName + "Accessor.java"), template);
        return template;
    }

    public static String getAbstactAccessor(Class objectClass, String outputDir) {
        Map<String, FieldEntry> filedMap = new LinkedHashMap<String, FieldEntry>();
        String calssName = objectClass.getSimpleName();
        String tableName = getFiledSqlString(calssName);
        String objectName = getTuofengFiledString(calssName);

        for (Field field : objectClass.getDeclaredFields()) {
            filedMap.put(field.getName(), new FieldEntry(field, getSetFiledString(field.getName()), getFiledSqlString(field.getName()), getSqlType(field)));
        }

        String template = replaceClassName(Template.TEMPLATE_ABSTRACT_ACCESSOR, calssName);
        template = replaceObjectName(template, objectName);
        template = replaceTableName(template, tableName);
        template = replaceInsert(template, objectName, filedMap);
        template = replaceRsToObject(template, filedMap);
        writeFile(new File(outputDir + "/Abstract" + calssName + "Accessor.java"), template);
        return template;
    }

    public static String getDBFieldClass(Class objectClass, String outputDir) {
        Map<String, FieldEntry> filedMap = new LinkedHashMap<String, FieldEntry>();
        String calssName = objectClass.getSimpleName();
        for (Field field : objectClass.getDeclaredFields()) {
            filedMap.put(field.getName(), new FieldEntry(field, getSetFiledString(field.getName()), getFiledSqlString(field.getName()), getSqlType(field)));
        }

        String template = replaceClassName(Template.TEMPLATE_DB_FIELD, calssName);

        StringBuffer replaceString = new StringBuffer();
        for (Map.Entry<String, FieldEntry> entry : filedMap.entrySet()) {
            replaceString.append("public static final " + calssName + "Field " + entry.getKey().toUpperCase() + " = new " + calssName + "Field(\"" + entry.getValue().getSqlFiledName() + "\"" + ", ObjectFieldDBType." + entry.getValue().getSqlFiledType().toUpperCase() + ");\n");
        }
        template = template.replace("[joyme-fieldlist]", replaceString.toString());
        writeFile(new File(outputDir + "/" + calssName + "Field.java"), template);
        return template;

    }


    private static String getSqlType(Field field) {
        if (field.getType().equals(Boolean.class)) {
            return "Boolean";
        } else if (field.getType().equals(String.class)) {
            return "String";
        } else if (field.getType().equals(long.class)) {
            return "Long";
        } else if (field.getType().equals(int.class)) {
            return "Int";
        } else if (field.getType().equals(Date.class)) {
            return "Timestamp";
        } else {
            return "Object";
        }
    }

    private static String replaceClassName(String template, String className) {

        template = template.replace("[joyme-className]", className);

        return template;
    }

    private static String replaceSqlType(String template, String sqlType) {

        template = template.replace("[joyme-sqlType]", sqlType);

        return template;
    }

    private static String replaceTableName(String template, String className) {

        template = template.replace("[joyme-talbeName]", className);

        return template;
    }

    private static String replaceObjectName(String template, String objectName) {

        template = template.replace("[joyme-objcet]", objectName);

        return template;
    }

    private static String replaceRsToObject(String template, Map<String, FieldEntry> fileEntryMap) {

        StringBuffer replaceString = new StringBuffer();
        for (Map.Entry<String, FieldEntry> entry : fileEntryMap.entrySet()) {
            if (entry.getValue().getSqlFiledType().equals("Timestamp")) {
                replaceString.append("returnObject.set" + entry.getValue().getSetFiledName() + "(new Date(rs.get" + entry.getValue().getSqlFiledType() + "(\"" + entry.getValue().getSqlFiledName() + "\").getTime()));\n");
            } else {
                replaceString.append("returnObject.set" + entry.getValue().getSetFiledName() + "(rs.get" + entry.getValue().getSqlFiledType() + "(\"" + entry.getValue().getSqlFiledName() + "\"));\n");
            }
        }
        template = template.replace("[joyme-rstoobject]", replaceString.toString());

        return template;
    }

    private static String replaceInsert(String template, String object, Map<String, FieldEntry> fileEntryMap) {

        StringBuffer replaceString = new StringBuffer();


        int i = 1;
        for (Map.Entry<String, FieldEntry> entry : fileEntryMap.entrySet()) {
            if (entry.getValue().getSqlFiledType().equals("Timestamp")) {
                replaceString.append("pstmt.set" + entry.getValue().getSqlFiledType() + "(" + i + ", new " + entry.getValue().getSqlFiledType() + "(" + object + ".get" + entry.getValue().getSetFiledName() + "().getTime()));\n");
            } else if (entry.getValue().getSqlFiledType().equals("Objcct")) {
                replaceString.append("pstmt.set" + entry.getValue().getSqlFiledType() + "(" + i + ", " + object + ".get" + entry.getValue().getSetFiledName() + "().getCode());\n");
            } else {
                replaceString.append("pstmt.set" + entry.getValue().getSqlFiledType() + "(" + i + ", " + object + ".get" + entry.getValue().getSetFiledName() + "());\n");
            }


            i++;
        }
        template = template.replace("[joyme-insert]", replaceString.toString());

        StringBuffer prefix = new StringBuffer();

        StringBuffer suffix = new StringBuffer();

        i = 0;
        for (Map.Entry<String, FieldEntry> entry : fileEntryMap.entrySet()) {
            prefix.append(entry.getValue().getSqlFiledName());
            suffix.append("?");

            if (i < fileEntryMap.size() - 1) {
                prefix.append(",");
                suffix.append(",");
            }
            i++;
        }
        String insertSql = "(" + prefix.toString() + ") VALUES " + "(" + suffix.toString() + ")";
//        suffix.append("(").append(suffix).append(")");


        template = template.replace("[joyme-replaceInsertsql]", insertSql);
        return template;
    }

    private static String getFiledSqlString(String field) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] filedArray = field.split("|");

        int i = 0;
        for (String fc : filedArray) {
            if (UPER_ARRAY.contains(fc) && i != 1) {
                stringBuffer.append("_");
            }
            stringBuffer.append(fc.toLowerCase());
            i++;
        }
        return stringBuffer.toString();
    }

    private static String getSetFiledString(String field) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] filedArray = field.split("|");

        int i = 0;
        for (String fc : filedArray) {
            if (i == 1) {
                stringBuffer.append(fc.toUpperCase());
            } else {
                stringBuffer.append(fc);
            }
            i++;
        }
        return stringBuffer.toString();
    }

    private static String getTuofengFiledString(String field) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] filedArray = field.split("|");

        int i = 0;
        for (String fc : filedArray) {
            if (i == 1) {
                stringBuffer.append(fc.toLowerCase());
            } else {
                stringBuffer.append(fc);
            }
            i++;
        }
        return stringBuffer.toString();
    }
}
