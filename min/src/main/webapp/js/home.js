Home = {
	isHome: false,
	exibirProximasTarefas:function(){
		$.ajax({
			url: "/min/web/tarefas/listarProximasAgendadas",
			type: 'GET',
			success:function(tarefas){
				$("#tarefas-todo").html('');
				$.each(tarefas, function(index, tarefa){
					Tarefa.renderizarTarefa(tarefa, "tarefas-todo");
				});
			}
		});
	}	
};