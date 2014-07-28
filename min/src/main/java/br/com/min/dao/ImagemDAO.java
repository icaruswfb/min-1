package br.com.min.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Imagem;

@Repository
public class ImagemDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Imagem findById(Long id){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Imagem.class);
		criteria.add(Restrictions.eq("id", id));
		return (Imagem) criteria.uniqueResult();
	}
	
	public void deletarNaoUtilizados(){
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("delete from imagem where id not in(select imagem_id from pessoa where imagem_id is not null);");
		query.executeUpdate();
	}
	
}
