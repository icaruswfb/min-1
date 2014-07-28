package br.com.min.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.UsuarioDAO;
import br.com.min.entity.Usuario;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioDAO dao;
	
	public Usuario autenticar(String login, String senha){
		return dao.autenticar(login, senha);
	}
	
}
