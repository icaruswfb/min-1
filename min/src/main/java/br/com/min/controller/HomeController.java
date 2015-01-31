package br.com.min.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.Usuario;
import br.com.min.filter.SecurityFilter;
import br.com.min.service.PessoaService;
import br.com.min.service.UsuarioService;

@Controller()
public class HomeController {

	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value="/")
	public String index(){
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login( HttpServletRequest request ){
		String login = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		if( login == null ){
			return "login";
		}else{
			Usuario user = usuarioService.autenticar(login, password);
			if(user == null){
				request.setAttribute("error", "Usuário e senha inválidos");
				return "login";
			}
			request.getSession().setAttribute(SecurityFilter.LOGGED_USER, user);
			Map<String, Boolean> roles = new HashMap<>();
			boolean hasAdminRole = user.getRole().equals(Role.ADMIN);
			for(Role role : Role.values()){
				roles.put(
						role.name(), 
						hasAdminRole ? true : user.getRole().equals(role));
			}
			request.getSession().setAttribute(SecurityFilter.HAS_ROLE, roles);
			return "index";
		}
	}
	
	private ModelAndView list(List<Pessoa> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("resultadoPesquisa");
		mv.addObject("pessoas", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/pesquisar", method=RequestMethod.POST)
	public ModelAndView pesquisar(String pesquisa){
		return list(pessoaService.search(pesquisa), pesquisa);
	}
	
	
	
	@RequestMapping(value="/pesquisarest", method=RequestMethod.POST)
	public @ResponseBody() List<Pessoa> pesquisar(String pesquisa, Boolean funcionario){
		Pessoa pessoa = new Pessoa();
		pessoa.setCep(pesquisa);
		pessoa.setCidade(pesquisa);
		pessoa.setEmail(pesquisa);
		pessoa.setEndereco(pesquisa);
		pessoa.setNome(pesquisa);
		pessoa.setTelefone(pesquisa);
		pessoa.setFuncionario(funcionario);
		pessoa.setDocumento(pesquisa);
		pessoa.setObservacao(pesquisa);
		List<Pessoa> pessoas = pessoaService.findPessoa(pessoa);
		return pessoas;
	}
	
}
