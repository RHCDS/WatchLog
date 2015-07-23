
function getParam( name )
  {
	   name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	   var regexS = "[\\?&]"+name+"=([^&#]*)";
	   var regex = new RegExp( regexS );
	   var results = regex.exec( window.location.href );
	   if( results == null )
	    return "";
	  else
	   return results[1];
  }
  
var pid = getParam( 'proj' );
		  	  
$(document).ready(function() {
	getProjects();
});

// 获取project
function getProjects(){
  	$.ajax({
  		type: 'GET',
		url: '/projects',
		success :function(e){
			var data = e['data'];
			var i=0, html = "";
			
			if(typeof(pid)=='string' && pid.length==0){
				html  =  '<option  selected="true" value="#"> 请选择项目</option>';
			}
			
			for(i=0; i<data.length; i++){
				var id = data[i]['id'],  name=data[i]['name'];
				if(data[i]['id'] == pid){
					html  +=  '<option  selected="true" value="/logsrc/manage?proj=' +id  + '"> ' + name + '</option>';
				}
				else{
					html  +=  '<option value="/logsrc/manage?proj=' +id  + '"> ' + name + '</option>';
				}
				
			}
			
			$("#project_select").empty().append(html);
			$('#project_select').select2();
		}
	})
}


// 启动和设置popover
function popover_setting(){
	// 启动popover 
	$("[data-toggle='popover']").popover(); 	  		
	// 每次点击只展示当前点击的popover
	$("[data-toggle='popover']").on('click', function (e) {
	    $("[data-toggle='popover']").not(this).popover('hide');
	});				
	// 点击空白处所有popover消失
	$('body').on('click', function (e) {
	    //did not click a popover toggle or popover
	    if ($(e.target).data('toggle') !== 'popover'
	        && $(e.target).parents('.popover.in').length === 0) { 
	        $('[data-toggle="popover"]').popover('hide');
	    }
	});			
}
