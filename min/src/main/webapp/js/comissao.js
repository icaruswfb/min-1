Comissao = {
	
	comissoes: {},
	comissoesTotal: 0,
	comissoesSelecionadas: {},
		
	exibirComissoesPorFuncionario:function(id){
		$.ajax({
			url: '/min/web/report/comissao',
			type: 'POST',
			data: {
				funcionarioId: id,
				dataInicio: $("#data-inicio").val(),
				dataFim: $("#data-fim").val(),
			},
			success:function(comissoes){
				$("#pagamento-comissao").html('');
				Comissao.comissoes= {};
				Comissao.comissoesTotal= 0;
				Comissao.comissoesSelecionadas = {};
				$.each(comissoes, function(index, comissao){
					Comissao.comissoes[comissao.id] = comissao;
					Comissao.rederizarComissao(comissao);
				});
				$(".mask-money").mask("#.##0,00", {reverse: true, maxlength: false});
			}
		});
	},
	
	rederizarComissao:function(comissao){
		var html = '';
		html += "<div class='media'>";
		html += "<div class='comissao-detalhe'>";
		html += 	"<div id='checkbox-"+comissao.id+"' class='checkbox-mensagem"+ (comissao.dataPagamento ? "-muted" : "") +"' onclick='Comissao.selecionarComissao("+comissao.id+")' ></div>";
		html += 	"<input type='hidden' id='selecionado-"+comissao.id+"' value='" + (comissao.dataPagamento ? true : false)  + "' />";
		html += 	"<div class='data-comissao'>";
		html +=		Utils.formatDate(new Date(new Number(comissao.dataCriacao)));
		html +=		"</div>";
		html += 	"<div class='descricao-comissao'>";
		html +=			"<a href='/min/web/clientes/editar/"+comissao.comanda.cliente.id+"' >" + comissao.comanda.cliente.nome + "</a>: ";
		if(comissao.tipo == 'VENDA'){
			html +=			"venda de produto";
		}else if( comissao.tipo == 'AUXILIAR' ){
			html +=			"auxilar";
		}else if( comissao.tipo == 'SERVICO' ){
			html +=			"servi&ccedil;o (sem auxiliar)";
		}else if( comissao.tipo == 'SERVICO_COM_AUXILIAR' ){
			html +=			"servi&ccedil;o com auxiliar";
		}
		html += 	"</div>";
		html +=		"<div class='valor-comissao'>";
		html += 		"<div class='prefix-money'>R$</div>";
		html +=			 "<div class='mask-money'>" + new Number(comissao.valor).toFixed(2) + "</div>";
		html += 	"</div>";
		html += "</div>";
		html += "</div>";
		$("#pagamento-comissao").append(html);
	},
	downloadComissoes:function(todos){
		Comissao.submitComssoes('/min/web/report/comissao/download', todos);
	},
	
	pagarComissoes:function(todos){
		Comissao.submitComssoes('/min/web/report/comissao/pagar', todos);
	},
	
	submitComssoes:function(url, todos){
		var ids = [];
		if(todos){
			$.each(Comissao.comissoes, function(i, value){
				ids.push(i);
			});
		}else{
			$.each(Comissao.comissoesSelecionadas, function(i, value){
				ids.push(i);
			});
		}
		$("#comissao-data-form #ids").val(ids.toString());
		$("#comissao-data-form").attr("action", url);
		$("#comissao-data-form").submit();
//		$.ajax({
//			url: url,
//			type: 'POST',
//			data: {
//				ids: ids.toString(),
//				dataInicio: $("#data-inicio").val(),
//				dataFim: $("#data-fim").val(),
//			},
//			success:function(){
//			}
//		});
	},
	
	selecionarComissao:function(id){
		if( $("#checkbox-" + id).attr('class').indexOf("pago") < 0 ){
			var selecionado = ! ($("#selecionado-" + id).val() == "true");
			$("#selecionado-" + id).val( selecionado );
			if(selecionado){
				$("#checkbox-" + id).addClass("checkbox-checked");
				Comissao.comissoesTotal += Comissao.comissoes[id].valor;
				Comissao.comissoesSelecionadas[id] = Comissao.comissoes[id];
			}else{
				$("#checkbox-" + id).removeClass("checkbox-checked");
				Comissao.comissoesTotal -= Comissao.comissoes[id].valor;
				Comissao.comissoesSelecionadas[id] = undefined;
			}
			$("#comissao-pagamento-total").html('');
			$("#comissao-pagamento-total").html(Comissao.comissoesTotal.toFixed(2));
			$(".mask-money").mask("#.##0,00", {reverse: true, maxlength: false});
		}
	}
};