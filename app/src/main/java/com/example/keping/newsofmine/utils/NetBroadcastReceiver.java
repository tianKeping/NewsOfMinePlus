package com.example.keping.newsofmine.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.example.keping.newsofmine.activity.AActivity;


public class NetBroadcastReceiver extends BroadcastReceiver {
    /**
     * 自定义检查手机网络状态是否切换的广播接受器
     */

    public NetEvent event = AActivity.event;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if (event != null) {
                event.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvent {
        public void onNetChange(int netMobile);
    }
}
