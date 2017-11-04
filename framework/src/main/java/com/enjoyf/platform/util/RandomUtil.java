package com.enjoyf.platform.util;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 下午3:28
 * Description:
 */
public class RandomUtil {

    public static int getRandomInt(int base) {
        return (int) (Math.random() * base);
    }

    /**
     * 随机从一个list的下标处选择后面resultSize个元素，
     * 如果超过该list的范围会从第一个开始取，
     * 如果不足resultSize个元素不会重复放入list中
     * @param list
     * @param resultSize
     * @param <T>
     * @return
     */
    public static <T> List<T> getRandomByList(List<T> list, int resultSize) {
        List<T> returnList = new ArrayList<T>();

        int taoIdx = 0;
        Map<Integer, T> map = new LinkedHashMap<Integer, T>();
        int i = RandomUtil.getRandomInt(list.size());
        int end = i + resultSize;
        for (; i < end; i++) {
            if (i <= list.size() - 1) {
                map.put(i, list.get(i));
            } else {
                if (map.containsKey(taoIdx)) {
                    continue;
                }
                map.put(taoIdx, list.get(taoIdx));
                taoIdx++;
            }
        }

        returnList.addAll(map.values());
        return returnList;
    }

	/**
	 * 随机指定范围内N个不重复的数
	 * 最简单最基本的方法
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n   随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

    public static String getRandomUUID(int resultSize) {
        char[] uuidString= UUID.randomUUID().toString().replace("-","0").replace("_","0").toCharArray();

        char[] res=new char[resultSize];
        for(int i=0;i<res.length;i++){
          int index=getRandomInt(uuidString.length-1);

          res[i]=uuidString[index];
        }


        return new String(res);
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.getRandomUUID(32).length());
    }
}
