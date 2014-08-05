package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.UsuarioDAO;
import br.com.min.entity.Role;
import br.com.min.entity.Usuario;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioDAO dao;
	
	public Usuario autenticar(String login, String senha){
		return dao.autenticar(login, senha);
	}
	public Usuario findById(Long id){
		return dao.findById(id);
	}
	public List<Usuario> findByRole(Role role){
		return dao.findByRole(role);
	}
	
	public Usuario findById(String login){
		return dao.findByLogin(login);
	}
	
	public Usuario persist(Usuario usuario){
		return dao.persist(usuario, true);
	}
	
	public Usuario persist(Usuario usuario, boolean encryptPassword){
		return dao.persist(usuario, encryptPassword);
	}
	
}
