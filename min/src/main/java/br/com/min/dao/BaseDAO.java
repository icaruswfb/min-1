package br.com.min.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.entity.BaseEntity;
import br.com.min.utils.CommonsReflection;

public class BaseDAO<T extends BaseEntity> {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(propagation = Propagation.REQUIRED)
	public T persist(T entity) {
		Session session = getSession();
		entity = (T) session.merge(entity);
		session.flush();
		return entity;
	}

	public List<T> findAll(Class<T> type) {
		Criteria criteria = getSession().createCriteria(type);
		List<T> result = criteria.list();
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T findById(Serializable id) {
		Class<?> clazz = CommonsReflection.getParameterizedClass(this
				.getClass());
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("id", id));
		return (T) criteria.uniqueResult();
	}

	@Transactional
	public T refresh(T entity) {
		return findById(entity.getId());
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void delete(T entity) {
		Session session = getSession();
		session.delete(entity);
		session.flush();
	}
	
	@Transactional
	public void evict(T entity){
		getSession().evict(entity);
	}

}
