package com.dhunt.yb.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import com.dhunt.yb.R;


/**
 * @author illidantao
 * @date 2016/5/19 20:00
 */
public class BaseDialogFragment extends DialogFragment{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_MsgDialog);
    }

    public void notifyActivity(String key,boolean isLeft){
        try{
            ((OnMyDialogListener)getActivity()).onDialogClick(key,isLeft);
        }catch (Exception e){

        }
    }

}
