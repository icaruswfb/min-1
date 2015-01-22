Comanda = {
	hasRoleCaixa: false,
	hasRoleOperacional: false,
	hasRoleAdmin: false,
	kits:[],
	servicos : [],
	produtos: [],
	funcionarios: [],
	comandas:[],
	findKits: function(){
		$.ajax({
			url: '/min/web/kits/listar',
			type: 'GET',
			success: function(kits){
				Comanda.kits = kits;
				Lancamento.preencherKits(kits);
			}
		});
	},
	findServicos: function(){
		$.ajax({
			url: '/min/web/servicos/listar',
			type: 'GET',
			success: function(servicos){
				Comanda.servicos = servicos;
				Lancamento.preencherServicos(servicos);
			}
		});
	},
	findProdutos: function(){
		$.ajax({
			url: '/min/web/produtos/listar',
			type: 'GET',
			success: function(produtos){
				Comanda.produtos = produtos;
				Lancamento.preencherProdutos(produtos);
			}
		});
	},
	findFuncionarios: function(){
		$.ajax({
			url: '/min/web/funcionarios/listar',
			type: 'GET',
			success: function(funcionarios){
				Comanda.funcionarios = funcionarios;
				Lancamento.preencherFuncionarios(funcionarios);
			}
		});
	},
	
	init:function(){
		Comanda.findFuncionarios();
		Comanda.findProdutos();
		Comanda.findServicos();
		Comanda.findKits();
		Comanda.checkDados();
	},
	checkDados:function(){
		if(Comanda.funcionarios.length < 1 || Comanda.produtos.length < 1 || Comanda.servicos.length < 1 ){
			console.log("Aguardando processamento de dados para a comanda...");
			setTimeout(function(){
				Comanda.checkDados();
			}, 500);
		}else{
			Comanda.findComandaAberta();

            $(".tag-select-limited").chosen({
                max_selected_options: 1
            });
            $('.overflow').niceScroll();
		}
	},
	findComandaAberta:function(){

		var id = $("#cliente-id").val();
		$.ajax({
			url: '/min/web/clientes/findComandaAberta/' + id,
			type: 'GET',
			success: function(comanda){
				if(comanda == ""){
					var divAcao = '<a class="btn btn-lg m-l-5 m-b-10" onclick="Comanda.abrirComanda()" >Abrir comanda</a>';
					$("#comanda-form").prepend(divAcao);
				}else{
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
				}
			}
		});
		
	},
	expandirComandasAntigas:function(){
		if($("#comandas-fechadas").css('display') == 'block'){
			$("#comandas-fechadas").hide(300);
			$("#comandas-fechadas-acao").hide(100);
			$("#comandas-fechadas-acao").html("[+]");
			$("#comandas-fechadas-acao").show(100);
		}else{
			Comanda.findComandas();
		}
	},
	findComandas: function(){
		var id = $("#cliente-id").val();
		$.ajax({
			url: '/min/web/clientes/findComandasFechadas/' + id,
			type: 'GET',
			success: function(comandas){
				$("#comandas-fechadas").html('');
				Comanda.comandas = comandas;
				for(var i = 0; i<comandas.length; i++){
					var comanda = comandas[i];
					Comanda.criarFormComanda(comanda);
				}
				Comanda.mostrarAntigas();
			}
		});
	}	,
	mostrarAntigas: function(){
		$("#comandas-fechadas").show(300);
		$("#comandas-fechadas-acao").hide(100);
		$("#comandas-fechadas-acao").html("[-]");
		$("#comandas-fechadas-acao").show(100);
	},
	appendLinhaServico:function(linha, id){
		id = id == "" ? "#comanda-form" : id;
		$(id + " .bloco-servicos").append(linha);
//		$(id + " .bloco-servicos select").chosen({
//            max_selected_options: 1
//        });
		$(".mask-number").mask("##########0");
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	appendLinhaProduto:function(linha, id){
		id = id == "" ? '' : id;
		$(id + " .bloco-revenda").append(linha);
//		$(id + " .bloco-produtos select").chosen({
//            max_selected_options: 1
//        });

		$(".mask-number").mask("##########0");
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},

	appendLinhaProdutoServico:function(linha, id){
		id = id == "" ? '' : id;
		$(id + " .bloco-produtos").append(linha);
//		$(id + " .bloco-produtos select").chosen({
//            max_selected_options: 1
//        });

		$(".mask-number").mask("##########0");
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	criarLinhaServico: function(lancamentoServico, readOnly, comandaId){
		readOnly = readOnly == undefined ? "" : readOnly;
		var id = "servico-"+Utils.guid();
		var form = "<div id='" +id+ "' class='lancamento'>";
		form += "<div class='col-md-12'>";
		form += "<div class='float-left m-r-10'><strong>" + lancamentoServico.servico.nome + "</strong>, por <strong><a href='/min/web/funcionarios/editar/"+lancamentoServico.funcionario.id+"'>"+ 
						(lancamentoServico.funcionario.deleted == null ? "": "[Exclu&iacute;do] ") + (lancamentoServico.funcionario.nome) + "</a></strong>";
		if(lancamentoServico.assistente){
			form += " e auxiliado por <strong>" + lancamentoServico.assistente.nome + "</strong>";
		}
		form += "</div>";
		form += "<div class='float-right'>";
		form += "<strong class='m-r-10' >R$<span class='mask-money valorServico' >"+new Number(lancamentoServico.valor).toFixed(2)+"</span></strong>";
		if(!readOnly && Comanda.hasRoleAdmin){
			form += '<a href="javascript:Lancamento.deleteServico(\''+lancamentoServico.id+'\')" title="" class="m-l-10 float-right" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
		form += "</div>";
		form += "</div>";
		form += "<div class='clearfix' ></div>";
		if(lancamentoServico){
			for(var i = 0; i < lancamentoServico.produtosUtilizados.length; i++){
				form += Comanda.criarProdutoAoServico(id, lancamentoServico.produtosUtilizados[i], readOnly);
			}
		}
		form += "</div>";
		return form;
	},
	
	buscarValorServico: function(){
		Utils.unmaskMoney();
		var servicoId = $("#form-servico select[name='servicoId']").val();
		if(servicoId != null){
			for(var i = 0; i < Comanda.servicos.length; i++){
				var servico = Comanda.servicos[i];
				if(servico.id == servicoId){
					$("#form-servico input[name='valor']").val(new Number(servico.preco).toFixed(2));
				}
			}
		}else{
			$("#form-servico input[name='valor']").val("0.00");
		}
		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});

		return true;
	},
	
	buscarValorProduto: function(servico){
		Utils.unmaskMoney();
		id = servico == null ? 'form-produto' : ('form-produto-' + servico);
		var produtoId = $("#" + id + " select[name='produtoId']").val();
		if(produtoId != "-"){
			for(var i = 0; i < Comanda.produtos.length; i++){
				var produto = Comanda.produtos[i];
				if(produto.id == produtoId){
					var quantidade;
					if($("#" + id + " input[name='quantidade']").val() == ""){
						quantidade = 1;
						$("#" + id + " input[name='quantidade']").val(quantidade);
					}else{
						quantidade = $("#" + id + " input[name='quantidade']").val();
					}
					$("#" + id + " input[name='valor']").val(new Number(produto.precoRevenda * quantidade).toFixed(2));
					$("#" + id + " .label-quantidade").html("Quantidade (" + produto.unidade + "):");
					break;
				}
			}
		}else{
			$("#" + id + " input[name='valorProduto"+servico+"']").val("");
			$("#quantidade-" + id).html("Quantidade:");
			$("#" + id + " input[name='quantidadeProduto"+servico+"']").val("");
		}
		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		Comanda.preencherTotais($("#comanda-form input[name='comandaId']").val());

		return true;
	},
	
	preencherTotais:function(comandaId){
		Utils.unmaskMoney();
		var total = new Number(0.00);
		var valoresServicos = $("#bloco-geral-comanda-"+comandaId+" .valorServico");
		var totalServicos = 0;
		for(var i = 0; i < valoresServicos.length; i++){
			var valorServico = $(valoresServicos[i]).html();
			totalServicos += new Number(valorServico); 
		}
		total += totalServicos; 
		
		var valoresProdutos = $("#bloco-geral-comanda-"+comandaId+" .valorProduto");
		var totalProdutos = 0;
		for(var i = 0; i < valoresProdutos.length; i++){
			var valorProduto = $(valoresProdutos[i]).html();
			totalProdutos += new Number(valorProduto);
		}
		total += totalProdutos;
		
		var valoresProdutosServico = $("#bloco-geral-comanda-"+comandaId+" .valorProdutoServico");
		var totalProdutosServico = 0;
		for(var i = 0; i < valoresProdutosServico.length; i++){
			var valorProdutoServico = $(valoresProdutosServico[i]).html();
			totalProdutosServico += new Number(valorProdutoServico);
		}
		total += totalProdutosServico;
		
		$("#bloco-geral-comanda-"+comandaId+" .total-servicos").html(new Number(totalServicos).toFixed(2));
		$("#bloco-geral-comanda-"+comandaId+" .total-produtos").html(new Number(totalProdutosServico).toFixed(2));
		$("#bloco-geral-comanda-"+comandaId+" .total-revendas").html(new Number(totalProdutos).toFixed(2));
		
		var desconto = $("#descontos-" + comandaId).val();
		var descontoPromocional = $("#desconto-promo" + comandaId).val();
		$("#valorTotal-" + comandaId).val(new Number(total).toFixed(2));
		$("#valorCobrado-" + comandaId).val(new Number(total - (desconto + descontoPromocional)).toFixed(2));
		var pago = $("#valorPago-" + comandaId).val();
		$("#valorPago-" + comandaId).val(new Number(pago).toFixed(2));
		$("#valorFaltante-" + comandaId).html(new Number(((total - (desconto + descontoPromocional)) - pago)).toFixed(2));
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	
	criarLinhaProduto: function(lancamentoProduto, readOnly, comandaId){
		readOnly = readOnly == null ? "" : readOnly;
		var id = "produto-" + Utils.guid();
		var form = "<div id='" +id+ "' class='lancamento'>";
		
		form += "<div class='col-md-12'>";
		form += 	"<div class='float-left m-r-10'><strong>"+lancamentoProduto.produto.nome+"</strong>, ";
		form += 		"<strong>" + lancamentoProduto.quantidadeUtilizada + "</strong> unidade(s) vendidida(s) por ";
		form += 		"<strong>"+lancamentoProduto.vendedor.nome+"</strong>";
		form += "</div>";
		form += "<div class='float-right'>";
		
		form += 		"<strong class='m-r-10' >R$<span class='mask-money valorProduto' >"+((lancamentoProduto.valor).toFixed(2))+"</span></strong>";
		if(!readOnly && Comanda.hasRoleAdmin){
			form += '<a href="javascript:Lancamento.deleteProduto(\''+lancamentoProduto.id+'\')" title="" class="m-l-10 float-right" >';
			form += '<i class="sa-list-delete"></i></a>';
		}

		form += 	"</div>";
		form += "</div>";
		form += "</div>";
		return form;
	},
	
	criarLinhaProdutoServico: function(lancamentoProduto, readOnly, comandaId){
		readOnly = readOnly == null ? "" : readOnly;
		var id = "produto-" + Utils.guid();
		var form = "<div id='" +id+ "' class='lancamento'>";
		form += "<div class='col-md-12'>";
		form += 	"<div class='float-left m-r-10'><strong>"+lancamentoProduto.produto.nome+"</strong>, ";
		form += 		"<strong>" + lancamentoProduto.quantidadeUtilizada + "</strong> " + lancamentoProduto.produto.unidade;
		form += "</div>";
		form += "<div class='float-right'>";
		form += 		"<strong class='m-r-10' >R$<span class='mask-money valorProdutoServico' >"+((lancamentoProduto.valor).toFixed(2))+"</span></strong>";
		if(!readOnly && Comanda.hasRoleAdmin){
			form += '<a href="javascript:Lancamento.deleteProduto(\''+lancamentoProduto.id+'\')" title="" class="m-l-10 float-right" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
		form += 	"</div>";
		form += "</div>";
		form += "</div>";
		return form;
	},
	
	detalhesFechamento: function(){
		Comanda.verificarData(function(){
			$("#linkModalFechamento").click();
			Utils.unmaskMoney();
			$.ajax({
				url: '/min/web/clientes/verificarComanda',
				type: 'POST',
				data: $("#comanda-form").serialize(),
				success: function(verificacoes){
					$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					$("#comanda-form input[name='ultimaAtualizacao']").val(verificacoes.ultimaAtualizacao);
					Utils.removeMessageBlock("#fechamento-comanda-observacoes");
					if(verificacoes.criticalError){
						$("#fechar-comanda-button").hide();
					}else{
						$("#fechar-comanda-button").show();
					}
					for(var i = 0; i < verificacoes.messages.length; i++){
						var message = verificacoes.messages[i];
						Utils.createMessageBlock(message.message, "#fechamento-comanda-observacoes", message.severity, "fechamentoError-" + Utils.guid());
					}
				}
			});
		});
	},
	criarDivInfoComanda:function(comanda){
		var isComandaAberta = comanda.fechamento == null ? true : false;
		var info = "";
		var botoes = "";
		if(!isComandaAberta){
			info += "<form id='comanda-form"+comanda.id+"' >";
			botoes += "<div class='comanda-fechada-block' id='bloco-"+comanda.id+"'>";
			botoes += "<div class='comanda-fechada'>";
			botoes += '</div>';
		}
		
		info += "<div class='comandaInfo col-md-12 block-area' id='bloco-geral-comanda-"+comanda.id+"'>";
		info += "<input type='hidden' name='comandaId' value='"+comanda.id+"'>";
		info += "<input type='hidden' name='ultimaAtualizacao' value='"+comanda.ultimaAtualizacao+"'>";
		var abertura = new Date(comanda.abertura);
		info += isComandaAberta ? '' : '<a href="javascript:Comanda.mostrarComandaFechada(\''+comanda.id+'\')">';
		info += "<p>#"+ comanda.id + " - Comanda aberta em " + Utils.formatDateTime(abertura) ;
		if(!isComandaAberta){
			var fechamento = new Date(comanda.fechamento);
			info += " e fechada em " + Utils.formatDateTime(fechamento) ;
			info += " no valor de R$<span class='mask-money'>" + new Number(comanda.valorCobrado).toFixed(2) + "</span>";
			info += " <span id='abrirComanda-"+comanda.id+"' >[+]</span>";
			info += " <span id='fecharComanda-"+comanda.id+"' style='display:none' >[-]</span>";
			if(Comanda.hasRoleAdmin){
				info += " <a style='cursor: pointer' onclick='Comanda.deletarComanda("+comanda.id+")' id='deletarComanda-"+comanda.id+"' ><i class='sa-list-delete'></i></a>";
			}
		}
		info += "</p>";
		info += isComandaAberta ? '' : '</a>';
		info += botoes;
		//Servicos
		info+='<div class="clearfix"></div>';
		//Kits
		if(isComandaAberta){
			info += "<div class='col-md-12'>";
			info += "<a class='btn btn-lg m-b-10 m-t-10' data-toggle='modal' href='#modalComandaKit' >Adicionar kit...</a>";
			info += "</div>";
		}
		info+='<div class="clearfix"></div>';
		info += "<div class='col-md-12' style='z-index: 100;'>";
		info += 	"<div class='tile'>";
		if(isComandaAberta){
			info += 	"<div class='tile-title link' data-toggle='modal' href='#modalComandaServico'><div class='comanda-title'>Sevi&ccedil;os</div>";
			//info += '<a href="javascript:Comanda.appendLinhaServico(Comanda.criarLinhaServico(null, null, '+comanda.id+'))" title="Add" class="tooltips" style="float: right;">';
			info += '<a title="Add" class="tooltips" style="float: right;" data-toggle="modal" href="#modalComandaServico" id="linkModalComandaServico" style="display: none" onclick="Lancamento.limparServico()">';
			info +=  '<i class="sa-list-add"></i></a>';
		}else{
			info += 	"<div class='tile-title'><div class='comanda-title'>Sevi&ccedil;os</div>";
		}
		info +=  "</div>";
		if(isComandaAberta){
			info += "<div class='m-b-5 m-l-5' style='font-size: 20px'><a class='showServicos' href='javascript:Lancamento.showServicos()'>[-]</a></div>";
		}
		info += "<div class='bloco-servicos listview narrow'>";
		info += "</div>";
		info += "</div>";
		info += "</div>";
		
		info += "<div class='col-md-12'>";
		info += "<div class='float-right m-r-10 m-b-20'>";
		info += 	"Total servi&ccedil;os: <strong>R$<span class='mask-money total-servicos' >0,00</span></strong>";
		info += "</div>";
		info += "</div>";
		
		//Produtos
		info+='<div class="clearfix"></div>';
		info += "<div class='col-md-12' style='z-index: 100;'>";
		info += "<div class='tile'>";
		if(isComandaAberta){
			info += "<div class='tile-title link' data-toggle='modal' href='#modalComandaProdutoServico'><div class='comanda-title'  >Produtos</div>";
			//info += '<a href="javascript:Comanda.appendLinhaProduto(Comanda.criarLinhaProduto(null, null, '+comanda.id+'))" title="Add" class="tooltips" style="float: right;">';
			info += '<a title="Add" class="tooltips" style="float: right;" data-toggle="modal" href="#modalComandaProdutoServico" id="linkModalComandaProdutoSerivo" style="display: none" onclick="Lancamento.limparProdutoServico()">';
			info +=  '<i class="sa-list-add"></i></a>';
		}else{
			info += "<div class='tile-title'><div class='comanda-title'  >Produtos</div>";
		}
		info +=  "</div>";
		if(isComandaAberta){
			info += "<div class='m-b-5 m-l-5' style='font-size: 20px'><a class='showProdutos' href='javascript:Lancamento.showProdutos()'>[-]</a></div>";
		}
		info += "<div class='bloco-produtos listview narrow'>";
		info += "</div>";
		info += "</div>";
		info += "</div>";
		info+='<div class="clearfix"></div>';

		info += "<div class='col-md-12'>";
		info += "<div class='float-right m-r-10 m-b-20'>";
		info += 	"Total produtos: <strong>R$<span class='mask-money total-produtos' >0,00</span></strong>";
		info += "</div>";
		info += "</div>";
		
		//Revenda
		info+='<div class="clearfix"></div>';
		info += "<div class='col-md-12' style='z-index: 100;'>";
		info += "<div class='tile'>";
		if(isComandaAberta){
			info += "<div class='tile-title link' data-toggle='modal' href='#modalComandaProduto'><div class='comanda-title'>Produtos de revenda</div>";
			//info += '<a href="javascript:Comanda.appendLinhaProduto(Comanda.criarLinhaProduto(null, null, '+comanda.id+'))" title="Add" class="tooltips" style="float: right;">';
			info += '<a title="Add" class="tooltips" style="float: right;" data-toggle="modal" href="#modalComandaProduto" id="linkModalComandaProduto" style="display: none" onclick="Lancamento.limparProduto()">';
			info +=  '<i class="sa-list-add"></i></a>';
		}else{
			info += "<div class='tile-title'><div class='comanda-title'>Produtos de revenda</div>";
		}
		info +=  "</div>";
		if(isComandaAberta){
			info += "<div class='m-b-5 m-l-5' style='font-size: 20px'><a class='showRevenda' href='javascript:Lancamento.showProdutosRevenda()'>[-]</a></div>";
		}
		info += "<div class='bloco-revenda listview narrow'>";
		info += "</div>";
		info += "</div>";
		info += "</div>";
		info+='<div class="clearfix"></div>';

		info += "<div class='col-md-12'>";
		info += "<div class='float-right m-r-10 m-b-20'>";
		info += 	"Total revenda: <strong>R$<span class='mask-money total-revendas' >0,00</span></strong>";
		info += "</div>";
		info += "</div>";

		info+='<div class="clearfix"></div>';
		info += "<div class='col-md-12'>";

		if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
			info += "<div class='col-md-9'>";
		}else{
			info += "<div class='col-md-12'>";
		}
		
		//Totais


		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<p class='total'>Total: <strong>R$<span class='m-b-10 mask-money valorTotal' id='valorTotal-"+comanda.id+"' >"+ new Number((comanda.valorTotal ? comanda.valorTotal : 0)).toFixed(2)+"</span></strong></p>";
		info += "</div>";

		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<div class='float-right' >";
		info += "<span style='font-size: 20px'>Descontos (ajustes): </span>";
		info += "<input class='form-control input-sm m-b-10 mask-money float-right m-l-10' id='descontos-"+comanda.id+"' " +
						"name='descontos' value='"+new Number((comanda.desconto ? comanda.desconto : 0)).toFixed(2)+"' " +
						"onblur='Lancamento.lancarDesconto()' style='width: 100px; ' />";
		info += "</div>";
		info += "</div>";
		
		/**
		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<div class='float-right' >";
		info += "<span style='font-size: 20px'>Desconto promocional (em teste): </span>";
		info += "<input class='form-control input-sm m-b-10 mask-money float-right m-l-10' id='desconto-promo-"+comanda.id+"' " +
						"name='descontoPromocional' value='"+new Number((comanda.descontoPromocional ? comanda.descontoPromocional : 0)).toFixed(2)+"' " +
						"onblur='Lancamento.lancarDesconto()' style='width: 100px; ' />";
		info += "</div>";
		info += "</div>";
		 **/
		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<p class='total'>Valor cobrado: <strong>R$<span class='m-b-10 mask-money valorCobrado' id='valorCobrado-"+comanda.id+"' >"+ new Number((comanda.valorCobrado ? comanda.valorCobrado : 0)).toFixed(2)+"</span></strong></p>";
		info += "</div>";

		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<p class='total'>Valor pago: <strong>R$<span class='m-b-10 mask-money valorPago' id='valorPago-"+comanda.id+"' >"+ new Number((comanda.valorPago ? comanda.valorPago : 0)).toFixed(2)+"</span></strong></p>";
		info += "</div>";

		info+='<div class="clearfix"></div>';
		info += "<div class='w-100 float-left'>";
		info += "<p class='total' ><span id='labelValorFaltante-"+comanda.id+"' >Falta:</span> <strong>R$<span class='m-b-10 mask-money valorFaltante' id='valorFaltante-"+comanda.id+"' >0.00</span></strong></p>";
		info += "</div>";
		
		info += "</div>";
		
		//Pagamentos
		if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
			info += "<div class='col-md-3'>";
			if(isComandaAberta && comanda.credito != 0){
				if(comanda.credito > 0){
					info += "<p>H&aacute; um cr&eacute;dito de R$<span class='mask-money' >" + new Number(comanda.credito).toFixed(2) + "</span></p>";
				}else if(comanda.credito < 0){
					info += "<p>H&aacute; um d&eacute;bito de R$<span class='mask-money' >" + new Number(comanda.credito).toFixed(2) + "</span></p>";
				}
			}
			info += "<table class='table table-hover tile'>";
			info += "<thead><tr><th>Forma Pgto.</th><th>Valor</th><th style='width: 50px'></th></tr></thead>";
			info += "<tbody></tbody>";
			info += "</table>";
			if(isComandaAberta){
				info += "<div class=''>";
				info += "<a href='javascript:Comanda.novoPagamento()'class='btn btn-sm m-b-10' style='float: right'>Novo pagamento</a><br /><br />";
				info += "</div>";
			}
			info += "</div>";
		}
		
		info += "</div>";

		if(!isComandaAberta){
			if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
				info += "<div class='w-100-p'>";
				info += '<div class="col-md-3 float-right">';
				info += '<p>Observa&ccedil;&atilde;o</p>';
				info += '<input class="form-control input-sm m-b-10 mask-numer" name="notaAtendimento" id="nota-atendimento-'+comanda.id+'" value="'+(comanda.numeroNota ? comanda.numeroNota : '') +'"/>';
				info += "</div>";
				info += "</div>";
				info += "<div class='w-100-p'>";
				info += '<a class="btn btn-lg m-r-15 m-b-15" style="float: right" onclick="Comanda.alterarNota('+comanda.id+')" >Salvar</a>';
				info += "</div>";
			}else{
				info += "<div class='w-100-p'>";
				info += '<div class="col-md-3 float-right">';
				info += '<p>Observa&ccedil;&atilde;o: '+comanda.numeroNota+'</p>';
				info += "</div>";
				info += "</div>";
			}
			info += "</div>";
			info += "</form>";
		}else{
			if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
				info += "<div class='w-100-p'>";
				info += '<div class="col-md-3 float-right">';
				info += '<p>Observa&ccedil;&atilde;o</p>';
				info += '<input class="form-control input-sm m-b-10 mask-numer" name="notaAtendimento" id="nota-atendimento" />';
				info += "</div>";
				info += "</div>";
				info += "<div class='w-100-p'>";
				info += '<a class="btn btn-lg m-r-15 m-b-15" style="float: right" onclick="Comanda.detalhesFechamento()" >Fechar comanda</a><a data-toggle="modal"  href="#modalFechamento" id="linkModalFechamento" style="display: none"></a>';
				info += "</div>";
			}
		}
		
		return info;
	},
	
	alterarNota:function(comandaId){
		if(confirm("Tem certeza que deseja alterar este valor?")){
			var nota = $("#nota-atendimento-"+comandaId).val();
			
			$.ajax({
				url: '/min/web/clientes/alterarNota',
				type: 'POST',
				data: {
					nota: nota,
					comandaId: comandaId
				},
				success: function(comanda){
					alert("Valor alterado com sucesso");
				}
			});
		}
	},
	
	pagar:function(){
		Comanda.verificarData(function(){
			Utils.unmaskMoney();
			var valor = $("#novo-pagamento-form input[name='valor']").val();
			Utils.removeMessageBlock("#novo-pagamento-form");
			if(valor == '' || valor <= 0){
				Utils.createMessageBlock("Pagamento precisa de um valor maior que zero (0)", "#novo-pagamento-form", "danger");
				return false;
			}
			var parcelas = $("#novo-pagamento-form input[name='parcelas']").val();
			if(parcelas == ''){
				$("#novo-pagamento-form input[name='parcelas']").val(1);
			}
			$.ajax({
				url: '/min/web/clientes/pagar',
				type: 'POST',
				data: $("#novo-pagamento-form").serialize(),
				success:function(comanda){
					$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					//Comanda.refreshPagamentos(comanda);
					Comanda.findComandaAberta();
					//Comanda.salvarComanda();
					$("#fechar-popup-pagamento").click();
				}
			});
		});
	},
	
	refreshPagamentos: function(comanda){
		Utils.unmaskMoney();
		var isComandaAberta = comanda.fechamento == null ? true : false;
		$("#comanda-form" +(isComandaAberta ? '' : comanda.id)+ " tbody").html("");
		for(var i = 0; i < comanda.pagamentos.length; i++){
			var pagamento = comanda.pagamentos[i];
			Comanda.addPagamento(pagamento, (isComandaAberta ? '' : comanda.id));
		}
		var valorPago = new Number(comanda.valorPago);
		$("#valorPago-" + comanda.id).val(valorPago.toFixed(2));
		var cobrado = new Number($("#valorCobrado-" + comanda.id).val());
		var faltante = cobrado - valorPago;
		$("#valorFaltante-" + comanda.id).html(new Number(faltante).toFixed(2));
		if(faltante >= 0){
			$("#labelValorFaltante-" + comanda.id).html("Falta:");
		}else{
			$("#labelValorFaltante-" + comanda.id).html("Sobra:");
		}
		$(".mask-money").mask("#.##0,00", {reverse: true, maxlength: false});
	},
	
	addPagamento:function(pagamento, comandaId){
		var linha = "<tr id='pagamento-"+pagamento.id+"'>";
		linha += "<td>" + pagamento.formaPagamento;
		if(pagamento.parcelamento){
			linha += " ("+pagamento.parcela + "/" +pagamento.parcelamento+")";
		}
		linha += "</td>";
		linha += "<td class='mask-money'>" + new Number(pagamento.valor).toFixed(2) + "</td>";
		linha += "<td>";
		if(comandaId == ''){
			linha += "<a href='javascript:Comanda.deletarPagamento("+pagamento.id+", \""+comandaId+"\")'class='tooltips'><span class='icon' style='font-size: 14px'>&#61918;</span></a>";
		}
		linha += "</td>";
		linha += "</tr>";
		$("#comanda-form" +comandaId+ " tbody").append(linha);
	},
	
	deletarPagamento:function(id){
		if(confirm("Tem certeza que deseja excluir?")){
			$.ajax({
				url: '/min/web/clientes/deletarPagamento/' + id + "/" + $("#cliente-id").val(),
				type: 'GET',
				success:function(comanda){
					Comanda.refreshPagamentos(comanda);
					Comanda.salvarComanda();
				}
			});
			
		}
	},
	
	novoPagamento:function(){
		Utils.unmaskMoney();
		$("#novo-pagamento-form input[name='comandaId']").val($("#comanda-form input[name='comandaId']").val());
		var cobrado = $("#comanda-form .valorCobrado").html();
		var pago = $("#comanda-form .valorPago").html();
		var valor = cobrado - pago;
		if(valor < 0){
			valor = 0;
		}
		$("#novo-pagamento-form input[name='valor']").val(new Number(valor).toFixed(2));
		$("a[href='#modalWider']").click();

        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});		
	},
	comandasDetalhadas : {},
	mostrarComandaFechada: function(id){
		if($("#bloco-" + id).css('display') == 'block'){
			$('#bloco-'+id).hide(300);
			$('#abrirComanda-'+id).show(300);
			$('#fecharComanda-'+id).hide(300);
		}else{
			if( Comanda.comandasDetalhadas[id]){
				$('#bloco-'+id).show(300);
				$('#abrirComanda-'+id).hide(300);
				$('#fecharComanda-'+id).show(300);
			} else{
				$.ajax({
					url: '/min/web/clientes/findComanda/' + id,
					type: 'GET',
					success: function(comanda){
						Comanda.comandasDetalhadas[comanda.id] = comanda;
						Comanda.preencherDadosComanda(comanda);
						$('#bloco-'+id).show(300);
						$('#abrirComanda-'+id).hide(300);
						$('#fecharComanda-'+id).show(300);
					}
				});
			}
		}
	},
	
	abrirComanda: function(){
		var id = $("#cliente-id").val();
		$.ajax({
			url: '/min/web/clientes/abrirComanda/' + id,
			type: 'GET',
			success: function(comanda){
				Comanda.criarFormComanda(comanda);
				Comanda.preencherDadosComanda(comanda);
			}
		});
	},
	criarFormComanda: function(comanda){
		var content = '';
		var isComandaAberta = comanda.fechamento == null ? true : false;
		
		content += Comanda.criarDivInfoComanda(comanda);
		
		if(isComandaAberta){
			$("#comanda-form").html(content);
		}else{
			$("#comandas-fechadas").append(content);
			readonly = "readonly='readonly'";
		}

		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		
	},
	preencherDadosComanda:function(comanda){
		var isComandaAberta = comanda.fechamento == null ? true : false;
		var readonly = isComandaAberta ? "" : "readonly";
		var comandaFormId = isComandaAberta ? '' : ("#comanda-form"+comanda.id);
		for(var j = 0; j < comanda.servicos.length; j++){
			var lancamentoServico = comanda.servicos[j];
			Comanda.appendLinhaServico(Comanda.criarLinhaServico(lancamentoServico, readonly, comanda.id), comandaFormId);
		}
		for(var j = 0; j < comanda.produtos.length; j++){
			var lancamentoProduto = comanda.produtos[j];
			if(lancamentoProduto.revenda){
				Comanda.appendLinhaProduto(Comanda.criarLinhaProduto(lancamentoProduto, readonly, comanda.id), comandaFormId);
			}else{
				Comanda.appendLinhaProdutoServico(Comanda.criarLinhaProdutoServico(lancamentoProduto, readonly, comanda.id), comandaFormId);
			}
		}
		Comanda.refreshPagamentos(comanda);
		Comanda.preencherTotais(comanda.id);
	},
	deletarLancamento: function(id){
		if(confirm("Tem certeza que deseja remover?"))
			$("#" + id).remove();
	},
	
	adicionarProdutoAoServico: function(servicoId){
		var form = Comanda.criarProdutoAoServico(servicoId);
		$("#" + servicoId).append(form);
//		$("#" + servicoId + " select").chosen({
//            max_selected_options: 1
//        });
		$(".mask-number").mask("##########0");
	},
	
	criarProdutoAoServico: function(servicoId, produtoUtilizado, readonly){
		readonly = readonly ? readonly : '';
		var id = "produtoServico-" + Utils.guid();
		var form = "<div class='w-100 float-left' id='"+id+"'><div class='col-md-2'> <span class='icon float-left m-r-5' style='font-size: 14px;'>&#61807;</span></div><div class='col-md-10'>";
		
		form += 	"<div class='float-left m-r-10'><strong>"+produtoUtilizado.produto.nome+"</strong>, ";
		form += 		"<strong>" + produtoUtilizado.quantidadeUtilizada + "</strong> " + produtoUtilizado.produto.unidade;
		form += "</div>";
		form += "<div class='float-right'>";
		form += 		"<strong class='m-r-10' >R$<span class='mask-money' >"+((produtoUtilizado.valor).toFixed(2))+"</span></strong>";
		if(!readonly && Comanda.hasRoleAdmin){
			form += '<a href="javascript:Lancamento.deleteProduto(\''+produtoUtilizado.id+'\')" title="" class="m-l-10 float-right" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
		form += 	"</div>";
		form += "</div>";
		form += "</div>";
		return form;
	},
	
	alterarLabelQuantidade:function(id){
		var produtoId = $("#" + id + " select[name='produtoServicoId']").val();
		for(var i = 0; i < Comanda.produtos.length; i++){
			var produto = Comanda.produtos[i];
			if(produto.id == produtoId){
				$("#quantidade-" + id).html("Quantidade (" + produto.unidade + "):");
				break;
			}
		}
	},
	
	verificarData:function(callback){
//		$.ajax({
//			url: "/min/web/clientes/ultimaAtualizacao/" + $("#comanda-form input[name='comandaId']").val(),
//			type: "GET",
//			success: function(data){
//				var ultimaAtualizacao = new Number($("#comanda-form input[name='ultimaAtualizacao']").val());
//				data = new Number(data);
//				var diferencaTempo = (ultimaAtualizacao - data);
//				if( diferencaTempo > 1000 || diferencaTempo < (-1000) ){
//					$("#linkModalComandaErro").click();
//				}else{
					callback();
//				}
//			}
//		});
	},
	
	salvarComanda: function(id){
		Comanda.verificarData(function(){
			Utils.unmaskMoney();
			$.ajax({
				url: '/min/web/clientes/salvarComanda',
				type: 'POST',
				data: $("#comanda-form" + (id ? id : '')).serialize(),
				success: function(comanda){
					Comanda.criarFormComanda(comanda);
					Comanda.preencherDadosComanda(comanda);
			        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				}
			});
		});
	},
	fecharComanda:function(){
		Comanda.verificarData(function(){
			Utils.unmaskMoney();
			$.ajax({
				url: '/min/web/clientes/fecharComanda',
				type: 'POST',
				data: $("#comanda-form").serialize(),
				success: function(comanda){
					$("#comanda-form").html("");
					$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
					if($("#comandas-fechadas").css('display') == 'block'){
						Comanda.findComandas();
					}
					Comanda.findComandaAberta();
				}
			});
		});
	},
	fecharComandaPorId:function(id){
		if(confirm("Tem certeza que deseja fechar esta comanda?")){
			$.ajax({
				url: '/min/web/clientes/fecharComanda/' + id,
				type: 'GET',
				success: function(){
					location.reload();
				}
			});
		}
	},
	deletarComanda : function(id){
		if(confirm("Tem certeza que deseja excluir esta comanda?")){
			$.ajax({
				url: '/min/web/clientes/deleteComanda/' + id,
				type: 'GET',
				success: function(){
					location.reload();
				}
			});
		}
	}
};