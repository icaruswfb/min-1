package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;
import br.com.min.service.PessoaService;

@Controller("")
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private PessoaService pessoaService;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(pessoaService.listarClientes(), null);
	}
	
	private ModelAndView list(List<Pessoa> lista, String pesquisa){
		ModelAndView mv = new ModelAndView("clientes");
		mv.addObject("clientes", lista);
		mv.addObject("pesquisa", pesquisa);
		return mv;
	}
	
	@RequestMapping(value="/novo/p", method=RequestMethod.GET)
	public ModelAndView novo(){
		return editar(null);
	}
	
	@RequestMapping(value="/editar/{id}", method=RequestMethod.GET)
	public ModelAndView editar(@PathVariable("id") Long id){
		ModelAndView mv = new ModelAndView("cliente");
		
		Pessoa cliente;
		if(id == null){
			cliente = new Pessoa();
		}else{
			cliente = pessoaService.findById(id);
		}
		mv.addObject("cliente", cliente);
		
		return mv;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("cliente") Pessoa cliente){
		cliente.setFuncionario(false);
		pessoaService.persist(cliente);
		return listar();
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id){
		pessoaService.delete(id);
		return listar();
	}
	
}
