package br.com.min.schedule;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.min.entity.Pessoa;
import br.com.min.entity.Tarefa;
import br.com.min.service.PessoaService;
import br.com.min.service.TarefaService;
import br.com.min.utils.Utils;

@Component
public class AniversarioSchedule {

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private TarefaService tarefaService;
	
	public void avisarProximosAniversarios(){
		avisarAniversarios(7);
		avisarAniversarios(3);
		avisarAniversarios(0);
	}
	
	private void avisarAniversarios(int periodo){
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DAY_OF_YEAR, periodo);
		
		System.out.println("Buscando aniversariantes do dia " + Utils.dateFormat.format(data.getTime()));
		Pessoa query = new Pessoa();
		query.setAniversario(data.getTime());
		List<Pessoa> pessoas = pessoaService.findPessoa(query);
		for(Pessoa cliente : pessoas){
			Tarefa tarefa = new Tarefa();
			tarefa.setCliente(cliente);
			tarefa.setConcluida(false);
			Pessoa criador = pessoaService.findById(1L);
			tarefa.setCriador(criador);
			tarefa.setDataAgendada(data.getTime());
			if(periodo == 0){
				tarefa.setDescricao("Aniversário hoje!");
			}else{
				tarefa.setDescricao("Aniversário em " + periodo + " dias!");
			}
			tarefa.setParaTodos(true);
			tarefaService.persist(tarefa);
		}
	}
	
}
