Tarefa = {
		criarBlocoTopo: function(){
			var html = '';
			html += '<div id="messages" class="tile drawer animated">';
			html += '    <div class="listview narrow">';
			html += '        <div class="media">';
			html += '             <a href="">Send a New Message</a>';
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
		
		exibirTodas:function(){
			$.ajax({
				url: "/min/web/tarefas/listarTodas",
				type: 'GET',
				success:function(tarefas){
					$.each(tarefas, function(index, value){
						Tarefa.renderizarTarefa(value);
					});
				}
			});
		},
		
		renderizarTarefa:function(tarefa){
			var html = "";
			html += ' <div class="media">';
			html += 	' <input type="checkbox" class="pull-left list-check" value="" >';
			html += 	'<div class="pull-left">';
			html += 		'<img width="40" src="/min/web/upload/49" alt="">';
			html +=		'</div>';
			html += 	'<div class="media-body">';
			html += 		'<small class="text-muted" >data e criador</small>';
			html += 		'<p>sbrubles</p>';
			html += '	</div>';
			html += '</div>';
			$("#tarefas-block").append(html);
		}

};
Tarefa.criarBlocoTopo();