Utils = {
	showError:function(text){
		html = '<div class="alert alert-danger alert-dismissable fade in error-alert" style="display: none">' + 
		                  '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' + 
		                  '<div class="texto"></div>' + 
		              '</div>';
		$("section[id='content']").prepend(html);
		
		$(".error-alert .texto").html(text);
		$(".error-alert").show();
	}	,
	createMessageBlock:function(text, elementToAppend, serverity, alertId){
		serverity = serverity ? serverity : 'info';
		html = '<div class="alert alert-'+serverity+' alert-dismissable fade in message-alert" style="display: none" id="'+alertId+'">' + 
	        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' + 
	        '<div class="texto"></div>' + 
	    '</div>';
		$(elementToAppend).prepend(html);
		alertId = alertId ? ("#" + alertId) : '.message-alert';
		$(elementToAppend + " " + alertId +" .texto").html(text);
		$(elementToAppend + " " + alertId).show();
	}	,
	removeMessageBlock:function(elementoToRemove, alertId){
		alertId = alertId ? ("#" + alertId) : '.message-alert';
		$(elementoToRemove + " " + alertId).remove();
	},
	guid: function(){
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		    return v.toString(16);
		});
	},
	formatDateTime:function(date){
		var str = "" + ((date.getDate() < 10 ? "0" : '') + date.getDate()) ;
		str += "/" + (((date.getMonth() + 1) < 10 ? "0" : "") + (date.getMonth() + 1)) ;
		str += "/" + date.getFullYear();
		str += " " + ((date.getHours() < 10 ? "0" : "") + date.getHours());
		str += ":" + ((date.getMinutes() < 10 ? "0" : "") + date.getMinutes());
		return str;
	},
	formatTime:function(date){
		var str = "" ;
		str += " " + ((date.getHours() < 10 ? "0" : "") + date.getHours());
		str += ":" + ((date.getMinutes() < 10 ? "0" : "") + date.getMinutes());
		return str;
	}
};