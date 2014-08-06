package br.com.min.utils;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.itau.internet.seguranca.jarvis.sectoken.JarvisToken;
import br.com.min.entity.Role;
import br.com.min.entity.Usuario;
import br.com.min.filter.SecurityFilter;

public class Utils {

	public static final SimpleDateFormat dateFormat  = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");
	
	public static String encriyt(String toEncrypt){
		JarvisToken token = new JarvisToken();
		String encryptedPassword;
		try {
			encryptedPassword = token.getToken("", toEncrypt, "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return encryptedPassword;
	}
	
	public static void main(String[] args) {
		System.out.println(encriyt("admin"));
	}
	
	public static boolean isTokenValid(String encrypted, String compare){
		JarvisToken token = new JarvisToken();
		try {
			return token.isTokenValid("", compare, "", encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
			
	}
	
	public static boolean hasRole(Role role, HttpServletRequest request){
		Map<String, Boolean> hasRole = (Map<String, Boolean>) request.getSession().getAttribute(SecurityFilter.HAS_ROLE);
		if(hasRole.get(Role.ADMIN.name())){
			return true;
		}
		return hasRole.get(role.name());
	}

	public static Usuario getUsuarioLogado(HttpServletRequest request){
		Usuario logado = (Usuario) request.getSession().getAttribute(SecurityFilter.LOGGED_USER);
		return logado;
	}
	
}
