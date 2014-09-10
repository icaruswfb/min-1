Lancamento = {
		limparServico: function(){
			$("#form-servico select[name='servicoId']").val("");
			$("#form-servico select[name='funcionarioId']").val("");
			$("#form-servico select[name='assistenteId']").val("");
			$("#form-servico input[name='valor']").val("");
			$("#add-servico-msg").html("");
		},
		
		limparProduto: function(){
			$("#form-produto select[name='produtoId']").val("");
			$("#form-produto select[name='vendedorId']").val("");
			$("#form-produto input[name='quantidade']").val("");
			$("#form-produto input[name='valor']").val("");
			$("#add-produto-msg").html("");
		},
		
		limparProdutoServico: function(servicoId){
			$("#form-produto-servico select[name='produtoId']").val("");
			$("#form-produto-servico input[name='quantidade']").val("");
			$("#form-produto-servico input[name='valor']").val("");
			$("#add-produto-servico-msg").html("");
			$("#form-produto-servico input[name='servicoId']").val(servicoId);
		},
		
		preencherServicos: function(servicos){
			$.each(servicos, function(index, servico){
				$("#form-servico select[name='servicoId']").append("<option value='"+servico.id+"'>" + servico.nome + "</option>");
			});
		},
		preencherFuncionarios: function(funcionarios){
			$.each(funcionarios, function(index, funcionario){
				$("#form-servico select[name='funcionarioId']").append("<option value='"+funcionario.id+"'>" + funcionario.nome + "</option>");
				$("#form-servico select[name='assistenteId']").append("<option value='"+funcionario.id+"'>" + funcionario.nome + "</option>");
				$("#form-produto select[name='vendedorId']").append("<option value='"+funcionario.id+"'>" + funcionario.nome + "</option>");
			});
		},
		preencherProdutos: function(produtos){
			$.each(produtos, function(index, produto){

				if(produto.categoria != 'LOJA'){
					$("#form-produto-servico select[name='produtoId']").append("<option value='"+produto.id+"'>" + produto.nome + "</option>");
				}else{
					$("#form-produto select[name='produtoId']").append("<option value='"+produto.id+"'>" + produto.nome + "</option>");
				}
			});
		},
		
		addServico:function(){
			$("#add-servico-msg").html("");
			var hasError = false;
			if($("#form-servico select[name='servicoId']").val() == ''){
				hasError = true;
				Utils.createMessageBlock("Campo Servi&ccedil;o &eacute; obrigat&oacute;rio", "#add-servico-msg", "danger", "add-servico-msg" + Utils.guid());
			}
			if($("#form-servico select[name='funcionarioId']").val() == ''){
				Utils.createMessageBlock("Campo Funcion&aacute;rio &eacute; obrigat&oacute;rio", "#add-servico-msg", "danger", "add-servico-msg" + Utils.guid());
				hasError = true;
			}
			if( hasError){
				return false;
			}
			$("#fechar-modal-servico").click();
			$.ajax({
				url: '/min/web/clientes/addServico',
				type: 'POST',
				data: $("#form-servico").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				}
			});
		},
		
		deleteServico:function(servicoId){
			if(confirm("Tem certeza que deseja excluir?")){
				$.ajax({
					url: '/min/web/clientes/deleteServico/'+ $("#cliente-id").val() +'/'+servicoId,
					type: 'GET',
					success: function(comanda){
						Comanda.criarFormComanda(comanda);
						Comanda.preencherDadosComanda(comanda);
						$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					}
				});
			}
		},
		
		addProduto:function(){
			$("#add-produto-msg").html("");
			var hasError = false;
			if($("#form-produto select[name='produtoId']").val() == ''){
				hasError = true;
				Utils.createMessageBlock("Campo Produto &eacute; obrigat&oacute;rio", "#add-produto-msg", "danger", "add-produto-msg" + Utils.guid());
			}
			if($("#form-produto select[name='vendedorId']").val() == ''){
				Utils.createMessageBlock("Campo Vendedor &eacute; obrigat&oacute;rio", "#add-produto-msg", "danger", "add-produto-msg" + Utils.guid());
				hasError = true;
			}
			if($("#form-produto input[name='quantidade']").val() == ''){
				Utils.createMessageBlock("Campo Quantidade &eacute; obrigat&oacute;rio", "#add-produto-msg", "danger", "add-produto-msg" + Utils.guid());
				hasError = true;
			}
			if( hasError){
				return false;
			}
			$("#fechar-modal-produto").click();
			$.ajax({
				url: '/min/web/clientes/addProduto',
				type: 'POST',
				data: $("#form-produto").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				}
			});
		},
		
		deleteProduto:function(produtoId){
			if(confirm("Tem certeza que deseja excluir?")){
				$.ajax({
					url: '/min/web/clientes/deleteProduto/'+ $("#cliente-id").val() +'/'+produtoId,
					type: 'GET',
					success: function(comanda){
						Comanda.criarFormComanda(comanda);
						Comanda.preencherDadosComanda(comanda);
						$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					}
				});
			}
		},
		
		addProdutoServico:function(){
			$("#add-produto-servico-msg").html("");
			var hasError = false;
			if($("#form-produto-servico select[name='produtoId']").val() == ''){
				hasError = true;
				Utils.createMessageBlock("Campo Produto &eacute; obrigat&oacute;rio", "#add-produto-servico-msg", "danger", "add-produto-servico-msg" + Utils.guid());
			}
			if($("#form-produto-servico input[name='quantidade']").val() == ''){
				Utils.createMessageBlock("Campo Quantidade &eacute; obrigat&oacute;rio", "#add-produto-servico-msg", "danger", "add-produto-servico-msg" + Utils.guid());
				hasError = true;
			}
			if( hasError){
				return false;
			}
			$("#fechar-modal-produto-servico").click();
			$.ajax({
				url: '/min/web/clientes/addProdutoServico',
				type: 'POST',
				data: $("#form-produto-servico").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				}
			});
		},

		deleteProdutoServico:function(produtoId){
			if(confirm("Tem certeza que deseja excluir?")){
				$.ajax({
					url: '/min/web/clientes/deleteProdutoServico/'+ $("#cliente-id").val() +'/'+produtoId,
					type: 'GET',
					success: function(comanda){
						Comanda.criarFormComanda(comanda);
						Comanda.preencherDadosComanda(comanda);
						$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					}
				});
			}
		},
};