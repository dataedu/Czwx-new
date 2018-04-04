package com.dk.mp.xxgk.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dk.mp.core.util.Logger;
import com.dk.mp.xxgk.entity.Depart;
import com.dk.mp.xxgk.entity.History;
import com.dk.mp.xxgk.entity.Introduction;

/**
 * 概况数据库.
 * 
 * @since
 * @version 2014-9-26
 * @author zhaorm
 */
public class IntroDBHelper extends SQLiteOpenHelper{
	private SQLiteDatabase sqlitedb;
	public static final String TABLE_SCHOOL_INFO = "INTRO_SCHOOL_INFO",
			TABLE_HISTROY = "INTRO_HISTROY",
			TABLE_DEPART_INFO = "INTRO_DEPART_INFO";
	private static final String ID = "id", CONTENT = "content", IMG = "img",
			TIMESTAMP = "TIMESTAMP", NAME = "name", TITLE = "TITLE",
			TIME = "TIME";

	public IntroDBHelper(Context context) {
		super(context, "schiofo.db", null, 2);
		// 实例化默认数据库辅助操作对象
		sqlitedb = getWritableDatabase(); // 实例化数据库
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createSchoolInfoTable());
		db.execSQL(createHistoryTable());
		db.execSQL(createDepartTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	/**
	 * 创建学院介绍表.
	 */
	public static String createSchoolInfoTable() {
		StringBuffer sql1 = new StringBuffer("CREATE TABLE  IF NOT EXISTS "
				+ TABLE_SCHOOL_INFO + "(" + ID + " INTEGER  ");
		sql1.append("," + IMG + " text");
		sql1.append("," + CONTENT + " text");
		sql1.append("," + TIMESTAMP + " text");
		sql1.append(" )");
		return sql1.toString();
	}

	/**
	 * 创建辉煌校史表.
	 */
	public static String createHistoryTable() {
		StringBuffer sql2 = new StringBuffer("CREATE TABLE  IF NOT EXISTS "
				+ TABLE_HISTROY + "(" + ID + " text  ");
		sql2.append("," + CONTENT + " text");
		sql2.append("," + IMG + " text");
		sql2.append("," + TITLE + " text");
		sql2.append("," + TIME + " text");
		sql2.append("," + TIMESTAMP + " text");
		sql2.append(" )");
		return sql2.toString();
	}

	/**
	 * 创建院系介绍表.
	 */
	public static String createDepartTable() {
		StringBuffer sql2 = new StringBuffer("CREATE TABLE  IF NOT EXISTS "
				+ TABLE_DEPART_INFO + "(" + ID + " text  ");
		sql2.append("," + NAME + " text");
		sql2.append("," + CONTENT + " text");
		sql2.append("," + TIMESTAMP + " text");
		sql2.append(" )");
		return sql2.toString();
	}

	/**
	 * 插入院系数据.
	 *            News
	 */
	public void insertTable(List<Depart> list) {
		try {
			for (int i = 0; i < list.size(); i++) {
				Depart depart = list.get(i);
				ContentValues cv = new ContentValues();
				cv.put(ID, depart.getId());
				cv.put(TIMESTAMP, depart.getTimeStamp());
				cv.put(CONTENT, depart.getContent());
				cv.put(NAME, depart.getName());
				boolean b = checkUpdate(TABLE_DEPART_INFO, depart.getId());
				if (b) {
					sqlitedb.update(TABLE_DEPART_INFO, cv,
							ID + "=" + depart.getId(), null);
				} else {
					sqlitedb.insert(TABLE_DEPART_INFO, null, cv);
				}
			}
		} catch (Exception e) {
			Logger.info("插入数据失败");
			e.printStackTrace();
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 插入校史数据.
	 *            history
	 */
	public void updateHistory(History history) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(ID, history.getIdHistory());
			cv.put(CONTENT, history.getContext());
			cv.put(IMG, history.getImage());
			cv.put(TITLE, history.getTitle());
			cv.put(TIME, history.getTime());
			cv.put(TIMESTAMP, history.getTimeStamp());
			if (checkUpdate(TABLE_HISTROY, history.getIdHistory())) {
				sqlitedb.update(TABLE_HISTROY, cv,
						ID + "='" + history.getIdHistory() + "'", null);
			} else {
				sqlitedb.insert(TABLE_HISTROY, null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("update 数据失败");
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 插入校园简介数据.
	 * 
	 * @param introduction
	 */
	public void updateSchoolInfo(final Introduction introduction) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(CONTENT, introduction.getContent());
			cv.put(TIMESTAMP, introduction.getTimeStamp());
			cv.put(ID, introduction.getIdIntroduction());
			cv.put(IMG, introduction.getImg());
			if (checkUpdate(TABLE_SCHOOL_INFO, introduction.getIdIntroduction())
					&& !"".equals(introduction.getIdIntroduction())) {
				sqlitedb.update(TABLE_SCHOOL_INFO, cv,
						ID + "=" + introduction.getIdIntroduction(), null);
			} else {
				sqlitedb.insert(TABLE_SCHOOL_INFO, null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("update 数据失败");
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 检查更新.
	 * 
	 * @param tableName
	 * @param id
	 * @return
	 */
	private boolean checkUpdate(String tableName, String id) {
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM " + tableName + " WHERE "
				+ ID + "='" + id + "'", null);
		if (cur != null) {
			return cur.getCount() > 0;
		}
		return false;
	}

	/**
	 * 查询院系介绍.
	 */
	public List<Depart> getDepartList(String name) {
		Cursor cur = null;
		List<Depart> list = new ArrayList<Depart>();
		String sql = "SELECT * FROM " + TABLE_DEPART_INFO;
		if (null != name && !"".equals(name)) {
			sql = sql + " WHERE " + NAME + " like '%" + name + "%'";
		}
		sql = sql + " order by "+TIMESTAMP+" asc";
		try {
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					Depart depart = new Depart();
					depart.setId(cur.getString(0));
					depart.setName(cur.getString(1));
					depart.setContent(cur.getString(2));
					list.add(depart);
				}
			}
		} catch (Exception e) {
			Logger.info("搜索部门列表失败");
		} finally {
			if (cur != null) {
				cur.close();
			}
			sqlitedb.close();
		}
		return list;
	}

	/**
	 * 查询校史.
	 * 
	 * @return
	 */
	public History getHistory() {
		Cursor cur = null;
		History history = null;
		String sql = "SELECT * FROM " + TABLE_HISTROY;
		try {
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null && cur.getCount() > 0) {
				history = new History();
				cur.moveToFirst();
				history.setIdHistory(cur.getString(0));
				history.setContext(cur.getString(1));
				history.setImage(cur.getString(2));
				history.setTitle(cur.getString(3));
				history.setTime(cur.getString(4));
				history.setTimeStamp(cur.getString(5));
			}
		} catch (Exception e) {
			Logger.info("搜索部门列表失败");
		} finally {
			if (cur != null) {
				cur.close();
			}
			sqlitedb.close();
		}
		return history;
	}

	/**
	 * 查询校园简介.
	 * 
	 * @return
	 */
	public Introduction getIntroduction() {
		Introduction introduction = null;
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM " + TABLE_SCHOOL_INFO,
				null);
		if (cur != null && cur.getCount() > 0) {
			introduction = new Introduction();
			cur.moveToFirst();
			introduction.setImg(cur.getString(1));
			introduction.setContent(cur.getString(2));
		}
		if (cur != null) {
			cur.close();
		}
		return introduction;
	}

	/**
	 * 功能:取时间戳.
	 * 
	 * @param tableName
	 * @param columnIndex
	 * @return
	 */
	public String getTimestap(String tableName) {
		String str = "";
		Cursor cur = sqlitedb.query(tableName, new String[] { TIMESTAMP },
				null, null, null, null, TIMESTAMP + " desc");
		if (cur != null && cur.getCount() > 0) {
			cur.moveToFirst();
			str = cur.getString(0);
		}
		if (cur != null) {
			cur.close();
		}
		return str;
	}

}
