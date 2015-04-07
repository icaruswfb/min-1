package br.com.min.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;
import br.com.min.entity.TipoLancamentoEstoque;

@Repository
public class ProdutoDAO extends BaseDAO<Produto> {

	public List<Produto> find(Produto entity){
		Session session = getSession();
		Criteria criteria = session.createCriteria(Produto.class);
		if(entity != null){
			List<Criterion> predicates = new ArrayList<>();
			if(entity.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", entity.getId()));
			}
			if(StringUtils.isNotBlank(entity.getNome())){
				predicates.add(Restrictions.ilike("nome", entity.getNome(), MatchMode.ANYWHERE));
			}
			if(entity.getSituacaoEstoque() != null){
				criteria.add(Restrictions.eq("situacaoEstoque", entity.getSituacaoEstoque()));
			}
			if(entity.getCategoria() != null){
				criteria.add(Restrictions.eq("categoria", entity.getCategoria()));
			}
			Criterion[] predicatesArray = new Criterion[predicates.size()];
			predicatesArray = predicates.toArray(predicatesArray);
			criteria = criteria.add(Restrictions.or(predicatesArray));
		}
		criteria.addOrder(Order.asc("nome"));
		List<Produto> entities = criteria.list();
		return entities;
	}

	public List<LancamentoEstoque> findLancamentoEstoquePorProduto(Long produtoId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(LancamentoEstoque.class);
		criteria = criteria.add(Restrictions.eq("produto.id", produtoId));
		criteria.addOrder(Order.desc("dataCriacao"));
		List<LancamentoEstoque> lancamentos = criteria.list();
		return lancamentos;
	}
	
	public void delete(Produto produto){
		Session session = getSession();
		Query query = session.createQuery("delete from LancamentoEstoque l where l.produto.id = " + produto.getId());
		query.executeUpdate();
		session.delete(produto);
		session.flush();
	}

	public List<LancamentoEstoque> findLancamentoByTipo(
			TipoLancamentoEstoque entrada) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(LancamentoEstoque.class);
		criteria.add(Restrictions.eq("tipo", entrada));
		List<LancamentoEstoque> result = criteria.list();
		
		return result;
	}

}
