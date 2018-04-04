package com.dk.mp.ydqj.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.ydqj.R;

public class ImagePreviewActivity extends MyActivity {
	private ViewPager mViewPager;
	private List<String> mList;
	private List<Bitmap> mBitmaps;
	private boolean animating = false;
	private boolean show = true;
	private ImagePagerAdapter mAdapter;
	private int index;

	@Override
	protected int getLayoutID() {
		return R.layout.image_preview;
	}

	@Override
	protected void initialize() {
		super.initialize();

		setTitle("查看图片");
		initViews();
		initDatas();
	}
	
	public void initViews(){
		mViewPager = (ViewPager) findViewById(R.id.viewpage);
	}
	
	public void initDatas(){
		mList = getIntent().getStringArrayListExtra("list");
		index = getIntent().getIntExtra("index", 0);
//		if (!mList.get(0).startsWith("http")) {
//			compressImage();
//		}
		mAdapter = new ImagePagerAdapter(ImagePreviewActivity.this, mList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
	//			mTitle.setText((arg0 + 1) + "/" + mList.size());
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mViewPager.setCurrentItem(index);
	}
	
	private void compressImage() {
		mBitmaps = new ArrayList<Bitmap>();
		for (int i = 0; i < mList.size(); i++) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(mList.get(i), opts);
			int w = opts.outWidth;
			int h = opts.outHeight;
//			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//			float hh = 800f;// 这里设置高度为800f
//			float ww = 480f;// 这里设置宽度为480f
//			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//			int be = 1;// be=1表示不缩放
//			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
//				be = (int) (opts.outWidth / ww);
//			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
//				be = (int) (opts.outHeight / hh);
//			}
//			if (be <= 0)
//				be = 1;
//			opts.inSampleSize = be;// 设置缩放比例
			opts.inJustDecodeBounds = false;
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			Bitmap bitmap = BitmapFactory.decodeFile(mList.get(i), opts);
			bitmap = compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
			Logger.info("Bitmap++"+opts.outWidth+"," + opts.outHeight);
			mBitmaps.add(bitmap);
		}
	}
	
	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	public class ImagePagerAdapter extends PagerAdapter {

		private List<String> list;
		private Context context;
		private int screenWidth;

		public ImagePagerAdapter(Context context, List<String> list) {
			this.list = list;
			this.context = context;
			screenWidth = DeviceUtil.getScreenWidth(getApplicationContext());
		}

		public void setData(List<String> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (list.get(position).startsWith("http")) {
				ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.photo_item_image, container, false);
				imageView.setMaxWidth(screenWidth);
//				utils.display(imageView,list.get(position));
				Glide.with(context).load(list.get(position)).into(imageView);
				imageView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						switchHeadBar();
					}
				});
				container.addView(imageView);
				return imageView;
			} else {
				ImageView imageView = (ImageView) LayoutInflater.from(context)
						.inflate(R.layout.photo_item_image, container, false);
				imageView.setMaxWidth(screenWidth);
				Glide.with(context).load(list.get(position)).into(imageView);
				imageView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						switchHeadBar();
					}
				});
				container.addView(imageView);
				return imageView;
			}
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}
	
	private void switchHeadBar() {
		if (animating) {
			return;
		}
		if (show) {
			Animation headAnimation = AnimationUtils.loadAnimation(
					ImagePreviewActivity.this, R.anim.translate_top_up);
			headAnimation.setFillAfter(true);
			headAnimation.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}
				public void onAnimationEnd(Animation animation) {
					animating = false;
					show = false;
				}
			});
			Animation bottomAnimation = AnimationUtils.loadAnimation(
					ImagePreviewActivity.this, R.anim.translate_bottom_down);
			bottomAnimation.setFillAfter(true);
			bottomAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}

				public void onAnimationEnd(Animation animation) {

				}
			});
			animating = true;
		} else {
			Animation headAnimation = AnimationUtils.loadAnimation(
					ImagePreviewActivity.this, R.anim.translate_top_down);
			headAnimation.setFillAfter(true);
			headAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}

				public void onAnimationEnd(Animation animation) {
					animating = false;
					show = true;
				}
			});
			Animation bottomAnimation = AnimationUtils.loadAnimation(
					ImagePreviewActivity.this, R.anim.translate_bottom_up);
			bottomAnimation.setFillAfter(true);
			bottomAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {

				}

				public void onAnimationRepeat(Animation animation) {

				}

				public void onAnimationEnd(Animation animation) {

				}
			});
			animating = true;
		}
	}
}
