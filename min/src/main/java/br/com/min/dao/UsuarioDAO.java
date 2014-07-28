package br.com.min.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.itau.internet.seguranca.jarvis.sectoken.JarvisToken;
import br.com.min.entity.Usuario;

@Repository
public class UsuarioDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void persist(Usuario usuario){
		Session session = sessionFactory.openSession();
		JarvisToken token = new JarvisToken();
		String encryptedPassword;
		try {
			encryptedPassword = token.getToken("", usuario.getSenha(), "");
			usuario.setSenha(encryptedPassword);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		session.merge(usuario);
		session.flush();
	}
	
	public Usuario autenticar(String login, String senha){
		JarvisToken token = new JarvisToken();
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("login", login));
		
		Usuario usuario = (Usuario) criteria.uniqueResult();
		try {
			if( ! token.isTokenValid("", senha, "", usuario.getSenha())){
				usuario = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		session.flush();
		return usuario;
	}
	
}
