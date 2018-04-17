package com.dhunt.yb.async;

/**
 * @author illidantao
 * @date 2016/5/25 17:17
 */
public interface AsyncTaskCallBack {

    /**
     * 当结果返回的时候执行
     * @param jsonString 返回的结果
     */
    public void onPostExecute(String jsonString);

}
