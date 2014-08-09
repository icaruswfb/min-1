$(document).ready(function()
	    {
	        $('#imagem').on('change', function()
	        {
	        	Funcionario.uploadFotoPerfil();
	        });

});

Funcionario = {
		uploadFotoPerfil: function(){
			$("#upload").attr("enctype", "multipart/form-data");
			 $("#upload").submit(function(event){
	         	event.preventDefault();
	         	var formData = new FormData($(this)[0]);

	         	$.ajax({
	         	    url: '/min/web/upload/',
	         	    type: 'POST',
	         	    data: formData,
	         	    async: false,
	         	    cache: false,
	         	    contentType: false,
	         	    processData: false,
	         	    success: function (returndata) {
	         	      $("#imagem-id").val(returndata);
	         	      $.ajax({
	         	    	  url: "/min/web/funcionarios/alterarImagem/"+ $("#funcionario-id").val() +"/" + returndata,
	         	    	  type: "GET",
	         	    	  success:function(data){
	         	    		  
	         	    	  }
	         	      });
	         	    }
	         	  });
	         	  
	         	return false;
		         });         
			$("#upload").submit();
		},
	submit:function(){
		$("#funcionario-form").removeAttr("enctype");
		$("#funcionario-form").attr("action", "/min/web/funcionarios/salvar");
		var percents = $(".mask-percent");
		for(var i = 0; i < percents.length; i++){
			var percent = percents[i];
			var valor = $(percent).val();
			valor = valor.replace(",", ".");
			valor = valor.replace("%", "");
			$(percent).val(valor);
		}
		Utils.unmaskMoney();
		
		$("#funcionario-form").submit();
	}	
};