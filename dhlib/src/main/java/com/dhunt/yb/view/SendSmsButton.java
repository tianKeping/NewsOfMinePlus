package com.dhunt.yb.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * @author illidantao
 * @date 2016/5/21 13:53
 */
public class SendSmsButton extends Button implements View.OnClickListener {

    public boolean isClick = false;
    private ISendSmsListener listener;

    //该button内部实现了点击事件，点击后开始计数并置为disable，外界可以实现该接口，自行处理按下按钮后的事件
    public interface ISendSmsListener {
        void sendSms();
    }

    public SendSmsButton(Context context) {
        super(context);
        init();
    }

    public SendSmsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setText("发送验证码");
        setOnClickListener(this);
    }

    public void setListener(ISendSmsListener listener) {
        this.listener = listener;
    }

    //用户可以不点击，自动发送
    public void sendSms(boolean isTrueSend) {
        isClick = true;
        setEnabled(false);
        countDownTimer.start();
        if (listener != null && isTrueSend) {
            listener.sendSms();
        }
    }

    @Override
    public void onClick(View v) {
        sendSms(true);
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            setText("已发送(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            setEnabled(true);
            setText("发送验证码");
        }
    };

    public void reSet() {
        countDownTimer.cancel();
        setEnabled(true);
        setText("发送验证码");
    }

}
