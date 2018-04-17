package com.example.keping.newsofmine.utils;


import android.content.Context;
import android.widget.Toast;


public class MyToast extends Toast {
	public MyToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/** 之前显示的内容 */
	private static String oldMsg ;
	/** Toast对象 */
	private static Toast toast = null ;
	/** 第一次时间 */
	private static long oneTime = 0 ;
	/** 第二次时间 */
	private static long twoTime = 0 ;

	/**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
	public static void makeText(Context context, String message){
		if(toast == null){
			try{
				toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
				toast.show();
			}catch (Exception e){
				e.printStackTrace();

			}

			oneTime = System.currentTimeMillis() ;
		}else{
			twoTime = System.currentTimeMillis() ;
			if(message.equals(oldMsg)){
				if(twoTime - oneTime > Toast.LENGTH_SHORT){
					toast.show() ;
				}
			}else{
				oldMsg = message ;
				toast.setText(message) ;
				toast.show() ;
			}
		}
		oneTime = twoTime ;
	}

}
