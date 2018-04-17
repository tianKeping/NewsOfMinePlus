package com.dhunt.yb.acti;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.dhunt.yb.frag.OnMyDialogListener;

public class BaseActivity extends FragmentActivity implements OnMyDialogListener {

    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        showProgressDialog(null);
    }
    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(cancelable,null);
    }
    public void showProgressDialog(String msg) {
        showProgressDialog(true,msg);
    }
    public void showProgressDialog(boolean cancelable,String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            if (!TextUtils.isEmpty(msg)){
                progressDialog.setMessage(msg);
            }
            progressDialog.setCancelable(cancelable);
            progressDialog.show();
        }
    }
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public <T extends View> T fd(int id) {
        return (T) findViewById(id);
    }

    private boolean isShowKeyBoard = false;

    void showSoftInput(final View view) {
        if (view == null) {
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isShowKeyBoard) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    return;
                }
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                isShowKeyBoard = manager.showSoftInput(view, 0);
            }
        });
    }


    @Override
    public void onDialogClick(String key, boolean isLeft) {

    }
}
