package kkkj.android.revgoods.utils;

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
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static final byte[] hex21byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    public static byte[] hex2byte(String inputString) throws IllegalArgumentException {
        if (inputString.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
//        if (inputString == null || inputString.length() < 2) {
//            return new byte[0];
//        }
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }



    public static String toHexStringForLog(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                String tempHexStr = Integer.toHexString(data[i] & 0xff) + " ";
                tempHexStr = tempHexStr.length() == 2 ? "0" + tempHexStr : tempHexStr;
                sb.append(tempHexStr);
            }
        }
        return sb.toString();
    }

}
