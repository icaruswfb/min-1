package br.com.min.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("")
@RequestMapping("/agenda")
public class AgendaController {

	@RequestMapping("/")
	public String index(){
		return "agenda";
	}
	
}
