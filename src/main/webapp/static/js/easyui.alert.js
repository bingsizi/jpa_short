/**
 * zpr
 */
;var Popbox = (function($){
	/**
	 * 上右弹出
	 */
	function topRight(title,msg){
		$.messager.show({
			title:title,
			msg:msg,
			showType:'fade',
			style:{
				left:'',
				right:0,
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
	}
	/**
	 * 上中弹出
	 */
	function topCenter(title,msg){
		$.messager.show({
			title:title,
			msg:msg,
			showType:'fade',
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
	};
	/**
	 * 下右弹出
	 */
	function bottomRight(title,msg){
		$.messager.show({
			title:title,
			msg:msg,
			showType:'fade'
		});
	}
	return {
		topRight:topRight,
		topCenter:topCenter,
		bottomRight:bottomRight
	}
})(jQuery);

