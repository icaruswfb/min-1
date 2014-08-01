package br.com.min.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Tarefa;
import br.com.min.entity.Usuario;
import br.com.min.service.TarefaService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

	@Autowired
	private TarefaService tarefaService;
	
	@RequestMapping("/")
	public ModelAndView listarTarefas(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("tarefas");
		return mv;
	}
	
	@RequestMapping("/listarTodas")
	public @ResponseBody List<Tarefa> listarTodas(HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		List<Tarefa> tarefas = tarefaService.listarTarefasDoFuncionario(logado.getPessoa().getId());
		limparTarefasJSON(tarefas);
		return tarefas;
	}

	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public @ResponseBody Tarefa salvar(@RequestBody() Tarefa tarefa, HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		tarefa.setCriador(logado.getPessoa());
		tarefaService.persist(tarefa);
		limparTarefaJSON(tarefa);		
		return tarefa;
	}

	private void limparTarefasJSON(List<Tarefa> tarefas){
		for(Tarefa tarefa : tarefas){
			limparTarefaJSON(tarefa);
		}
	}
	
	private void limparTarefaJSON(Tarefa tarefa){
		if( tarefa.getFuncionario() != null ){
			tarefa.getFuncionario().setUsuario(null);
		}
		if( tarefa.getCriador() != null ){
			tarefa.getCriador().setUsuario(null);
		}
	}
	
}
