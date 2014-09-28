package br.com.min.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Kit;

@Repository
public class KitDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Kit> find(Kit kit){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Kit.class);
		if(kit != null){
			if(kit.getId() != null){
				criteria.add(Restrictions.eq("id", kit.getId()));
			}
		}
		criteria.addOrder(Order.asc("nome"));
		List<Kit> kits = criteria.list();
		
		return kits;
	}
	
}
