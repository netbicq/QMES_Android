package kkkj.android.revgoods.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    //查表法，将16进制转为2进制
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {

            return null;
        }

        String bString = "";
        String tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 比较连续n个重量是否相等,即读数是否稳定
     */
    public static boolean isStable(List<String> weightList,int n) {
        boolean isStable = true;
        int size = weightList.size();
        List<String> list = new ArrayList<>();


        if (size >= n) {
            for (int i=0;i<n;i++) {
                String s = weightList.get(size - (i + 1));
                list.add(s);
            }

            for (int j=0;j<list.size();j++) {

                isStable = isStable && (list.get(j).equals(list.get(j + 1)));
            }

            return isStable;

        } else {

            return false;
        }

    }


}
