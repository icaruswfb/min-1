Financeiro = {

		adicionarLancamento: function(){
			Utils.unmaskMoney();
			$.ajax({
				url: '/min/web/report/addLancamento',
				type: 'POST',
				data: $("#novo-lancamento-form").serialize(),
				success: function(){
					$("#fluxo-data-form").submit();
				}
			});
		}

};