<#macro myLayout>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>日志分析系统</title>
       <!--  jquery  -->    
     <script src="/res/js/jquery-1.11.3.min.js"></script>
         
     <!--  bootstrap  -->    
    <link rel="stylesheet" href="/res/css/bootstrap.min.css" />
	<script src="/res/js/bootstrap.min.js"></script>
	
	<!--  select2 plugin -->
     <link href="/res/css/plugin/select2/select2.min.css" rel="stylesheet" />
	<script src="/res/js/plugin/select2/select2.min.js"></script>
	
	<!-- table plugin -->
	     <link href="/res/css/plugin/table/bootstrap-table.min.css" rel="stylesheet" />
		<script src="/res/js/plugin/table/bootstrap-table.min.js"></script>	 
		
		<!--datetime picker -->
	 <#if controller == "WlogPM">
		<link href="/res/css/plugin/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" />	 
	 	<script src="/res/js/plugin/datetimepicker/moment.min.js"></script>
		 <script src="/res/js/plugin/datetimepicker/bootstrap-datetimepicker.min.js"></script>
	 </#if>		

     <!--  self defined  -->    
	 <link rel="stylesheet" href="/res/css/self/layout.css" />
	 <script src="/res/js/self/layout.js" ></script>
	 	 
	 <#if controller == "WlogManage">
	  	<link rel="stylesheet" href="/res/css/self/logsrc_manage.css" />
		 <script src="/res/js/self/logsrc_table.js"></script>
		  <script src="/res/js/self/logsrc_crud.js"></script>
	 </#if>
	 
	 <#if controller == "WlogRT">
	  	<link rel="stylesheet" href="/res/css/self/logsrc_rt.css" />
	  	<script src="/res/js/self/rt_crud.js"></script>
	 </#if>	
	 
	 <#if controller == "WlogPM">
	  	<link rel="stylesheet" href="/res/css/self/logsrc_pm.css" />
		 <script src="/res/js/self/pm_table.js"></script>
		 <script src="/res/js/self/pm_crud.js"></script>
	 </#if>
	 
	 <#if action == "debug">
	 	<script src="/res/js/self/logsrc_debug.js"></script>
	 </#if>	 
  </head>
  
  <body>
   	<div class="wrapper">
		<#include "header.ftl"/>
	     <#include "menu.ftl"/>
	     <#if message?exists>	 <#include "notice.ftl"/> </#if> 	
	     	<#nested>     	
    </div>
    <#include "footer.ftl"/>
  </body>
</html>
</#macro>