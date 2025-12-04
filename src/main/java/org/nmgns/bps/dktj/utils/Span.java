package org.nmgns.bps.dktj.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by syb on 2016/12/31.
 */
public class Span {
    private Date startDate;
    private Date endDate;
    private String strStartDate;
    private String strEndDate;
    private short days;
    private short month;

    public Span(String startDate, String endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            setStartDate(sdf.parse(startDate));
            setEndDate(sdf.parse(endDate));
            setStrStartDate(startDate);
            setStrEndDate(endDate);
        }catch (Exception e){
            throw new Exception("日期格式错误");
        }

        if (getStartDate().after(getEndDate()))
            throw new Exception("开始时间大于终止时间");
    }

    public Span(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            setStartDate(sdf.parse(sdf.format(startDate)));
            setEndDate(sdf.parse(sdf.format(endDate)));
            setStrStartDate(sdf.format(startDate));
            setStrEndDate(sdf.format(endDate));
        }catch (Exception e){
            throw new Exception("日期格式错误");
        }

        if (getStartDate().after(getEndDate()) )
            throw new Exception("开始时间大于终止时间");
    }

    public short getDays() {
        return days;
    }

    public void setDays(short days) {
        this.days = days;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    /**
     * 获取两个时间之间间隔的月
     * @return
     */
    public int getMonthSpace() {
        int result = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = LocalDate.parse(getStrStartDate(), formatter);
        LocalDate to = LocalDate.parse(getStrEndDate(), formatter);
        result = (int)from.until(to, ChronoUnit.MONTHS);
        return result;
    }

    /**
     * 获取两个时间之间间隔月之外剩余的天数；即间隔x月y天的y值
     * @return
     * @throws Exception
     */
    public int getMonthLeftDaysSpace() throws Exception{
        int result = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startTime = LocalDate.parse(getStrStartDate(), formatter);
        LocalDate endTime = LocalDate.parse(getStrEndDate(), formatter);
        LocalDate tempTime = startTime.plusMonths(getMonthSpace());
        if (tempTime.equals(endTime)) result = 0;
        else result = (int)tempTime.until(endTime,ChronoUnit.DAYS);

        return result;
    }

    /**
     * 获取两个时间之间间隔的天数
     * @return
     */
    public int getDateSpace() {
        int result = 0;
        Calendar calst = Calendar.getInstance();;
        Calendar caled = Calendar.getInstance();
        calst.setTime(startDate);
        caled.setTime(endDate);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数，算头算尾
        int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24 + 1;
        return days;
    }

    /**
     * 获取指定日期X月后的日期
     * @param startDate 起始日期
     * @param month 间隔的月数
     * @return x个月后的日期
     */
    public static Date getXMonthLaterTime(Date startDate,int month){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();

        cal.setTime(startDate);
        cal.add(Calendar.MONTH,month);

       return cal.getTime();
    }

    public static Date getXDaysLaterTime(Date startDate,int days){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();

        cal.setTime(startDate);
        cal.add(Calendar.DATE,days);

        return cal.getTime();
    }
}
