package ackgames.acquire;

import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: LAckner
 * Date: Apr 14, 2005
 * Time: 3:23:19 PM
 * To change this template use Options | File Templates.
 */
public class JSCalendar {
    public static String months[] = {"January", "Febuary", "March", "April",
                                     "May", "June", "July", "August", "September",
                                     "October", "November", "December"};
    public static int daysInMonths[] = {31, 28, 31, 30,
                                        31, 30, 31, 31, 30,
                                        31, 30, 31};
    public static String days[] = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};

    private int month;
    private int year;




    private String htmlOutput = null;
    JSCalendar(int month, int year) {
        this.month=month;
        this.year = year;

        GregorianCalendar gc = new GregorianCalendar(year, month - 1, 1);
        int firstDayofMonth = gc.get(Calendar.DAY_OF_WEEK);
        setHtmlOutput("<TABLE>\n <TR><TH colspan=7 align=\"CENTER\">" +
                months[month - 1] +" "+year+
                "</TH></TR> \n<TR>");
        for(int i = 0; i < 7; i++) {
           setHtmlOutput(getHtmlOutput() + ("<TD>"+days[i]+"</TD>"));
        }
        setHtmlOutput(getHtmlOutput() + "</TR>\n<TR>\n");
        int j=1;
        int lastDayOfMonth = daysInMonths[month-1];
        if (month==2 && isLeapYear()){
            lastDayOfMonth++;
        }
        for(int i = 1; i <= 42; i++) {
            if(i<firstDayofMonth || lastDayOfMonth<j){
                setHtmlOutput(getHtmlOutput() + "<TD></TD>\n");
            }else{
                String layerName = "CAL_"+year+"_"+month+"_"+j;
                setHtmlOutput(getHtmlOutput() + ("<TD  ALIGN=\"CENTER\" ID=\""+layerName+"\" STYLE=\""+
		                 "font-family:sans-serif;font-size:8pt;font-weight:bold;\""+
                 "onClick=selectDate(\""+layerName+"\");>"+j++ +"</TD>\n"));
            }
            if(i!=0 && i%7==0){
                  setHtmlOutput(getHtmlOutput() + "</TR><TR>\n");
            }
        }
        setHtmlOutput(getHtmlOutput() + "</TR>\n</TABLE>");
        System.out.println(getHtmlOutput());
    }
    private boolean isLeapYear(){
        return year%4 == 0;
    }

    public static void main(String[] args) {
        JSCalendar jsc = new JSCalendar(2, 2008);
    }

    public String getHtmlOutput() {
        return htmlOutput;
    }

    public void setHtmlOutput(String htmlOutput) {
        this.htmlOutput = htmlOutput;
    }
}
