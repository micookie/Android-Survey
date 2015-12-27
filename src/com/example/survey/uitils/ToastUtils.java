package com.example.survey.uitils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * 
 */
public class ToastUtils {

	private static Toast toast = null;

	/**
	 * Toast发送消息，默认Toast.LENGTH_SHORT
	 * 
	 * @param act
	 * @param msg
	 */
	public static void show(final Context act, final String msg) {
		show(act, msg, Toast.LENGTH_SHORT);
	}

	/**
	 * Toast发送消息，默认Toast.LENGTH_LONG
	 * 
	 * @param act
	 * @param msg
	 */
	public static void showLong(final Context act, final String msg) {
		show(act, msg, Toast.LENGTH_LONG);
	}

	/**
	 * Toast发送消息
	 * 
	 * @param act
	 * @param msg
	 * @param len
	 */
	public static void show(final Context act, final String msg, final int len) {
		cancelCurrentToast();
		toast = Toast.makeText(act, msg, len);
		toast.show();

	}

	/**
	 * 关闭当前Toast
	 * 
	 */
	public static void cancelCurrentToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
}