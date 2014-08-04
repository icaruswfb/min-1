package br.com.min.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Pessoa;
import br.com.min.entity.Tarefa;
import br.com.min.entity.Usuario;
import br.com.min.service.PessoaService;
import br.com.min.service.TarefaService;
import br.com.min.utils.Utils;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

	@Autowired
	private TarefaService tarefaService;
	@Autowired
	private PessoaService pessoaService;
	
	@RequestMapping("/")
	public ModelAndView listarTarefas(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("tarefas");
		mv.addObject("tarefa", new Tarefa());
		List<Pessoa> funcionarios = pessoaService.listarFuncionarios();
		List<Pessoa> clientes = pessoaService.listarClientes();
		mv.addObject("funcionarios", funcionarios);
		mv.addObject("clientes", clientes);
		return mv;
	}
	
	@RequestMapping("/listarUltimas")
	public @ResponseBody List<Tarefa> listarUltimas(HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		List<Tarefa> tarefas = tarefaService.listarUltimas(logado.getPessoa(), 5);
		limparTarefasJSON(tarefas);
		return tarefas;
	}
	
	@RequestMapping("/contarNaoLidas")
	public @ResponseBody Integer contarNaoLidas(HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		return tarefaService.contarNaoLidas(logado.getPessoa());
	}
	
	@RequestMapping("/listarTodas")
	public @ResponseBody List<Tarefa> listarTodas(HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		List<Tarefa> tarefas = tarefaService.listarTarefasDoFuncionario(logado.getPessoa().getId());
		limparTarefasJSON(tarefas);
		return tarefas;
	}
	
	@RequestMapping(value="/fecharTarefa/{id}", method=RequestMethod.GET)
	public @ResponseBody Boolean fecharTarefa(@PathVariable("id") Long id, HttpServletRequest request){
		Tarefa tarefa = tarefaService.findById(id);
		if(tarefa != null){
			Usuario logado = Utils.getUsuarioLogado(request);
			if( logado.getPessoa().equals( tarefa.getFuncionario() )){
				tarefa.setConcluida(true);
				tarefaService.persist(tarefa);
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public @ResponseBody String delete(@PathVariable("id") Long id, HttpServletRequest request){
		Tarefa tarefa = tarefaService.findById(id);
		if(tarefa != null){
			Usuario logado = Utils.getUsuarioLogado(request);
			if(tarefa.getCriador().equals(logado.getPessoa())){
				tarefaService.delete(id);
			}
		}
		return "OK";
	}
	
	@RequestMapping(value="/pesquisar", method=RequestMethod.POST)
	public @ResponseBody List<Tarefa> pesquisar(String pesquisa, HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		List<Tarefa> tarefas = tarefaService.pesquisar(pesquisa, logado.getPessoa()); 
		limparTarefasJSON(tarefas);
		return tarefas;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public @ResponseBody Tarefa salvar(Long id, Long funcionario, Long cliente,
										String dataAgendadaStr,
										String descricao,
										HttpServletRequest request){
		Tarefa tarefa = tarefaService.findById(id);
		if(tarefa == null){
			tarefa = new Tarefa();
		}
		tarefa.setDescricao(descricao);
		try {
			if(StringUtils.isNotBlank(dataAgendadaStr)){
				tarefa.setDataAgendada( Utils.dateFormat.parse(dataAgendadaStr) );
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Usuario logado = Utils.getUsuarioLogado(request);
		tarefa.setCriador(logado.getPessoa());
		
		if(funcionario != null){
			tarefa.setFuncionario(pessoaService.findById(funcionario));
		}
		if(cliente != null){
			tarefa.setCliente(pessoaService.findById(cliente));
		}
		
		tarefa = tarefaService.persist(tarefa);
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
