package ackgames.acquire;

/**
 * Created by IntelliJ IDEA.
 * User: LAckner
 * Date: Apr 14, 2005
 * Time: 6:39:15 PM
 * To change this template use Options | File Templates.
 */
public class JSCalendarYear {
    private String htmlOutput =null;
     JSCalendarYear(int month, int year) {
         htmlOutput ="<TABLE CELLSPACING=10> \n ";
         htmlOutput +="<TR><TH colspan=4 align=\"TOP\">" +
                 +year+
                "</TH></TR> \n<TR>";
         int currentMonth=month;
         for (int i = 1; i <= 12; i++) {
               htmlOutput+="<TD ALIGN=\"CENTER\">"+ new JSCalendar(currentMonth,year).getHtmlOutput()+"</TD>";
               if(currentMonth== 12){
                   currentMonth=1;
                   year++;
               }else{
                   currentMonth++;
               }
               if (i%4 ==0){
                   htmlOutput+="</TR>\n<TR>";
               }

         }
          htmlOutput+="</TR>\n</TABLE>";

     }

    public String getHtmlOutput() {
        return htmlOutput;
    }

    public void setHtmlOutput(String htmlOutput) {
        this.htmlOutput = htmlOutput;
    }
}
