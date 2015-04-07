package br.com.min.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.TarefaDAO;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Tarefa;

@Service
public class TarefaService extends BaseService<Tarefa, TarefaDAO> {

	@Autowired
	private TarefaDAO dao;
	@Autowired
	private GenericDAO genericDao;
	@Autowired
	private PessoaService pessoaService;
	
	public Tarefa persist(Tarefa tarefa){
		if(tarefa.getId() == null){
			tarefa.setDataCriacao(new Date());
			if(tarefa.getFuncionario() == null){
				tarefa.setParaTodos(true);
				List<Tarefa> novas = copiarParaTodos(tarefa);
				Tarefa toReturn = null;
				for(Tarefa nova : novas){
					if(nova.getFuncionario().equals(tarefa.getCriador())){
						toReturn = nova;
					}
					genericDao.persist(nova);
				}
				return toReturn;
			}else{
				tarefa.setParaTodos(true);
			}
		}
		return (Tarefa) genericDao.persist(tarefa);
	}
	
	private List<Tarefa> copiarParaTodos(Tarefa tarefa){
		List<Pessoa> funcionarios = pessoaService.listarFuncionarios();
		List<Tarefa> novas = new ArrayList<Tarefa>();
		for(Pessoa funcionario : funcionarios){
			novas.add(copiarTarefaPara(tarefa, funcionario));
		}
		return novas;
	}
	
	private Tarefa copiarTarefaPara(Tarefa tarefa, Pessoa pessoa){
		Tarefa nova = new Tarefa();
		try {
			BeanUtils.copyProperties(nova, tarefa);
			nova.setFuncionario(pessoa);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return nova;
	}
	
	public List<Tarefa> listarTarefasDoFuncionario(Long funcionarioId){
		Tarefa tarefa = criarQuery(funcionarioId, null, null);
		List<Tarefa> result = dao.find(tarefa);
		cleanResult(result);
		return result;
	}
	
	public List<Tarefa> listarTarefasDoDia(Long funcionarioId, Date dia){
		Tarefa tarefa = criarQuery(funcionarioId, dia, null);
		List<Tarefa> result = dao.find(tarefa);
		cleanResult(result);
		return result;
	}
	
	public List<Tarefa> listarUltimas(Pessoa pessoa, Integer quantidade){
		List<Tarefa> result = dao.listarUltimas(pessoa, quantidade);
		cleanResult(result);
		return result;
	}
	
	public List<Tarefa> listarTarefasCriadasAPartirDe(Long funcionarioId, Date data){
		Tarefa tarefa = criarQuery(funcionarioId, null, data);
		List<Tarefa> result = dao.find(tarefa);
		cleanResult(result);
		return result;
	}
	
	public List<Tarefa> listarTarefasAgendadasEntre(Long funcionarioId, Date inicio, Date fim){
		Tarefa tarefa = criarQuery(funcionarioId, null, null);
		return cleanResult(dao.find(tarefa, inicio, fim));
	}
	
	private Tarefa criarQuery(Long funcionarioId, Date agendamento, Date criacao){
		Tarefa tarefa = new Tarefa();
		tarefa.setFuncionario(new Pessoa());
		tarefa.getFuncionario().setId(funcionarioId);
		tarefa.setDataAgendada(agendamento);
		tarefa.setDataCriacao(criacao);
		return tarefa;
	}
	
	public void delete(Long id) {
		Tarefa tarefa = new Tarefa();
		tarefa.setId(id);
		genericDao.delete(tarefa);
	}

	public Tarefa findById(Long id) {
		Tarefa tarefa = dao.findById(id);
		return tarefa;
	}

	public List<Tarefa> pesquisar(String pesquisa, Pessoa pessoa) {
		return cleanResult(dao.pesquisar(pesquisa, pessoa));
	}

	public Integer contarNaoLidas(Pessoa pessoa) {
		return dao.contarNaoLidas(pessoa);
	}
	
}
