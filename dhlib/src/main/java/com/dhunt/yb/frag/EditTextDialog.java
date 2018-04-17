package com.dhunt.yb.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dhunt.yb.R;


/**
 * @author illidantao
 * @date 2016/6/25 0:29
 */
public class EditTextDialog extends BaseDialogFragment {

    public static void show(FragmentActivity fragmentActivity, String hint,String key){
        EditTextDialog fragment = new EditTextDialog();
        Bundle b = new Bundle();
        b.putString("hint",hint);
        b.putString("key",key);
        fragment.setArguments(b);
        fragment.show(fragmentActivity.getSupportFragmentManager(),"EditTextDialog");
    }

    public static void show(FragmentActivity fragmentActivity, String hint,String key,boolean isMoneyInput){
        EditTextDialog fragment = new EditTextDialog();
        Bundle b = new Bundle();
        b.putString("hint",hint);
        b.putString("key",key);
        b.putBoolean("isMoneyInput",isMoneyInput);
        fragment.setArguments(b);
        fragment.show(fragmentActivity.getSupportFragmentManager(),"EditTextDialog");
    }

    EditText editText;
    String key;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_edit_dialog,null);
        editText = (EditText) view.findViewById(R.id.et_msg);
        TextView tvTip = (TextView) view.findViewById(R.id.tv_edt_tips);
        Bundle bundle = getArguments();
        if(bundle != null){
            editText.setHint(bundle.getString("hint","请输入"));
            tvTip.setText(bundle.getString("hint","请输入"));
            key = bundle.getString("key","EditTextDialog");
            if(bundle.getBoolean("isMoneyInput",false)){
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                notifyActivity(key+editText.getText().toString(),false);
            }
        });
        return view;
    }
}
