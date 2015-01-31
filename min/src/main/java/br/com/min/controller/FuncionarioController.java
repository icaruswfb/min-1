package br.com.min.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.DadosCompiladosFuncionarioVO;
import br.com.min.entity.Comanda;
import br.com.min.entity.Comissao;
import br.com.min.entity.ComissaoServico;
import br.com.min.entity.Funcao;
import br.com.min.entity.Imagem;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.TipoServico;
import br.com.min.entity.Usuario;
import br.com.min.filter.SecurityFilter;
import br.com.min.service.ComandaService;
import br.com.min.service.PessoaService;
import br.com.min.service.ServicoService;
import br.com.min.service.UsuarioService;
import br.com.min.utils.Utils;

@Controller("")
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ComandaService comandaService;
	@Autowired
	private ServicoService servicoService;
	
	@RequestMapping("/")
	public ModelAndView listar(){
		return list(pessoaService.listarFuncionarios(), null);
	}

	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public @ResponseBody List<Pessoa> listarFuncionarios(HttpServletRequest request){
		List<Pessoa> pessoas = pessoaService.listarFuncionarios();
		limparPessoasJSON(pessoas);
		return pessoas;
	}
	
	@RequestMapping(value="/listarParaAgenda",method=RequestMethod.GET)
	public @ResponseBody List<Pessoa> listarFuncionariosParaAgenda(HttpServletRequest request){
		Pessoa funcionario = criarPesquisaAgenda(request);
		List<Pessoa> pessoas = pessoaService.findPessoa(funcionario, Funcao.Administrador, Funcao.Recepcionista);
		limparPessoasJSON(pessoas);
		return pessoas;
	}
	
	private Pessoa criarPesquisaAgenda(HttpServletRequest request) {
		Pessoa query = new Pessoa();
		query.setFuncionario(true);
		if( ! Utils.hasRole(Role.ADMIN, request) && ! Utils.hasRole(Role.CAIXA, request) ){
			Usuario logado = Utils.getUsuarioLogado(request);
			query.setId(logado.getPessoa().getId());
		}
		return query;
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
			funcionario.setComissao(new Comissao());
			funcionario.setImagem(new Imagem());
			funcionario.setUsuario(new Usuario());
			
			criarComissoesServicoPadrao(funcionario.getComissao());
		}else{
			funcionario = pessoaService.findById(id);
			if(funcionario.getComissao().getComissoesServico().isEmpty()){
				criarComissoesServicoPadrao(funcionario.getComissao());
			}
		}
		mv.addObject("funcionario", funcionario);
		
		return mv;
	}
	
	private void criarComissoesServicoPadrao(Comissao comissao){
		List<TipoServico> tiposServico = servicoService.listarTipoServico();
		for(TipoServico tipoServico : tiposServico){
			ComissaoServico comissaoServico = new ComissaoServico();
			comissaoServico.setComissao(0d);
			comissaoServico.setTipoServico(tipoServico);
			comissao.getComissoesServico().add(comissaoServico);
		}
	}
	
	private boolean hasPermissaoEditar(Pessoa funcionario, HttpServletRequest request){
		boolean hasPermission = false;
		if(funcionario.getId() != null){
			if( ! Utils.hasRole(Role.ADMIN, request)){
				Usuario logado = Utils.getUsuarioLogado(request);
				if( funcionario.getId().equals( logado.getPessoa().getId() )){
					hasPermission = true;
					Pessoa antigo = pessoaService.findById(funcionario.getId());
					funcionario.setFuncaoPrincipal(antigo.getFuncaoPrincipal());
					funcionario.getUsuario().setRole(antigo.getUsuario().getRole());
				}
			}else{
				hasPermission = true;
			}
		}else{
			if( Utils.hasRole(Role.ADMIN, request)){
				hasPermission = true;
			}
		}
		return hasPermission;
	}
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	public ModelAndView salvar(@ModelAttribute("funcionario") Pessoa funcionario, HttpServletRequest request){
		if( hasPermissaoEditar(funcionario, request)){
			funcionario.setFuncionario(true);
			List<String> errorMessages = validarUsuario(funcionario);
			if( ! errorMessages.isEmpty() ){
				ModelAndView mv = new ModelAndView("funcionario");
				mv.addObject("funcionario", funcionario);
				mv.addObject("errorMessages", errorMessages);
				return mv;
			}
			
			preencherComissoes(funcionario.getComissao(), request);
			
			tratarUsuario(funcionario);
			
			pessoaService.persist(funcionario);
			updateSession(request, funcionario);
		}
		return listar();
	}
	
	private void preencherComissoes(Comissao comissao, HttpServletRequest request){
		List<TipoServico> tiposServico = servicoService.listarTipoServico();
		for(TipoServico tipoServico : tiposServico){
			String valor = request.getParameter("comissaoServico" + tipoServico.getId());
			Double valorDouble = Double.parseDouble(valor);					
			ComissaoServico comissaoServico = comissao.findComissaoServico(tipoServico);
			if(comissaoServico == null){
				comissaoServico = new ComissaoServico();
				comissaoServico.setTipoServico(tipoServico);
				comissao.getComissoesServico().add(comissaoServico);
			}
			comissaoServico.setComissao(valorDouble);
		}
	}
	
	@RequestMapping(value="/alterarImagem/{funcionarioId}/{imagemId}", method=RequestMethod.GET)
	public @ResponseBody String alterarImagem(@PathVariable("funcionarioId") Long funcionarioId, @PathVariable("imagemId") Long imagemId, HttpServletRequest request){
		Pessoa funcionario = pessoaService.findById(funcionarioId);
		if(hasPermissaoEditar(funcionario, request)){
			if(funcionario.getImagem() == null){
				funcionario.setImagem(new Imagem());
			}
			funcionario.getImagem().setId(imagemId);
			pessoaService.persist(funcionario);
			updateSession(request, funcionario);
		}
		return "ok";
	}
	
	private void tratarUsuario(Pessoa funcionario){
		Usuario usuario = funcionario.getUsuario();
		String login = usuario.getLogin();
		Role roleNova = usuario.getRole();
		boolean encryptPassword = true;
		if( StringUtils.isBlank( usuario.getSenha() )){
			if(usuario.getId() != null){
				usuario = usuarioService.findById(usuario.getId());
				usuario.setLogin(login);
				if(usuario != null){
					funcionario.setUsuario(usuario);
					encryptPassword = false;
				}
			}
		}
		if(roleNova != null){
			usuario.setRole(roleNova);
		}
		funcionario.setUsuario(usuario);
		if(encryptPassword){
			String encryptedPassword = Utils.encriyt(funcionario.getUsuario().getSenha());
			funcionario.getUsuario().setSenha(encryptedPassword);
		}
		funcionario.getUsuario().setPessoa(funcionario);
	}
	
	private void updateSession(HttpServletRequest request, Pessoa funcionario){
		Usuario logado = Utils.getUsuarioLogado(request);
		if(logado.getPessoa().getId().equals(funcionario.getId())){
			logado.setPessoa(funcionario);
			request.getSession().setAttribute(SecurityFilter.LOGGED_USER, logado);
			Map<String, Boolean> hasRole = (Map<String, Boolean>) request.getSession().getAttribute(SecurityFilter.HAS_ROLE);
			boolean hasAdminRole = funcionario.getUsuario().getRole().equals(Role.ADMIN);
			for(Role role : Role.values()){
				hasRole.put(
						role.name(), 
						hasAdminRole ? true : funcionario.getUsuario().getRole().equals(role));
			}
			request.getSession().setAttribute(SecurityFilter.HAS_ROLE, hasRole);
		}
	}
	
	private void limparPessoasJSON(List<Pessoa> pessoas){
		for(Pessoa pessoa : pessoas){
			limparPessoaJSON(pessoa);
		}
	}
	
	private void limparPessoaJSON(Pessoa pessoa){
		pessoa.setUsuario(null);
	}
	
	private List<String> validarUsuario(Pessoa funcionario){
		List<String> errorsMessages = new ArrayList<>();
		if(funcionario.getUsuario() != null){
			
			if( StringUtils.isBlank( funcionario.getUsuario().getLogin() ) ){
				errorsMessages.add("Nome de usuário não pode ser vazio");
			}
			if(StringUtils.isBlank( funcionario.getUsuario().getSenha() )){
				if( funcionario.getUsuario().getId() == null ){
					errorsMessages.add("Senha não pode ser vazio");
				}
			}else{
				Usuario usuario = null;
				boolean hasPasswordChanged = false;
				if( StringUtils.isNotBlank( funcionario.getUsuario().getSenha() ) ){
					usuario = usuarioService.autenticar(funcionario.getUsuario().getLogin(), funcionario.getUsuario().getSenha());
					hasPasswordChanged = usuario == null;
				}
				if( funcionario.getUsuario().getId() == null || hasPasswordChanged ){
					if( ! funcionario.getUsuario().getSenha().equals( funcionario.getUsuario().getConfirmacaoSenha() ) ){
						errorsMessages.add("Senha e Confirmação de senha não são iguais");
					}
				}else{
					if( StringUtils.isNotBlank( funcionario.getUsuario().getConfirmacaoSenha() )){
						if( ! funcionario.getUsuario().getSenha().equals( funcionario.getUsuario().getConfirmacaoSenha() ) ){
							errorsMessages.add("Senha e Confirmação de senha não são iguais");
						}
					}
				}
			}
		}
		return errorsMessages;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletRequest request){
		Pessoa funcionario = pessoaService.findById(id);
		if(hasPermissaoEditar(funcionario, request)){
			pessoaService.delete(id);
		}
		return listar();
	}
	
	@RequestMapping(value="/compiled/{id}", method=RequestMethod.GET)
	public @ResponseBody DadosCompiladosFuncionarioVO getCompiledInformation(@PathVariable(value="id") Long funcionarioId){
		DadosCompiladosFuncionarioVO vo = new DadosCompiladosFuncionarioVO();
		
		List<Comanda> comandas = comandaService.findComandasByFuncionario(funcionarioId);
		Calendar mesAtual = Calendar.getInstance();
		Integer mes = mesAtual.get(Calendar.MONTH);
		Integer clientesMes = 0;
		for(Comanda comanda : comandas){
			Date abertura = comanda.getAbertura();
			Calendar calendarComanda = Calendar.getInstance();
			calendarComanda.setTime(abertura);
			Integer mesComanda = calendarComanda.get(Calendar.MONTH);
			if(mes.equals(mesComanda)){
				clientesMes++;
			}
		}
		
		vo.setClientesTotal(comandas.size());
		vo.setClientesMes(clientesMes);
		
		return vo;
	}
	
}
