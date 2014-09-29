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
	
	entity:{},
	
	calcularValor:function(){
		if(Kit.servicos.length == 0){
			console.log("Aguardando processamento de dados para o kit...");
			setTimeout(function(){
				Kit.calcularValor();
			}, 500);
		}else{
			Kit.entity.id = $("#id").val();
			Kit.entity.servicos = [];
			var somatoria = new Number(0);
			$.each($("select[name='servico']"), function(index, select){
				var id = $(select).attr("id");
				var value = $(select).val();
				var servicoEntity = {};
				servicoEntity.id = value == null ? null : value[0];
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
				Kit.entity.servicos.push(servicoEntity);
			});
			
			

			Kit.entity.produtos = [];
			$.each($("select[name='produto']"), function(index, produto){
				var id = $(produto).attr("id");
				var value = $(produto).val();
				var produtoEntity = {};
				produtoEntity.id =  value == null ? null : value[0];
				var quantidade = $("#quantidade-" + id).val();
				if(quantidade == ""){
					quantidade = 1;
					$("#quantidade-" + id).val(1);
				}
				produtoEntity.quantidade = quantidade;
				if(value != null){
					for(var i = 0; i < Kit.produtos.length; i++){
						var produto = Kit.produtos[i];
						if(value == produto.id){
							var preco = new Number(produto.precoRevenda);
							preco = (preco * quantidade);
							somatoria += preco;
							$("#preco-" + id).val(preco.toFixed(2));
						}
					}
				}else{
					$("#preco-" + id).val("0,00");
				}
				Kit.entity.produtos.push(produtoEntity);
			});
			
			
			$("#preco").html(new Number(somatoria).toFixed(2));
			Kit.entity.nome = $("#nome").val();
			$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		}
	},
	
	addServico:function(outterId){
		if(Kit.servicos.length == 0){
			console.log("Aguardando processamento de dados para o kit...");
			setTimeout(function(){
				Kit.addServico(outterId);
			}, 500);
		}else{
			var id = outterId == null ?Utils.guid() : outterId;
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
			html += 		"<input class='mask-money form-control input-sm m-b-10' readonly disabled id='preco-servico-"+ id +"'></input>";
			html += 	'</div>';
			html += 	"<div class='col-md-1'><a href='#' onclick='Kit.deleteServico(\""+id+"\")'> <i class='sa-list-delete'></i></a></div>";
			html += '</div>';
			$("#bloco-servicos").append(html);

            $(".tag-select-limited").chosen({
                max_selected_options: 1
            });
            
            /* Overflow */
            $('.overflow').niceScroll();

            $('.mask-number').mask('##0');
            $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
            
            return id;
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
	
	addProduto:function(servico, outterId){
		var id = outterId == null ?Utils.guid() : outterId;
		var html = "<div class='w-100 float-left' id='div-produto-"+id+"'>";
		html += 		"<div class='col-md-7'>";
		html += 			'<select name="produto" data-placeholder="Selecione um produto..." class="tag-select-limited form-control m-b-10 " multiple onchange="Kit.calcularValor()" id="produto-'+id+'" >';
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
		html += 		"<div class='col-md-1'><a href='#' onclick='Kit.deleteProduto(\""+id+"\")'> <i class='sa-list-delete'></i></a></div>";
		html += 	"</div>";
		$("#bloco-produto").append(html);

        $(".tag-select-limited").chosen({
            max_selected_options: 1
        });
        
        /* Overflow */
        $('.overflow').niceScroll();

        $('.mask-number').mask('##0');
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
        return id;
	},
	
	submit:function(){
		Utils.unmaskMoney();
		var nome = $("#nome").val();
		if(nome == "" || nome == null){
			Utils.showError("O campo Nome &eacute; obrigat&oacute;rio.");
		}else{
			Kit.calcularValor();
			$.ajax({
				url: "/min/web/kits/salvar",
				type: 'POST',
				data: JSON.stringify(Kit.entity),
				contentType: 'application/json',
				success: function(ok){
					if(ok == "OK"){
						window.location = "/min/web/kits/";
					}else{
						Utils.showError("Erro ao persistir");
					}
				}
			});
		}
		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	}
};