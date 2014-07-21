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
				Cliente.abrirAgendaCliente();
			});
		}	
	},
	abrirAgendaCliente:function(){
		Cliente.renderizarEventos(-1, -1, -1);
	},
	renderizarEventos: function(dia, mes, ano){
		var id = $("#cliente-id").val();
		$.ajax({
			url: '/min/web/agenda/cliente/' + id + '/'+ dia + "/" + mes + "/" + ano,
			type: 'GET',
			success:function(horarios){
				$('#calendar').fullCalendar('removeEvents');
				for(var i = 0; i < horarios.length; i++){
					var horario = horarios[i];
					var event = {
							title: "com " + horario.funcionario.nome,
							start: new Date(new Number(horario.inicio)),
							end: new Date(new Number(horario.termino)),
							allDay: false
					};
					$('#calendar').fullCalendar('renderEvent', event);
				}
				overflowRegular = $('.overflow').niceScroll();
			}
		});
	},
	criarBlocoDadosCompilados:function(){
		$("#dados-compilados").html("<h2 class='tile-title'>DADOS COMPILADOS</h2>");
		var keys = Object.keys(Cliente.dadosCompilados);
		for(var i = 0; i < keys.length; i++){
			var property = Cliente.dadosCompilados[keys[i]];
			if(property.title){
				var content = "<div class='listview narrow w-50-p'>";
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
		},
		servicosUsados:[],
		funcionariosAtendentes: []
	},
	comandas: [],
	gerarDados:function(){
		var id = $("#cliente-id").val();
		$.ajax({
			url: '/min/web/clientes/findComandas/' + id,
			type: 'GET',
			success: function(comandas){
				Agenda.comandas = comandas;
				
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
				Cliente.dadosCompilados.servicosUsados = [];
				var servicosIds = Object.keys(countServicos);
				if(servicosIds.length > 0){
					var servicoPreferidoId = servicosIds[0];
					for(var i = 0; i < servicosIds.length; i++){
						//Gráfico de pizza
						var servico = countServicos[servicosIds[i]];
						Cliente.dadosCompilados.servicosUsados.push({
							data: servico.length, label: servico[0].nome
						});
						
						if(countServicos[servicosIds[i]].length > countServicos[servicoPreferidoId].length){
							servicoPreferidoId = servicosIds[i];
						}
					}
					Cliente.dadosCompilados.servicoPreferido.value = countServicos[servicoPreferidoId][0].nome;
				}
				var funcionariosIds = Object.keys(countFuncionarios);
				Cliente.dadosCompilados.funcionariosAtendentes = [];
				if(funcionariosIds.length > 0){
					var funcionarioPreferidoId = funcionariosIds[0];

					for(var i = 0; i < funcionariosIds.length; i++){
						//Gráfico de pizza
						var funcionario = countFuncionarios[funcionariosIds[i]];
						Cliente.dadosCompilados.funcionariosAtendentes.push({
							data: funcionario.length, color: funcionario[0].cor, label: funcionario[0].nome
						});
						if(countFuncionarios[funcionariosIds[i]].length > countFuncionarios[funcionarioPreferidoId].length){
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
				

				Cliente.criarBlocoDadosCompilados();
				Cliente.criarGrafico();
				Cliente.criarGraficoPizza();
				
			}
		});
		
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

	},
	criarGraficoPizza:function(){
		var pieData = Cliente.dadosCompilados.funcionariosAtendentes;
		$.plot('#funcionarios-pizza', pieData, {
            series: {
                pie: {
                    show: true,
                    stroke: { 
                        width: 0,
                        
                    },
                    label: {
                        show: true,
                        radius: 3/4,
                        formatter: function(label, series){
                            return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+Math.round(series.percent)+'%</div>';
                        },
                        background: { 
                            opacity: 0.5,
                            color: '#000'
                        }
                    }
                }
            }
        });
		

		var servicos = Cliente.dadosCompilados.servicosUsados;
		$.plot('#servicos-pizza', servicos, {
			series: {
                
                
                pie: {
                    
                    show: true,
                    stroke: { 
                        width: 0,
                        
                    },
                    label: {
                        show: true,
                        radius: 3/4,
                        formatter: function(label, series){
                            return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+Math.round(series.percent)+'%</div>';
                        },
                        background: { 
                            opacity: 0.5,
                            color: '#000'
                        }
                    },
                    fill: 1,
                    fillColor: {
                        colors : ['rgba(255, 255, 255, 0.5)', 'rgba(0, 215, 76, 0.8)', 'rgba(255,255,255,0.8)']
                    } 
                }
            }
        });
		
		
		
		
	}
};