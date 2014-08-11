Comanda = {
	hasRoleCaixa: false,
	hasRoleOperacional: false,
	hasRoleAdmin: false,
	servicos : [],
	produtos: [],
	funcionarios: [],
	comandas:[],
	findServicos: function(){
		$.ajax({
			url: '/min/web/servicos/listar',
			type: 'GET',
			success: function(servicos){
				Comanda.servicos = servicos;
			}
		});
	},
	findProdutos: function(){
		$.ajax({
			url: '/min/web/produtos/listar',
			type: 'GET',
			success: function(produtos){
				Comanda.produtos = produtos;
			}
		});
	},
	findFuncionarios: function(){
		$.ajax({
			url: '/min/web/funcionarios/listar',
			type: 'GET',
			success: function(funcionarios){
				Comanda.funcionarios = funcionarios;
			}
		});
	},
	
	init:function(){
		Comanda.findFuncionarios();
		Comanda.findProdutos();
		Comanda.findServicos();
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
		id = id == undefined ? "#comanda-form" : id;
		$(id + " .bloco-servicos").append(linha);
//		$(id + " .bloco-servicos select").chosen({
//            max_selected_options: 1
//        });
		$(".mask-number").mask("##########0");
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	appendLinhaProduto:function(linha, id){
		id = id == undefined ? '' : id;
		$(id + " .bloco-produtos").append(linha);
//		$(id + " .bloco-produtos select").chosen({
//            max_selected_options: 1
//        });

		$(".mask-number").mask("##########0");
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	criarLinhaServico: function(lancamentoServico, readOnly, comandaId){
		readOnly = readOnly == undefined ? "" : readOnly;
		var disabled = readOnly == "" ? "" : "disabled='disabled'";
		var id = "servico-"+Utils.guid();
		var form = "<div id='" +id+ "' class='lancamento'>";
		form += "<input type='hidden' value='" + id + "' name='guidServico' />";

		form += "<div class='col-md-2'>";
		var criacao = new Date();
		var criacaoFormatada = Utils.formatDateTime(criacao);
		form += "<p>Criado em: </p>";
		form += "<input name='dataCriacaoServico' class='form-control input-sm m-b-10' readonly='readonly' value='"+criacaoFormatada+"' />";
		form += "</div>";
		
		form += "<div class='col-md-4'>";
		form += "<p>Servi&ccedil;o:</p>";
		form += "<select name='servicoId' "+readOnly+" " + disabled + " class='form-control input-sm m-b-10' onchange='Comanda.buscarValorServico(\""+id+"\", \""+comandaId+"\")' placeholder='Selecione um servi&ccedil;o' >";
		form += "<option value='-' ></option>";
		var servicoSelecionado = null;
		for(var s = 0; s < Comanda.servicos.length; s++){
			var servico = Comanda.servicos[s];
			var selected = "";
			if(lancamentoServico && lancamentoServico.servico && lancamentoServico.servico.id == servico.id){
				selected = "selected='selected'";
				servicoSelecionado = servico;
			}
			form+="<option "+selected+" value='"+servico.id+"'>" +servico.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p>Funcion&aacute;rio:</p>";
		form += "<select name='funcionarioId'  "+readOnly+" " + disabled + " class='form-control input-sm m-b-10' placeholder='Selecione um funcion&aacute;rio' onchange='Comanda.salvarComanda();' >";
		form += "<option value='-' ></option>";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = (lancamentoServico && lancamentoServico.funcionario) ? (lancamentoServico.funcionario.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p>Assistente:</p>";
		form += "<select name='assistenteId' "+readOnly+" " + disabled + " class='form-control input-sm m-b-10' placeholder='Selecione um funcionario' onchange='Comanda.salvarComanda();' >";
		form += "<option value='-' ></option>";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = (lancamentoServico && lancamentoServico.assistente) ? (lancamentoServico.assistente.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-1'>";
		form += "<p>Valor (R$):</p>";
		form += "<input name='valorServico' class='form-control input-sm m-b-10  mask-money' readonly='readonly' id='valor-"+id+"' ";
		if(servicoSelecionado){
			form+= "value='" + new Number(servicoSelecionado.preco).toFixed(2) + "' ";
		}
		form += "/>";
		form += "</div>";
		form += "<div class='col-md-1'>";
		if(readOnly == ""){
			form += '<a href="javascript:Comanda.adicionarProdutoAoServico(\''+id+'\')" title="" class="tooltips acaoLancamento" >';
			form += '<i class="sa-list-add"></i></a>';
			form += '<a href="javascript:Comanda.deletarLancamento(\''+id+'\')" title="" class="tooltips acaoLancamento" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
		form += "</div>";
		
		if(lancamentoServico){
			for(var i = 0; i < lancamentoServico.produtosUtilizados.length; i++){
				form += Comanda.criarProdutoAoServico(id, lancamentoServico.produtosUtilizados[i], readOnly);
			}
		}
		
		form += "</div>";
		return form;
	},
	
	buscarValorServico: function(id, comandaId){
		Utils.unmaskMoney();
		var servicoId = $("#" + id + " select[name='servicoId']").val();
		if(servicoId != "-"){
			for(var i = 0; i < Comanda.servicos.length; i++){
				var servico = Comanda.servicos[i];
				if(servico.id == servicoId){
					$("#" + id + " input[name='valorServico']").val(new Number(servico.preco).toFixed(2));
				}
			}
		}else{
			$("#" + id + " input[name='valorServico']").val("");
		}
		$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		Comanda.preencherTotais(comandaId);
		Comanda.salvarComanda();

		return true;
	},
	
	buscarValorProduto: function(id, servico){
		Utils.unmaskMoney();
		servico = servico == null ? '' : servico;
		var produtoId = $("#" + id + " select[name='produto"+servico+"Id']").val();
		if(produtoId != "-"){
			for(var i = 0; i < Comanda.produtos.length; i++){
				var produto = Comanda.produtos[i];
				if(produto.id == produtoId){
					var quantidade;
					if($("#" + id + " input[name='quantidadeProduto"+servico+"']").val() == ""){
						quantidade = 1;
						$("#" + id + " input[name='quantidadeProduto"+servico+"']").val(quantidade);
					}else{
						quantidade = $("#" + id + " input[name='quantidadeProduto"+servico+"']").val();
					}
					$("#" + id + " input[name='valorProduto"+servico+"']").val(new Number(produto.precoRevenda * quantidade).toFixed(2));
					$("#quantidade-" + id).html("Quantidade (" + produto.unidade + "):");
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
		Comanda.salvarComanda();

		return true;
	},
	
	preencherTotais:function(comandaId){
		Utils.unmaskMoney();
		var total = new Number(0.00);
		var valoresServicos = $("#bloco-geral-comanda-"+comandaId+" input[name='valorServico']");
		for(var i = 0; i < valoresServicos.length; i++){
			var valorServico = $(valoresServicos[i]).val();
			total += new Number(valorServico);
		}
		var valoresProdutos = $("#bloco-geral-comanda-"+comandaId+" input[name='valorProduto']");
		for(var i = 0; i < valoresProdutos.length; i++){
			var valorProduto = $(valoresProdutos[i]).val();
			total += new Number(valorProduto);
		}
		var valoresProdutosServico = $("#bloco-geral-comanda-"+comandaId+" input[name='valorProdutoServico']");
		for(var i = 0; i < valoresProdutosServico.length; i++){
			var valorProdutoServico = $(valoresProdutosServico[i]).val();
			total += new Number(valorProdutoServico);
		}
		var desconto = $("#descontos-" + comandaId).val();
		$("#valorTotal-" + comandaId).val(new Number(total).toFixed(2));
		$("#valorCobrado-" + comandaId).val(new Number(total - desconto).toFixed(2));
		var pago = $("#valorPago-" + comandaId).val();
		$("#valorPago-" + comandaId).val(new Number(pago).toFixed(2));
		$("#valorFaltante-" + comandaId).val(new Number(((total - desconto) - pago)).toFixed(2));
        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
	},
	
	criarLinhaProduto: function(lancamentoProduto, readOnly, comandaId){
		readOnly = readOnly == null ? "" : readOnly;
		var disabled = readOnly == "" ? "" : "disabled='disabled'";
		var id = "produto-" + Utils.guid();
		var form = "<div id='" +id+ "' class='lancamento'>";
		form += "<input type='hidden' value='" + id + "' name='guidProduto' />";
		
		form += "<div class='col-md-2'>";
		var criacao = new Date();
		var criacaoFormatada = Utils.formatDateTime(criacao);
		form += "<p>Criado em: </p>";
		form += "<input name='dataCriacaoProduto'  readonly='readonly' class='form-control input-sm m-b-10' value='"+criacaoFormatada+"' />";
		form += "</div>";
		
		form += "<div class='col-md-3'>";
		form += "<p>Produto:</p>";
		form += "<select name='produtoId' "+readOnly+" " + disabled + " onchange='Comanda.buscarValorProduto(\""+id+"\")' class='form-control input-sm m-b-10 ' placeholder='Selecione um produto' >";
		form += "<option value='-' ></option>";
		var produtoSelecionado = null;
		for(var s = 0; s < Comanda.produtos.length; s++){
			var produto = Comanda.produtos[s];
			if(produto.categoria != 'LOJA'){
				continue;
			}
			var selected = "";
			if(lancamentoProduto && lancamentoProduto.produto && lancamentoProduto.produto.id == produto.id){
				selected = "selected='selected'";
				produtoSelecionado = produto;
			}
			form+="<option "+selected+" value='"+produto.id+"'>" +produto.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-3'>";
		form += "<p>Vendedor:</p>";
		form += "<select name='vendedorId'  "+readOnly+" " + disabled + " class='form-control input-sm m-b-10' placeholder='Selecione um vendedor'  onchange='Comanda.salvarComanda();' >";
		form += "<option value='-' ></option>";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = (lancamentoProduto && lancamentoProduto.vendedor) ? (lancamentoProduto.vendedor.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p id='quantidade-"+id+"'>Quantidade";
		if(lancamentoProduto && lancamentoProduto.produto){
			form += "(" + lancamentoProduto.produto.unidade + ")";
		}
		form += ":</p>";
		form += "<input name='quantidadeProduto' class='form-control input-sm m-b-10 mask-number' "+readOnly+" onchange='Comanda.buscarValorProduto(\""+id+"\")' ";
		if(lancamentoProduto){
			form+= "value='" + lancamentoProduto.quantidadeUtilizada + "' ";
		}
		form += "/>";
		form += "</div><div class='col-md-1'>";
		form += "<p>Valor (R$):</p>";
		form += "<input name='valorProduto' class='form-control input-sm m-b-10 mask-money' readonly='readonly' ";
		if(produtoSelecionado){
			form+= "value='" + ((produtoSelecionado.precoRevenda * lancamentoProduto.quantidadeUtilizada).toFixed(2)) + "' ";
		}
		form += "/>";
		form += "</div>";
		form += "<div class='col-md-1'>";
		if(readOnly == ""){
			form += '<a href="javascript:Comanda.deletarLancamento(\''+id+'\')" title="" class="tooltips acaoLancamento" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
		form += "</div>";
		form += "</div>";
		return form;
	},
	detalhesFechamento: function(){
		Utils.unmaskMoney();
		$.ajax({
			url: '/min/web/clientes/verificarComanda',
			type: 'POST',
			data: $("#comanda-form").serialize(),
			success: function(verificacoes){
		        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
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
	},
	criarDivInfoComanda:function(comanda){
		var isComandaAberta = comanda.fechamento == null ? true : false;
		var info = "";
		var botoes = "";
		if(isComandaAberta){
			info += "<div class='action-buttons'>";
			info += '<a class="btn btn-lg m-r-5" onclick="Comanda.salvarComanda()" >Salvar comanda</a>';
			if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
				info += '<a class="btn btn-lg btn-alt m-r-5" data-toggle="modal" href="#modalFechamento" onclick="Comanda.detalhesFechamento()" >Fechar comanda</a>';
			}
			info += '</div>';
		}else{
			info += "<form id='comanda-form"+comanda.id+"' >";
			botoes += "<div class='comanda-fechada-block' id='bloco-"+comanda.id+"'>";
			botoes += "<div class='comanda-fechada'>";
			botoes += '</div>';
		}
		
		info += "<div class='comandaInfo col-md-12 block-area' id='bloco-geral-comanda-"+comanda.id+"'>";
		info += "<input type='hidden' name='comandaId' value='"+comanda.id+"'>";
		var abertura = new Date(comanda.abertura);
		info += isComandaAberta ? '' : '<a href="javascript:Comanda.mostrarComandaFechada(\''+comanda.id+'\')">';
		info += "<p>#"+ comanda.id + " - Comanda aberta em " + Utils.formatDateTime(abertura) ;
		if(!isComandaAberta){
			var fechamento = new Date(comanda.fechamento);
			info += " e fechada em " + Utils.formatDateTime(fechamento) ;
			info += " no valor de R$" + comanda.valorCobrado;
			info += " <span id='abrirComanda-"+comanda.id+"' >[+]</span>";
			info += " <span id='fecharComanda-"+comanda.id+"' style='display:none' >[-]</span>";
		}
		info += "</p>";
		info += isComandaAberta ? '' : '</a>';
		info += botoes;
		//Servicos
		info += "<div class='col-md-12'>";
		info += "<div class='tile'>";
		info += "<div class='tile-title'><div class='comanda-title'>Sevi&ccedil;os</div>";
		if(isComandaAberta){
			info += '<a href="javascript:Comanda.appendLinhaServico(Comanda.criarLinhaServico(null, null, '+comanda.id+'))" title="Add" class="tooltips" style="float: right;">';
			info +=  '<i class="sa-list-add"></i></a>';
		}
		info +=  "</div>";
		info += "<div class='bloco-servicos listview narrow'>";
		info += "</div>";
		info += "</div>";
		info += "</div>";
		
		//Produtos
		info += "<div class='col-md-12'>";
		info += "<div class='tile'>";
		info += "<div class='tile-title'>";
		info += "<div class='comanda-title'>Produtos</div>";
		if(isComandaAberta){
			info += '<a href="javascript:Comanda.appendLinhaProduto(Comanda.criarLinhaProduto(null, null, '+comanda.id+'))" title="Add" class="tooltips" style="float: right;">';
			info +=  '<i class="sa-list-add"></i></a>';
		}
		info +=  "</div>";
		info += "<div class='bloco-produtos listview narrow'>";
		info += "</div>";
		info += "</div>";
		info += "</div>";

		info += "<div class='col-md-12'>";

		if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
			info += "<div class='col-md-9'>";
		}else{
			info += "<div class='col-md-12'>";
		}
		
		//Totais
		info += "<div class='col-md-5'>";
		info += "<p class='total'>Total:</p>";
		info += "</div>";
		info += "<div class='col-md-2'>";
		info += "<input class='form-control input-sm m-b-10 mask-money' id='valorTotal-"+comanda.id+"' name='total' readonly value='"+ new Number((comanda.valorTotal ? comanda.valorTotal : 0)).toFixed(2)+"' />";
		info += "</div>";
		info += "<div class='col-md-3'>";
		info += "<p class='total'>Descontos:</p>";
		info += "</div>";
		info += "<div class='col-md-2'>";
		info += "<input class='form-control input-sm m-b-10 mask-money' id='descontos-"+comanda.id+"' " +
						"name='descontos' value='"+new Number((comanda.desconto ? comanda.desconto : 0)).toFixed(2)+"' " +
						"onblur='Comanda.preencherTotais("+comanda.id+");' "+(isComandaAberta ? '' : 'readonly')+"  />";
		info += "</div>";
		info += "<div class='col-md-5'>";
		info += "<p class='total'>Valor cobrado:</p>";
		info += "</div>";
		info += "<div class='col-md-2'>";
		info += "<input class='form-control input-sm m-b-10 mask-money' id='valorCobrado-"+comanda.id+"' name='valorCobrado' readonly value='"+ new Number((comanda.valorCobrado ? comanda.valorCobrado : 0)).toFixed(2)+"' />";
		info += "</div>";
		info += "<div class='col-md-3'>";
		info += "<p class='total'>Valor pago:</p>";
		info += "</div>";
		info += "<div class='col-md-2'>";
		info += "<input class='form-control input-sm m-b-10 mask-money' id='valorPago-"+comanda.id+"' name='valorPago' readonly value='"+ new Number((comanda.valorPago ? comanda.valorPago : 0)).toFixed(2)+"' />";
		info += "</div>";
		info += "<div class='col-md-10'>";
		info += "<p class='total'>Falta:</p>";
		info += "</div>";
		info += "<div class='col-md-2'>";
		info += "<input class='form-control input-sm m-b-10 mask-money' id='valorFaltante-"+comanda.id+"' name='valorFaltante' readonly value='0.00' />";
		info += "</div>";
		info += "</div>";
		
		//Pagamentos
		if(Comanda.hasRoleAdmin || Comanda.hasRoleCaixa){
			info += "<div class='col-md-3'>";
			if(isComandaAberta && comanda.credito != 0){
				if(comanda.credito > 0){
					info += "<p>H&aacute; um cr&eacute;dito de R$" + comanda.credito + "</p>";
				}else if(comanda.credito < 0){
					info += "<p>H&aacute; um d&eacute;bito de R$" + comanda.credito + "</p>";
				}
			}
			info += "<table class='table table-hover tile'>";
			info += "<thead><tr><th>Forma Pgto.</th><th>Valor</th><th style='width: 50px'></th></tr></thead>";
			info += "<tbody></tbody>";
			info += "</table>";
			if(isComandaAberta){
				info += "<div class=''>";
				info += "<a href='javascript:Comanda.novoPagamento()'class='btn btn-sm' style='float: right'>Novo pagamento</a><br /><br />";
				info += "</div>";
			}
			info += "</div>";
		}
		
		info += "</div>";

		if(!isComandaAberta){
			info += "</div>";
			info += "</form>";
		}
		
		return info;
	},
	
	pagar:function(){
		Utils.unmaskMoney();
		var valor = $("#novo-pagamento-form input[name='valor']").val();
		Utils.removeMessageBlock("#novo-pagamento-form");
		if(valor == '' || valor <= 0){
			Utils.createMessageBlock("Pagamento precisa de um valor maior que zero (0)", "#novo-pagamento-form", "danger");
			return false;
		}
		$.ajax({
			url: '/min/web/clientes/pagar',
			type: 'POST',
			data: $("#novo-pagamento-form").serialize(),
			success:function(comanda){
				$('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
				Comanda.refreshPagamentos(comanda);
				Comanda.salvarComanda();
				$("#fechar-popup-pagamento").click();
			}
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
		$("#valorFaltante-" + comanda.id).val(new Number(faltante).toFixed(2));
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
	
	deletarPagamento:function(id, comandaId){
		if(confirm("Tem certeza que deseja excluir?")){
			if(comandaId == ''){
				comandaId = $("#comanda-form input[name='comandaId']").val();
			}
			$.ajax({
				url: '/min/web/clientes/deletarPagamento/' + id + "/" + comandaId,
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
		var cobrado = $("#comanda-form input[name='valorCobrado']").val();
		var pago = $("#comanda-form input[name='valorPago']").val();
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
			Comanda.appendLinhaProduto(Comanda.criarLinhaProduto(lancamentoProduto, readonly, comanda.id), comandaFormId);
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
		var disabled = readonly == '' ? "" : "disabled='disabled'";
		var id = "produtoServico-" + Utils.guid();
		var form = "<div class='' id='"+id+"'><div class='col-md-4'> <span class='icon' style='float: right;font-size: 20px;'>&#61807;</span></div><div class='col-md-4'>";
		form += "<input type='hidden' value='" + servicoId + "' name='guidProdutoServico' />";
		form += "<p>Produto:</p>";
		form += "<select name='produtoServicoId'  class='form-control input-sm m-b-10' "+readonly+" " + disabled + " placeholder='Selecione um produto' onchange='Comanda.buscarValorProduto(\"" + id + "\", \"Servico\")' >";
		form += "<option value='-' ></option>";
		for(var s = 0; s < Comanda.produtos.length; s++){
			var produto = Comanda.produtos[s];
			if(produto.categoria != 'SALAO'){
				continue;
			}
			var selected = (produtoUtilizado && produtoUtilizado.produto) ? (produtoUtilizado.produto.id == produto.id ? "selected='selected'" : "") : "";
			form+="<option "+selected+" value='"+produto.id+"'>" +produto.nome+"</option>";
		}
		form += "</select>";
		form += "</div>";
		form += "<div class='col-md-2'>";
		form += "<p id='quantidade-"+id+"'>Quantidade";
		if(produtoUtilizado && produtoUtilizado.produto){
			form += " (" + produtoUtilizado.produto.unidade + ")";
		}
		form += ":</p>";
		form += "<input name='quantidadeProdutoServico' class='form-control input-sm m-b-10 mask-number' "+readonly+"  onblur='Comanda.buscarValorProduto(\"" + id + "\", \"Servico\")' ";
		if(produtoUtilizado){
			form+= "value='" + (produtoUtilizado.quantidadeUtilizada)+ "' ";
		}
		form += "/>";
		form += "</div>";
		form += "<div class='col-md-1'>";
		form += '<p>Valor (R$):</p>';
		form += '<input name="valorProdutoServico" class="form-control input-sm m-b-10 mask-money" readonly ';
		if(produtoUtilizado && produtoUtilizado.produto){
			form+= "value='" + ((produtoUtilizado.produto.precoRevenda * produtoUtilizado.quantidadeUtilizada).toFixed(2))+ "' ";
		}
		form += "/>";
		form += "</div>";
		form += "<div class='col-md-1'>";
		if(readonly == ''){
			form += '<a href="javascript:Comanda.deletarLancamento(\''+id+'\')" title="" class="tooltips acaoLancamento" >';
			form += '<i class="sa-list-delete"></i></a>';
		}
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
	
	salvarComanda: function(id){
		Utils.unmaskMoney();
		$.ajax({
			url: '/min/web/clientes/salvarComanda',
			type: 'POST',
			data: $("#comanda-form" + (id ? id : '')).serialize(),
			success: function(comanda){
		        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
			}
		});
	},
	
	fecharComanda:function(){
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
	}

};