package br.com.min.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.TarefaDAO;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Tarefa;

@Service
public class TarefaService {

	@Autowired
	private TarefaDAO dao;
	@Autowired
	private GenericDAO genericDAO;
	
	public Tarefa persist(Tarefa tarefa){
		tarefa.setDataCriacao(new Date());
		return (Tarefa) genericDAO.persist(tarefa);
	}
	
	public List<Tarefa> listarTarefasDoFuncionario(Long funcionarioId){
		Tarefa tarefa = criarQuery(funcionarioId, null, null);
		return dao.find(tarefa);
	}
	
	public List<Tarefa> listarTarefasDoDia(Long funcionarioId, Date dia){
		Tarefa tarefa = criarQuery(funcionarioId, dia, null);
		return dao.find(tarefa);
	}
	
	public List<Tarefa> listarTarefasCriadasAPartirDe(Long funcionarioId, Date data){
		Tarefa tarefa = criarQuery(funcionarioId, null, data);
		return dao.find(tarefa);
	}
	
	public List<Tarefa> listarTarefasAgendadasEntre(Long funcionarioId, Date inicio, Date fim){
		Tarefa tarefa = criarQuery(funcionarioId, null, null);
		return dao.find(tarefa, inicio, fim);
	}
	
	private Tarefa criarQuery(Long funcionarioId, Date agendamento, Date criacao){
		Tarefa tarefa = new Tarefa();
		tarefa.setFuncionario(new Pessoa());
		tarefa.getFuncionario().setId(funcionarioId);
		tarefa.setDataAgendada(agendamento);
		tarefa.setDataCriacao(criacao);
		return tarefa;
	}
	
}
