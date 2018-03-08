package com.cloudbox.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.crypto.Data;

public class DateTools {
	public final static int YEAR=0;
	public final static int MONTH=1;
	public final static int DAY=2;
	
	public final static int HOUR=3;
	public final static int MINUTE=4;
	public final static int SECOND=5;
	/**
	* Description: 根据时间获取年/月/日/时/分/秒
	* @param date 时间
	* @param field 获取字段
	* @return int<br>
	* @author lishun 
	* @date 2016-6-12 下午2:37:30
	 */
	public static int getDate(Date date,int field){
		int result;
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		switch(field){
			case 0:result=c.get(Calendar.YEAR); break;
			case 1:result=c.get(Calendar.MONTH)+1; break;
			case 2:result=c.get(Calendar.DATE); break;
			case 3:result=c.get(Calendar.HOUR); break;
			case 4:result=c.get(Calendar.MINUTE); break;
			case 5:result=c.get(Calendar.SECOND); break;
			default:result=-1;break;
		}
		return result;
	}
    /**
     * 日期转换：长时间（毫秒）——>指定日期格式（yyyy-MM-dd HH:mm:ss）
     * 
     * @param time
     *            -相对于“1970 年 1 月 1 日，00:00:00 GMT”的毫秒数, pattern-模式匹配字符串
     * @return
     */
    public static String timeToDateFormat(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("",
                Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern(pattern);// 设置日期显示格式
        Date date0 = new Date();
        date0.setTime(time);
        String dateStr1 = sdf.format(date0);
        return dateStr1;
    }
    /**
     * 某一个月第一天和最后一天
     * @param date
     * @return
     */
    public static Map<String, String> getFirstdayLastday(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        
        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }
    /**
     * 判断2个时间相差1个小时以上就返回false,1小时内就返回true
     * 
     * @param pBeginTime
     *            开始
     * @param pEndTime
     * @return
     * @throws ParseException
     */
    public static boolean timeCompareIsOneHour(String pBeginTime,
            String pEndTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Long beginL = format.parse(pBeginTime).getTime();
            Long endL = format.parse(pEndTime).getTime();

            if (3600000 < (endL - beginL)) {
                return false;
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    /**
     * 字符串转换成日期
     * 
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String DateStrToStrformat(String str) {
    	String dateFormatstr = null;
    	try {
    		 Date  dateFormat = new SimpleDateFormat("yyyy/MM/dd").parse(str);
    		 dateFormatstr= new SimpleDateFormat("yyyy-MM-dd").format(dateFormat);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return dateFormatstr;
    }
    
    /**
     * 日期转换成字符串
     * 
     * @param str
     * @return date
     */
    public static String DateToStr(Date str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date = format.format(str);
        return date;
    }
    /**
     * 日期转换成字符串
     * 
     * @param str
     * @return date
     */
    public static String DateToStrchina(Date str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date = null;
        date = format.format(str);
        return date;
    }
    /**
     * 日期转换成字符串
     * 
     * @param str
     * @return date
     */
    public static String DateToStrchina(String str) {
    	String date = null;
    	try {
    		if(str!=null&&!"".equals(str)){
    			Date datastr= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                date = format.format(datastr);	
    		}else{
    			return null;
    		}
    		
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return date;
    }
    /**
     * 字符串转换成日期
     * 
     * @param str
     * @param marg 转换格式如yyyy-MM-dd HH:mm:ss
     * @return date
     */
    public static Date StrToDate(String str, String marg) {

        SimpleDateFormat format = new SimpleDateFormat(marg);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 
     * Description：为CloudOS API请求设置时间参数
     * @return       
     *
     */
    public static String setTimeParamForOSApi(){
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 100);//有效期设置10分钟
		Date expiredDate = cal.getTime();
		return dateFormat.format(expiredDate);
    }
    
    /**
     * 
     * Description：获取当前时间，格式：yyyy-MM-dd HH:mm:ss
     * @return       
     *
     */
    public static String nowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("",
                Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// 设置日期显示格式
        Date date0 = new Date();
        date0.setTime(System.currentTimeMillis());
        String dateStr1 = sdf.format(date0);
        return dateStr1;
    }
    
    /**
     * 
     * Description：时间的加法
     * @param time
     * @return       
     *
     */
    public static Date timePlus(Date date,int time){
		Calendar afterTime = Calendar.getInstance();
		afterTime.setTime(date);
		afterTime.add(Calendar.MINUTE, time);
		Date afterDate = (Date) afterTime.getTime();
		return afterDate;    	
    }
    
    public static void main(String[] args) {
    	try {
			Date dateFormat = new SimpleDateFormat("yyyy/MM/dd").parse("2014/2/17");
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(dateFormat));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
