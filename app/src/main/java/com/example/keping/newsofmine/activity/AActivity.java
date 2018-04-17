package com.example.keping.newsofmine.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.dhunt.yb.acti.BaseActivity;
import com.example.keping.newsofmine.utils.MySharedPreference;
import com.example.keping.newsofmine.utils.MyToast;
import com.example.keping.newsofmine.utils.NetBroadcastReceiver;
import com.example.keping.newsofmine.utils.NetUtil;
import com.example.keping.newsofmine.utils.RequestPer;
import com.example.keping.newsofmine.utils.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AActivity extends BaseActivity implements NetBroadcastReceiver.NetEvent {

    public AActivity activity = this;
    Unbinder unbinder;

    /**
     * 判断是否可以右划边缘退出
     */
    public boolean isCanSlideClose = true;

    public static NetBroadcastReceiver.NetEvent event;
    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);
        if (isCanSlideClose) {
            SwipeBackHelper.onCreate(this);
            SwipeBackHelper.getCurrentPage(this)
                    .setSwipeBackEnable(true)
                    .setSwipeEdgePercent(0.2f)
                    .setSwipeSensitivity(0.5f)
                    .setSwipeRelateEnable(true)
                    .setSwipeRelateOffset(200);
        }
        unbinder = ButterKnife.bind(this);

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        StatusBarUtil.StatusBarLightMode(activity);
        event = this;

        inspectNet();

    }


    public abstract void init();

    public abstract int setLayout();

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void Onclick(View view) {
        finish();
    }


    public void showSoftInput(final View view) {
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
                if (manager != null) {
                    isShowKeyBoard = manager.showSoftInput(view, 0);
                }
            }
        });
    }

    public void hideSoftInput() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                isShowKeyBoard = !manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    boolean isShowKeyBoard;

    /**
     * 以下方法为右划退出activity设置的生命周期
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isCanSlideClose) SwipeBackHelper.onPostCreate(this);
    }

    /**
     * 以下方法为右划退出activity设置的生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCanSlideClose) SwipeBackHelper.onDestroy(this);
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }

    }

    /**
     * 显示Toast
     */
    public void showToast(String message) {
        MyToast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    //改变状态栏颜色
    public void setToolBar_Type(int num, int color, boolean texttype) {
        Window window = activity.getWindow();
        switch (num) {
            case 1://添加状态栏颜色，隐藏系统知道的
                ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                View statusBarView = new View(window.getContext());
                int statusBarHeight = getStatusBarHeight(window.getContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
                params.gravity = Gravity.TOP;
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(getResources().getColor(color));
                decorViewGroup.addView(statusBarView);
                break;
            case 2:
//                当5.0>SDK>=4.4的时候，可以给startBar 设置透明 来保持和Toolbar一样的背景，但是无法填充颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                break;
            case 3:
//                当6.0>SDK>5.0的时候，可以去给startBar填充颜色（当填充的颜色接近白色的时候，回到是状态栏上的内容看不见,解决方法在6.0才有）
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().setStatusBarColor(getResources().getColor(color));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        View decor = getWindow().getDecorView();
                        int ui = decor.getSystemUiVisibility();
                        if (texttype) {
                            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //黑字
                        } else {
                            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//白字
                        }
                        decor.setSystemUiVisibility(ui);
                    }
                }
                break;
        }
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(activity);
        return isNetConnect();
        // if (netMobile == 1) {
        // System.out.println("inspectNet：连接wifi");
        // } else if (netMobile == 0) {
        // System.out.println("inspectNet:连接移动数据");
        // } else if (netMobile == -1) {
        // System.out.println("inspectNet:当前没有网络");
        //
        // }
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        String last_wifi_type = MySharedPreference.get("wifi_type", "", activity);

        if (netMobile == -1) {
            //断开网络
            MyToast.makeText(activity, "请检查网络!");
            MySharedPreference.save("wifi_type", "" + netMobile, activity);

        } else {

            MySharedPreference.save("wifi_type", "" + netMobile, activity);

        }
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }

    RequestPer requestPer;

    public void setRequestPer(RequestPer requestPer) {
        this.requestPer = requestPer;
    }

    final int requestCodeNum = 111111;

    public void RequestPermission(String permissionsArray[]) {
        AndPermission.with(this)
                .requestCode(requestCodeNum)
                .permission(permissionsArray)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(activity, rationale).show();
                        if (requestPer != null) {
                            requestPer.isPermission(false);
                        }
                    }
                })
                .start();
        return;
    }

    /**
     * 回调监听。
     */
    public PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case requestCodeNum: {
                    // TODO do something.
//                    Toast.makeText(activity, R.string.successfully, Toast.LENGTH_SHORT).show();
                    if (requestPer != null) {
                        requestPer.isPermission(true);
                    }
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case requestCodeNum: {
                    // TODO The strategy after failure.
//                    Toast.makeText(ListenerActivity.this, R.string.failure, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (requestPer != null) {
                requestPer.isPermission(false);
            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(activity, 111)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
            }
        }
    };

}
