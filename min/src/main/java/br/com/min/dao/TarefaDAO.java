package br.com.min.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Pessoa;
import br.com.min.entity.Tarefa;

@Repository
public class TarefaDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Tarefa> find(Tarefa tarefa, Date agendamentoInicio, Date agendamentoFim){
		Criteria criteria = criarCriteriaBasica(tarefa);
		
		if(agendamentoInicio == null){
			criteria.add(Restrictions.le("dataAgendada", agendamentoFim));
		}else{
			if(agendamentoFim == null){
				criteria.add(Restrictions.ge("dataAgendada", agendamentoInicio));
			}else{
				criteria.add(Restrictions.between("dataAgendada", agendamentoInicio, agendamentoFim));
			}
		}
		
		criteria.addOrder(Order.desc("dataCriacao"));
		List<Tarefa> tarefas = criteria.list();
		return tarefas;
	}
	
	public Tarefa findById(Long id){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Tarefa.class);
		
		criteria.add(Restrictions.eq("id", id));
		return (Tarefa) criteria.uniqueResult();
	}
	
	public List<Tarefa> find(Tarefa tarefa){
		Criteria criteria = criarCriteriaBasica(tarefa);
		
		if(tarefa.getDataAgendada() != null){
			Calendar inicio = Calendar.getInstance();
			inicio.setTime(tarefa.getDataAgendada());
			inicio.set(Calendar.HOUR_OF_DAY, 0);
			inicio.set(Calendar.MINUTE, 0);
			inicio.set(Calendar.SECOND, 0);
			inicio.set(Calendar.MILLISECOND, 0);

			Calendar fim = Calendar.getInstance();
			fim.setTime(tarefa.getDataAgendada());
			fim.set(Calendar.HOUR_OF_DAY, 23);
			fim.set(Calendar.MINUTE, 59);
			fim.set(Calendar.SECOND, 59);
			fim.set(Calendar.MILLISECOND, 999);
			criteria.add(Restrictions.between("dataAgendada", inicio.getTime(), fim.getTime()));
		}

		criteria.addOrder(Order.desc("dataCriacao"));
		List<Tarefa> tarefas = criteria.list();
		return tarefas;
	}
	
	private Criteria criarCriteriaBasica(Tarefa tarefa){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Tarefa.class);
		
		if(tarefa.getId() != null){
			criteria.add(Restrictions.eq("id", tarefa.getId()));
		}
		
		if(tarefa.getFuncionario() != null){
			criteria.add(
						Restrictions.or(
								Restrictions.isNull("funcionario"), 
								Restrictions.eqOrIsNull("funcionario", tarefa.getFuncionario()),
								Restrictions.eq("criador", tarefa.getFuncionario()))
					);
		}
		if(tarefa.getCliente() != null){
			criteria.add(Restrictions.eq("cliente", tarefa.getCliente()));
		}
		if(tarefa.getDataCriacao() != null){
			criteria.add(Restrictions.ge("dataCriacao", tarefa.getDataCriacao()));
		}

		return criteria;
	}
	
	public List<Tarefa> listarUltimas(Pessoa pessoa, int maxResult){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Tarefa.class);

		if(pessoa != null){
			criteria.add(
								Restrictions.eqOrIsNull("funcionario", pessoa)
					);
		}
		criteria.addOrder(Order.desc("dataCriacao"));
		criteria.setMaxResults(maxResult);
		return criteria.list();
	}

	public List<Tarefa> pesquisar(String pesquisa, Pessoa pessoa) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Tarefa.class);

		if(pessoa != null){
			criteria.add(
						Restrictions.or(
								Restrictions.isNull("funcionario"), 
								Restrictions.eqOrIsNull("funcionario", pessoa),
								Restrictions.eq("criador", pessoa))
					);
		}
		
		List<Criterion> orPredicates = new ArrayList<>();
		orPredicates.add(Restrictions.ilike("descricao", pesquisa, MatchMode.ANYWHERE));
		criteria.createAlias("criador", "cri", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("funcionario", "fun", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cliente", "cli", JoinType.LEFT_OUTER_JOIN);
		orPredicates.add(Restrictions.ilike("cri.nome", pesquisa, MatchMode.ANYWHERE));
		orPredicates.add(Restrictions.ilike("fun.nome", pesquisa, MatchMode.ANYWHERE));
		orPredicates.add(Restrictions.ilike("cli.nome", pesquisa, MatchMode.ANYWHERE));
		
		Criterion[] or = new Criterion[orPredicates.size()];
		or = orPredicates.toArray(or);
		criteria.add(Restrictions.or(or));

		criteria.addOrder(Order.desc("dataCriacao"));
		List<Tarefa> result = criteria.list();
		return result;
	}

	public Integer contarNaoLidas(Pessoa pessoa) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Tarefa.class);

		if(pessoa != null){
			criteria.add(Restrictions.eqOrIsNull("funcionario", pessoa));
		}
		criteria.add(
				Restrictions.or(
						Restrictions.isNull("concluida"),
						Restrictions.eqOrIsNull("concluida", false)
					)
			);
		Integer count = ((Number) (criteria.setProjection(Projections.rowCount()).uniqueResult())).intValue();
		return count;
	}
	
}
