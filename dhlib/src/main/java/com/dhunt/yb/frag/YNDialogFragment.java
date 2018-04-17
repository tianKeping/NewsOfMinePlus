package com.dhunt.yb.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhunt.yb.R;

/**
 */
public class YNDialogFragment extends BaseDialogFragment {

    public static void show(FragmentActivity fragmentActivity,String message,String bizKey,String left,String right){
        YNDialogFragment fragment = new YNDialogFragment();
        Bundle b = new Bundle();
        b.putString("msg",message);
        if(!TextUtils.isEmpty(left)){
            b.putString("left",left);
        }
        if(!TextUtils.isEmpty(right)){
            b.putString("right",right);
        }
        b.putString("bizkey",bizKey);
        fragment.setArguments(b);
        fragment.show(fragmentActivity.getSupportFragmentManager(),"YNDialogFragment");
    }

    String bizKey = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_yn_dialog,null);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_msg);
        TextView tvLeft = (TextView) view.findViewById(R.id.btn_l);
        TextView tvR = (TextView) view.findViewById(R.id.btn_r);
        Bundle b = getArguments();
        if(b != null){
            String content = b.getString("msg");
            tvContent.setText(content);
            tvLeft.setText(b.getString("left","确定"));
            tvR.setText(b.getString("right","取消"));
            bizKey = b.getString("bizkey","");
        }
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                notifyActivity(bizKey,true);
            }
        });
        tvR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                notifyActivity(bizKey,false);
            }
        });
        return view;
    }
}
