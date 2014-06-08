<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="ackgames.acquire.*"%>
	<%  
                String msgStr="";
	        System.out.println(" hfdg chat name="+session.getAttribute("name"));
		String mStr =request.getParameter("inmsg");
		session.setAttribute("msg","\"\"");
		StringBuffer outArg=new StringBuffer();
		StringBuffer outCmd=new StringBuffer();
		StringBuffer msg=new StringBuffer();
		StringBuffer tile=new StringBuffer();
	        BoardGenerator bg= new BoardGenerator();
		if(mStr!=null && mStr.length()!=0){
	                System.out.println("mstr="+mStr);
			int s= bg.processCmd(0, (String)(session.getAttribute("name")),
                                  mStr,outCmd,
                                  outArg,tile,msg);
                        mStr=session.getAttribute("name")+ ":"+mStr;				
		}else{
			mStr="";
		}
  	%>
<HTML>
<HEAD>
  <TITLE>acquire</TITLE>

<script language="JavaScript">



function sub(str) {
	obj3 =document.getElementById("inmsg");
        obj3.value=str;
	obj = document.getElementById("cmdForm");	
   	obj.submit();	
}

function setText2() {

        im =parent.frames[0].document.getElementById("inmsg");
        im.value=""; 
	obj3 =document.getElementById("outmsg");
        setText(obj3.value);
	asyncCom();
}





</script>
</HEAD>
<BODY BGCOLOR="#5092E4" >
<FORM ACTION="/chatcmd.jsp" NAME="cmdForm" ID="cmdForm"  >
  <CENTER>
  inmsgf:<INPUT ID="inmsg" TYPE="TEXT" NAME="inmsg" VALUE = <%=mStr %>  ><BR>
  outmsg:<INPUT ID="outmsg" TYPE="TEXT" NAME="outmsg"  VALUE =<%=mStr%>  ><BR>
    <INPUT TYPE="SUBMIT" VALUE="State">
  </CENTER>
</FORM>
<%!
	private String outArgStr="\"\"";
	private String outCmdStr="\"\"";
	private String tileStr="\"\"";
	
 %>
</BODY>
</HTML>
