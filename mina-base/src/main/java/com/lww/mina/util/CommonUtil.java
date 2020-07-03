package com.lww.mina.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lww
 * @date 2020-06-11 23:21
 */
public class CommonUtil {

    private CommonUtil() {
    }

    public static String getNowTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}
