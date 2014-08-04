Tarefa = {
		criarBlocoTopo: function(){
			var html = '';
			html += '<div id="messages" class="tile drawer animated">';
			html += '    <div class="listview narrow">';
			html += '        <div class="media">';
			html += '             <a href="/min/web/tarefas/">Ver todas/Enviar mensagens</a>';
			html += '             <span class="drawer-close">&times;</span>';
			html += '        </div>';
			html += '        <div id="mensagens-top-list">';
			html += '        </div>';
			html += '        <div class="media text-center whiter l-100">';
			html += '             <a href="/min/web/tarefas/"><small>Ver todas/Enviar mensagens</small></a>';
			html += '        </div>';
			html += '    </div>';
			html += '</div>';
			$("#content").prepend(html);
		},
		exibirTarefasBlocoTopo:function(){
			$.ajax({
				url: "/min/web/tarefas/listarUltimas",
				type: "GET",
				success: function(tarefas){
					$("#mensagens-top-list").html("");
					$.each(tarefas, function(index, value){
						Tarefa.renderizarTarefa(value, "mensagens-top-list");
					});
				}
			});
		},
		contarNaoLidas:function(){
			$.ajax({
				url: "/min/web/tarefas/contarNaoLidas",
				type: "GET",
				success: function(quantidade){
					if(quantidade > 0){
						$(".n-count").html(quantidade);
						$(".n-count").show(300);
					}else{
						$(".n-count").hide(300);
					}
				}
			});
		},
		refreshTopo:function(){
			Tarefa.exibirTarefasBlocoTopo();
			Tarefa.contarNaoLidas();
		},
		salvarTarefa:function(){
			$.ajax({
				url: "/min/web/tarefas/salvar",
				type: "POST",
				data: $("#tarefa-form").serialize(),
				success:function(){
					Tarefa.exibirTodas();
					Tarefa.refreshTopo();
				}
			});
		},
		
		limparFormulario:function(){
			$("#tarefa-id").val("");
			$("#agendamento").val("");
			$("#agendamento-hora").val("");
			$("#descricao").val("");
		},
		
		exibirTodas:function(){
			$.ajax({
				url: "/min/web/tarefas/listarTodas",
				type: 'GET',
				success:function(tarefas){
					Tarefa.contarNaoLidas();
					$("#tarefas-block").html("");
					$.each(tarefas, function(index, value){
						Tarefa.renderizarTarefa(value);
					});
				}
			});
		},
		
		renderizarTarefa:function(tarefa, bloco){
			bloco = bloco ? bloco : "tarefas-block";
			var criacao = new Date(tarefa.dataCriacao);
			var usuarioLogadoPessoaId = $("#usuario-logado-pessoa-id").val();
			var html = "";
			html += ' <div id="tarefa-'+tarefa.id+'" class="media ';
//			if( tarefa.paraTodos){
//				html += " tarefa-geral";
//				html += '	">';
//				html += 	'<div class="checkbox-mensagem" onclick="Tarefa.fecharTarefa(\''+tarefa.id+'\')" ></div>';
//			}else{
					html += " tarefa-especifica ";
					if(tarefa.funcionario.id == usuarioLogadoPessoaId){
						html += (tarefa.concluida ? 'lido' : 'nao-lido' );
						html += '	">';
						html += 	'<div class="checkbox-mensagem" onclick="Tarefa.fecharTarefa(\''+tarefa.id+'\')" ></div>';
					}else{
						html += '	">';
					}
//			}
			html += 	"<input type='hidden' name='tarefaId' value='"+tarefa.id+"' />";
			html += 	'<a href="/min/web/funcionarios/editar/'+tarefa.criador.id+'" class="pull-left">';
			html += 	'<div class="imagem-notificacao" ';
			if(tarefa.funcionario && tarefa.funcionario.id != usuarioLogadoPessoaId){
				if(tarefa.funcionario.imagem){
					html += 	'style="background-image: url(\'/min/web/upload/'+tarefa.funcionario.imagem.id+'\');"';
				}
				html += 	'>';
				html +=		'</div></a>';
				html += 	'<div class="media-body">';
				html += 		'<small class="text-muted" >ENVIADA - '+ (tarefa.concluida ? '[LIDA]' : '[PENDENTE]' ) + " " + Utils.formatDateTime(criacao)+' - ';
			
				html +=				'para <a href="/min/web/funcionarios/editar/';
				html+= tarefa.funcionario.id+'" >' + tarefa.funcionario.nome;
				html +=				'</a>';
			}else{
				if(tarefa.criador.imagem){
					html += 	'style="background-image: url(\'/min/web/upload/'+tarefa.criador.imagem.id+'\');"';
				}
				html += 	'>';
				html +=		'</div></a>';
				html += 	'<div class="media-body">';
				html += 		'<small class="text-muted" >'+ Utils.formatDateTime(criacao)+' - ';
				
				html +=				'<a href="/min/web/funcionarios/editar/';
				html+= tarefa.criador.id+'" >' + tarefa.criador.nome;
				html +=				'</a>';
			}
			
			html +=				': </small>';
			html += 		'<div class="m-b-5 w-100-p">';
			if(tarefa.cliente){
				html += 'Sobre <a href="/min/web/clientes/editar/'+tarefa.cliente.id+'" >' + tarefa.cliente.nome + '</a> ';
			}
			if(tarefa.dataAgendada){
				var dataAgendada = new Date(tarefa.dataAgendada);
				html += ' para o dia ' + Utils.formatDate(dataAgendada);
			}
			html += 		'</div>';
			html += 		'<div class="descricao w-100-p" >'+tarefa.descricao+'</div>';
			if(tarefa.criador.id == usuarioLogadoPessoaId){
				html += 		'<div class="list-options">';
				html += 		'	  <button class="btn btn-sm" onclick="Tarefa.excluirTarefa(\''+tarefa.id+'\')">Excluir</button>';
				html += 		'</div>';
			}
			html += '	</div>';
			html += '</div>';
			$("#" + bloco).append(html);
		},
		excluirTarefa:function(id){
			if(confirm("Tem certeza que deseja excluir?")){
				$.ajax({
					url: "/min/web/tarefas/delete/" + id,
					type: "GET",
					success:function(){
						Tarefa.exibirTodas();
						Tarefa.refreshTopo();
					}
				});
			}
		},
		fecharTarefa:function(id){
			$.ajax({
				url: '/min/web/tarefas/fecharTarefa/' + id,
				type: 'GET',
				success:function(concluida){
					if(concluida){
						$("#tarefa-" + id).removeClass("nao-lido");
						$("#tarefa-" + id).addClass("lido");
					}
					Tarefa.refreshTopo();
				}
			});
		},
		
		lerTodas:function(){
			var naoLidos = $("#tarefas-block .nao-lido");
			$.each(naoLidos, function(index, value){
			   var id = $("#" + $(value).attr('id') + " input[name='tarefaId']").val();
			   Tarefa.fecharTarefa(id);
			});
		},
		
		pesquisar:function(){
			$.ajax({
				url: "/min/web/tarefas/pesquisar",
				type: 'POST',
				data: {
					pesquisa: $("#pesquisa-tarefa").val()
				},
				success:function(tarefas){
					$("#tarefas-block").html("");
					$.each(tarefas, function(index, value){
						Tarefa.renderizarTarefa(value);
					});
				}
			});
		}

};
Tarefa.criarBlocoTopo();
Tarefa.contarNaoLidas();