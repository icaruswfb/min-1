package br.com.min.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.min.entity.Role;
import br.com.min.entity.Usuario;
import br.com.min.utils.Utils;

@Repository
public class UsuarioDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Usuario persist(Usuario usuario, boolean encryptPassword){
		Session session = sessionFactory.openSession();
		if(encryptPassword){
			String encryptedPassword = Utils.encriyt(usuario.getSenha());
			usuario.setSenha(encryptedPassword);
		}
		usuario = (Usuario) session.merge(usuario);
		session.flush();
		return usuario;
	}
	
	public Usuario autenticar(String login, String senha){
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("login", login));
		Usuario usuario = (Usuario) criteria.uniqueResult();
		if(usuario != null){
			if( ! Utils.isTokenValid(usuario.getSenha(), senha) ){
				usuario = null;
			}
		}
		session.flush();
		return usuario;
	}

	public Usuario findById(Long id) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("id", id));
		Usuario usuario = (Usuario) criteria.uniqueResult();
		return usuario;
	}

	public Usuario findByLogin(String login) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("login", login));
		Usuario usuario = (Usuario) criteria.uniqueResult();
		return usuario;
	}

	public List<Usuario> findByRole(Role role) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("role", role));
		return criteria.list();
	}
	
}
