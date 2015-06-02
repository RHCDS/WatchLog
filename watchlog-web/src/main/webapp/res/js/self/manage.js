

  $(document).ready(function() {
	  var pid = getParam( 'proj' );
	  console.log(pid); //tmp log
	  
	  // remove datatable warning alert message
	  $.fn.dataTableExt.sErrMode = 'throw';  

        $('#logsrc_table').dataTable( {
            "processing": false,  
            "serverSide": true,
            "ajax": {
                "url": "manage/tabledata",
                "data": function ( d ) {
                    d.proj = pid;
                }
            },
            "columns": [
                { "data": "logsrc_name" },
                { "data": "host_name" },
                { "data": "logsrc_path" },
                { "data": "logsrc_file" },
                { "data": "status" },
                { "data": "update_time" },
                { "data": "creator" }
            ]

        } );
    } );
  
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
  
function test(){
	$.ajax({
		url: '/api/logsource/1',
		success :function(data){
			$('#result').html(data);
		}
	})
}


