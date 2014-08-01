package br.com.min.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

		if(tarefa.getFuncionario() != null){
			criteria.add(
						Restrictions.or(
								Restrictions.isNull("funcionario"), 
								Restrictions.eqOrIsNull("funcionario", tarefa.getFuncionario()))
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
	
}
