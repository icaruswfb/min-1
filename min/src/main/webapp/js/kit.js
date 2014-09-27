Kit = {
	init: function(){
		Kit.carregarProdutos();
		Kit.carregarServicos();
	},
		
	servicos : [],
	carregarServicos: function(){
		$.ajax({
			url:"/min/web/servicos/listar",
			type: "GET",
			success: function(servicos){
				Kit.servicos = servicos;
			}
		});
	},
	
	produtos: [],
	carregarProdutos: function(){
		$.ajax({
			url:"/min/web/produtos/listar",
			type: "GET",
			success: function(produtos){
				Kit.produtos = produtos;
			}
		});
	},
	
	calcularValor:function(){
		var somatoria = new Number(0);
		$.each($("select[name='servico']"), function(index, select){
			var id = $(select).attr("id");
			var value = $(select).val();
			if(value != null){
				for(var i = 0; i < Kit.servicos.length; i++){
					var servico = Kit.servicos[i];
					if(value == servico.id){
						var preco = new Number(servico.preco);
						somatoria += preco;
						$("#preco-" + id).val(preco.toFixed(2));
						break;
					}
				}
			}else{
				$("#preco-" + id).val("0,00");
			}
			$.each($("." + id), function(index, produto){
				var id = $(produto).attr("id");
				var value = $(produto).val();

				if(value != null){
					for(var i = 0; i < Kit.produtos.length; i++){
						var produto = Kit.produtos[i];
						if(value == produto.id){
							var preco = new Number(produto.precoRevenda);
							var quantidade = $("#quantidade-" + id).val();
							if(quantidade == ""){
								quantidade = 1;
								$("#quantidade-" + id).val(1);
							}
							preco = (preco * quantidade);
							somatoria += preco;
							$("#preco-" + id).val(preco.toFixed(2));
						}
					}
				}else{
					$("#preco-" + id).val("0,00");
				}
			});
		});
		$("#preco").val(new Number(somatoria).toFixed(2));
		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	
	addServico:function(){
		if(Kit.servicos == []){
			console.log("Aguardando processamento de dados para o kit...");
			setTimeout(function(){
				Kit.addServico();
			}, 500);
		}else{
			var id = Utils.guid();
			var html = "";
			html += "<div class='w-100 float-left' id='div-servico-"+id+"'>";
			html += 	"<div class='col-md-9'>";
			html += 		'<select name="servico" data-placeholder="Selecione um servi&ccedil;o..." class="tag-select-limited form-control m-b-10" multiple onchange="Kit.calcularValor()" id="servico-'+ id +'">';
			var servicos = Kit.servicos;
			for(var i = 0; i < servicos.length; i++){
				var servico = servicos[i];
				html += 		'<option value="'+ servico.id +'">'+servico.nome+'</option>';
			}
			html += 		'</select>';
			html += 	'</div>';
			html += 	"<div class='col-md-2'>";
			html += 		"<input class='m	ask-money form-control input-sm m-b-10' readonly disabled id='preco-servico-"+ id +"'></input>";
			html += 	'</div>';
			html += 	"<div class='col-md-1'><a href='javascript:Kit.deleteServico(\""+id+"\")'> <i class='sa-list-delete'></i></a></div>";
			html += 	"<div class='w-100 float-left'>";
			html += 		"<div class='col-md-1'> <span class='icon' style='float: right;font-size: 20px;'>&#61807;</span></div>";
			html += 		'<div class="col-md-11"><a href="javascript:Kit.addProduto(\''+id+'\')" ><span class="add-kit" >Adicionar produto</span></a><a href="javascript:Kit.addServico()" ><i class="sa-list-add add-kit-img"></i></a></div>';
			html += 	'</div>';

			html += 	"<div class='w-100 float-left m-b-20' id='bloco-servico-"+id+"'>";
			html += 	'</div>';
			html += '</div>';
			$("#bloco-servicos").append(html);

            $(".tag-select-limited").chosen({
                max_selected_options: 1
            });
            
            /* Overflow */
            $('.overflow').niceScroll();
		}
	},
	
	deleteServico: function(id){
		$("#div-servico-" + id).html("");
		Kit.calcularValor();
	},
	
	deleteProduto: function(id){
		$("#div-produto-" + id).html("");
		Kit.calcularValor();
	},
	
	addProduto:function(servico){
		var id = Utils.guid();
		var html = "<div class='w-100 float-left' id='div-produto-"+id+"'>";
		html += 		"<div class='col-md-1'></div>";
		html += 		"<div class='col-md-6'>";
		html += 			'<select name="produto" data-placeholder="Selecione um produto..." class="tag-select-limited form-control m-b-10 servico-'+servico+'" multiple onchange="Kit.calcularValor()" id="produto-'+id+'" >';
		var produtos = Kit.produtos;
		for(var i = 0; i < produtos.length; i++){
			var produto = produtos[i];
			html += 			'<option value="'+ produto.id +'">'+produto.nome + " (" + produto.unidade+")"+'</option>';
		}
		html += 			'</select>';
		html += 		"</div>";
		html += 		"<div class='col-md-2'>";
		html += 			"<input onchange='Kit.calcularValor()' placeholder='Quantidade' class='mask-number form-control input-sm m-b-10' id='quantidade-produto-"+ id +"'></input>";
		html += 		"</div>";
		html += 		"<div class='col-md-2'>";
		html += 			"<input class='mask-money form-control input-sm m-b-10' readonly disabled id='preco-produto-"+ id +"'></input>";
		html += 		"</div>";
		html += 		"<div class='col-md-1'><a href='javascript:Kit.deleteProduto(\""+id+"\")'> <i class='sa-list-delete'></i></a></div>";
		html += 	"</div>";
		$("#bloco-servico-" + servico).append(html);

        $(".tag-select-limited").chosen({
            max_selected_options: 1
        });
        
        /* Overflow */
        $('.overflow').niceScroll();
	},
	
	submit:function(){
		Utils.unmaskMoney();
		$("#kit-form").submit();
	}
};