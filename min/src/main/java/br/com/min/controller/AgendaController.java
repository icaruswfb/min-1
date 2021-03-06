package br.com.min.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.error.HorarioOcupadoException;
import br.com.min.entity.Horario;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.Servico;
import br.com.min.service.HorarioService;
import br.com.min.service.PessoaService;
import br.com.min.service.ServicoService;
import br.com.min.utils.Utils;

@Controller("")
@RequestMapping("/agenda")
public class AgendaController {

	@Autowired
	private FuncionarioController funcionarioController;
	@Autowired
	private ServicoService servicoService;
	@Autowired
	private HorarioService horarioService;
	@Autowired
	private PessoaService pessoaService;
	
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request){
		ModelAndView mv = criarViewAgenda(request);
		Date hoje = new Date();
		mv.addObject("dataMillis", hoje.getTime());
		mv.addObject("dataStr", Utils.dateFormat.format(hoje));
		return mv;
	}

	private ModelAndView criarViewAgenda(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("agenda");
		List<Pessoa> pessoas = pessoaService.listarClientes();
		pessoas.addAll(funcionarioController.listarFuncionariosParaAgenda(request));
		List<Servico> servicos = servicoService.listar();
		mv.addObject("pessoas", pessoas);
		mv.addObject("servicos", servicos);
		return mv;
	}
	
	@RequestMapping(value="/ver/{dia}/{mes}/{ano}", method=RequestMethod.GET)
	public ModelAndView exibirAgenda(
			@PathVariable("dia") Integer dia, 
			@PathVariable("mes")Integer mes, 
			@PathVariable("ano") Integer ano, HttpServletRequest request){
		ModelAndView mv = criarViewAgenda(request);
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes - 1);
		data.set(Calendar.YEAR, ano);
		mv.addObject("dataMillis", data.getTime().getTime());
		mv.addObject("dataStr", Utils.dateFormat.format(data.getTime()));
		return mv;
	}
	
	@RequestMapping(value="/{dia}/{mes}/{ano}/{funcionariosId}", method=RequestMethod.GET)
	public @ResponseBody List<Horario> findHorarios(
					@PathVariable("dia") Integer dia, 
					@PathVariable("mes")Integer mes, 
					@PathVariable("ano") Integer ano, 
					@PathVariable("funcionariosId") String funcionariosId,
					HttpServletRequest request){
		Horario horario  = criarHorarioPesquisa(dia, mes, ano, null, request);
		List<Horario> horarios = horarioService.findHorario(horario);
		return horarios;
	}
	
	@RequestMapping(value="cliente/{id}/{dia}/{mes}/{ano}", method=RequestMethod.GET)
	public @ResponseBody List<Horario> findHorariosCliente(
					@PathVariable("dia") Integer dia, 
					@PathVariable("mes")Integer mes, 
					@PathVariable("ano") Integer ano, 
					@PathVariable("id") Long clienteId,
					HttpServletRequest request){
		Horario horario  = criarHorarioPesquisa(dia, mes, ano, clienteId, request);
		List<Horario> horarios = horarioService.findHorario(horario);
		return horarios;
	}
	
	private Horario criarHorarioPesquisa(Integer dia, Integer mes, Integer ano, Long clienteId, HttpServletRequest request){
		Horario horario  = new Horario();
		if( ! Utils.hasRole(Role.ADMIN, request) && ! Utils.hasRole(Role.CAIXA, request) ){
			horario.setFuncionario(Utils.getUsuarioLogado(request).getPessoa());
		}
		StringBuffer dataInicioStr = new StringBuffer();
		StringBuffer dataTerminoStr = new StringBuffer();
		if(dia == null || dia == -1){
			if(mes == null || mes == -1){
				dataInicioStr.append("01/01/");
				dataTerminoStr.append("31/12/");
			}else{
				dataInicioStr.append("01/").append(mes).append("/");
				dataTerminoStr.append("31/").append(mes).append("/");
			}
		}else{
			dataInicioStr.append(dia).append("/").append(mes).append("/");
			dataTerminoStr.append(dia).append("/").append(mes).append("/");
		}
		if(ano == null || ano == -1){
			Calendar calendar = Calendar.getInstance();
			dataInicioStr.append(calendar.get(Calendar.YEAR) - 1);
			dataTerminoStr.append(calendar.get(Calendar.YEAR) + 1);
		}else{
			dataInicioStr.append(ano);
			dataTerminoStr.append(ano);
		}
		dataInicioStr.append(" 00:00");
		dataTerminoStr.append(" 23:59");
		
		try {
			horario.setInicio(Utils.dateTimeFormat.parse(dataInicioStr.toString()));
			horario.setTermino(Utils.dateTimeFormat.parse(dataTerminoStr.toString()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		if(clienteId != null){
			horario.setCliente(new Pessoa());
			horario.getCliente().setId(clienteId);
		}
		
		return horario;
	}
	
	@RequestMapping(value="/delelarHorario/{id}", method=RequestMethod.GET)
	public @ResponseBody() String deletarHorario(@PathVariable("id") Long id){
		horarioService.delete(id);
		return "ok";
	}

	@RequestMapping(value="/alterar", method=RequestMethod.POST)
	@ResponseBody()
	public String alterarHorario(Long id, Long funcionarioId, String inicio, String termino, String observacao) throws ParseException{
		Horario horario = horarioService.findById(id);
		popularHorario(horario, funcionarioId, inicio, termino);
		horario.setObservacao(observacao);
		verificarDisponibilidade(horario);
		horarioService.persist(horario);
		return "ok";
	}
	
	private Horario popularHorario(Horario horario, Long funcionarioId, String inicio, String termino) throws ParseException{
		 Pessoa funcionario = pessoaService.findById(funcionarioId);
		 horario.setFuncionario(funcionario);
		 
		Date dataInicio = Utils.dateTimeFormat.parse(inicio);
		Calendar dataInicioCalendar = Calendar.getInstance();
		dataInicioCalendar.setTime(dataInicio);
		if(dataInicioCalendar.get(Calendar.HOUR_OF_DAY) < 8){
			dataInicioCalendar.set(Calendar.HOUR_OF_DAY, 8);
			dataInicioCalendar.set(Calendar.MINUTE, 0);
		}
		if(dataInicioCalendar.get(Calendar.HOUR_OF_DAY) > 20){
			dataInicioCalendar.set(Calendar.HOUR_OF_DAY, 20);
			dataInicioCalendar.set(Calendar.MINUTE, 0);
		}
		dataInicio = dataInicioCalendar.getTime();
		
		Date dataTermino = Utils.dateTimeFormat.parse(termino);
		Calendar dataTerminoCalendar = Calendar.getInstance();
		dataTerminoCalendar.setTime(dataTermino);
		
		if(dataTerminoCalendar.get(Calendar.HOUR_OF_DAY) < 8){
			dataTerminoCalendar.set(Calendar.HOUR_OF_DAY, 8);
			dataTerminoCalendar.set(Calendar.MINUTE, 0);
		}
		if(dataTerminoCalendar.get(Calendar.HOUR_OF_DAY) > 20){
			dataTerminoCalendar.set(Calendar.HOUR_OF_DAY, 20);
			dataTerminoCalendar.set(Calendar.MINUTE, 0);
		}
		dataTerminoCalendar.set(Calendar.DAY_OF_YEAR, dataInicioCalendar.get(Calendar.DAY_OF_YEAR));
		if(dataTerminoCalendar.before(dataInicioCalendar)){
			throw new RuntimeException("O hor�rio termina antes do in�cio");
		}
		dataTermino = dataTerminoCalendar.getTime();
		
		horario.setInicio(dataInicio);
		horario.setTermino(dataTermino);
		return horario;
	}
	
	@RequestMapping(value="/agendar", method=RequestMethod.POST)
	@ResponseBody()
	public String agendar(String clienteStr, Long funcionarioId, String inicio, String termino, String  servicosId, String observacao, Boolean folga){
		 try {
			 String result = "ok";
			 Horario horario = new Horario();
			 horario.setFolga(folga);
			 horario.setObservacao(observacao);
			 if( ! horario.getFolga()){
				 try{
					 Long clienteId = Long.parseLong(clienteStr); 
					 Pessoa cliente = pessoaService.findById(clienteId);
					 horario.setCliente(cliente);
				 }catch(Exception e){
					 Pessoa novoCliente = new Pessoa();
					 novoCliente.setNome(clienteStr);
					 novoCliente.setFuncionario(false);
					 novoCliente = pessoaService.persist(novoCliente);
					 horario.setCliente(novoCliente);
					 horario.setObservacao("� preciso completar o cadastro deste cliente. " + horario.getObservacao());
					 result = "refresh";
				 }
				 horario.getServicos().addAll(findServicos(servicosId));
			 }
			popularHorario(horario, funcionarioId, inicio, termino);
			verificarDisponibilidade(horario);
			horarioService.persist(horario);
			return result;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		 
	}
	
	private void verificarDisponibilidade(Horario horario){
		Date termino = new Date(horario.getTermino().getTime());
		horario.setTermino(new Date(termino.getTime() + 61000));
		List<Horario> search = horarioService.findHorario(horario);
		if(search != null && !search.isEmpty()){
			for(Horario h : search){
				if(!h.getId().equals(horario.getId())){
					throw new HorarioOcupadoException("Hor�rio j� ocupado");
				}
			}
		}
		horario.setTermino(termino);
	}
	
	private List<Servico> findServicos(String servicosIdsStr){
		String[] ids = servicosIdsStr.split(",");
		List<Servico> servicos = new ArrayList<Servico>();
		for(String idStr : ids){
			Long id = Long.parseLong(idStr.trim());
			Servico servico = servicoService.findById(id);
			servicos.add(servico);
		}
		return servicos;
	}
	
}
