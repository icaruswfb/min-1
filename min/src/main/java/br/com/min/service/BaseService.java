package br.com.min.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.BaseDAO;
import br.com.min.entity.BaseEntity;
import br.com.min.utils.CommonsReflection;

@Transactional()
public class BaseService<T extends BaseEntity, DAO extends BaseDAO<T>> {

	@Autowired
	private DAO dao;

	public DAO getDAO() {
		return dao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public T save(T entity) {
		if (!preValidate(entity)) {
			throw new IllegalArgumentException(
					"Was not possible to validate entity :::: " + entity);
		}

		return (T) getDAO().persist(entity);
	}

	public List<T> listAll() {
		Class<T> clazz = (Class<T>) CommonsReflection
				.getParameterizedClass(this.getClass());
		return getDAO().findAll(clazz);
	}

	/**
	 *
	 * @param Id
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T findById(Serializable id) {
		T result = getDAO().findById(id);
		System.out.println(result);
		return result;
	}

	@Transactional
	public T refresh(T entity) {
		return getDAO().refresh(entity);
	}

	/**
	 * Este metodo sempre será chamado antes de persistir algum registro no
	 * banco de dados.
	 * 
	 * @param entity
	 * @return boolean
	 */
	public boolean preValidate(T entity) {
		return true;
	}
	
	public List<T> cleanResult(List<T> result){
		if(result != null){
			result.toString();
		}
		return result;
	}

	public T cleanResult(T result){
		if(result != null){
			result.toString();
		}
		return result;
	}
	
}
