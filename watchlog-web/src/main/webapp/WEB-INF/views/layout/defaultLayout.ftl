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
     <link href="/res/css/plugin/select2/select2.min.css" rel="stylesheet" />
	<script src="/res/js/plugin/select2/select2.min.js"></script>
	
	<!-- dataTables plugin -->
	<link href="/res/css/plugin/dataTables/jquery.dataTables.min.css"  rel="stylesheet"/>
	<script src="/res/js/plugin/dataTables/jquery.dataTables.min.js"></script>

     <!--  self defined  -->    
	 <link rel="stylesheet" href="/res/css/self/layout.css" />
	 <script src="/res/js/self/layout.js" ></script>
	 	 
	 <#if controller == "WlogManage">
	 <script src="/res/js/self/manage.js"></script>
     <link href="/res/css/plugin/table/bootstrap-table.min.css" rel="stylesheet" />
	<script src="/res/js/plugin/table/bootstrap-table.min.js"></script>	 
	 </#if>
	 

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