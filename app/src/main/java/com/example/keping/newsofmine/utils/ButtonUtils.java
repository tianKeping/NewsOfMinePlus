package com.example.keping.newsofmine.utils;

import android.content.Context;

public class ButtonUtils {
    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;


    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(Context context) {
        return isFastDoubleClick(-1, DIFF, context);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, Context context) {

        return isFastDoubleClick(buttonId, DIFF, context);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff, Context context) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
//            MyToast.makeText(context, "请勿重复点击");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }
}
