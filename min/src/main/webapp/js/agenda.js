Agenda = {
			servicos: {},
			calcularHorario: function(){
				var values = $("#servico-select").val();
				var totalMillis = 0;
				if( values == null ){
					$("#horario-fim-agenda").val("");
					return true;
				}
				for(var i = 0; i < values.length; i++){
					var servico = Agenda.servicos[values[i]];
					totalMillis += servico.duracao;
				}
				var now = new Date();
				var horarioInicio = $("#horario-inicio-agenda").val();
				var splitInicio = horarioInicio.split(":");
				var inicio = new Date(now.getFullYear(), now.getMonth(), now.getDate(), splitInicio[0], splitInicio[1]);
				var fim = new Date(inicio.getTime() + totalMillis);
				var fimMin = fim.getMinutes() < 10 ? "0" + fim.getMinutes() : fim.getMinutes();
				var fimHora = fim.getHours() < 10 ? "0" + fim.getHours() : fim.getHours();
				$("#horario-fim-agenda").val("" + fimHora + ":" + fimMin);
			},
			funcionarios: [],
			selecionarData: function(){
				setTimeout(function(){
                	var dataMillis = new Number($("#dia-agenda-millis").val());
	       			var data = new Date(dataMillis);
	       			$("#sidebar-calendar").fullCalendar("select", data);
				}, 1000);
			},
			verAgenda: function(dia, mes, ano){
				window.location = "/min/web/agenda/ver/" + dia + "/" + mes + "/" + ano;
			},
			
			popupHorario: function(hora, minuto, funcionarioIndex){
				var horaStr = "";
				if(hora < 10){
					horaStr += "0";
				}
				horaStr += hora;
				var minutoStr = "";
				if(minuto < 10){
					minutoStr += "0";
				}
				minutoStr += minuto;
				$("#horario-inicio-agenda").val(horaStr + ":" + minutoStr);
				$("#horario-fim-agenda").val("");
				
				var funcionario = Agenda.funcionarios[funcionarioIndex];
				$("#funcionario-select").val(funcionario.id).change();
				
				$("a[href='#modalWider']").click();
				
				Agenda.selecionarData();
			},
			
			criarCalendario: function(){
				$.ajax({
					type: 'GET',
					url: '/min/web/funcionarios/listar',
					success: function(funcionarios){
						Agenda.funcionarios = funcionarios;
						var html = "<table class='calendario-table table tile'></table>";
						$("#calendar").html(html);
						
						//Colunas por funcionários
						var header = "<thead><tr><th class='first'></th>";
						var width = $(".calendario-table").width() - 50 / funcionarios.length;
						for(var i = 0; i < funcionarios.length; i++){
							var funcionario = funcionarios[i];
							header += '<th style="width: '+width+'px"><a href="/min/web/funcionarios/editar/'+funcionario.id+'">' + funcionario.nome + '</a></th>';
						}
						header+= "</tr></thead>";
						
						$("#calendar .calendario-table").append(header);
						//Linhas por hora
						for(var h = 8; h < (20); h++){
							var linha = "<tr class='hora' >" +
													"<td class='first'>" +
														"<div class='horario'>" + h + ":00</div>" +
														"<div class='horario'><small class='text-muted'>15</small></div>" +
														"<div class='horario'><small class='text-muted'>30</small></div>" +
														"<div class='horario'><small class='text-muted'>45</small></div>" +
													"</td>";
							for(var i = 0; i < funcionarios.length; i++){
								linha += "<td>";
								linha += "<div class='horario h-"+h+"-00-"+i+"' onclick='Agenda.popupHorario("+h+", 0,"+i+")'><small class='hora-vaga text-muted'>"+h+":00</small></div>";
								linha +="<div class='horario h-"+h+"-15-"+i+"' onclick='Agenda.popupHorario("+h+", 15,"+i+")'><small class='hora-vaga text-muted'>"+h+":15</small></div>";
								linha +="<div class='horario h-"+h+"-30-"+i+"' onclick='Agenda.popupHorario("+h+", 30,"+i+")'><small class='hora-vaga text-muted'>"+h+":30</small></div>";
								linha +="<div class='horario h-"+h+"-45-"+i+"' onclick='Agenda.popupHorario("+h+", 45,"+i+")'><small class='hora-vaga text-muted'>"+h+":45</small></div>";
								linha += "</td>";
							}
							linha += "</tr>";

							$("#calendar .calendario-table").append(linha);
						}
						
					}
				});
			},
			
		
			findHorariosDoDia: function(){
				var dataMillis = new Number($("#dia-agenda-millis").val());
				var data = new Date(dataMillis);
				var dia = data.getDate();
				var mes = data.getMonth() + 1;
				var ano = data.getFullYear();
				Agenda.callFindHorarios(dia, mes, ano, "all");
			},
			
			colunas: {},
			colunasCount: 0,
			esquerda: undefined,
			largura: undefined,
			
			callFindHorarios: function (dia, mes, ano, funcionarios){
				$.ajax({
					url: "/min/web/agenda/" + dia + "/" + mes + "/" + ano + "/" + funcionarios,
					type: "GET",
					success: function (horarios){
						Agenda.renderFeedAgenda(horarios);
						for(var i = 0; i < horarios.length; i++){
							var data = horarios[i];
							Agenda.renderHorario(data);
						}
					}
				});
			},

			criarFeedAgenda:function(){
				var data = new Date();
				var dia = data.getDate();
				var mes = data.getMonth() + 1;
				var ano = data.getFullYear();
				$.ajax({
					url: "/min/web/agenda/" + dia + "/" + mes + "/" + ano + "/" + "all",
					type: "GET",
					success: function (horarios){
						Agenda.renderFeedAgenda(horarios);
					}
				});
			},
			
			renderFeedAgenda:function(horarios){
				$("#feed-agenda").html('');
				var agora = new Date();
				for(var i = 0; i < horarios.length; i++){
					var horario = horarios[i];
					var inicio = new Date(new Number(horario.inicio));
					if(agora.getDate() == inicio.getDate() 
							&& agora.getMonth() == inicio.getMonth()
							&& agora.getFullYear() == inicio.getFullYear()	){
						if(agora.getHours() < inicio.getHours() || (agora.getHours() == inicio.getHours() && agora.getMinutes() <= inicio.getMinutes())){
							var content = "";
							
							content += "<div class='side-border'>";
							content += "	<div class=''media-body'>";
							content += "		<small class='text-muted'>";
							content += 				Utils.formatTime(inicio);
							content += "			com ";
							content += "		<a href='/min/web/funcionarios/editar/"+horario.funcionario.id+"'>";
							content += 				horario.funcionario.nome;
							content += "		</a>";
							content += "		</small><br />";
							content += "		<a class='t-overflow' href='/min/web/clientes/editar/"+horario.cliente.id+"'>";
							content += 				horario.cliente.nome;
							content += "		</a>";
							content += "	</div>";
							content += "</div>";
							$("#feed-agenda").append(content);
						}
					}
				}
			},
			
			renderHorario : function(horario){
				var indexFuncionario = 0;
				for(var i = 0; i < Agenda.funcionarios.length; i++){
					var funcionario = Agenda.funcionarios[i];
					if(funcionario.id == horario.funcionario.id){
						indexFuncionario = i;
						break;
					}
				}
				var inicioData = new Date(horario.inicio);
				var terminoDate = new Date(horario.termino);
				
				var inicioHora = inicioData.getHours();
				var inicioMin = inicioData.getMinutes();
				var terminoHora = terminoDate.getHours();
				var terminoMin = terminoDate.getMinutes();
				
				var setMinInicio = true;
				var minutosInicio = 0;
				var primeiro = true;
				
				for(var h = inicioHora;h <= terminoHora; h++){
					if(setMinInicio)
						minutosInicio = inicioMin;
					var minutosFim = terminoMin;
					if(h < terminoHora){
						minutosFim = 60;
					}
					for(var m = minutosInicio;m <= minutosFim; m++){
						var minClass = Agenda.getStringClassMinutos( m, primeiro );
						var classDivInicio = "h-" + h + "-" + minClass + "-" + indexFuncionario;
						var divHorario = $("." + classDivInicio);
						divHorario.attr('onclick', '');
						if(primeiro){
							divHorario.attr('style', 'border-radius: 4px 4px 0 0 !important;');
							var tooltip = "<div class='tooltip-popup t-"+classDivInicio+"' style='width:"+divHorario.width()+"px'><p>" + horario.cliente.nome + 
									" (" + inicioHora + ":" + (inicioMin < 10 ? "0" + inicioMin : inicioMin) + " - " + terminoHora + ":" + (terminoMin < 10 ? "0" + terminoMin : terminoMin) + ")</p>";
							tooltip += "<p>Fazendo: <br />";
							for(var i = 0; i < horario.servicos.length; i++){
								tooltip += "-" + horario.servicos[i].nome + "<br />";
							}
							tooltip += "</p>";
							if(horario.observacao){
								tooltip += "<p>" + horario.observacao + "</p>";
								
							}
							tooltip += "<a href='javascript:Agenda.delelarHorario("+horario.id+")' style='float: right;margin-right: 5px;'><img src='/min/img/icon/delete.png' /></a>"+
							"</div>";
							var div = "<div class='titulo-horario'><a href='/min/web/clientes/editar/"+horario.cliente.id+"'>" + horario.cliente.nome + 
											"</a></div>";
							divHorario.html(div);
							divHorario.append(tooltip);
							primeiro = false;
						}else{
							divHorario.css("background-color", Agenda.funcionarios[indexFuncionario].cor);
							divHorario.css("box-shadow", "0 0 3px " + Agenda.funcionarios[indexFuncionario].cor);
						}
					}
					minutosInicio = 0;
					setMinInicio = false;
				}
				
			},
			delelarHorario: function(horarioId){
				if(confirm("Tem certeza que deseja excluir?")){
					$.ajax({
						url: '/min/web/agenda/delelarHorario/' + horarioId,
						type: 'GET',
						success: function(){
							Agenda.refresh();
						}
					});
				}
			},
			
			refresh: function(){
				$("#calendar").html('');
				Agenda.criarCalendario();
				Agenda.findHorariosDoDia();
			},
			getStringClassMinutos:function(minutos, primeiro){
				var min = "00";
				if( ! primeiro){
					minutos = minutos -1;
				}
				if(minutos < 15){
					min = "00";
				}else if(minutos < 30){
					min = "15";
				}else if(minutos < 45){
					min = "30";
				}else if(minutos <= 60){
					min = "45";
				}
				return min;
			},
			
			addHorario:function (){
				var clienteId = $("#cliente-select").val();
				var funcionarioId = $("#funcionario-select").val();
				var dataStr = $("#dia-agenda").val();
				var horarioInicio = $("#horario-inicio-agenda").val();
				var horarioFim = $("#horario-fim-agenda").val();
				var servicos = $("#servico-select").val();
				var obs = $("#observacao").val();
				if(clienteId == null || horarioFim == '' || servicos == null){
					Utils.showError("Preencha todos os campos para agendar um novo hor&aacute;rio");
					return false;
				}
				var request = {
						clienteId : clienteId.toString(),
						funcionarioId: funcionarioId.toString(),
						inicio: dataStr + " " + horarioInicio,
						termino: dataStr + " " + horarioFim,
						servicosId: servicos.toString(),
						observacao: obs
				};
				
				$.ajax({
					url: "/min/web/agenda/agendar",
					type: "POST",
					data: request,
					success: function(data){
		                Agenda.findHorariosDoDia();
					},
					error: function(e){
						Utils.showError("Hor&aacute;rio j&aacute; est&aacute; ocupado.");
					}
				});
			}
};