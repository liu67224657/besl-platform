/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-19 下午8:00
 * Description:
 */
public class PinYinUtil {
    // 国标码和区位码转换常量
    static final int GB_SP_DIFF = 160;
    static final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    //存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {
            1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
            3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,
            4390, 4558, 4684, 4925, 5249, 5600
    };

    //存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'w', 'x', 'y', 'z'
    };

    //获取一个字符串的拼音码
    public static String getAllFirstLetter(final String originalStr) {
        StringBuffer returnValue = new StringBuffer();

        //
        String str = originalStr.toLowerCase();

        //
        char ch;
        char[] temp;

        //依次处理str中每个字符
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            temp = new char[]{ch};

            byte[] uniCode = new byte[0];
            try {
                uniCode = new String(temp).getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //非汉字
            if (uniCode[0] < 128 && uniCode[0] > 0) {
                returnValue.append(temp);
            } else {
                returnValue.append(convert(uniCode));
            }
        }

        return returnValue.toString();
    }

    //获取一个字符串的拼音码
    public static String getFirstWordLetter(final String originalStr) {
        String returnValue = null;

        //
        char ch = originalStr.toLowerCase().charAt(0);

        //
        char[] temp = new char[]{ch};

        byte[] uniCode = new byte[0];
        try {
            uniCode = new String(temp).getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //非汉字
        if (uniCode[0] < 128 && uniCode[0] > 0) {
            returnValue = String.valueOf(temp);
        } else {
            returnValue = String.valueOf(convert(uniCode));
        }

        return returnValue;
    }

    /**
     * 获取一个汉字的拼音首字母。
     * GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';

        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }

        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }

        return result;
    }

    public static String getAllLetters(String originalStr){
        String pinyinStr = "";
        try {
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pinyinStr = PinyinHelper.toHanyuPinyinString(originalStr,format,"");

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return pinyinStr;
    }

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            System.out.println(PinYinUtil.getAllFirstLetter(args[0]));
//            System.out.println(PinYinUtil.getFirstWordLetter(args[0]));
//        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        try {
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            System.out.println(PinyinHelper.toHanyuPinyinString("明日への扉",format,""));

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(System.currentTimeMillis() - t);
    }
}
