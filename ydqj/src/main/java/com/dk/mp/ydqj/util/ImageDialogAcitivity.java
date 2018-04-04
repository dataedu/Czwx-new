package com.dk.mp.ydqj.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.ydqj.R;

/**
 * 上传图片插件.
 * @version 2013-3-22
 * @author wangw
 */
public class ImageDialogAcitivity extends Activity {
	private String noCutFilePath ="";
	private String noCutPath = CoreConstants.BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
	private String path;
	private int width, height;
	private Button camera, album, cancel;
	private Bitmap photo;
	private boolean cut = false;
	int aspectX, aspectY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.core_photo_dialogs);
		width = getIntent().getIntExtra("width", 200);
		height = getIntent().getIntExtra("height", 200);

		Logger.info("width:" + width);
		Logger.info("height:" + height);

		getAspect();
		cut = getIntent().getBooleanExtra("cut", false);
		initView();
		path = CoreConstants.BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
	}

	private void getAspect() {
		if (width > height) {
			aspectY = 1;
			aspectX = width / height;
		} else {
			aspectX = 1;
			aspectY = height / width;
		}
		Logger.info("aspectX:" + aspectX);
		Logger.info("aspectY:" + aspectY);
	}

	/**
	 * 初始化控件.
	 */
	private void initView() {
		camera = (Button) findViewById(R.id.camera_btn);
		album = (Button) findViewById(R.id.album_btn);
		cancel = (Button) findViewById(R.id.cancel_btn);
		click();
	}

	private void click() {
		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cut) {
					Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
					startActivityForResult(intent2, 2);
				} else {
					noCutFilePath = CoreConstants.BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
					Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					getImageByCamera.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
					getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(noCutFilePath)));
					startActivityForResult(getImageByCamera, 4);
				}
			}
		});
		album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cut) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, 1);
				} else {
					Intent intent=new Intent(Intent.ACTION_PICK);
					intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, 5);
//					Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
//					getImage.addCategory(Intent.CATEGORY_OPENABLE);
//					getImage.setType("image/jpeg");
//					startActivityForResult(getImage, 5);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
	}

	/**
	 * 裁剪图片的方法.
	 * @param uri Uri
	 */
	public void startPicCut(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		//intentCarema.putExtra("scale", false);
		//intentCarema.putExtra("noFaceDetection", true);//不需要人脸识别功能
		//intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域

		//aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", aspectX);
		intentCarema.putExtra("aspectY", aspectY);
		//outputX outputY	是裁剪图片的宽高
		intentCarema.putExtra("outputX", width / 2);
		intentCarema.putExtra("outputY", height / 2);
		intentCarema.putExtra("return-data", true);
		startActivityForResult(intentCarema, 3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1://相册选取 剪切
			if (data != null) {
				startPicCut(data.getData());
			}
			break;
		case 2://拍照 剪切
			File temp = new File(path);
			if (temp.exists()) {
				startPicCut(Uri.fromFile(temp));
			}
			break;
		case 3://剪切后保存
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 4://拍照 不剪切
			if(data != null){
				FileOutputStream fos=null;
				try {
//					noCutFilePath = CoreConstants.BASEPICPATH + UUID.randomUUID().toString() + ".jpg";
					noCutFilePath = noCutPath;
					Bundle extras = data.getExtras();
					Bitmap myBitmap = (Bitmap) extras.get("data");
					fos = new FileOutputStream(new File(noCutFilePath));
					myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {  
						if(fos != null)  
							fos.close() ;  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
				}
			}
			back();
			break;
		case 5://相册选取不 剪切
			if (data != null) {
			   Uri uri = data.getData();  
			   ContentResolver cr = this.getContentResolver();  
               Cursor c = cr.query(uri, null, null, null, null); 
               if (c.moveToNext()) {
            	   noCutFilePath=c.getString(c.getColumnIndex(Images.Media.DATA));
	   				Log.i("path", path);
   				}
               c.close();
               System.out.println(noCutPath+"----------保存路径2");  
			}
			back();
			break;
		default:
			break;
		}

	}

	/**
	 * 保存裁剪后的图片.
	 * @param picData Intent
	 */
	private void setPicToView(Intent picData) {
		Bundle bundle = picData.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			try {

				FileOutputStream outStreamz = new FileOutputStream(new File(path));
				photo.compress(Bitmap.CompressFormat.PNG, 50, outStreamz);
				outStreamz.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Intent back = new Intent();
		back.putExtra("path", path);
		//请求代码可以自己设置，这里设置成20  
		setResult(RESULT_OK, back);
		finish();
	}

	/**
	 * 返回.
	 */
	private void back() {
		try{
			if(StringUtils.isNotEmpty(noCutFilePath)){
			   Bitmap bitmap = createThumbnail(noCutFilePath);
			   FileOutputStream fos = new FileOutputStream(new File(noCutPath));
			   bitmap.compress(Bitmap.CompressFormat.JPEG,30,fos);// 把数据写入文件
			}else{
				noCutPath = "";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Intent back = new Intent();
//		back.putExtra("image", photo);
		back.putExtra("path", noCutPath);
		//请求代码可以自己设置，这里设置成20  
		setResult(RESULT_OK, back);
		finish();
	}

	/**
	 * 取消.
	 */
	private void cancel() {
		noCutPath ="";
		Intent data = new Intent();
		//请求代码可以自己设置，这里设置成20  
		setResult(-1, data);
		//关闭掉这个Activity  
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancel();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**
	 * 压缩图片
	 * 
	 * 
	 */
	private Bitmap createThumbnail(String filepath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容  
		options.inJustDecodeBounds = true;  
		options.inPreferredConfig = Config.RGB_565;  
		BitmapFactory.decodeFile(filepath, options);
		
		options.inJustDecodeBounds = false;    
        int w = options.outWidth;    
        int h = options.outHeight;    
        // 想要缩放的目标尺寸  
        float hh = h/2;// 设置高度为240f时，可以明显看到图片缩小了  
        float ww = w;// 设置宽度为120f，可以明显看到图片缩小了  
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可    
        int be = 1;//be=1表示不缩放    
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放    
            be = (int) (options.outWidth / ww);    
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放    
            be = (int) (options.outHeight / hh);    
        }    
        if (be <= 0) be = 1;    
        options.inSampleSize = be;//设置缩放比例  
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        
        return BitmapFactory.decodeFile(filepath, options);  
	}
	
}