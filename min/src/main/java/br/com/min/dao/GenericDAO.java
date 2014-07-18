package br.com.min.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GenericDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Object persist(Object entity){
		Session session = sessionFactory.openSession();
		entity = session.merge(entity);
		session.flush();
		return entity;
	}

	public void delete(Object entity) {
		Session session = sessionFactory.openSession();
		session.delete(entity);
		session.flush();
	}
	
}
