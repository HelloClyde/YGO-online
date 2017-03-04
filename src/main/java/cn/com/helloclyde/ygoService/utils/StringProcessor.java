package cn.com.helloclyde.ygoService.utils;

/**
 * Created by HelloClyde on 2017/2/9.
 */
public class StringProcessor {
    public static String getFileExt(String fileName) {
        int pointPos = -1;
        for (int idx = fileName.length() - 1; idx >= 0; idx--) {
            if (fileName.charAt(idx) == '.') {
                pointPos = idx;
                break;
            }
        }
        if (pointPos != -1) {
            return fileName.substring(pointPos, fileName.length());
        } else {
            return "";
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < byteArray.length - 1) {
                hs = hs + "";
            }
        }
        return hs;
    }
}
