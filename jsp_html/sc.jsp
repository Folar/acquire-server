<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://xdooracle.com/" prefix="cal" %>
<html>
<head>
     <style type="text/css">
        body {
           background-color: #ffffff; 
		}
    </style>
</head>
    <body  >
    <f:view>
     <h2>
           XML Publisher Scheduler
        </h2>
      <h:form >
       
        <hr></hr>
        <cal:calendar color="green"  columns="5" value="#{scheduler.dates}"/>
        <h:commandButton value="submit" />
      </h:form>
     </f:view>
    </body>
  </html>
