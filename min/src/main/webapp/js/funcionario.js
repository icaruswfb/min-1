Funcionario = {
	submit:function(){
		var percents = $(".mask-percent");
		for(var i = 0; i < percents.length; i++){
			var percent = percents[i];
			var valor = $(percent).val();
			valor = valor.replace(",", ".");
			valor = valor.replace("%", "");
			$(percent).val(valor);
		}
		var money = $(".mask-money");
		for(var i = 0; i < money.length; i++){
			var m = money[i];
			var valor = $(m).val();
			valor = valor.replace(".", "");
			valor = valor.replace(",", ".");
			valor = valor.replace("R$", "");
			$(m).val(valor);
		}
		$("#funcionario-form").submit();
	}	
};