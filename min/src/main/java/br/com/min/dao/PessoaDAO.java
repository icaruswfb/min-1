package br.com.min.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import br.com.min.entity.Funcao;
import br.com.min.entity.Pessoa;

@Repository
public class PessoaDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Pessoa> search(List<String> words){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Pessoa.class);

		criteria = criteria.add(Restrictions.isNull("deleted"));

		List<Criterion> orPredicates = new ArrayList<>();
		
		for(String word : words){
			orPredicates.add(Restrictions.ilike("nome", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("email", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("telefone", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("documento", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("endereco", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("cidade", word, MatchMode.ANYWHERE));
			orPredicates.add(Restrictions.ilike("observacao", word, MatchMode.ANYWHERE));
		}

		Criterion[] orPredicatesArray = new Criterion[orPredicates.size()];
		orPredicatesArray = orPredicates.toArray(orPredicatesArray);

		criteria = criteria.add(Restrictions.or(orPredicatesArray));
		
		criteria.addOrder(Order.asc("nome"));
		List<Pessoa> pessoas = criteria.list();
		
		return pessoas;
	}
	
	public List<Pessoa> findPessoa(Pessoa entity, Funcao... funcoesExcluidas){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Pessoa.class);
		if(entity != null){
			List<Criterion> orPredicates = new ArrayList<>();
			List<Criterion> andPredicates = new ArrayList<>();
			if(entity.getId() != null){
				criteria = criteria.add(Restrictions.eq("id", entity.getId()));
			}else{
				criteria = criteria.add(Restrictions.isNull("deleted"));
			}
			if(funcoesExcluidas != null && funcoesExcluidas.length > 0){
				criteria = criteria.add( Restrictions.not(Restrictions.in("funcaoPrincipal", funcoesExcluidas)) );
			}
			if(StringUtils.isNotBlank(entity.getNome())){
				orPredicates.add(Restrictions.ilike("nome", entity.getNome(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getEmail())){
				orPredicates.add(Restrictions.ilike("email", entity.getEmail(), MatchMode.ANYWHERE));
			}
			if(StringUtils.isNotBlank(entity.getTelefone())){
				orPredicates.add(Restrictions.ilike("telefone", entity.getTelefone(), MatchMode.ANYWHERE));
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
			if(StringUtils.isNotBlank(entity.getObservacao())){
				orPredicates.add(Restrictions.ilike("observacao", entity.getObservacao(), MatchMode.ANYWHERE));
			}
			if(entity.getFuncaoPrincipal() != null){
				andPredicates.add(Restrictions.eq("funcaoPrincipal", entity.getFuncaoPrincipal()));
			}
			if(entity.getFuncionario() != null){
				andPredicates.add(Restrictions.eq("funcionario", entity.getFuncionario()));
			}
			if(entity.getAniversario() != null){
				Calendar fim = Calendar.getInstance();
				fim.setTime(entity.getAniversario());
				fim.set(Calendar.HOUR_OF_DAY, 23);
				fim.set(Calendar.MINUTE, 59);
				fim.set(Calendar.SECOND, 59);
				fim.set(Calendar.MILLISECOND, 999);
				fim.set(Calendar.YEAR, 1900);
				Calendar inicio = Calendar.getInstance();
				inicio.setTime(entity.getAniversario());
				inicio.set(Calendar.HOUR_OF_DAY, 0);
				inicio.set(Calendar.MINUTE, 0);
				inicio.set(Calendar.SECOND, 0);
				inicio.set(Calendar.MILLISECOND, 0);
				inicio.set(Calendar.YEAR, 1900);
				
				andPredicates.add(Restrictions.between("aniversario", inicio.getTime(), fim.getTime()));
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
	
	public void delete(Pessoa pessoa){
		Session session = sessionFactory.openSession();
		pessoa = findPessoa(pessoa).get(0);
		pessoa.setDeleted(new Date());
		session.merge(pessoa);
		session.flush();
		session.close();
	}
	
}
