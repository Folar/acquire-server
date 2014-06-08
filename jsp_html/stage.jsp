<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
-->
 <%response.addHeader("Cache-Control","no-cache");%>
<html>
<head>
	<title>fegAcquire</title>
	<% String loginname = request.getParameter("name");
		System.out.println("start loginname= "+loginname);
      session.setAttribute("name",loginname);
      session.setAttribute("gameId","3");
    %>
	<style type="text/css">
		div.main {
			width:auto
			height:auto;
		}

		div.stats {
			float:right;
			width:500px
			height:auto;
			padding-right: 100px;
		}

		.xxx {
			background-color:#b0b0ff;
			font-family:garamond;
			font-size:16pt;
			font-weight:bold;
			font-style:italic;
			color:purple;
	     }

		.Luxor{color:red; font-family:"franklin gothic medium"; font-weight:bold}
		.Tower{color:yellow; font-family:"franklin gothic medium"; font-weight:bold}
		.American{color:#8787FF; font-family:"franklin gothic medium"; font-weight:bold}
		.WorldWide{color:#B3AF91; font-family:"franklin gothic medium"; font-weight:bold}
		.Festival{color:#80FF80; font-family:"franklin gothic medium"; font-weight:bold}
		.Continental{color:cyan; font-family:"franklin gothic medium"; font-weight:bold}
		.Imperial{color:pink; font-family:"franklin gothic medium"; font-weight:bold}

	</style>
	
</head>

<body>
	<div class='main'>
	<h1>jj</h1>
		<div id='toppart'>
		</div>	
		<%@include file="board.jsp" %>
		
	</div>
	<div class='stats'>
		
		<div id="stats1"></div>
	
		<BR>
		<HR>
		<BR>
		

		<div id="stats2"></div>
	
	</div>
	<INPUT ID="hack" TYPE="HIDDEN" NAME="inmsg" VALUE = ""  >

</body>
</html>

