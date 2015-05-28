<#macro myLayout>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="author" content="Martin Bean" />
    <title>日志分析系统</title>
    <link rel="stylesheet" href="res/css/bootstrap.min.css" />
     <link rel="stylesheet" href="res/css/layout.css" />
  </head>
  
  <body>
   	<div class="wrapper">
		<#include "header.ftl"/>
		
	     <#include "menu.ftl"/>
	   
	     <div class="container">  	<#nested>     	</div>
    </div>
    
    <#include "footer.ftl"/>
	
	<script src="/res/js/jquery-1.11.3.min.js"></script>
	<script src="/res/js/bootstrap.min.js"></script>
	
  </body>
</html>
</#macro>