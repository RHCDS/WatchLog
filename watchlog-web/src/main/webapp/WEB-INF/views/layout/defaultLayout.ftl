<#macro myLayout>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="author" content="Martin Bean" />
    <title>日志分析系统</title>
    
       <!--  jquery  -->    
     <script src="/res/js/jquery-1.11.3.min.js"></script>
     
         
     <!--  bootstrap  -->    
    <link rel="stylesheet" href="/res/css/bootstrap.min.css" />
	<script src="/res/js/bootstrap.min.js"></script>
	

	<!--  select2 plugin -->
     <link href="/res/css/select2.min.css" rel="stylesheet" />
	<script src="/res/js/select2.min.js"></script>
	

     <!--  self defined  -->    
	 <link rel="stylesheet" href="/res/css/layout.css" />
	 
	<#if controller == "WlogManage">
	<script src="/res/js/manage.js"></script>
	</#if>
	
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