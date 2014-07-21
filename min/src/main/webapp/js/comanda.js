Comanda = {
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
		$(id + " .bloco-servicos select").chosen({
            max_selected_options: 1
        });
		$(".mask-number").mask("##########0");
	},
	appendLinhaProduto:function(linha, id){
		id = id == undefined ? '' : id;
		$(id + " .bloco-produtos").append(linha);
		$(id + " .bloco-produtos select").chosen({
            max_selected_options: 1
        });

		$(".mask-number").mask("##########0");
	},
	criarLinhaServico: function(lancamentoServico, readOnly, comandaId){
		readOnly = readOnly == undefined ? "" : readOnly;
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
		form += "<select name='servicoId' multiple='multiple' "+readOnly+" class='tag-select-limited' onchange='Comanda.buscarValorServico(\""+id+"\", \""+comandaId+"\")' placeholder='Selecione um servi&ccedil;o' >";
		var servicoSelecionado = null;
		for(var s = 0; s < Comanda.servicos.length; s++){
			var servico = Comanda.servicos[s];
			var selected = "";
			if(lancamentoServico && lancamentoServico.servico.id == servico.id){
				selected = "selected='selected'";
				servicoSelecionado = servico;
			}
			form+="<option "+selected+" value='"+servico.id+"'>" +servico.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p>Funcionario:</p>";
		form += "<select name='funcionarioId' multiple='multiple' "+readOnly+" class='tag-select-limited' placeholder='Selecione um funcionario' >";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = lancamentoServico ? (lancamentoServico.funcionario.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p>Assistente:</p>";
		form += "<select name='assistenteId' multiple='multiple' "+readOnly+" class='tag-select-limited' placeholder='Selecione um funcionario' >";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = lancamentoServico ? (lancamentoServico.assistente.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-1'>";
		form += "<p>Valor (R$):</p>";
		form += "<input name='valorServico' class='form-control input-sm m-b-10' readonly='readonly' id='valor-"+id+"' ";
		if(servicoSelecionado){
			form+= "value='" + servicoSelecionado.preco + "' ";
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
		var servicoId = $("#" + id + " select[name='servicoId']").val();
		if(servicoId){
			for(var i = 0; i < Comanda.servicos.length; i++){
				var servico = Comanda.servicos[i];
				if(servico.id == servicoId){
					$("#" + id + " input[name='valorServico']").val(servico.preco);
				}
			}
		}else{
			$("#" + id + " input[name='valorServico']").val("");
		}
		Comanda.preencherTotais(comandaId);
		return true;
	},
	
	buscarValorProduto: function(id, servico){
		servico = servico == null ? '' : servico;
		var produtoId = $("#" + id + " select[name='produto"+servico+"Id']").val();
		if(produtoId){
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
					$("#" + id + " input[name='valorProduto"+servico+"']").val((produto.precoRevenda * quantidade));
					$("#quantidade"+servico+"-" + id).html("Quantidade (" + produto.unidade + "):");
					break;
				}
			}
		}else{
			$("#" + id + " input[name='valorProduto']").val("");
		}
		Comanda.preencherTotais($("#comanda-form input[name='comandaId']").val());
		return true;
	},
	
	preencherTotais:function(comandaId){
		var total = 0;
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
		$("#valorTotal-" + comandaId).val(total);
		$("#valorCobrado-" + comandaId).val(total - desconto);
	},
	
	criarLinhaProduto: function(lancamentoProduto, readOnly, comandaId){
		readOnly = readOnly == null ? "" : readOnly;
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
		form += "<select name='produtoId' multiple='multiple' "+readOnly+" onchange='Comanda.buscarValorProduto(\""+id+"\")' class='tag-select-limited' placeholder='Selecione um produto' >";
		var produtoSelecionado = null;
		for(var s = 0; s < Comanda.produtos.length; s++){
			var produto = Comanda.produtos[s];
			if(produto.categoria != 'LOJA'){
				continue;
			}
			var selected = "";
			if(lancamentoProduto && lancamentoProduto.produto.id == produto.id){
				selected = "selected='selected'";
				produtoSelecionado = produto;
			}
			form+="<option "+selected+" value='"+produto.id+"'>" +produto.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-3'>";
		form += "<p>Vendedor:</p>";
		form += "<select name='vendedorId' multiple='multiple' "+readOnly+" class='tag-select-limited' placeholder='Selecione um vendedor' >";
		for(var s = 0; s < Comanda.funcionarios.length; s++){
			var funcionario = Comanda.funcionarios[s];
			var selected = lancamentoProduto ? (lancamentoProduto.vendedor.id == funcionario.id ? "selected='selected'": "") : "";
			form+="<option "+selected+" value='"+funcionario.id+"'>" +funcionario.nome+"</option>";
		}
		form += "</select>";
		form += "</div><div class='col-md-2'>";
		form += "<p id='quantidade-"+id+"'>Quantidade";
		if(lancamentoProduto){
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
		form += "<input name='valorProduto' class='form-control input-sm m-b-10' readonly='readonly' ";
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
	criarDivInfoComanda:function(comanda){
		var isComandaAberta = comanda.fechamento == null ? true : false;
		var info = "";
		var botoes = "";
		if(isComandaAberta){
			info += "<div class='action-buttons'>";
			info += '<a class="btn btn-lg m-r-5" onclick="Comanda.salvarComanda()" >Salvar comanda</a>';
			info += '<a class="btn btn-lg btn-alt m-r-5" onclick="Comanda.fecharComanda()" >Fechar comanda</a>';
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
		info += "<div class='col-md-9'>";
		info += "<div class='col-md-11'>";
		info += "<p class='total'>Total:</p>";
		info += "</div>";
		info += "<div class='col-md-1'>";
		info += "<input class='form-control input-sm m-b-10' id='valorTotal-"+comanda.id+"' name='total' readonly value='"+(comanda.valorTotal ? comanda.valorTotal : 0)+"' />";
		info += "</div>";
		info += "<div class='col-md-11'>";
		info += "<p class='total'>Descontos:</p>";
		info += "</div>";
		info += "<div class='col-md-1'>";
		info += "<input class='form-control input-sm m-b-10' id='descontos-"+comanda.id+"' " +
						"name='descontos' value='"+(comanda.desconto ? comanda.desconto : 0)+"' " +
						"onblur='Comanda.preencherTotais("+comanda.id+");' "+(isComandaAberta ? '' : 'readonly')+"  />";
		info += "</div>";
		info += "<div class='col-md-11'>";
		info += "<p class='total'>Valor cobrado:</p>";
		info += "</div>";
		info += "<div class='col-md-1'>";
		info += "<input class='form-control input-sm m-b-10' id='valorCobrado-"+comanda.id+"' name='valorCobrado' readonly value='"+(comanda.valorCobrado ? comanda.valorCobrado : 0)+"' />";
		info += "</div>";
		info += "<div class='col-md-11'>";
		info += "<p class='total'>Valor pago:</p>";
		info += "</div>";
		info += "<div class='col-md-1'>";
		info += "<input class='form-control input-sm m-b-10' id='valorPago-"+comanda.id+"' name='valorPago' readonly value='"+(comanda.valorPago ? comanda.valorPago : 0)+"' />";
		info += "</div>";
		info += "</div>";
		
		//Pagamentos
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
		
		info += "</div>";

		if(!isComandaAberta){
			info += "</div>";
			info += "</form>";
		}
		
		return info;
	},
	
	pagar:function(){
		$.ajax({
			url: '/min/web/clientes/pagar',
			type: 'POST',
			data: $("#novo-pagamento-form").serialize(),
			success:function(comanda){
				Comanda.refreshPagamentos(comanda);
			}
		});
	},
	
	refreshPagamentos: function(comanda){
		var isComandaAberta = comanda.fechamento == null ? true : false;
		$("#comanda-form" +(isComandaAberta ? '' : comanda.id)+ " tbody").html("");
		for(var i = 0; i < comanda.pagamentos.length; i++){
			var pagamento = comanda.pagamentos[i];
			Comanda.addPagamento(pagamento, (isComandaAberta ? '' : comanda.id));
		}
		$("#valorPago-" + comanda.id).val(comanda.valorPago);
	},
	
	addPagamento:function(pagamento, comandaId){
		var linha = "<tr id='pagamento-"+pagamento.id+"'>";
		linha += "<td>" + pagamento.formaPagamento + "</td>";
		linha += "<td>" + pagamento.valor + "</td>";
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
				}
			});
			
		}
	},
	
	novoPagamento:function(){
		$("#novo-pagamento-form input[name='comandaId']").val($("#comanda-form input[name='comandaId']").val());
		var cobrado = $("#comanda-form input[name='valorCobrado']").val();
		var pago = $("#comanda-form input[name='valorPago']").val();
		var valor = cobrado - pago;
		if(valor < 0){
			valor = 0;
		}
		$("#novo-pagamento-form input[name='valor']").val(valor);
		$("a[href='#modalWider']").click();
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
		$("#" + servicoId + " select").chosen({
            max_selected_options: 1
        });
		$(".mask-number").mask("##########0");
	},
	
	criarProdutoAoServico: function(servicoId, produtoUtilizado, readonly){
		readonly = readonly ? readonly : '';
		var id = "produtoServico-" + Utils.guid();
		var form = "<div class='' id='"+id+"'><div class='col-md-4'> <span class='icon' style='float: right;font-size: 20px;'>&#61807;</span></div><div class='col-md-4'>";
		form += "<input type='hidden' value='" + servicoId + "' name='guidProdutoServico' />";
		form += "<p>Produto:</p>";
		form += "<select name='produtoServicoId' multiple='multiple'  class='tag-select-limited' "+readonly+" placeholder='Selecione um produto' onchange='Comanda.buscarValorProduto(\"" + id + "\", \"Servico\")' >";
		for(var s = 0; s < Comanda.produtos.length; s++){
			var produto = Comanda.produtos[s];
			if(produto.categoria != 'SALAO'){
				continue;
			}
			var selected = produtoUtilizado ? (produtoUtilizado.produto.id == produto.id ? "selected='selected'" : "") : "";
			form+="<option "+selected+" value='"+produto.id+"'>" +produto.nome+"</option>";
		}
		form += "</select>";
		form += "</div>";
		form += "<div class='col-md-2'>";
		form += "<p id='quantidade-"+id+"'>Quantidade";
		if(produtoUtilizado){
			form += " (" + produtoUtilizado.produto.unidade + ")";
		}
		form += ":</p>";
		form += "<input name='quantidadeProdutoServico' class='form-control input-sm m-b-10' "+readonly+"  onblur='Comanda.buscarValorProduto(\"" + id + "\", \"Servico\")' ";
		if(produtoUtilizado){
			form+= "value='" + (produtoUtilizado.quantidadeUtilizada)+ "' ";
		}
		form += "/>";
		form += "</div>";
		form += "<div class='col-md-1'>";
		form += '<p>Valor (R$):</p>';
		form += '<input name="valorProdutoServico" class="form-control input-sm m-b-10" readonly ';
		if(produtoUtilizado){
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
		$.ajax({
			url: '/min/web/clientes/salvarComanda',
			type: 'POST',
			data: $("#comanda-form" + (id ? id : '')).serialize(),
			success: function(comanda){
				;
			}
		});
	},
	
	fecharComanda:function(){
		if(confirm("Tem certeza que deseja fechar esta comanda?")){
			$.ajax({
				url: '/min/web/clientes/fecharComanda',
				type: 'POST',
				data: $("#comanda-form").serialize(),
				success: function(comanda){
					$("#comanda-form").html("");
					if($("#comandas-fechadas").css('display') == 'block'){
						Comanda.findComandas();
					}
					Comanda.findComandaAberta();
				}
			});
		}
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