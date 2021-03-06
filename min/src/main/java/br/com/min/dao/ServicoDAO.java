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

import br.com.min.entity.Servico;
import br.com.min.entity.TipoServico;

@Repository
public class ServicoDAO extends BaseDAO<Servico> {

	public List<Servico> findServico(Servico servico){
		Session session = getSession();
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
		criteria.addOrder(Order.asc("nome"));
		
		List<Servico> servicos = criteria.list();
		return servicos;
	}
	
	public List<TipoServico> listarTiposServicos(){
		Session session = getSession();
		Criteria criteria = session.createCriteria(TipoServico.class);
		
		List<TipoServico> tiposServicos = criteria.list();
		return tiposServicos;
	}

	public TipoServico findTipoServicoById(Long id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(TipoServico.class);
		criteria.add(Restrictions.eq("id", id));
		
		TipoServico tipoServico = (TipoServico) criteria.uniqueResult();
		return tipoServico;
	}
	
}
