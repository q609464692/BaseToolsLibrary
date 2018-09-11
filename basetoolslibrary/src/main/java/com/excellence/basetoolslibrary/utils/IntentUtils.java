package com.excellence.basetoolslibrary.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;

import static com.excellence.basetoolslibrary.utils.FileUtils.isFileExists;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/9/10
 *     desc   : 常见的Intent相关
 * </pre> 
 */
public class IntentUtils
{
	/**
	 * 判断Intent是否存在
	 *
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent)
	{
		if (context == null || intent == null)
		{
			return false;
		}
		return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
	}

	/**
	 * Intent跳转
	 *
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean startIntent(Context context, Intent intent)
	{
		try
		{
			if (isIntentAvailable(context, intent))
			{
				context.startActivity(intent);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 跳转Settings
	 *
	 * @return
	 */
	public static Intent getSettingIntent()
	{
		return new Intent(Settings.ACTION_SETTINGS);
	}

	/**
	 * 隐式开启WiFi
	 *
	 * @return
	 */
	public static Intent getWiFiIntent()
	{
		return new Intent(Settings.ACTION_WIFI_SETTINGS);
	}

	/**
	 * 直接开启WiFi，防止被拦截
	 *
	 * @return
	 */
	public static Intent getDirectWiFiIntent()
	{
		Intent intent = new Intent();
		ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		intent.setComponent(componentName);
		return intent;
	}

	/**
	 * 安装应用
	 *
	 * @param context
	 * @param file
	 * @return
	 */
	public static Intent getInstallIntent(Context context, File file)
	{
		if (!isFileExists(file))
		{
			return null;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data;
		String type = "application/vnd.android.package-archive";
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
		{
			data = Uri.fromFile(file);
		}
		else
		{
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			String authority = context.getPackageName() + ".provider";
			data = FileProvider.getUriForFile(context, authority, file);
		}
		intent.setDataAndType(data, type);
		return intent;
	}

	/**
	 * 卸载应用
	 *
	 * @param packageName
	 * @return
	 */
	public static Intent getUninstallIntent(String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + packageName));
		return intent;
	}

	/**
	 * 分享文本
	 *
	 * @param content
	 * @return
	 */
	public static Intent getShareTextIntent(String content)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		return intent;
	}

	/**
	 * 分享图片
	 *
	 * @param content
	 * @return
	 */
	public static Intent getShareImageIntent(String content, Uri uri)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * 分享图片
	 *
	 * @param content
	 * @param uris
	 * @return
	 */
	public static Intent getShareImageIntent(String content, ArrayList<Uri> uris)
	{
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		intent.setType("image/*");
		return intent;
	}

	/**
	 * 跳转拨号界面
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static Intent getDialIntent(String phoneNumber)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		return intent;
	}

	/**
	 * 拨打电话
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static Intent getCallIntent(String phoneNumber)
	{
		Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
		return intent;
	}

	/**
	 * 跳转短信界面
	 *
	 * @param content
	 * @return
	 */
	public static Intent getSmsIntent(String content)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("sms_body", content);
		intent.setType("vnd.android-dir/mms-sms");
		return intent;
	}

	/**
	 * 发送短信
	 *
	 * @param phoneNumber
	 * @param content
	 * @return
	 */
	public static Intent getSendSmsIntent(String phoneNumber, String content)
	{
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		return intent;
	}

	/**
	 * 发送邮件
	 *
	 * @param email
	 * @param content
	 * @return
	 */
	public static Intent getEmailIntent(String email, String content)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, email);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setType("text/plain");
		return intent;
	}

	/**
	 * 打开相机
	 *
	 * @param uri
	 * @return
	 */
	public static Intent getCaptureIntent(Uri uri)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		return intent;
	}

	/**
	 * 播放本地视频
	 *
	 * @param filePath
	 * @return
	 */
	public static Intent getVideoIntent(String filePath)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	/**
	 * 播放网络视频
	 *
	 * @param url
	 * @return
	 */
	public static Intent getNetVideoIntent(String url)
	{
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(url), mimeType);
		return intent;
	}

	/**
	 * 播放本地音乐
	 *
	 * @param filePath
	 * @return
	 */
	public static Intent getAudioIntent(String filePath)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(filePath);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

}
