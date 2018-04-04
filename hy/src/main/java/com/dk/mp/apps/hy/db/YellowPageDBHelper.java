package com.dk.mp.apps.hy.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dk.mp.apps.hy.entity.Bm;
import com.dk.mp.apps.hy.entity.Ks;
import com.dk.mp.core.util.Logger;

/**
 * 黄页数据库操作类.
 * @since 
 * @version 2014-10-8
 * @author SunLiang
 */
public class YellowPageDBHelper extends SQLiteOpenHelper {

	private SQLiteDatabase sqlitedb;
	private static final String TABLE_DEPART = "contacts_depart", TABLE_PERSON = "contacts_person",
			TABLEVALUE = "TABLEVALUE";
	private static final String ID_DEPART = "id_depart", NAME_DEPART = "name_depart", ID_PERSON = "id_person",
			NAME_PERSON = "name_person", TEL = "TEL";

	/**
	 * 构造方法.
	 * 
	 * @param context
	 *            Context
	 */
	public YellowPageDBHelper(Context context) {
		super(context, "yellowpage.db", null, 1);
		sqlitedb = getWritableDatabase(); // 实例化数据库
	}

	// 在数据库第一次生成的时候会调用这个方法，同时我们在这个方法里边生成数据库表.
	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.info("BusDBHelper onCreate");
		// 创建数据表的操作
		db.execSQL(createDepartTable());
		db.execSQL(createPersonTable());
		db.execSQL(createValueTable());
	}

	// 更新或者升级数据库的时候会自动调用这个方法，一般我们会在这个方法中
	// 删除数据表，然后再创建新的数据表操作。
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * 创建�?
	 * 
	 * @return sql
	 */
	public String createDepartTable() {
		StringBuffer sql1 = new StringBuffer("CREATE TABLE  IF NOT EXISTS " + TABLE_DEPART
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT ");
		sql1.append("," + ID_DEPART + " text");
		sql1.append("," + NAME_DEPART + " text");
		sql1.append(" )");
		return sql1.toString();
	}

	/**
	 * 创建�?
	 * 
	 * @return sql
	 */
	public static String createPersonTable() {
		StringBuffer sql1 = new StringBuffer("CREATE TABLE  IF NOT EXISTS " + TABLE_PERSON
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT ");
		sql1.append("," + ID_PERSON + " text");
		sql1.append("," + NAME_PERSON + " text");
		sql1.append("," + ID_DEPART + " text");
		sql1.append("," + TEL + " text");
		sql1.append(" )");
		return sql1.toString();
	}

	/**
	 * 创建�?
	 * 
	 * @return sql
	 */
	public static String createValueTable() {
		StringBuffer sql1 = new StringBuffer("CREATE TABLE  IF NOT EXISTS " + TABLEVALUE + "(id text PRIMARY KEY  ");
		sql1.append("," + ID_DEPART + " text");
		sql1.append("," + ID_PERSON + " text");
		sql1.append("," + TEL + " text");
		sql1.append(" )");
		return sql1.toString();
	}

	/**
	 * 插入部门和联系人.
	 * 
	 * @param departs
	 *            List<DepartMent>
	 * @param persons
	 *            List<Person>
	 */
	public void insertDepartList(List<Bm> list) {
		try {
			for (int i = 0; i < list.size(); i++) {
				Bm departMent = list.get(i);
				ContentValues cvD = new ContentValues();
				cvD.put(ID_DEPART, departMent.getIdDepart());
				cvD.put(NAME_DEPART, departMent.getNameDepart());
				if (checkDepart(departMent.getIdDepart())) {
					sqlitedb.update(TABLE_DEPART, cvD, ID_DEPART + "=?", new String[] { departMent.getIdDepart() });
				} else {
					sqlitedb.insert(TABLE_DEPART, null, cvD);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("插入部门和联系人失败");
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 插入部门和联系人.
	 * 
	 * @param departs
	 *            List<DepartMent>
	 * @param persons
	 *            List<Person>
	 */
	public void insertPeopleList(String idDepart, List<Ks> persons) {
		try {
			for (int j = 0; j < persons.size(); j++) {
				Ks d = persons.get(j);
				ContentValues cv = new ContentValues();
				cv.put(ID_PERSON, d.getIdUser());
				cv.put(NAME_PERSON, d.getName());
				cv.put(ID_DEPART, idDepart);
				cv.put(TEL, d.getTels());
				if (checkPerson(d.getIdUser(), idDepart)) {
					sqlitedb.update(TABLE_PERSON, cv, ID_PERSON + "=? and " + ID_DEPART + "=?",
							new String[] { d.getIdUser(), idDepart });
				} else {
					sqlitedb.insert(TABLE_PERSON, null, cv);
				}

				List<String> values = d.getList();
				for (int index = 0; index < values.size(); index++) {
					String tel = values.get(index);
					ContentValues cv2 = new ContentValues();
					cv2.put("id", idDepart + d.getIdUser() + tel);
					cv2.put(ID_PERSON, d.getIdUser());
					cv2.put(ID_DEPART, idDepart);
					cv2.put(TEL, values.get(index));
					if (checkTel(idDepart + d.getIdUser() + tel)) {
						sqlitedb.update(TABLEVALUE, cv, "id" + "=?", new String[] { idDepart + d.getIdUser() + tel });
					} else {
						sqlitedb.insert(TABLEVALUE, null, cv);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("插入部门和联系人失败");
		} finally {
			sqlitedb.close();
		}
	}

	/**
	 * 判断部门是否存在.
	 * 
	 * @param id
	 *            id
	 * @return true:存在；false：不存在
	 */
	private boolean checkDepart(String id) {
		int count = 0;
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM " + TABLE_DEPART + " WHERE " + ID_DEPART + "='" + id + "'", null);
		if (cur != null) {
			count = cur.getCount();
			cur.close();
		}
		return count > 0;
	}

	/**
	 * 判断部门是否存在.
	 * 
	 * @param id
	 *            id
	 * @return true:存在；false：不存在
	 */
	private boolean checkTel(String id) {
		int count = 0;
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM " + TABLEVALUE + " WHERE id='" + id + "'   ", null);
		if (cur != null) {
			count = cur.getCount();
			cur.close();
		}
		return count > 0;
	}

	/**
	 * 判断部门是否存在.
	 * 
	 * @param id
	 *            id
	 * @return true:不为空；false：空
	 */
	public boolean checkEmpty() {
		int count = 0;
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM " + TABLE_DEPART, null);
		if (cur != null) {
			count = cur.getCount();
		}
		if (cur != null) {
			cur.close();
		}
		return count > 0;
	}

	/**
	 * 判断联系人是否存�?
	 * 
	 * @param id
	 *            id
	 * @return true:存在；false：不存在
	 */
	private boolean checkPerson(String id, String idDepart) {
		Cursor cur = null;
		int count = 0;
		cur = sqlitedb.rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " + ID_PERSON + "='" + id
				+ "' and ID_DEPART='" + idDepart + "' ", null);
		if (cur != null) {
			count = cur.getCount();
		}
		if (cur != null) {
			cur.close();
		}

		return count > 0;
	}

	/**
	 * 查找联系�?
	 * 
	 * @param context
	 *            Context
	 * @param idDepart
	 *            部门id
	 * @param key
	 *            查询关键�?
	 * @return List<Person>
	 */
	public List<Ks> queryDepart(Context context, String key) {
		List<Ks> list = new ArrayList<Ks>();
		Cursor cur = null;
		try {
			String sql = "SELECT a." + ID_PERSON + ",a." + NAME_PERSON + "b." + NAME_DEPART + " FROM " + TABLE_PERSON
					+ " a ," + TABLE_DEPART + " b WHERE (a." + NAME_PERSON + " like '%" + key + "%' ) and a."
					+ ID_DEPART + "=b." + ID_DEPART;
			Logger.info(sql);
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					Ks d = new Ks();
					d.setIdUser(cur.getString(cur.getColumnIndex(ID_PERSON)));
					d.setName(cur.getString(cur.getColumnIndex(NAME_PERSON)) + "(" + cur.getColumnIndex(NAME_DEPART)
							+ ")");
					list.add(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("查找联系人失败");
		} finally {
			if (cur != null) {
				cur.close();
			}
			sqlitedb.close();
		}
		Logger.info("query: list  " + list.size());
		return list;
	}

	/**
	 * 获取部门列表.
	 * 
	 * @param context
	 *            Context
	 * @return List<DepartMent>
	 */
	public List<Bm> getDepartMentList() {
		List<Bm> list = new ArrayList<Bm>();
		Cursor cur = null;
		try {
			String sql = "SELECT * FROM " + TABLE_DEPART;
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					Bm d = new Bm();
					d.setIdDepart(cur.getString(cur.getColumnIndex(ID_DEPART)));
					d.setNameDepart(cur.getString(cur.getColumnIndex(NAME_DEPART)));
					list.add(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("获取部门列表失败");
		} finally {
			if (cur != null) {
				cur.close();
			}
			sqlitedb.close();
		}
		Logger.info("getDepartMentList:" + list.size());
		return list;
	}

	/**
	 * 后去部门列表.
	 * 
	 * @param context
	 *            Context
	 * @return List<DepartMent>
	 */

	/**
	 * 获取联系人列�?
	 * 
	 * @param context
	 *            Context
	 * @param idDepart
	 *            部门id
	 * @return List<Person>
	 */
	public List<Ks> getPersonListByDepart(String idDepart) {
		List<Ks> list = new ArrayList<Ks>();
		Cursor cur = null;
		try {
			Logger.info("idDepart:" + idDepart);
			String sql = "SELECT * FROM " + TABLE_PERSON + " WHERE " + ID_DEPART + "='" + idDepart + "'";
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					Ks d = new Ks();
					d.setIdUser(cur.getString(cur.getColumnIndex(ID_PERSON)));
					d.setName(cur.getString(cur.getColumnIndex(NAME_PERSON)));
//					d.setList(getValueByUser(cur.getString(cur.getColumnIndex(ID_DEPART)), cur.getString(cur.getColumnIndex(ID_PERSON))));
					
//					String hm="";
//					for(int i=0;i<d.getList().size();i++){
//						if(i==0){
//							hm=	d.getList().get(i);
//						}else{
//							hm+="/"+	d.getList().get(i);
//						}
//					}
//					d.setTels(hm);
					String tels = cur.getString(cur.getColumnIndex(TEL));
					d.setTels(tels);
					String[] ss = tels.split("/");
					List<String> l = new ArrayList<String>(Arrays.asList(ss));
					d.setList(l);
					list.add(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info(" 获取联系人列表失败");
		} finally {
			if (cur != null) {
				cur.close();
			}
			sqlitedb.close();
		}
		return list;
	}

	/**
	 * 获取联系人列�?
	 * 
	 * @param context
	 *            Context
	 * @param idDepart
	 *            部门id
	 * @return List<Person>
	 */
	public List<String> getValueByUser(String idDepart, String idUser) {
		List<String> list = new ArrayList<String>();
		Cursor cur = null;
		try {
			String sql = "SELECT * FROM " + TABLEVALUE + " WHERE " + ID_DEPART + "='" + idDepart + "' and " + ID_PERSON
					+ "='" + idUser + "'";
			cur = sqlitedb.rawQuery(sql, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					list.add(cur.getString(cur.getColumnIndex(TEL)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info(" 获取联系人列表失败");
		} 
		return list;
	}

}
