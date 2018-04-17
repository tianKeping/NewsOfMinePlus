package com.dhunt.yb.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author illidantao
 * @date 2016/5/22 20:51
 */
public class TimerViewPager extends ViewPager{

    private int currentItem = 0;

    /**
     * 请求更新显示的View。
     */
    protected static final int MSG_UPDATE_IMAGE  = 1;

    private int delay  = 3000;

    public TimerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void start(){
        if(getAdapter() != null && getAdapter().getCount() > 0){
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,delay);
        }
    }

    public void stop(){
        handler.removeCallbacksAndMessages(null);
    }

    public void restart(){
        if(getAdapter() != null && getAdapter().getCount() > 0){
            handler.removeMessages(MSG_UPDATE_IMAGE);
            handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,delay);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_UPDATE_IMAGE){
                setCurrentItem(getCurrentItem()+1);
                handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE,delay);
            }
        }
    };
}
