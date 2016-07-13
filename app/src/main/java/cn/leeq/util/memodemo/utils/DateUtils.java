package cn.leeq.util.memodemo.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LeeQ on 2016-6-16.
 */
public class DateUtils {

    private static Date stringToDate(String strTime, String formetType) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formetType);
        return format.parse(strTime);
    }


    private static long stringToLong(String strTime, String formatType) throws ParseException {
        Date date = stringToDate(strTime, formatType);
        if (date == null) {
            return 0;
        }else{
            long currentTime = dateToLong(date);
            return currentTime;
        }
    }

    private static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String convertTimeToFormat(String strTime) throws ParseException {
        long timeStamp = stringToLong(strTime, "yy-MM-dd hh:mm:ss SSS");

        long cTime = System.currentTimeMillis() / 1000;
        long time = cTime - timeStamp/1000;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time > 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 4) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 4) {
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date(strTime.substring(0,strTime.indexOf(" "))));
            Log.e("dt", "DateUtil " + format);*/
            // 2016-12-20 12:30:01 795
            String a = "";
            try {
                a = strTime.substring(strTime.indexOf("-") + 1, strTime.indexOf(":") + 3);
                //Log.e("test", "Date " + a + " strTime " + strTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return a;
        } else {
            return "刚刚";
        }
    }


}
