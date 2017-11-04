package com.enjoyf.test;

import com.enjoyf.platform.service.service.ServiceException;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhimingli on 2015/9/10 0010.
 */
public class ReadLogCase {
    public static void main(String[] args) throws ServiceException {
        //ReadLogCase.readFileByLines("D:\\pclog.log.20150909");
        //User user = PcstatServiceSngl.get().getPcstatUserById(1L);
        //  System.out.println(user);
    }

    private static Pattern pattern = Pattern.compile("\\{.*\\}");

    private static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            String tempString = null;
            String matchStr = null;
            while ((tempString = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(tempString);
                if (matcher.find()) {
                    matchStr = matcher.group(0);
                    JSONObject jsonObject = JSONObject.fromObject(matchStr);
                    System.out.println(jsonObject.get("commonParams"));
                }
            }
        } catch (Exception e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
