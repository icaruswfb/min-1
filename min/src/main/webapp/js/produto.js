Produto = {
		findEstoque: function(){
			var produtoId = $("#produto-id").val();
			$.ajax({
				url: '/min/web/produtos/listarEstoque/' + produtoId,
				type: 'GET',
				success:function(vo){
					$("#quantidade-estoque").val(vo.quantidade);
					$("#lancamentos-estoque").html('');
					for(var i = 0; i < vo.lancamentos.length; i++){
						var lancamento = vo.lancamentos[i];
						var 
						content = '<div class="media">';
							content += ' <div class="media-body">';
								content += Utils.formatDateTime(new Date(lancamento.dataCriacao)) + ": ";
								content += (lancamento.tipo == "SAIDA" ? '(-)' : '(+)') + " ";
								content += lancamento.quantidade;
								content += vo.produto.unidade;
								if(lancamento.comanda){
									content += " com <a class='text-muted' href='/min/web/clientes/editar/"+lancamento.comanda.cliente.id+"' >";
									content += lancamento.comanda.cliente.nome + "</a>";
								}
							content += '  </div>';
						content += '   </div>';
						$("#lancamentos-estoque").append(content);
					}
				}
			});
		},
		exibirEstoque:function(){
			if($("#estoque-block").css("display") == 'block'){
				$("#estoque-title-action").hide(100);
				$("#estoque-title-action").html("[+]");
				$("#estoque-title-action").show(100);
				$("#estoque-block").hide(300);
			}else{
				$("#estoque-title-action").hide(100);
				$("#estoque-title-action").html("[-]");
				$("#estoque-title-action").show(100);
				$("#estoque-block").show(300, function(){
						Produto.findEstoque();
					});
			}
		},
		adicionarLancamento: function(){
			$.ajax({
				url: '/min/web/produtos/addEstoque',
				type: 'POST',
				data: $("#novo-lancamento-form").serialize(),
				success: function(){
					Produto.findEstoque();
				}
			});
		}
};