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

import br.com.min.entity.Servico;

@Repository
public class ServicoDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Servico> findServico(Servico servico){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Servico.class);
		if(servico != null){
			List<Criterion> predicates = new ArrayList<>();
			if(servico.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", servico.getId()));
			}
			if(StringUtils.isNotBlank(servico.getNome())){
				predicates.add(Restrictions.ilike("nome", servico.getNome(), MatchMode.ANYWHERE));
			}
			Criterion[] predicatesArray = new Criterion[predicates.size()];
			predicatesArray = predicates.toArray(predicatesArray);
			criteria = criteria.add(Restrictions.or(predicatesArray));
		}
		List<Servico> servicos = criteria.list();
		return servicos;
	}
	
}
