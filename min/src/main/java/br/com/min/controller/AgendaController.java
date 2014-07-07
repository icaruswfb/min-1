package br.com.min.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;

@Controller("")
@RequestMapping("/agenda")
public class AgendaController {

	@Autowired
	private HomeController homeController;
	
	@RequestMapping("/")
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView("agenda");
		List<Pessoa> pessoas = homeController.pesquisar(null, null);
		mv.addObject("pessoas", pessoas);
		return mv;
	}
	
}
