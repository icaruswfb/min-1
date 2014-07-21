package br.com.min.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Horario;

@Repository
public class HorarioDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Horario> findHorario(Horario horario){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Horario.class);
		if(horario != null){
			List<Criterion> orPredicates = new ArrayList<>();
			List<Criterion> andPredicates = new ArrayList<>();
			if(horario.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", horario.getId()));
			}
			if(horario.getInicio() != null && horario.getTermino() != null){
				orPredicates.add(Restrictions.between("inicio", horario.getInicio(), horario.getTermino()));
				orPredicates.add(Restrictions.between("termino", horario.getInicio(), horario.getTermino()));
			}
			if(horario.getFuncionario() != null ){
				andPredicates.add(Restrictions.eq("funcionario", horario.getFuncionario()));
			}
			if(horario.getCliente() != null){
				andPredicates.add(Restrictions.eq("cliente.id", horario.getCliente().getId()));
			}
			Criterion[] orPredicatesArray = new Criterion[orPredicates.size()];
			orPredicatesArray = orPredicates.toArray(orPredicatesArray);
			Criterion[] andPredicatesArray = new Criterion[andPredicates.size()];
			andPredicatesArray = orPredicates.toArray(andPredicatesArray);
			criteria.addOrder(Order.asc("inicio"));
			criteria = criteria.add(Restrictions.and(andPredicatesArray));
		}
		List<Horario> clientes = criteria.list();
		return clientes;
	}

}
