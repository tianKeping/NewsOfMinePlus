package com.dhunt.yb.frag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhunt.yb.R;


/**
 * @author illidantao
 * @date 2016/5/19 17:36
 */
public class MessageDialogFragment extends BaseDialogFragment{

    public static void show(FragmentActivity fragmentActivity, String message){
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle b = new Bundle();
        b.putString("msg",message);
        fragment.setArguments(b);
        fragment.show(fragmentActivity.getSupportFragmentManager(),"MessageDialogFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_message_dialog,null);
        Bundle b = getArguments();
        final String content = b.getString("msg");
        TextView tvContent = (TextView) view.findViewById(R.id.tv_msg);
        tvContent.setText(content);
        TextView tvBtn = (TextView) view.findViewById(R.id.btn_ok);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                notifyActivity(content,false);
            }
        });
        return view;
    }
}
