
// 获取指定url和指定名字参数
function getParam( name, href_str )
  {
	   name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	   var regexS = "[\\?&]"+name+"=([^&#]*)";
	   var regex = new RegExp( regexS );
	 //  var results = regex.exec( window.location.href );
	   var results = regex.exec(href_str );
	   if( results == null )
	    return "";
	  else
	   return results[1];
  }
  

//var log_id = getParam( 'log_id',"/logsrc/rt_analyse?proj=38&amp;log_id=2" );


function rt_analyse_refresh(){
	// 获取当前选中日志源的log_id和proj
	var select_url = $("#logsrc_list").children(".active").attr('href');
	var pid = getParam( 'proj',select_url );
	var log_id =  getParam( 'log_id', select_url);
	console.log(pid);
	console.log(log_id);
	
	
	
}