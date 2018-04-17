package com.dhunt.yb.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author illidantao
 * @date 2016/5/19 17:22
 */
public class ToastUtil {

    public static void show(Context ctx, String str){
        if (ctx != null) {
            Toast.makeText(ctx.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

}
