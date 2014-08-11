Financeiro = {

		adicionarLancamento: function(){
			Utils.unmaskMoney();
			$.ajax({
				url: '/min/web/report/addLancamento',
				type: 'POST',
				data: $("#novo-lancamento-form").serialize(),
				success: function(){
					$("#fluxo-data-form").submit();
				}
			});
		},
		
		pagamentos:[],
		
		gerarDadosGraficoFinanceiro:function(){
			 var totalPorMes = {};
		        for(var i = 0; i < Financeiro.pagamentos.length; i++){
		        	var pagamento = Financeiro.pagamentos[i];
		        	var index = pagamento.ano + "" + pagamento.mes;
		        	var pagamentosDoMes = totalPorMes[index];
		        	if(pagamentosDoMes == undefined){
		        		pagamentosDoMes = [];
		        	}
		        	pagamentosDoMes.push(pagamento);
		        	totalPorMes[index] = pagamentosDoMes; 
		        }

		        var saida = [];
		        var entrada = [];
		        var lucro = [];
		        var base = [];
		        $.each(totalPorMes, function(index){
		        	if(index != 0){
		        		var b = null;
		        		for(var j = 0; j < base.length; j++){
		        			var test = base[j];
		        			if(test[0] == index){
		        				b = test;
		        				break;
		        			}
		        		}
		        		if(b == null){
		        			b = [index, 0];
		        			base.push(b);
		        		}
		        	}
		        });
		        sortFunction = function(a, b){
		        	return a[0] - b[0];
		        };
		        base.sort(sortFunction);
		        
		        var nomesMeses = {};
		        
		        for(var i = 0; i < base.length; i++){
		        	var b = base[i];
		        	var pagamentos = totalPorMes[b[0]];
		        	var totalEntrada = 0;
		        	var totalSaida = 0;
		        	$.each(pagamentos, function(index, pagamento){
		        		nomesMeses[(i + 1)] = pagamento.mes+ "/" + pagamento.ano;
		        		if(pagamento.tipo == "SAIDA"){
		        			totalSaida += pagamento.valor;
		        		}else{
		        			totalEntrada += pagamento.valor;
		        		}
		        	});
		        	entrada.push([(i + 1), totalEntrada]);
		        	saida.push([(i + 1), totalSaida]);
		        	lucro.push([(i + 1), totalEntrada - totalSaida]);
		        }
		        return {
		        	entrada: entrada,
		        	saida: saida,
		        	lucro: lucro,
		        	nomesMeses: nomesMeses
		        };
		},
		
		criarGrafico: function(){
			if ($("#grafico-entrada")[0]) {
				var dados = Financeiro.gerarDadosGraficoFinanceiro();
		        var barData = new Array();

		        barData.push({
		                data : dados.saida,
		                label: 'Sa&iacute;da',
		                bars : {
		                        show : true,
		                        barWidth : 0.1,
		                        order : 1,
		                        fill:1,
		                        lineWidth: 0,
		                        fillColor: 'rgba(255,255,255,0.6)'
		                }
		        });
		        barData.push({
		                data : dados.entrada,
		                label: 'Entrada',
		                bars : {
		                        show : true,
		                        barWidth : 0.1,
		                        order : 2,
		                        fill:1,
		                        lineWidth: 0,
		                        fillColor: 'rgba(255,255,255,0.8)'
		                }
		        });
		        barData.push({
		                data : dados.lucro,
		                label: 'Lucro',
		                bars : {
		                        show : true,
		                        barWidth : 0.1,
		                        order : 3,
		                        fill:1,
		                        lineWidth: 0,
		                        fillColor: 'rgba(255,255,255,0.3)'
		                },
		        });

		        //Display graph
		        $.plot($("#grafico-entrada"), barData, {
		                
		                grid : {
		                        borderWidth: 1,
		                        borderColor: 'rgba(255,255,255,0.25)',
		                        show : true,
		                        hoverable : true,
		                        clickable : true,       
		                },
		                
		                yaxis: {
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
		                    },
		                    shadowSize: 0,
		                },
		                
		                legend : true,
		                tooltip : true,
		                tooltipOpts : {
		                        content : "<b>%x</b> = <span>%y</span>",
		                        defaultTheme : false
		                }

		        });
		        
		        $("#grafico-entrada").bind("plothover", function (event, pos, item) {
		            if (item) {
		                var x = item.datapoint[0].toFixed(2),
		                    y = item.datapoint[1].toFixed(2);
		                $("#barchart-tooltip").html(item.series.label + " de " + dados.nomesMeses[(Math.round(x))]  + " = R$<span class='mask-money'>" + new Number(y).toFixed(2) + "</span>").css({top: item.pageY+5, left: item.pageX+5}).fadeIn(200);

				        $('.mask-money').mask("#.##0,00", {reverse: true, maxlength: false});
		            }
		            else {
		                $("#barchart-tooltip").hide();
		            }
		        });

		        $("<div id='barchart-tooltip' class='chart-tooltip'></div>").appendTo("body");
		    }

		},
		
		gerarDadosFormasPagamento:function(){
			var totalFormaPagamento = {};
			$.each(Financeiro.pagamentos, function(index, pagamento){
				var total = totalFormaPagamento[pagamento.formaPagamento];
				if(pagamento.formaPagamento != "" && pagamento.tipo == "ENTRADA"){
					if(total == undefined){
						total = 0;
					}
					total += pagamento.valor;
					totalFormaPagamento[pagamento.formaPagamento] = total;
				}
			});
			var result = [];
			$.each(totalFormaPagamento, function(index, valor){
				result.push({
					data: valor,
					label: index
				});
			});
			
			return result;
		},
		
		criarGraficoFormasPagamento: function(){
			var formasPagamento = Financeiro.gerarDadosFormasPagamento();
			$.plot('#grafico-formasPagamento', formasPagamento, {
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