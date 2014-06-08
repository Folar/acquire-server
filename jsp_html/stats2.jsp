

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AJAX calls using Jquery in Servlet</title>
         <script type="text/javascript" src="jquery-1.3.2.js"></script>  
    
          <script>
                jQuery(document).ready(function() {                        
                   
                    	debugger;
                       
                   $.get('game',{command:3},function(responseText) { 
                        $('#welcometext').html(responseText);         
                    })         
                      
                });
            </script>
    </head>
    <body>
  
    <div id="welcometext">
    </div>

    </body>
</html>
    
    
  
