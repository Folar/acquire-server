<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<script language="JavaScript">
var req=getHTTPObject() ;
var sreq=getHTTPObject() ;

function sub() {
    obj=document.getElementById("inmsg");
    str = obj.value;
   if(str.length == 0){
		alert("must not be blank");
		return false;
   }
   obj.value="";
   sendComment(str);
}
function callback2() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            str=req.responseText;
            req.abort();
            str= str.substring(str.indexOf("\"")+1,str.lastIndexOf("\""));
            setText(str);
	    a();
        }
    }

}
function setText(txt) {
        ta =document.getElementById("info");
	if(txt.length != 0){  
		ma=txt.split("#");
		str="";
		for(i=0;i<ma.length;i++){
			if(i==0){
				str=ma[i];
			}else{
				str=str+"\n"+ma[i];
			}
		}
		if(ta.value.length == 0){  
			ta.value=str;
		} else{
			ta.value= ta.value +"\n"+ str;
			rng=ta.createTextRange();
			rng.scrollIntoView(false);
		}
	}
}
function sendComment(str) {
                var SendChaturl = "chatSender.jsp?inmsg="+str+"&id=" + <%= java.lang.Math.random() %>;
		sreq.open("GET", SendChaturl, true);
  	        sreq.onreadystatechange = handlehHttpSendChat;
  		sreq.send(null);
}
function  handlehHttpSendChat()
{
}
function load() {
    setTimeout('a();',4000); //executes the next data query in 4 seconds
}
function a() {
    var url = "chatListener.jsp?id=" + <%= java.lang.Math.random() %>;
    //req.open("GET", url, true);
    //req.onreadystatechange = callback2;
    //req.send(null);
}

//initiates the XMLHttpRequest object
//as found here: http://www.webpasties.com/xmlHttpRequest
function getHTTPObject() {
  var xmlhttp;
  /*@cc_on
  @if (@_jscript_version >= 5)
    try {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
      try {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
      } catch (E) {
        xmlhttp = false;
      }
    }
  @else
  xmlhttp = false;
  @end @*/
  if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
    try {
      xmlhttp = new XMLHttpRequest();
    } catch (e) {
      xmlhttp = false;
    }
  }
  return xmlhttp;
}
</script>
</HEAD>
<BODY  onLoad="a()" BGCOLOR=#b0b0ff>

  <CENTER>
        <TEXTAREA name="info"  id="info" rows="3" cols="30" wrap="hard"></TEXTAREA>
        Type:<INPUT ID="inmsg" TYPE="TEXT" NAME="inmsg" VALUE = ""  >
        <input type="button" value="Enter" ID="ent_but" onClick=sub(); STYLE ="width:50;height:25">
  </CENTER>

</BODY>
</HTML>
