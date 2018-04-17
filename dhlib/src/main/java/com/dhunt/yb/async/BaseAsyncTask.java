package com.dhunt.yb.async;

import android.os.AsyncTask;
import android.util.Log;

import com.dhunt.yb.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @author illidantao
 * @date 2016/5/25 17:19
 */
public class BaseAsyncTask<Params> extends AsyncTask<Params, Void, String> {

    private AsyncTaskCallBack mLoadDataCallBack;

    public static String URL_DOMAIN = "http://aashenghuo.cn";
//    public static String URL_DOMAIN = "http://192.168.15.58:8080/kadmos";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType IMAGE = MediaType.parse("image/png");

    private String url;
    private Call call;
    private boolean cacheEnable = false;
    private static final Cache cache = new Cache(new File(Utils.getExDir(), "/aalife/caches"), 8 * 1024 * 1024);
    private static final int cache_KEEP_HOUR = 8;//缓存持续时间

    public BaseAsyncTask(AsyncTaskCallBack callBack) {
        mLoadDataCallBack = callBack;
    }

    public BaseAsyncTask url(String url) {
        this.url = URL_DOMAIN + url;
        return this;
    }

    /**
     * 是否开启缓存
     *
     * @param enable
     * @return
     */
    public BaseAsyncTask cache(boolean enable) {
        //cacheEnable = enable;暂时关闭
        return this;
    }

    /**
     * 立即执行，支持并发,execute 是排队执行
     */
    public void executeRight(Params... request) {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }

    /**
     * 取消异步任务，一般在Activity或者Fragment的生命周期结束({@link android.app.Activity#onDestroy() onDestroy()})中调用此方法
     * <p>
     * <b>请调用此方法取消任务，而不是 {@link #cancel(boolean)}}方法</b>
     */
    final public void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    protected String doInBackground(Params... params) {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        if (cacheEnable) {
            okBuilder.cache(cache);
        }
        OkHttpClient client = okBuilder.build();
        Request request = null;
        if (params != null && params.length > 0 && params[0] != null) {
            RequestBody body;
            if (params[0] instanceof RequestBody) {
                body = (RequestBody) params[0];
            } else if (params[0] instanceof byte[]) {
                body = RequestBody.create(IMAGE, (byte[]) params[0]);
            } else if (params[0] instanceof String) {
                body = RequestBody.create(JSON, (String) params[0]);
            } else if (params[0] instanceof JSONObject) {
                body = RequestBody.create(JSON, ((JSONObject) params[0]).toString());
            } else {
                return "HTTP_error:不支持的请求参数类型";
            }
            request = new Request.Builder().url(url).post(body).build();
        } else {
            request = new Request.Builder().url(url).build();
        }
        if (cacheEnable) {
            request = request.newBuilder().cacheControl(new CacheControl.Builder().maxStale(cache_KEEP_HOUR, TimeUnit.HOURS).build()).build();
        } else {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
        }
        if (isCancelled()) {
            return "HTTP_tips:取消请求";
        }
        call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return "HTTP_error:IOException" + e.getMessage();
        }
        if (response != null) {
            if (response.isSuccessful()) {
                try {
                    String body = response.body().string();
                    Log.i("http", url + " http response data:" + body);
                    return body;
                } catch (IOException e) {
                    e.printStackTrace();
                    return "HTTP_error:IOException" + e.getMessage();
                } finally {
                    response.body().close();
                }
            } else if (cacheEnable && response.cacheResponse() != null && response.cacheResponse().isSuccessful()) {
                //检测下缓存是否可以拿来用
                try {
                    return response.cacheResponse().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "HTTP_error_cache:IOException" + e.getMessage();
                } finally {
                    response.body().close();
                }
            }
            return "HTTP_error:" + response.message();
        } else {
            return "HTTP_error:response响应为空";
        }
    }

    @Override
    final protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null || result.startsWith("HTTP_")) {
            result = "{\"code\":\"-100\",\"message\":\"" + result + "\",\"success\":false}";
        }
        if (mLoadDataCallBack != null) {
            try {
                mLoadDataCallBack.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
