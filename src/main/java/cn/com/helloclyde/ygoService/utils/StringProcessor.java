package cn.com.helloclyde.ygoService.utils;

import java.util.List;

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

    public static String join(String[] strings, String sp) {
        if (strings.length == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strings.length - 1; i++) {
            stringBuffer.append(strings[i]);
            stringBuffer.append(sp);
        }
        stringBuffer.append(strings[strings.length - 1]);
        return stringBuffer.toString();
    }

    public static <T> String join(List<T> list, String sp) {
        if (list.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size() - 1; i++) {
            stringBuffer.append(list.get(i));
            stringBuffer.append(sp);
        }
        stringBuffer.append(list.get(list.size() - 1));
        return stringBuffer.toString();
    }

}
