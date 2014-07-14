package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;
import br.com.min.service.PessoaService;

@Controller("")
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private PessoaService pessoaService;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(pessoaService.listarFuncionarios(), null);
	}
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public @ResponseBody List<Pessoa> listarFuncionarios(){
		return pessoaService.listarFuncionarios();
	}
	
	private ModelAndView list(List<Pessoa> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("funcionarios");
		mv.addObject("funcionarios", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(){
		return editar(null);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("funcionario");
		
		Pessoa funcionario;
		if(id == null){
			funcionario = new Pessoa();
		}else{
			funcionario = pessoaService.findById(id);
		}
		mv.addObject("funcionario", funcionario);
		
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("funcionario") Pessoa funcionario){
		funcionario.setFuncionario(true);
		pessoaService.persist(funcionario);
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id){
		pessoaService.delete(id);
		return listar();
	}
	
}
