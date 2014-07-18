Cliente = {
	findHistorico:function(){
		$.ajax({
			url: '/min/web/clientes/historico/' + $("#cliente-id").val(),
			type: 'GET',
			success:function(historicos){
				$("#historico-list").html('');
				for(var i = 0; i < historicos.length; i++){
					var historico = historicos[i];
					var 
					content = '<div class="media">';
						content += '<small class="text-muted">';
						content += historico.textoPequeno;
						content += '</small><br />';
						content += ' <div class="media-body">';
							if(historico.funcionario){
								content += ' <a href="/min/web/funcionarios/editar/' + 
								historico.funcionario.id + 
								'">';
								content += historico.funcionario.nome;
								content += '</a>: ';
							}
							content += historico.texto;
						content += '  </div>';
					content += '   </div>';
					$("#historico-list").append(content);
				}
			}
		});
	},
	exibirHistorico: function(){
		if($("#historico-block").css("display") == 'block'){
			$("#historico-title-action").hide(100);
			$("#historico-title-action").html("[+]");
			$("#historico-title-action").show(100);
			$("#historico-block").hide(300);
		}else{
			$("#historico-title-action").hide(100);
			$("#historico-title-action").html("[-]");
			$("#historico-title-action").show(100);
			$("#historico-block").show(300, function(){
				Cliente.gerarDados();
				Cliente.criarBlocoDadosCompilados();
				Cliente.criarGrafico();
				Cliente.findHistorico();
			});
		}	
	},
	criarBlocoDadosCompilados:function(){
		$("#dados-compilados").html("<h2 class='tile-title'>DADOS COMPILADOS</h2>");
		var keys = Object.keys(Cliente.dadosCompilados);
		for(var i = 0; i < keys.length; i++){
			var property = Cliente.dadosCompilados[keys[i]];
			if(property.title){
				var content = "<div class='listview narrow'>";
				content += "<div class='media p-l-5'>";
				content += "<div class='media-body'>";
				content += "<small class='text-muted'>";
				content += property.title;
				content += "</small><br/>";
				content += property.valuePrefix + property.value;
				content += "</div>";
				content += "</div>";
				content += "</div>";
				$("#dados-compilados").append(content);
			}
		}
	},
	dadosCompilados:{
		grafico: [],
		totalVisitas: {
			title: "Total de visitas",
			valuePrefix: "",
			value: 0,
		},
		quantiaGasta: {
			title: "Quantia total gasta",
			valuePrefix: "R$",
			value: 0,
		},
		quantiaMediaGasta: {
			title: "Quantia m&eacute;dia gasta por visita",
			valuePrefix: "R$",
			value: 0,
		},
		servicoPreferido: {
			title: "Servi&ccedil;o preferido",
			valuePrefix: "",
			value: 0,
		},
		funcionarioPreferido: {
			title: "Mais atendido por",
			valuePrefix: "",
			value: 0,
		}
	},
	
	gerarDados:function(){
		var comandas = Comanda.comandas;
		var agrupadas = {};
		var hoje = new Date();
		Cliente.dadosCompilados.totalVisitas.value = comandas.length;
		var countServicos = {};
		var countFuncionarios = {};
		var totalGasto = 0;
		for(var i = 0; i < comandas.length; i++){
			var comanda = comandas[i];
			//Total gasto
			totalGasto += comanda.valorCobrado;
			
			//Servico preferido
			for(var s = 0; s < comanda.servicos.length; s++){
				var servico = comanda.servicos[s];
				if( ! countServicos[servico.servico.id]){
					countServicos[servico.servico.id] = [];
				}
				countServicos[servico.servico.id].push(servico.servico);
				if( ! countFuncionarios[servico.funcionario.id]){
					countFuncionarios[servico.funcionario.id] = [];
				}
				countFuncionarios[servico.funcionario.id].push(servico.funcionario);
			}
			
			//Grafico
			var dataAbertura = new Date(new Number(comanda.abertura));
			if(dataAbertura.getFullYear() == hoje.getFullYear()){
				var array = agrupadas[(dataAbertura.getMonth() + 1)];
				if( ! array){
					array = [];
				}
				array.push(comanda);
				agrupadas[(dataAbertura.getMonth() + 1)] = array;
			}
		}
		//Total gasto
		Cliente.dadosCompilados.quantiaGasta.value = totalGasto.toFixed(2);
		//Preferencias
		var servicosIds = Object.keys(countServicos);
		if(servicosIds.length > 0){
			var servicoPreferidoId = servicosIds[0];
			for(var i = 1; i < servicosIds.length; i++){
				if(countServicos[servicosIds[i]].length > countServicos[servicoPreferidoId]){
					servicoPreferidoId = servicosIds[i];
				}
			}
			Cliente.dadosCompilados.servicoPreferido.value = countServicos[servicoPreferidoId][0].nome;
		}
		var funcionariosIds = Object.keys(countFuncionarios);
		if(funcionariosIds.length > 0){
			var funcionarioPreferidoId = funcionariosIds[0];
			for(var i = 1; i < funcionariosIds.length; i++){
				if(countFuncionarios[funcionariosIds[i]].length > countFuncionarios[funcionarioPreferidoId]){
					funcionarioPreferidoId = funcionariosIds[i];
				}
			}
			Cliente.dadosCompilados.funcionarioPreferido.value = countFuncionarios[funcionarioPreferidoId][0].nome;
		}
		
		//Medias
		if(Cliente.dadosCompilados.totalVisitas.value == 0){
			Cliente.dadosCompilados.quantiaMediaGasta.value = 0;
		}else{
			Cliente.dadosCompilados.quantiaMediaGasta.value = (Cliente.dadosCompilados.quantiaGasta.value / Cliente.dadosCompilados.totalVisitas.value).toFixed(2);
		}
		
		//Grafico
		var grafico = [];
		for(var i = 1; i <= 12; i++){
			var mes = [i, (agrupadas[i] ? agrupadas[i].length : null)];
			grafico.push(mes);
		}
		Cliente.dadosCompilados.grafico = grafico;
	},
	criarGrafico: function(){
	    if ($('#grafico-frequencia')[0]) {
	        var d1 = Cliente.dadosCompilados.grafico;

	        $.plot('#grafico-frequencia', [ {
	            data: d1,
	            label: "Visitas",

	        },],

	            {
	                series: {
	                    lines: {
	                        show: true,
	                        lineWidth: 1,
	                        fill: 0.25,
	                    },

	                    color: 'rgba(255,255,255,0.7)',
	                    shadowSize: 0,
	                    points: {
	                        show: true,
	                    }
	                },

	                yaxis: {
	                    min: 0,
	                    max: 22,
	                    tickColor: 'rgba(255,255,255,0.15)',
	                    tickDecimals: 0,
	                    font :{
	                        lineHeight: 13,
	                        style: "normal",
	                        color: "rgba(255,255,255,0.8)",
	                    },
	                    shadowSize: 0,
	                },
	                xaxis: {
	                    tickColor: 'rgba(255,255,255,0)',
	                    tickDecimals: 0,
	                    font :{
	                        lineHeight: 13,
	                        style: "normal",
	                        color: "rgba(255,255,255,0.8)",
	                    }
	                },
	                grid: {
	                    borderWidth: 1,
	                    borderColor: 'rgba(255,255,255,0.25)',
	                    labelMargin:10,
	                    hoverable: true,
	                    clickable: true,
	                    mouseActiveRadius:6,
	                },
	                legend: {
	                    show: false
	                }
	            });

	        $("#grafico-frequencia").bind("plothover", function (event, pos, item) {
	            if (item) {
	                var x = item.datapoint[0],
	                    y = item.datapoint[1];
	                $("#linechart-tooltip").html(item.series.label + " no m&ecirc;s " + x + " = " + y).css({top: item.pageY+5, left: item.pageX+5}).fadeIn(200);
	            }
	            else {
	                $("#linechart-tooltip").hide();
	            }
	        });

	        $("<div id='linechart-tooltip' class='chart-tooltip'></div>").appendTo("body");
	    }

	}
};