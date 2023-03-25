package com.ken.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dateutil {
    public static String getNow()
    {
        //HH表示用24小时制，如18；hh表示用12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(new Date().getTime());

    }
}
