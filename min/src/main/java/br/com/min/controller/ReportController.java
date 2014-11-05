package br.com.min.controller;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.ComissoesPorFuncionarioVO;
import br.com.min.controller.vo.TotaisPorFormaPagamentoVO;
import br.com.min.controller.vo.TotalParcelamentoVO;
import br.com.min.controller.vo.TotalPorFuncionarioVO;
import br.com.min.controller.vo.TotalPorTipoServicoVO;
import br.com.min.entity.Comanda;
import br.com.min.entity.FluxoPagamento;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.LancamentoProduto;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.TipoComissao;
import br.com.min.entity.TipoServico;
import br.com.min.entity.Usuario;
import br.com.min.service.ComandaService;
import br.com.min.service.ComissaoService;
import br.com.min.service.PagamentoService;
import br.com.min.service.PessoaService;
import br.com.min.service.ServicoService;
import br.com.min.utils.ReportUtils;
import br.com.min.utils.Utils;

@Controller()
@RequestMapping("/report")
public class ReportController {

	private static final String VIEW_FATURAMENTO = "faturamento";
	private static final String VIEW_CAIXA = "caixa";
	@Autowired
	private ComandaService comandaService;
	@Autowired
	private ComissaoService comissaoServico;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private PagamentoService pagamentoService;
	@Autowired
	private ServicoService servicoService;
	
	@RequestMapping(value="/caixa", method=RequestMethod.GET)
	public ModelAndView listarCaixa(HttpServletRequest request){
		ModelAndView mv = new ModelAndView(VIEW_CAIXA);
		return criarViewCaixa(new Date(), new Date(), mv,  request);
	}
	
