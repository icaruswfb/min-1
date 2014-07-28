package br.com.min.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.min.entity.Usuario;
import br.com.min.service.UsuarioService;

@Component(value="logonProvider")
public class LogonProvider implements AuthenticationProvider{

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		String login = auth.getName();
		String password = (String) auth.getCredentials();
		
		Usuario user = usuarioService.autenticar(login, password);
		
		if(user == null){
			throw new BadCredentialsException("Usuário e senha não encontrados");
		}
		
		Authentication result = new UsernamePasswordAuthenticationToken(login, password, createAuthorities(user));
		
		return result;
	}

	private Collection<GrantedAuthority> createAuthorities(final Usuario usuario){
		List<GrantedAuthority> result = new ArrayList<>();
		GrantedAuthority usuarioRole = new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return usuario.getRole().name();
			}
		};
		result.add(usuarioRole);
		GrantedAuthority logadoRole = new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "LOGADO";
			}
		};
		result.add(logadoRole);
		return result;
	}
	
	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
