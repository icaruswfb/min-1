package br.com.min.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.min.entity.Usuario;

public class SecurityFilter implements Filter{

	public static final String LOGGED_USER = "loggedUser";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Usuario usuario = (Usuario) req.getSession().getAttribute(LOGGED_USER);
		String uri = req.getRequestURI();
		if(usuario == null){
			if(uri.equals("/min/web/login")){
				chain.doFilter(request, response);
			}else{
				resp.sendRedirect("/min/web/login");
			}
		}else{
			if(uri.equals("/min/web/logout")){
				req.getSession().invalidate();
				resp.sendRedirect("/min/web/login");
			}else{
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
		
	}

}
