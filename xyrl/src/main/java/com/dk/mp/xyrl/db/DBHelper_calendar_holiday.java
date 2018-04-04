package com.dk.mp.xyrl.db;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;
import com.dk.mp.xyrl.entity.JqEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_calendar_holiday extends SQLiteOpenHelper{

private static final String TABLE_NAME = "calendar_holiday";
	
private SQLiteDatabase sqlitedb;

	public DBHelper_calendar_holiday(Context context) {
		super(context, "xyrl.db", null, 2); // name, CursorFactory , version
		sqlitedb = getWritableDatabase();
	}

	/**
	 * 如果calendar_holiday表不存在就创建calendar_holiday表  
	 * 字段：id(主键)、year(年)、month(月)、date(日)、type(假期类型) 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="CREATE TABLE  IF NOT EXISTS  "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT , year text,month text,date text,type text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 插入假期数据
	 */
	public void insert(List<JqEntity> holiday_list){
		ContentValues values=new ContentValues();
		for(int i=0;i<holiday_list.size();i++){
			values.put("year", holiday_list.get(i).getYear());
			values.put("month", holiday_list.get(i).getMonth());
			values.put("date", holiday_list.get(i).getDate());
			values.put("type", holiday_list.get(i).getType());
//			sqlitedb.insert(TABLE_NAME, null, values);
//			values.clear();
			if (checkExsit(holiday_list.get(i).getDate())) {
				sqlitedb.update(TABLE_NAME, values, "date" + "=?", new String[] { holiday_list.get(i).getDate()});
			} else {
				sqlitedb.insert(TABLE_NAME, null, values);
			}
		}
	}

	/**
	 * 判断通知是否存在.	
	 * @return  true:存在；false：不存在
	 */
	private boolean checkExsit(String date) {
		Cursor cursor=sqlitedb.rawQuery("SELECT TYPE FROM " + TABLE_NAME+ " WHERE " + "date" + "='" + date + "'", null);
		if (cursor != null) {
			return cursor.getCount() > 0;
		}
		return false;
	}

	/**
	 * 查询假期数据
	 * @return 
	 */
	public List<JqEntity> query(String year,String month){
		Cursor cursor=sqlitedb.query(TABLE_NAME, new String[]{"date","type"}, "year=? and month=?", new String[]{year,month} , null, null, null);
		//db.rawQuery("select date,type from "+TABLE_NAME+" where year=? month=?" , new String[]{year,month});//这种写法和上面写法等价
		//select date,type from calendar_holiday where year=2017 and month=6;//上面两行代码就是在拼这句sql
		List<JqEntity> holiday_list=new ArrayList<JqEntity>();
		if(cursor.moveToFirst()){
			do{
				String date=cursor.getString(cursor.getColumnIndex("date"));
				String type=cursor.getString(cursor.getColumnIndex("type"));
				/*holiday_list.add(new JqEntity(year, month, date, type));*/
				JqEntity jqEntity = new JqEntity();
				jqEntity.setYear(year);
				jqEntity.setMonth(month);
				jqEntity.setDate(date);
				jqEntity.setType(type);

				holiday_list.add(jqEntity);
			}while (cursor.moveToNext());
		}
		cursor.close();
		return holiday_list;
	}

	
}
