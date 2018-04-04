package com.dk.mp.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.widget.ImageView;

import com.dk.mp.core.application.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 图片工具类.
 * @since 
 * @version 2012-10-16
 * @author wangw
 */
public class ImageUtil {
	/** 
	 * 获取图片名称获取图片的资源id的方法 .
	 * @param imageName 图片名称
	 * @return  资源id
	 */
	public static int getResource(String imageName) {
		Context context = MyApplication.getContext();
		int resId = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
		return resId;
	}

	public static final String BASEPICPATH = CoreConstants.BASEPICPATH;
	public static final String DOWNLOADPATH = CoreConstants.DOWNLOADPATH;
	private static Map<String, Bitmap> map = new HashMap<String, Bitmap>();
	private static DisplayImageOptions options;
	private static AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();

	static {
		File f = new File(BASEPICPATH);
		if (!f.exists()) {
			f.mkdirs();
		}
		File f2 = new File(DOWNLOADPATH);
		if (!f2.exists()) {
			f2.mkdirs();
		}
	}

	/**
	 * 获取图片. 首先从内存里取图片,没有的话 从本地sdcard取图片并保存在map里,
	 * 本地没有图片的话, 从网络上把图片取下来,保存在sdcard上,在保存在map里
	 * @param fileUrl 图片网络地址
	 * @return bitmap对象
	 */
	public static Bitmap getImage(String fileUrl) {
		try {
			String filePath = BASEPICPATH + filterPath(fileUrl);
			// 首先从内存里取图片
			Bitmap bitmap = map.get(filePath);
			if (bitmap != null) {
				Logger.info("getImage from map");
				return bitmap;
			}

			// 从本地sdcard取图片
			if (new File(filePath).exists()) {
				bitmap = BitmapFactory.decodeFile(filePath);
				put(filePath, bitmap);
				Logger.info("getImage from sdcard   " + filePath);
				return bitmap;
			}

			// 从网络上去图片，并保存在本地，并存储在map里
			bitmap = returnBitMap(fileUrl);
			if (bitmap != null) {
				Logger.info("getImage from network");
				put(filePath, bitmap);
				saveBitmap(bitmap, BASEPICPATH, filterPath(fileUrl));
			}
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void put(String key, Bitmap value) {
		Object s[] = map.keySet().toArray();
		if (map.size() < 100) {
			map.put(key, value);
		} else {
			map.remove(s[0]);

			if (!map.get(key).isRecycled()) {
				map.get(key).recycle(); //回收图片所占的内存
			}
			map.put(key, value);
		}

	}


	/**
	 * 下载网络图片.
	 * @param fileUrl 图片地址
	 * @param fileName 图片名称
	 */
	public static void saveImage(final String fileUrl, final String fileName) {
		new Thread() {
			@Override
			public void run() {
				Bitmap b = returnBitMap(fileUrl);
				try {
					String type = contain(fileUrl);
					saveBitmap(b, DOWNLOADPATH, fileName + type);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 获取图片链接的后缀名.
	 * @param fileUrl 图片链接
	 * @return 图片后缀名  (".gif", ".jpg", ".jpeg",".bmp", ".png")
	 */
	private static String contain(String fileUrl) {
		String[] strings = new String[] { ".gif", ".jpg", ".jpeg", ".bmp", ".png" };
		for (int i = 0; i < strings.length; i++) {
			String s = strings[i];
			if (fileUrl.contains(s))
				return s;
		}
		return ".jpg";
	}

	/**
	 * 下载网络图片.
	 * @param fileUrl 图片地址
	 */
	public static void downloadCache(final String fileUrl) {
		new Thread() {
			@Override
			public void run() {
				Bitmap b = returnBitMap(fileUrl);
				try {
					saveBitmap(b, BASEPICPATH, fileUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 将网络图片转换为bitmap对象.
	 * @param url  图片地址
	 * @return Bitmap
	 */
	private static Bitmap returnBitMap(String url) {
		if (null == url || "".equals(url)) {
			return null;
		} else {
			URL myFileUrl;
			myFileUrl = null;
			Bitmap bitmap = null;
			try {
				myFileUrl = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(3000);
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 2; //width，hight设为原来的十分一
				bitmap = BitmapFactory.decodeStream(is, null, options);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	}

	/**
	 * 保存bitmap对象为图片文件.
	 * @param bm Bitmap
	 * @param root 保存路径
	 * @param fileUrl    文件名
	 * @throws IOException  IOException
	 */
	public static void saveBitmap(final Bitmap bm, final String root, final String fileUrl) {
		new Thread() {
			@Override
			public void run() {
				Logger.info("path:========"+root + filterPath(fileUrl));
				File myCaptureFile = new File(root + filterPath(fileUrl));
				BufferedOutputStream bos;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
					bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					bos.flush();
					bos.close();
					//					if(!bm.isRecycled()){
					//						bm.recycle();   //回收图片所占的内存
					//				         System.gc();  //提醒系统及时回收
					//				      }
					super.run();
					Logger.info("saveBitmap ok  " + root + fileUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 将网络图片转换为.
	 * @param fileUrl InputStream
	 * @return InputStream
	 */
	public static InputStream getInputStreamByImage(String fileUrl) {
		try {
			Bitmap bitmap = returnBitMap(fileUrl);
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
				return sbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 过滤网络连接的特殊字符.
	 * @param x  网络链接
	 * @return 过滤后的连接
	 */
	private static String filterPath(String x) {
		return x.replace("\\", "/").replace("?", "").replace("/", "").replace(":", "");
	}

	/**
	 * 获取图片名称获取图片的资源id的方法 .
	 * @param context Context
	 * @param imageName 图片名称
	 * @return  资源id
	 */
	public static int getResource(Context context, String imageName) {
		int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
		return resId;
	}

	/**
	 * 获取圆角图片.
	 * @param bitmap Bitmap
	 * @param bool 是否需要倒影
	 * @return Bitmap
	 */
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, boolean bool) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 20;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			if (bool) {
				return createReflectionImageWithOrigin(output);
			} else {
				return output;
			}
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 获得带倒影的图片方法
	 * @param bitmap Bitmap
	 * @return Bitmap
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		// 图片与倒影间隔距离
		final int reflectionGap = 0;
		// 图片的宽度
		int width = bitmap.getWidth();
		// 图片的高度
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		// 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
		matrix.preScale(1, -1);
		// 创建反转后的图片Bitmap对象，图片高是原图的一半。
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
		// 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。 可以理解为这张图将会在屏幕上显示 是原图和倒影的合体
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);
		// 构造函数传入Bitmap对象，为了在图片上画图
		Canvas canvas = new Canvas(bitmapWithReflection);
		// 画原始图片
		canvas.drawBitmap(bitmap, 0, 0, null);
		// 画间隔矩形
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
		// 画倒影图片
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		// 实现倒影渐变效果
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader);

		// Set the Transfer mode to be porter duff and destination in
		// 覆盖效果
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}




	public static void setImageView(final Context context, final ImageView imageView, String url,
									final int moren, final int fail) {
		if (options == null) {
			options = new DisplayImageOptions.Builder().showImageOnLoading(moren).showImageForEmptyUri(moren).showImageOnFail(fail).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
		}
		try {
			ImageLoader.getInstance().displayImage(url, imageView, options, animateFirstListener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public void clean() {
		AnimateFirstDisplayListener.displayedImages.clear();
	}


}
