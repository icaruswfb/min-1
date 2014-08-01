package br.com.min.dao;

import java.util.ArrayList;
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

import br.com.min.entity.Pessoa;

@Repository
public class PessoaDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Pessoa> findPessoa(Pessoa entity){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Pessoa.class);
		if(entity != null){
			List<Criterion> orPredicates = new ArrayList<>();
			List<Criterion> andPredicates = new ArrayList<>();
			if(entity.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", entity.getId()));
			}
			if(StringUtils.isNotBlank(entity.getNome())){
				orPredicates.add(Restrictions.ilike("nome", entity.getNome(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getEmail())){
				orPredicates.add(Restrictions.ilike("email", entity.getEmail(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getDocumento())){
				orPredicates.add(Restrictions.ilike("documento", entity.getDocumento(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getEndereco())){
				orPredicates.add(Restrictions.ilike("endereco", entity.getEndereco(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getCidade())){
				orPredicates.add(Restrictions.ilike("cidade", entity.getCidade(), MatchMode.ANYWHERE));
			}
			if(entity.getFuncaoPrincipal() != null){
				andPredicates.add(Restrictions.eq("funcaoPrincipal", entity.getFuncaoPrincipal()));
			}
			if(entity.getFuncionario() != null){
				andPredicates.add(Restrictions.eq("funcionario", entity.getFuncionario()));
			}
			Criterion[] orPredicatesArray = new Criterion[orPredicates.size()];
			orPredicatesArray = orPredicates.toArray(orPredicatesArray);
			Criterion[] andPredicatesArray = new Criterion[andPredicates.size()];
			andPredicatesArray = andPredicates.toArray(andPredicatesArray);
			criteria = criteria.add(Restrictions.or(orPredicatesArray));
			criteria = criteria.add(Restrictions.and(andPredicatesArray));
			criteria.addOrder(Order.asc("nome"));
		}
		List<Pessoa> clientes = criteria.list();
		return clientes;
	}

}
