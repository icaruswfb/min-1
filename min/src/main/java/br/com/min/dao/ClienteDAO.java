package br.com.min.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Cliente;

@Repository
public class ClienteDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void persist(Cliente cliente){
		Session session = sessionFactory.openSession();
		session.merge(cliente);
		session.flush();
	}
	
	public List<Cliente> findCliente(Cliente cliente){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Cliente.class);
		if(cliente != null){
			List<Criterion> predicates = new ArrayList<>();
			if(cliente.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", cliente.getId()));
			}
			if(StringUtils.isNotBlank(cliente.getNome())){
				predicates.add(Restrictions.ilike("nome", cliente.getNome(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(cliente.getEmail())){
				predicates.add(Restrictions.ilike("email", cliente.getEmail(), MatchMode.ANYWHERE));
			}
			Criterion[] predicatesArray = new Criterion[predicates.size()];
			predicatesArray = predicates.toArray(predicatesArray);
			criteria = criteria.add(Restrictions.or(predicatesArray));
		}
		List<Cliente> clientes = criteria.list();
		return clientes;
	}

	public void delete(Cliente cliente) {
		Session session = sessionFactory.openSession();
		session.delete(cliente);
		session.flush();
	}
	
}
