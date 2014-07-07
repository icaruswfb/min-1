package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;
import br.com.min.service.PessoaService;

@Controller()
public class HomeController {

	@Autowired
	private PessoaService pessoaService;
	
	@RequestMapping(value="/")
	public String index(){
		return "index";
	}
	
	private ModelAndView list(List<Pessoa> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("resultadoPesquisa");
		mv.addObject("pessoas", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/pesquisar", method=RequestMethod.POST)
	public ModelAndView pesquisar(String pesquisa){
		return list(pesquisar(pesquisa, null), pesquisa);
	}
	
	@RequestMapping(value="/pesquisarest", method=RequestMethod.POST)
	public @ResponseBody List<Pessoa> pesquisar(@PathVariable("pesquisa") String pesquisa, @PathVariable("isFuncionario") Boolean funcionario){
		Pessoa pessoa = new Pessoa();
		pessoa.setCep(pesquisa);
		pessoa.setCidade(pesquisa);
		pessoa.setEmail(pesquisa);
		pessoa.setEndereco(pesquisa);
		pessoa.setNome(pesquisa);
		pessoa.setTelefone(pesquisa);
		pessoa.setFuncionario(funcionario);
		List<Pessoa> pessoas = pessoaService.findPessoa(pessoa);
		return pessoas;
	}
	
}