	@RequestMapping(value="/caixa/data", method=RequestMethod.POST)
	public ModelAndView listarCaixa(String data, HttpServletRequest request){
		try {
			ModelAndView mv = new ModelAndView(VIEW_CAIXA);
			return criarViewCaixa(Utils.dateFormat.parse(data), Utils.dateFormat.parse(data), mv, request);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value="/faturamento", method=RequestMethod.GET)
	public ModelAndView listarFaturamento(HttpServletRequest request){
		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.DAY_OF_MONTH, 1);
		Date dataInicio = inicio.getTime();
		
		Calendar fim = Calendar.getInstance();
		fim.set(Calendar.DAY_OF_MONTH, inicio.getMaximum(Calendar.DAY_OF_MONTH));
		Date dataFim = fim.getTime();

		ModelAndView mv = new ModelAndView(VIEW_FATURAMENTO);
		mv = criarViewCaixa(dataInicio, dataFim, mv, request);
		compilarDadosFaturamento(mv);
		return mv;
	}
	
	
	@RequestMapping(value="/faturamento/data", method=RequestMethod.POST)
	public ModelAndView listarFaturamento(String dataInicio, String dataFim, HttpServletRequest request){
		try {
			ModelAndView mv = new ModelAndView(VIEW_FATURAMENTO);
			mv = criarViewCaixa(Utils.dateFormat.parse(dataInicio), Utils.dateFormat.parse(dataFim), mv, request);
			compilarDadosFaturamento(mv);
			return mv;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void compilarDadosFaturamento(ModelAndView mv){
		List<Comanda> comandas = (List<Comanda>)mv.getModel().get("comandas");
		Double totalRevenda = 0d;
		Double totalProdutos = 0d;
		Double totalDescontos = 0d;
		Double totalServicos = 0d;
		List<TotalPorTipoServicoVO> totais = new ArrayList<>();
		List<TipoServico> tiposServico = servicoService.listarTipoServico();
		for(TipoServico tipoServico : tiposServico){
			TotalPorTipoServicoVO vo = new TotalPorTipoServicoVO();
			vo.setTipoServico(tipoServico);
			totais.add(vo);
		}
		List<TotalPorFuncionarioVO> totaisFuncionario = new ArrayList<>();
		List<TotalPorFuncionarioVO> totaisFuncionarioRevenda = new ArrayList<>();
		List<Pessoa> funcionarios = pessoaService.listarFuncionarios();
		for(Pessoa funcionario : funcionarios){
			TotalPorFuncionarioVO vo = new TotalPorFuncionarioVO();
			vo.setId(funcionario.getId());
			vo.setFuncionario(funcionario.getNome());
			totaisFuncionario.add(vo);
			
			vo = new TotalPorFuncionarioVO();
			vo.setId(funcionario.getId());
			vo.setFuncionario(funcionario.getNome());
			totaisFuncionarioRevenda.add(vo);
		}
		for(Comanda comanda : comandas){
			for(LancamentoServico servico : comanda.getServicos()){
				TipoServico tipoServico = servico.getServico().getTipoServico();
				for(TotalPorTipoServicoVO vo : totais){
					if(vo.getTipoServico().equals(tipoServico)){
						vo.setTotal(vo.getTotal() + servico.getValor());
						break;
					}
				}
				for(TotalPorFuncionarioVO vo : totaisFuncionario){
					if(vo.getId().equals(servico.getFuncionario().getId())){
						vo.setTotal(vo.getTotal() + servico.getValor());
						break;
					}
				}
				totalServicos += servico.getValor();
			}
			for(LancamentoProduto produto : comanda.getProdutos()){
				if(produto.getRevenda()){
					totalRevenda += produto.getValor();
					for(TotalPorFuncionarioVO vo : totaisFuncionarioRevenda){
						if(produto.getVendedor().getId().equals(vo.getId())){
							vo.setTotal(vo.getTotal() + produto.getValor());
						}
					}
				}else{
					totalProdutos += produto.getValor();
				}
			}
			totalDescontos += comanda.getDesconto();
		}
		mv.addObject("totaisFuncionario", totaisFuncionario);
		mv.addObject("totaisFuncionarioRevenda", totaisFuncionarioRevenda);
		mv.addObject("totalDescontos", totalDescontos);
		mv.addObject("totalProdutos", totalProdutos);
		mv.addObject("totalRevenda", totalRevenda);
		mv.addObject("totalServicos", totalServicos);
		mv.addObject("totaisTipoServico", totais);
	}
	
	@RequestMapping(value="/comissao", method=RequestMethod.GET)
	public ModelAndView listarComissoes(HttpServletRequest request){
		
		Date hoje = new Date();
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(hoje);
		inicio.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar fim = Calendar.getInstance();
		fim.setTime(hoje);
		fim.set(Calendar.DAY_OF_MONTH, fim.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return listarComissoes(inicio.getTime(), fim.getTime(), request);
	}
	
	@RequestMapping(value="/comissao/data", method=RequestMethod.POST)
	public ModelAndView listarComissoes(String dataInicio, String dataFim, HttpServletRequest request){
		try {
			Date inicio =Utils.dateFormat.parse(dataInicio);
			Date fim = Utils.dateFormat.parse(dataFim);
			return listarComissoes(inicio, fim, request);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(value="/comissao", method=RequestMethod.POST)
	public @ResponseBody List<LancamentoComissao> listarComissoes(Long funcionarioId, String dataInicio, String dataFim, HttpServletRequest request){
		Usuario logado = Utils.getUsuarioLogado(request);
		if(Utils.hasRole(Role.ADMIN, request) || logado.getPessoa().getId().equals(funcionarioId)){
			Date inicio;
			try {
				inicio = Utils.dateFormat.parse(dataInicio);
				Date fim = Utils.dateFormat.parse(dataFim);
				Pessoa funcionario = pessoaService.findById(funcionarioId);
				List<LancamentoComissao> comissoes = comissaoServico.findByPeriodo(inicio, fim, funcionario);
				for(LancamentoComissao comissao : comissoes){
					limparComandaJSON(comissao.getComanda());
					comissao.getFuncionario().setUsuario(null);
				}
				return comissoes;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	@RequestMapping("/comissao/pagar")
	public @ResponseBody String pagar(String ids, String dataInicio, String dataFim, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			String[] idsStr = ids.split(",");
			Double valorTotal = 0.0;
			Pessoa funcionario = null;
			for(String idStr : idsStr){
				Long id = Long.parseLong(idStr);
				LancamentoComissao comissao = comissaoServico.findById(id);
				if(comissao.getDataPagamento() == null){
					comissao.setDataPagamento(new Date());
					comissaoServico.persist(comissao);
					valorTotal += comissao.getValor();
					funcionario = comissao.getFuncionario();
				}
			}
			if(funcionario != null){
				Pagamento pagamento = new Pagamento();
				pagamento.setFluxoPagamento(FluxoPagamento.SAIDA);
				pagamento.setFormaPagamento(FormaPagamento.Dinheiro);
				pagamento.setParcelamento(1);
				pagamento.setObservacao("Comissão para " + funcionario.getNome() + " de " + dataInicio + " até " + dataFim);
				pagamento.setValor(valorTotal);
				pagamentoService.persist(pagamento);
			}
		}
		return "ok";
	}
	
	private Comanda limparComandaJSON(Comanda comanda){
		if(comanda != null){
			comanda.setProdutos(null);
			comanda.setServicos(null);
			comanda.setEstoque(null);
			comanda.setCriador(null);
			comanda.setPagamentos(null);
			comanda.setComissoes(null);
		}
		return comanda;
	}
	
	@RequestMapping(value="/fluxoFinanceiro/data")
	public ModelAndView listarFluxoCaixa(String dataInicio, String dataFim, HttpServletRequest request){
		try {
			Date inicio =Utils.dateFormat.parse(dataInicio);
			Date fim = Utils.dateFormat.parse(dataFim);
			return listarFluxoCaixa(inicio, fim, request);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	@RequestMapping(value="/fluxoFinanceiro")
	public ModelAndView listarFluxoCaixa(HttpServletRequest request){
		Calendar inicio = Calendar.getInstance();
		inicio.add(Calendar.DAY_OF_YEAR, -3);
		Calendar fim = Calendar.getInstance();
		return listarFluxoCaixa(inicio.getTime(), fim.getTime(), request);
	}
	
	@RequestMapping(value="/addLancamento")
	public @ResponseBody String adicionarLancamento(String tipo, Double valor, String observacao){
		Pagamento pagamento = new Pagamento();
		pagamento.setFluxoPagamento(FluxoPagamento.valueOf(tipo));
		pagamento.setObservacao(observacao);
		pagamento.setValor(valor);
		pagamentoService.persist(pagamento);
		return "ok";
	}
	private ModelAndView listarFluxoCaixa(Date inicio, Date fim, HttpServletRequest request){
		if(Utils.hasRole(Role.ADMIN, request)){
			ModelAndView mv = new ModelAndView("fluxoFinanceiro");
			List<Pagamento> pagamentos = pagamentoService.find(inicio, fim);
			mv.addObject("pagamentos", pagamentos);
			mv.addObject("dataInicio", inicio);
			mv.addObject("dataFim", fim);
			return mv;
		}
		throw new RuntimeException("Sem permissão para vizualizar o fluxo financeiro");
	}
	
	private ModelAndView listarComissoes(Date inicio, Date fim, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("comissoes");
		Pessoa funcionario = null;
		if( ! Utils.hasRole(Role.ADMIN, request) && ! Utils.hasRole(Role.CAIXA, request) ){
			funcionario = Utils.getUsuarioLogado(request).getPessoa();
		}
		List<LancamentoComissao> comissoes = comissaoServico.findByPeriodo(inicio, fim, funcionario);
		List<ComissoesPorFuncionarioVO> comissoesPorFuncionario = processarComissoes(comissoes, request);
		
		mv.addObject("comissoesPorFuncionario", comissoesPorFuncionario);
		mv.addObject("comissoes", comissoes);
		mv.addObject("dataInicio", inicio);
		mv.addObject("dataFim", fim);
		return mv;
	}
	
	private Map<Long, ComissoesPorFuncionarioVO> iniciarVosPorFuncionario(HttpServletRequest request){
		Map<Long, ComissoesPorFuncionarioVO> vosPorFuncionario = new HashMap<>();
		List<Pessoa> funcionarios = null;
		if( ! Utils.hasRole(Role.ADMIN, request) && ! Utils.hasRole(Role.CAIXA, request)){
			Pessoa funcionario = Utils.getUsuarioLogado(request).getPessoa();
			funcionarios = pessoaService.findPessoa(funcionario);
		} else {
			funcionarios = pessoaService.listarFuncionarios();
		}
		for(Pessoa funcionario : funcionarios){
			ComissoesPorFuncionarioVO vo = new ComissoesPorFuncionarioVO();
			vo.setFuncionario(funcionario);
			vosPorFuncionario.put(funcionario.getId(), vo);
		}
		return vosPorFuncionario;
	}
	
	private List<ComissoesPorFuncionarioVO>  processarComissoes(List<LancamentoComissao> comissoes, HttpServletRequest request){
		List<ComissoesPorFuncionarioVO> vos = new ArrayList<>();
		Map<Long, ComissoesPorFuncionarioVO> vosPorFuncionario = iniciarVosPorFuncionario(request);
		for(LancamentoComissao comissao : comissoes){
			ComissoesPorFuncionarioVO vo = vosPorFuncionario.get(comissao.getFuncionario().getId());
			vo.setTotal(vo.getTotal() + comissao.getValor());
			if(comissao.getTipo().equals(TipoComissao.AUXILIAR)){
				vo.setTotalAuxilar(vo.getTotalAuxilar() + comissao.getValor());
			}else
			if(comissao.getTipo().equals(TipoComissao.SERVICO)){
				vo.setTotalServico(vo.getTotalServico() + comissao.getValor());
			}else
			if(comissao.getTipo().equals(TipoComissao.SERVICO_COM_AUXILIAR)){
				vo.setTotalServicoAuxiliado(vo.getTotalServicoAuxiliado() + comissao.getValor());
			}else
			if(comissao.getTipo().equals(TipoComissao.VENDA)){
				if( ! vo.getPercentuais().contains(comissao.getPercentual())){
					vo.getPercentuais().add(comissao.getPercentual());
				}
				Double totalPorPercentual = vo.getTotalPorPercentual().get(comissao.getPercentual());
				if(totalPorPercentual == null){
					totalPorPercentual = 0.0;
				}
				vo.getTotalPorPercentual().put(comissao.getPercentual(), totalPorPercentual + comissao.getValor());
				vo.setTotalVendas(vo.getTotalVendas() + comissao.getValor());
			}
			Collections.sort(vo.getPercentuais());
			vosPorFuncionario.put(comissao.getFuncionario().getId(), vo);
		}
		vos.addAll(vosPorFuncionario.values());
		return vos;
	}
	
	private ModelAndView criarViewCaixa(Date dataInicio, Date dataFim, ModelAndView mv, HttpServletRequest request){
		if( Utils.hasRole(Role.ADMIN, request) || Utils.hasRole(Role.CAIXA, request)){
			List<Comanda> comandas = comandaService.findFechamento(dataInicio, dataFim);
			mv.addObject("comandas", comandas);
			Map<FormaPagamento, TotaisPorFormaPagamentoVO> totais = new LinkedHashMap<>();
			for(FormaPagamento fp : FormaPagamento.values()){
				TotaisPorFormaPagamentoVO vo = new TotaisPorFormaPagamentoVO();
				vo.setFormaPagamento(fp);
				totais.put(fp, vo);
			}
			Double totalLancado = 0d;
			Double totalPago = 0d;
			for(Comanda comanda : comandas){
				totalLancado += comanda.getValorCobrado();
				for(Pagamento pagamento : comanda.getPagamentos()){
					TotaisPorFormaPagamentoVO total = totais.get(pagamento.getFormaPagamento());
					total.setTotal( total.getTotal() + pagamento.getValor());
					totalPago += pagamento.getValor();
					if(pagamento.getFormaPagamento().isParcelavel()){
						TotalParcelamentoVO parcelaParaAtualizar = null;
						for(TotalParcelamentoVO parcelaVO : total.getParcelas()){
							if(parcelaVO.getParcelas().equals(pagamento.getParcelamento())){
								parcelaParaAtualizar = parcelaVO;
							}
						}
						if(parcelaParaAtualizar == null){
							parcelaParaAtualizar = new TotalParcelamentoVO();
							parcelaParaAtualizar.setParcelas(pagamento.getParcelamento());
							total.getParcelas().add(parcelaParaAtualizar);
							Collections.sort(total.getParcelas());
						}
						parcelaParaAtualizar.setTotal( parcelaParaAtualizar.getTotal() + pagamento.getValor() );
					}
					totais.put(pagamento.getFormaPagamento(), total);
				}
			}
			
			mv.addObject("totais", totais.values());
			
			mv.addObject("totalLancado", totalLancado);
			mv.addObject("totalPago", totalPago);
			
			mv.addObject("dataInicio", dataInicio);
			mv.addObject("dataFim", dataFim);
		}
		return mv;
	}
	
	@RequestMapping(value="/caixa/download/{dia}/{mes}/{ano}", method=RequestMethod.GET)
	public void downloadCaixaDia(@PathVariable("ano") String ano,@PathVariable("mes") String mes, @PathVariable("dia") String dia , HttpServletRequest request, HttpServletResponse response){
		File file = null;
		try {
			String data = dia + "/" + mes + "/" + ano;
			Map<String, Object> values = criarDadosCaixaDownload(data, data, request);
			String foFile = ReportUtils.processTemplate(values, "caixa-dia.xml");
			file = ReportUtils.generatePDF("caixa-dia", foFile);
			response.setContentType("application/pdf");
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(file != null){
				FileUtils.deleteQuietly(file);
			}
		}
	}
	
	@RequestMapping(value="/faturamento/download/{diaInicio}/{mesInicio}/{anoInicio}/{diaFim}/{mesFim}/{anoFim}", method=RequestMethod.GET)
	public void download	(
				@PathVariable("anoInicio") String anoInicio,
				@PathVariable("mesInicio") String mesInicio, 
				@PathVariable("diaInicio") String diaInicio ,
				@PathVariable("anoFim") String anoFim,
				@PathVariable("mesFim") String mesFim, 
				@PathVariable("diaFim") String diaFim , 
				HttpServletRequest request, HttpServletResponse response){
		File file = null;
		try {
			String dataInicio = diaInicio + "/" + mesInicio + "/" + anoInicio;
			String dataFim = diaFim + "/" + mesFim + "/" + anoFim;
			Map<String, Object> values = criarDadosCaixaDownload(dataInicio, dataFim, request);
			String foFile = ReportUtils.processTemplate(values, "caixa-dia.xml");
			file = ReportUtils.generatePDF("caixa-dia", foFile);
			response.setContentType("application/pdf");
			response.getOutputStream().write(FileUtils.readFileToByteArray(file));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(file != null){
				FileUtils.deleteQuietly(file);
			}
		}
	}
	
	private Map<String, Object> criarDadosCaixaDownload(String dataInicio, String dataFim, HttpServletRequest request) throws ParseException{
		ModelAndView mv = new ModelAndView(VIEW_CAIXA);
		mv = criarViewCaixa(Utils.dateFormat.parse(dataInicio), Utils.dateFormat.parse(dataFim), mv, request);
		Map<String, Object> values = mv.getModel();
		Collection<TotaisPorFormaPagamentoVO> vos = (Collection<TotaisPorFormaPagamentoVO>)values.get("totais");
		Map<String, Object> result = new HashMap<>();
		for(TotaisPorFormaPagamentoVO vo : vos){
			result.put(vo.getFormaPagamento().name(), Utils.moneyFormat.format(vo.getTotal()));
		}
		result.put("total", Utils.moneyFormat.format((Double)values.get("totalPago")));
		result.put("dataInicio", dataInicio);
		result.put("dataFim", dataFim);
		return result;
	}
	
}
