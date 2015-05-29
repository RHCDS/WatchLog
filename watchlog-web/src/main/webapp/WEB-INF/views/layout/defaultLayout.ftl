<#macro myLayout>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="author" content="Martin Bean" />
    <title>日志分析系统</title>
    <link rel="stylesheet" href="/res/css/bootstrap.min.css" />
     <link rel="stylesheet" href="/res/css/layout.css" />
     <script src="/res/js/jquery-1.11.3.min.js"></script>
	<script src="/res/js/bootstrap.min.js"></script>
     <link href="/res/css/select2.min.css" rel="stylesheet" />
	<script src="/res/js/select2.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		  	$('#project_select').select2();
		});
	</script>

  </head>
  
  <body>
   	<div class="wrapper">
		<#include "header.ftl"/>
		
	     <#include "menu.ftl"/>
	   
	     <div class="container">  	<#nested>     	</div>
	               		

    		
    </div>
    
    <#include "footer.ftl"/>
	

	
  </body>
</html>
</#macro>