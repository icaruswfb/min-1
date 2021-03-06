package br.com.min.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Historico;

@Repository
public class HistoricoDAO extends BaseDAO<Historico>{

	public List<Historico> findHistorico(Historico historico){
		Session session = getSession();
		Criteria criteria = session.createCriteria(Historico.class);
		if(historico != null){
			List<Criterion> predicates = new ArrayList<>();
			if(historico.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", historico.getId()));
			}
			if(historico.getCliente() != null){
				predicates.add(Restrictions.eq("cliente", historico.getCliente()));
			}
			if(historico.getFuncionario() != null){
				predicates.add(Restrictions.eq("funcionario", historico.getFuncionario()));
			}
			Criterion[] predicatesArray = new Criterion[predicates.size()];
			predicatesArray = predicates.toArray(predicatesArray);
			criteria = criteria.add(Restrictions.or(predicatesArray));
			criteria = criteria.addOrder(Order.desc("data"));
		}
		List<Historico> servicos = criteria.list();
		return servicos;
	}
	
}
