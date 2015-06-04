

$(function() {

	// 过滤关键字 AND/OR切换
	$('#filter_keyword_select_id').change(function(){ 
			var p1=$(this).children('option:selected').val();  //selected值 : AND OR
			$('.filter_keyword_td_select').html(p1);  //修改table第二个元素
		});
	
	// 正则表达式  AND/OR切换
	$('#reg_regex_select_id').change(function(){ 
		var p1=$(this).children('option:selected').val();  //selected值 
		$('.reg_regex_td_select').html(p1);  //修改table第二个元素
	});

	
});

// 过滤关键字 添加1行
function add_row_filter_keyword(){
	var select_val = $("#filter_keyword_select_id option:selected").val();   //selected值 : AND OR
	var cell_1_html ='<td class="col-md-6" style="padding:0px"><input type="text" class="form-control"  id="filter_keyword_input_id"   name="filter_keyword_arr[]"    style="display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;" ></td>';
	var cell_2_html = '<td class="col-md-1 filter_keyword_td_select">'+select_val+'</td>';
	var tr = '<tr>' + cell_1_html  + cell_2_html + '</tr>';	
	$('#filter_keyword_table > tbody:last-child').append(tr);
}


//正则表达式 添加1行
function add_row_reg_regex(){
	
	var select_val = $("#reg_regex_select_id option:selected").val();    //selected值 : AND OR
	var cell_1_html ='<td class="col-md-6" style="padding:0px"><input type="text" class="form-control"  id="reg_regex_input_id"   name="reg_regex_arr[]"  style="display: block;padding: 0px;margin: 0px;border: 0;width: 100%;border-radius: 0;line-height: 1;" ></td>';
	var cell_2_html = '<td class="col-md-1 reg_regex_td_select">'+select_val+'</td>';
	var tr = '<tr>' + cell_1_html  + cell_2_html + '</tr>';	
	$('#reg_regex_table > tbody:last-child').append(tr);
	
}


