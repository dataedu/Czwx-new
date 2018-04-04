package com.dk.mp.xyrl.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.xyrl.R;
import com.dk.mp.xyrl.db.DBHelper_calendar_holiday;
import com.dk.mp.xyrl.entity.JqEntity;
import com.dk.mp.xyrl.entity.LunarCalendar;
import com.dk.mp.xyrl.entity.SpecialCalendar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author Vincent Lee
 * 
 */
public class CalendarAdapter extends BaseAdapter{
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private Context context;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
	// private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null;
	private Resources res = null;
	private Drawable drawable = null;

	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1; // 用于标记当天
	private int[] schDateTagFlag = null; // 存储当月所有的日程日期

	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	private int width;
	private List<JqEntity> listJq;
	
	public CalendarAdapter() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];

	}

	public CalendarAdapter(Context context, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c) {
		this();
		this.context = context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		
		width=DeviceUtil.getScreenWidth(context)/7;

		int stepYear = year_c + jumpYear;
		int stepMonth = month_c + jumpMonth;
		if (stepMonth > 0) {
			// 往下一个月滑动
			if (stepMonth % 12 == 0) {
				stepYear = year_c + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = year_c + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// 往上一个月滑动
			stepYear = year_c - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}

		currentYear = String.valueOf(stepYear); // 得到当前的年份
		currentMonth = String.valueOf(stepMonth); // 得到本月
													// （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = String.valueOf(day_c); // 得到当前日期是哪天

		getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

		JqList();

	}

	private void JqList(){
		Map<String, Object> map = new HashMap<String, Object>();
		String mm = "";
		if(getShowMonth().length()<2){
			mm = "0" + getShowMonth();
		}else {
			mm = getShowMonth();
		}
		if (DeviceUtil.checkNet()) {
			
			map.put("month", getShowYear() + "-" + mm);
			HttpUtil.getInstance().postJsonObjectRequest("apps/xyrl/list", map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") == 200){
                            listJq = new Gson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<JqEntity>>(){}.getType());
							if(listJq.size() > 0){
								for(int i=0; i<listJq.size(); i++){
									listJq.get(i).setYear(getShowYear());
									listJq.get(i).setMonth(getShowMonth());
								}
								DBHelper_calendar_holiday db_helper = new DBHelper_calendar_holiday(context);
								db_helper.insert(listJq);

								notifyDataSetChanged();

							}
                        }
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(VolleyError error) {

				}
			});
		}
	}
	
	public CalendarAdapter(Context context, int year, int month, int day) {
		this();
		this.context = context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		currentYear = String.valueOf(year);// 得到跳转到的年份
		currentMonth = String.valueOf(month); // 得到跳转到的月份
		currentDay = String.valueOf(day); // 得到跳转到的天
		getCalendar(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item2, null);
		}
		
		TextView nll = (TextView) convertView.findViewById(R.id.tvtext);
		TextView nl = (TextView) convertView.findViewById(R.id.nl);
		TextView nls = (TextView) convertView.findViewById(R.id.jq_txt);
		
		String d = dayNumber[position].split("\\.")[0];
		String dv = dayNumber[position].split("\\.")[1];
		
		nll.setText(d);
		nl.setText(dv);
		nll.setTextColor(Color.GRAY);

		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			nll.setTextColor(Color.BLACK);// 当月字体设黑
			
			DBHelper_calendar_holiday db_helper=new DBHelper_calendar_holiday(context);
			listJq = db_helper.query(getShowYear(), getShowMonth());
			for(int i=0; i<listJq.size(); i++){
				String jq = listJq.get(i).getDate().split("-")[2];
				if(jq.startsWith("0")){
					jq = jq.replace("0", "");
				}
				if(jq.equals(d)){
					nls.setText(listJq.get(i).getType());
					if(listJq.get(i).getType().equals("假期")){
						nls.setTextColor(context.getResources().getColor(R.color.jiaqi));
						nls.setTextSize(11);
					}else if(listJq.get(i).getType().equals("补班")){
						nls.setTextColor(context.getResources().getColor(R.color.buban));
					}
					
					nls.setVisibility(View.VISIBLE);
				}
			}
			
			if (check(d)) {
				nl.setCompoundDrawables(null, null, null, drawable);
			} else {
				nl.setCompoundDrawables(null, null, null, null);
			}
			
			if (checkToday(d)) {
				//设置当天的背景
				convertView.setBackgroundResource(R.drawable.rcap_today);
				nll.setTextColor(Color.WHITE);
			}else{
				convertView.setBackgroundResource(R.drawable.rcap_nottoday);
				nll.setTextColor(context.getResources().getColor(R.color.rcap_txt));
			}
		}else{
			nl.setCompoundDrawables(null, null, null, null);
			convertView.setBackgroundResource(R.drawable.touming);
			nll.setTextColor(Color.GRAY);
		}

		convertView.setLayoutParams(new AbsListView.LayoutParams(width,width));
		return convertView;
	}
	
	//得到某年的某月的天数且这月的第一天是星期几
		public boolean check(String day) {
			boolean bool = false;
			if (schDateTagFlag != null && schDateTagFlag.length > 0) {
				for (int i = 0; i < schDateTagFlag.length; i++) {
					if (schDateTagFlag[i] == Integer.parseInt(day)) {
						//设置日程标记背景
						bool = true;
					}
				}
			}
			return bool;
		}

		private boolean checkToday(String day) {
			boolean bool = false;
			if ((TimeUtils.formatDateTime(currentYear + "-" + currentMonth + "-" + day)).equals(TimeUtils.getToday())) {
				bool= true;
			}
			return bool;
		}

		
	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth + "  ============  " + dayOfWeek + "  =========   " + lastDaysOfMonth);
		getweek(year, month);
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";

		// 得到当前月的所有日程日期(这些日期需要标记)

		for (int i = 0; i < dayNumber.length; i++) {
			// 周一
			// if(i<7){
			// dayNumber[i]=week[i]+"."+" ";
			// }
			if (i < dayOfWeek) { // 前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				lunarDay = lc.getLunarDate(year, month - 1, temp + i, false);
				dayNumber[i] = (temp + i) + "." + lunarDay;

			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
				lunarDay = lc.getLunarDate(year, month, i - dayOfWeek + 1, false);
				dayNumber[i] = i - dayOfWeek + 1 + "." + lunarDay;
				// 对于当前月才去标记当前日期
				if (sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)) {
					// 标记当前日期
					currentFlag = i;
				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(lc.animalsYear(year));
				setLeapMonth(lc.leapMonth == 0 ? "" : String.valueOf(lc.leapMonth));
				setCyclical(lc.cyclical(year));
			} else { // 下一个月
				lunarDay = lc.getLunarDate(year, month + 1, j, false);
				dayNumber[i] = j + "." + lunarDay;
				j++;
			}
		}

		String abc = "";
		for (int i = 0; i < dayNumber.length; i++) {
			abc = abc + dayNumber[i] + ":";
		}
		Log.d("DAYNUMBER", abc);

	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek + 7;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}

}
