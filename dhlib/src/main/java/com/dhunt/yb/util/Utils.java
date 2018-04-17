package com.dhunt.yb.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

/**
 * @author illidantao
 * @date 2016/5/19 17:19
 */
public class Utils {


    private static void hideSoftInput(Activity actv){
        if(actv.getCurrentFocus()!=null && actv.getCurrentFocus().getWindowToken()!=null) {
            InputMethodManager manager = (InputMethodManager) actv.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(actv.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void clearAllFragments(FragmentActivity activity){
        showFragmentInActivity(null,null,activity);
    }

    public static boolean showFragmentInActivity(Fragment frag, String tag, FragmentActivity actv) {
        if (actv == null || actv.getSupportFragmentManager() == null) {
            return false;
        }
        FragmentTransaction transaction = actv.getSupportFragmentManager().beginTransaction();
        if (transaction == null) {
            return false;
        }
        List<Fragment> fragments = actv.getSupportFragmentManager().getFragments();
        for (int i = 0; fragments != null && i < fragments.size(); i++) {
            if (fragments.get(i) != null) {
                transaction.remove(fragments.get(i));
            }
        }
        if(frag != null){
            transaction.add(frag,tag);
        }
        transaction.commitAllowingStateLoss();
        return true;
    }

    public static File getExDir(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return Environment.getExternalStorageDirectory();
        }else{
            return Environment.getDownloadCacheDirectory();
        }
    }

}
