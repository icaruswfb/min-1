package br.com.min.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDAO {

	private Session session;
	
	@Autowired
	private SessionFactory sessionFactory;

    public Session getSession() {
        if (session != null && session.isOpen()) {
        	session.close();
        }
        session = sessionFactory.openSession();
        return session;
    }
    
}
