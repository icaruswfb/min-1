package br.com.min.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Pacote;

@Repository
public class PacoteDAO extends BaseDAO<Pacote>{

	public List<Pacote> findByClienteId(Long clienteId){
		Session session = getSession();
		Criteria criteria = session.createCriteria(Pacote.class);
		criteria.add(Restrictions.gt("quantidade", 0L));
		criteria.add(Restrictions.eq("cliente.id", clienteId));
		
		criteria.addOrder(Order.asc("dataCriacao"));
		
		return criteria.list();
	}
	
	public Long countQuantidade(Long pacoteId){
		String query = "select count(*) from lancamentoServico where pacote_id = " + pacoteId;
		SQLQuery sqlQuery = getSession().createSQLQuery(query);
		Number result = (Number)sqlQuery.list().get(0);
		return result.longValue();
	}
	
}
