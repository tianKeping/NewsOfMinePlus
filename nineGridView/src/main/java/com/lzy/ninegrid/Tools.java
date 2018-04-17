package com.lzy.ninegrid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.ClipboardManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Tools {


    /**
     * 时间戳
     */
    public static String getStandardDate(String timeStr) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年
        StringBuffer stringBuffer = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d = null;
        try {
            d = sdf.parse(timeStr);
            Log.i("DateUtil", "testDate: " + d.getTime());
            long diff = new Date().getTime() - d.getTime();
            Log.i("DateUtil", "testDate: " + d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = new Date().getTime() - d.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            stringBuffer.append(r + "").append("年前");
            Log.i("DateUtil", "testDate: " + r + "年前");
        } else if (diff > month) {
            r = (diff / month);
            stringBuffer.append(r + "").append("个月前");
            Log.i("DateUtil", "testDate: " + r + "个月前");
        } else if (diff > day) {
            r = (diff / day);
            stringBuffer.append(r + "").append("天前");
            Log.i("DateUtil", "testDate: " + r + "天前");
        } else if (diff > hour) {
            r = (diff / hour);
            stringBuffer.append(r + "").append("个小时前");
            Log.i("DateUtil", "testDate: " + r + "个小时前");
        } else if (diff > minute) {
            r = (diff / minute);
            stringBuffer.append(r + "").append("分钟前");
            Log.i("DateUtil", "testDate: " + r + "分钟前");
        } else {
            stringBuffer.append("刚刚");
            Log.i("DateUtil", "testDate: 刚刚");
        }
        return stringBuffer.substring(0, stringBuffer.length());
    }

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态==="
                            + networkInfo[i].getState());
                    System.out.println(i + "===类型==="
                            + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 重新接受listview的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 重新接受gridview的高度
     *
     * @param gridView
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz
                    .getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static String getJson(Map<String, Object> paramMap) {

        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    /**
     * 加入QQ群聊
     *
     * @param activity
     * @param key
     * @return
     */
    public static boolean joinQQGroup(Activity activity, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    /**
     * 获取屏幕的宽度
     */

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        if (dp == 0) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public static void doSendSMSTo(final Activity activity, String phoneNumber,
                                   String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + phoneNumber));
            intent.putExtra("sms_body", message);
            activity.startActivity(intent);
        }
    }

    /**
     * 毫秒转时间格式
     */
    public static String toTime(String time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time));
        return formatter.format(calendar.getTime());
    }


    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static byte[] bitmaptobytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * 视频中取图片
     *
     * @param activity
     * @param uri
     * @return
     */
    public static Bitmap getVideoThumbnail(Activity activity, Uri uri) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(activity, uri);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 计算两个经纬度之间的距离
     * 返回单位是米
     */
    // private final double EARTH_RADIUS = 6378137.0;
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap
    {
        int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;         // 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应 bitmap
        Canvas canvas = new Canvas(bitmap);         // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);      // 把 drawable 内容画到画布中
        return bitmap;
    }

    /**
     * 信任所有主机-对于任何证书都不做检查
     */
    @SuppressLint("TrulyRandom")
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android 采用X509的证书信息机制
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }


            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                           String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }


            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                           String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }
        }};


        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置当前页面亮度
     *
     * @param context
     * @param brightness
     */
    public static void setLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 获取系统亮度
     * 取值在(0 -- 255)之间
     */
    public static int getSystemScreenBrightness(Context context) {
        int values = 0;
        try {
            values = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 设置系统亮度
     *
     * @param systemBrightness 返回的亮度值是处于0-255之间的整型数值
     */
    public static boolean setSystemScreenBrightness(Context context, int systemBrightness) {
        return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, systemBrightness);
    }

    /**
     * 关闭系统自动调节亮度
     */
    public static void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 打开系统自动调节亮度
     */
    public static void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
     */
    public static int getScreenMode(Activity activity) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param strText
     * @return
     */
    public static String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * 字符串 SHA 加密
     *
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String imgPath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 如果该值设为true那么将不返回实际的bitmap对象，不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        BitmapFactory.decodeFile(imgPath, options); // 解码文件
        // 计算缩放值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 设置好缩放值之后，inJustDecodeBounds设为false，就可以根据已经得到的缩放比例得到自己想要的图片缩放图
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Textview 跑马灯效果
     *
     * @param textView
     */
    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }


    /**
     * 获取TextView高度
     *
     * @param Context
     * @param text
     * @param textSize
     * @return
     */

    public static float getTextWidth(Context Context, String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = Context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }

    static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
