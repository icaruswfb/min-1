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
			
			limparHorario: function(){
				$(".search-choice-close").click();
			},
			
			popupHorario: function(hora, minuto, funcionarioIndex){
				Agenda.limparHorario();
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
				Agenda.calcularHorario();
				$("a[href='#modalWider']").click();
				
				Agenda.selecionarData();
			},
			
			criarCalendario: function(){
				$.ajax({
					type: 'GET',
					url: '/min/web/funcionarios/listarParaAgenda',
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
						Agenda.findHorariosDoDia();
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
						Agenda.organizarHorariosPorCliente(horarios);
						for(var i = 0; i < horarios.length; i++){
							var data = horarios[i];
							Agenda.renderHorario(data);
						}
					}
				});
			},
			horariosPorCliente: {},
			organizarHorariosPorCliente: function(horarios){
				Agenda.horariosPorCliente = {};
				for(var i = 0; i < horarios.length; i++){
					var horario = horarios[i];
					if(horario.folga){
						continue;
					}
					var horariosCliente = Agenda.horariosPorCliente[horario.cliente.id];
					if(!horariosCliente){
						horariosCliente = [];
					}
					horariosCliente.push(horario);
					Agenda.horariosPorCliente[horario.cliente.id] = horariosCliente;
				}
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
							if(!horario.folga){
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
					for(var m = minutosInicio;m < minutosFim; m++){
						var minClass = Agenda.getStringClassMinutos( m, primeiro );
						var classDivInicio = "h-" + h + "-" + minClass + "-" + indexFuncionario;
						var divHorario = $("." + classDivInicio);
						divHorario.attr('onclick', '');
						if(primeiro){
							Agenda.criarTooltip(horario, divHorario);
							primeiro = false;
						}else{
							divHorario.css("background-color", (Agenda.funcionarios[indexFuncionario].cor ? Agenda.funcionarios[indexFuncionario].cor : "#f0f0f0"));
							divHorario.css("box-shadow", "0 0 3px " +  (Agenda.funcionarios[indexFuncionario].cor ? Agenda.funcionarios[indexFuncionario].cor : "#f0f0f0"));
						}
					}
					minutosInicio = 0;
					setMinInicio = false;
				}
				
			},
			criarTooltip:function(horario, divHorario){
				divHorario.attr('style', 'border-radius: 4px 4px 0 0 !important;');
				var div = "<div class='titulo-horario'>";
				if( horario.folga){
					div += "<a>Folga</a>";
				}else{
					div += "<a href='/min/web/clientes/editar/"+horario.cliente.id+"'>" + horario.cliente.nome + 
					"</a>";
				}
				div += "</div>";
				divHorario.html(div);
				var tooltip = "<div class='tooltip-popup' >";
				if( ! horario.folga){
					var horariosCliente = Agenda.horariosPorCliente[horario.cliente.id];
					for(var x = 0; x < horariosCliente.length; x++){
						tooltip += "<div class='horarios-cliente'>";
						var horarioCliente = horariosCliente[x];
						
						var inicioData = new Date(horarioCliente.inicio);
						var terminoDate = new Date(horarioCliente.termino);
						
						var inicioHora = inicioData.getHours();
						var inicioMin = inicioData.getMinutes();
						var terminoHora = terminoDate.getHours();
						var terminoMin = terminoDate.getMinutes();
						
						tooltip += '<div style="height: 10px; width: 100%; margin-bottom: 10px; background-color: '+horarioCliente.funcionario.cor+';"></div>';
						tooltip += "<p>";
						tooltip += "" + inicioHora + ":" + (inicioMin < 10 ? "0" + inicioMin : inicioMin) + " - " + terminoHora + ":" + (terminoMin < 10 ? "0" + terminoMin : terminoMin) + "" ;
						tooltip += " com " + horarioCliente.funcionario.nome;
						
						tooltip +=	"</p>";
						tooltip += "<p>Fazendo: </p><ul>";
						for(var i = 0; i < horarioCliente.servicos.length; i++){
							tooltip += "<li>" + horarioCliente.servicos[i].nome + "</li>";
						}
						tooltip += "</ul>";
						if(horarioCliente.observacao){
							tooltip += "<p>" + horarioCliente.observacao + "</p>";
							
						}
						tooltip += '<div class="w-100 float-left">';

						tooltip += "<a href='javascript:Agenda.delelarHorario("+horarioCliente.id+")' style='float: right;margin: 5px'><img src='/min/img/icon/delete.png' /></a>";
						tooltip += "<a href='javascript:Agenda.popupEditar("+horarioCliente.id+")' style='float: right;margin: 5px'><img src='/min/img/icon/archive.png' /></a>";
						
						tooltip += "</div>";
						
						tooltip += "</div>";
					}
				}else{
					tooltip += "<div class='horarios-cliente'>";
					tooltip += "<a href='javascript:Agenda.delelarHorario("+horario.id+")'class='m-r-5' style='float: right;'><img src='/min/img/icon/delete.png' /></a>";
					if(horario.observacao){
						tooltip += "<p>" + horario.observacao + "</p>";
					}
					tooltip += "</div>";
				}
				tooltip + "</div>";
				divHorario.append(tooltip);
			},
			
			popupEditar:function(id){
				var horarios = Agenda.horariosPorCliente;
				var horario = null;
				$.each(horarios, function(index, horarioCliente){
					if(horario == null){
						for(var j = 0; j < horarioCliente.length;j++){
							if(horarioCliente[j].id == id){
								horario = horarioCliente[j];
								break;
							}
						}
					}
				});
				var inicioData = new Date(horario.inicio);
				var terminoDate = new Date(horario.termino);
				
				var inicioHora = inicioData.getHours();
				var inicioMin = inicioData.getMinutes();
				var terminoHora = terminoDate.getHours();
				var terminoMin = terminoDate.getMinutes();
				$("#editar-horario-id").val(id);
				var diaStr = $("#dia-agenda").val();
				$("#editar-data-agenda").val(diaStr);
				$("#editar-cliente").html(horario.cliente.nome);
				
				var tooltip = "<p>Fazendo: </p><ul>";
				for(var i = 0; i < horario.servicos.length; i++){
					tooltip += "<li>" + horario.servicos[i].nome + "</li>";
				}
				tooltip += "</ul>";
				$("#editar-servicos").html(tooltip);
				
				$("#editar-funcionario-select").val(horario.funcionario.id);
				$("#editar-funcionario-select").change();
				var inicioStr = (inicioHora < 10 ? ("0" + inicioHora) : inicioHora) + ":" + (inicioMin < 10 ? ("0" + inicioMin) : inicioMin);
				$("#editar-horario-inicio-agenda").val(inicioStr);
				var fimStr = (terminoHora < 10 ? ("0" + terminoHora) : terminoHora) + ":" + (terminoMin < 10 ? ("0" + terminoMin) : terminoMin);
				$("#editar-horario-fim-agenda").val(fimStr);
				$("#editar-observacao").val(horario.observacao);
				$("a[href='#modalEdit']").click();
			},
			
			editarHorario:function(){
				Utils.modalLoading();
				var id = $("#editar-horario-id").val();
				var funcionarioId = $("#editar-funcionario-select").val();
				var dataStr = $("#editar-data-agenda").val();
				var horarioInicio = $("#editar-horario-inicio-agenda").val();
				var horarioFim = $("#editar-horario-fim-agenda").val();
				var obs = $("#editar-observacao").val();
				if(horarioFim == "" || horarioInicio == "" || dataStr == ""){
					Utils.createMessageBlock("Preencha todos os campos para agendar um novo hor&aacute;rio", "#edit-horario-msg", "danger", "edit-horario-msg" + Utils.guid());
					Utils.modalLoadingFinish();
					return;
				}
				var request = 
				{
						id: id,
						funcionarioId: funcionarioId.toString(),
						inicio: dataStr + " " + horarioInicio,
						termino: dataStr + " " + horarioFim,
						observacao: obs
				};
				
				$.ajax({
					url: "/min/web/agenda/alterar",
					type: "POST",
					data: request,
					success: function(data){
						location.reload();
					},
					error: function(e){
						Utils.showError("Houve um erro na hora de cadastrar o agendamento. Verifique se o hor&aacute;rio j&aacute; est&aacute; ocupado e se as horas foram selecionadas corretamente.");
					},
					complete:function(){
						$("#modalEdit .close").click();
						Utils.modalLoadingFinish();
					}
				});
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
				Utils.modalLoading();
				var clienteId = $("#cliente-select").val();
				var folga = $("#folga").val();
				if(clienteId == null){
					clienteId = $("#cliente_select_chzn input").val();
					if( (clienteId == "Selecionar cliente..." || clienteId == "") && folga != "true" ){
						Utils.createMessageBlock("Preencha todos os campos para agendar um novo hor&aacute;rio", "#add-horario-msg", "danger", "add-horario-msg" + Utils.guid());
						Utils.modalLoadingFinish();
						return false;
					}
				}
				var funcionarioId = $("#funcionario-select").val();
				var dataStr = $("#dia-agenda").val();
				var horarioInicio = $("#horario-inicio-agenda").val();
				var horarioFim = $("#horario-fim-agenda").val();
				var servicos = $("#servico-select").val();
				var obs = $("#observacao").val();
				if(folga == "true"){
					if(horarioFim == '' || horarioInicio == ''){
						Utils.createMessageBlock("Preencha todos os campos para agendar um novo hor&aacute;rio", "#add-horario-msg", "danger", "add-horario-msg" + Utils.guid());
						Utils.modalLoadingFinish();
						return false;
					}
				}else{
					if(clienteId == null || horarioFim == '' || servicos == null || horarioInicio == ''){
						Utils.createMessageBlock("Preencha todos os campos para agendar um novo hor&aacute;rio", "#add-horario-msg", "danger", "add-horario-msg" + Utils.guid());
						Utils.modalLoadingFinish();
						return false;
					}
				}
				var request = folga == "true" ?
				{
					funcionarioId: funcionarioId.toString(),
					inicio: dataStr + " " + horarioInicio,
					termino: dataStr + " " + horarioFim,
					folga: folga,
					observacao: obs
				}
				: 
				{
						clienteStr : clienteId.toString(),
						funcionarioId: funcionarioId.toString(),
						inicio: dataStr + " " + horarioInicio,
						termino: dataStr + " " + horarioFim,
						servicosId: servicos.toString(),
						folga: folga,
						observacao: obs
				};
				
				$.ajax({
					url: "/min/web/agenda/agendar",
					type: "POST",
					data: request,
					success: function(data){
						if(data == "refresh"){
							location.reload();
						}else{
							Agenda.findHorariosDoDia();
						}
					},
					error: function(e){
						Utils.showError("Houve um erro na hora de cadastrar o agendamento. Verifique se o hor&aacute;rio j&aacute; est&aacute; ocupado e se as horas foram selecionadas corretamente.");
					},
					complete:function(){
						$("#modalWider .close").click();
						Utils.modalLoadingFinish();
					}
				});
			},
			
			showFolga:function(){
				var checked = !($("#folga").val() == "true");
				$("#folga").val( checked );
				if(checked){
					$("#checkbox-folga").addClass("checkbox-checked");
					$(".horario-trabalho").hide(300);
				}else{
					$("#checkbox-folga").removeClass("checkbox-checked");
					$(".horario-trabalho").show(300);
				}
			},
			
			verificarCliente:function(){
				var cliente = $("#cliente_select_chzn input").val();
				if(cliente != "Selecionar cliente..."){
					setTimeout(function(){
						var selecionado = $("#cliente-select").val();
						if(selecionado == null || selecionado == ""){
							$("#cliente_select_chzn input").val(cliente);
						}
					}, 200);
				}
			}
			
};