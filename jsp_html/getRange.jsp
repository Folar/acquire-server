<%@ page import="mytable.*"%>

    <% 
      rows = request.getParameter("rows");
      cols = request.getParameter("cols");
      offset = request.getParameter("offset");
      System.out.println("xxxxxxxxxxx:cols=" +cols);
    %>
    <%=DummyData.getSection(offset,rows,cols);%>
