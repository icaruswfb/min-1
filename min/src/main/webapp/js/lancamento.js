Lancamento = {
		limparServico: function(){
			$(".search-choice-close").click();
			$("#form-servico select[name='funcionarioId']").val("");
			$("#form-servico select[name='assistenteId']").val("");
			$("#form-servico input[name='valor']").val("");
			$("#add-servico-msg").html("");
		},
		
		limparProduto: function(){
			$(".search-choice-close").click();
			$("#form-produto select[name='vendedorId']").val("");
			$("#form-produto input[name='quantidade']").val("");
			$("#form-produto input[name='valor']").val("");
			$("#add-produto-msg").html("");
		},
		
		limparProdutoServico: function(){
			$(".search-choice-close").click();
			$("#form-produto-servico input[name='quantidade']").val("");
			$("#form-produto-servico input[name='valor']").val("");
			$("#add-produto-servico-msg").html("");
		},
		limparKit: function(){
			$(".search-choice-close").click();
			$("#form-kit input[name='valor']").val("");
			$("#add-kit-msg").html("");
		},
		preencherKits:function(kits){
			$.each(kits, function(index, kit){
				$("#form-kit select[name='kitId']").append("<option value='"+kit.id+"'>" + kit.nome + "</option>");
			});
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
		
		buscarValorKit:function(){
			var kitId = $("#form-kit select[name='kitId']").val();
			$("#descricao-kit").html("");
			if(kitId != null){
				for(var i = 0; i < Comanda.kits.length; i++){
					var kit = Comanda.kits[i];
					if(kitId == kit.id){
						var valor = 0;
						$.each(kit.produtos, function(index, produto){
							var preco = new Number(produto.produto.precoRevenda * produto.quantidade );
							valor += preco;
							var html = "<p>" + produto.produto.nome + ", " + produto.quantidade + "" + produto.produto.unidade + " no valor de R$<span class='mask-money'>"+new Number(preco).toFixed(2)+"</span></p>";
							$("#descricao-kit").append(html);
						});
						$("#form-kit input[name='valor']").val(new Number(valor).toFixed(2));
						$('#form-kit .mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
						break;
					}
				}
			}else{
				$("#form-kit input[name='valor']").val("0,00");
			}
		},
		
		addKit:function(fechar){
			Utils.modalLoading();
			$("#add-kit-msg").html("");
			var hasError = false;
			if($("#form-kit select[name='kitId']").val() == '' || $("#form-kit select[name='kitId']").val() == null){
				hasError = true;
				Utils.createMessageBlock("Campo kit &eacute; obrigat&oacute;rio", "#add-kit-msg", "danger", "add-kit-msg" + Utils.guid());
			}
			if( hasError){
				Utils.modalLoadingFinish();
				return false;
			}

			if(fechar){
				$("#fechar-modal-kit").click();
			}
			$.ajax({
				url: '/min/web/clientes/addKit',
				type: 'POST',
				data: $("#form-kit").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});

					Lancamento.limparServico();
				},
				complete:function(){
					Utils.modalLoadingFinish();
				}
			});
		},
		addServico:function(fechar){
			Utils.modalLoading();
			$("#add-servico-msg").html("");
			var hasError = false;
			if($("#form-servico select[name='servicoId']").val() == '' || $("#form-servico select[name='servicoId']").val() == null){
				hasError = true;
				Utils.createMessageBlock("Campo Servi&ccedil;o &eacute; obrigat&oacute;rio", "#add-servico-msg", "danger", "add-servico-msg" + Utils.guid());
			}
			if($("#form-servico select[name='funcionarioId']").val() == ''){
				Utils.createMessageBlock("Campo Funcion&aacute;rio &eacute; obrigat&oacute;rio", "#add-servico-msg", "danger", "add-servico-msg" + Utils.guid());
				hasError = true;
			}
			if( hasError){
				Utils.modalLoadingFinish();
				return false;
			}

			if(fechar){
				$("#fechar-modal-servico").click();
			}
			$.ajax({
				url: '/min/web/clientes/addServico',
				type: 'POST',
				data: $("#form-servico").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});

					Lancamento.limparServico();
				},
				complete:function(){
					Utils.modalLoadingFinish();
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
		
		addProduto:function(fechar){
			Utils.modalLoading();
			$("#add-produto-msg").html("");
			var hasError = false;
			if($("#form-produto select[name='produtoId']").val() == '' || $("#form-produto select[name='produtoId']").val() == null){
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
				Utils.modalLoadingFinish();
				return false;
			}
			if(fechar){
				$("#fechar-modal-produto").click();
			}
			$.ajax({
				url: '/min/web/clientes/addProduto',
				type: 'POST',
				data: $("#form-produto").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});

					Lancamento.limparProduto();
				},
				complete:function(){
					Utils.modalLoadingFinish();
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
		
		addProdutoServico:function(fechar){
			Utils.modalLoading();
			$("#add-produto-servico-msg").html("");
			var hasError = false;
			if($("#form-produto-servico select[name='produtoId']").val() == '' || $("#form-produto-servico select[name='produtoId']").val() == null){
				hasError = true;
				Utils.createMessageBlock("Campo Produto &eacute; obrigat&oacute;rio", "#add-produto-servico-msg", "danger", "add-produto-servico-msg" + Utils.guid());
			}
			if($("#form-produto-servico input[name='quantidade']").val() == ''){
				Utils.createMessageBlock("Campo Quantidade &eacute; obrigat&oacute;rio", "#add-produto-servico-msg", "danger", "add-produto-servico-msg" + Utils.guid());
				hasError = true;
			}
			if( hasError){
				Utils.modalLoadingFinish();
				return false;
			}

			if(fechar){
				$("#fechar-modal-produto-servico").click();
			}
			$.ajax({
				url: '/min/web/clientes/addProdutoServico',
				type: 'POST',
				data: $("#form-produto-servico").serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});

					Lancamento.limparProdutoServico();
				},
				complete:function(){
					Utils.modalLoadingFinish();
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
		
		lancarDesconto:function(){
			Utils.unmaskMoney();
			var descontos = $("#comanda-form input[name='descontos']").val();
			var params = {
				desconto : descontos,
				clienteId : $("#cliente-id").val()
			};
			$.ajax({
				url: '/min/web/clientes/addDesconto',
				type: 'POST',
				data: params,
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
					$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				}
			});
		}
		
};