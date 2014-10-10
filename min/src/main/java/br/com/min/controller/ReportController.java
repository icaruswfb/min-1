package br.com.min.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
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
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
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
import br.com.min.entity.Comanda;
import br.com.min.entity.FluxoPagamento;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.TipoComissao;
import br.com.min.entity.Usuario;
import br.com.min.service.ComandaService;
import br.com.min.service.ComissaoService;
import br.com.min.service.PagamentoService;
import br.com.min.service.PessoaService;
import br.com.min.utils.ReportUtils;
import br.com.min.utils.Utils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller()
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ComandaService comandaService;
	@Autowired
	private ComissaoService comissaoServico;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private PagamentoService pagamentoService;
	
	@RequestMapping(value="/caixa", method=RequestMethod.GET)
	public ModelAndView listarCaixa(HttpServletRequest request){
		return criarViewCaixa(new Date(), request);
	}
	
	@RequestMapping(value="/caixa/data", method=RequestMethod.POST)
	public ModelAndView listarCaixa(String data, HttpServletRequest request){
		try {
			return criarViewCaixa(Utils.dateFormat.parse(data), request);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
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
				vo.getPercentuais().add(comissao.getPercentual());
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
	
	private ModelAndView criarViewCaixa(Date data, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("caixa");
		if( Utils.hasRole(Role.ADMIN, request) || Utils.hasRole(Role.CAIXA, request)){
			List<Comanda> comandas = comandaService.findFechamento(data);
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
			
			mv.addObject("data", data);
		}
		return mv;
	}
	
	@RequestMapping(value="/caixa/download/{dia}/{mes}/{ano}", method=RequestMethod.GET)
	public void downloadCaixaDia(@PathVariable("ano") String ano,@PathVariable("mes") String mes, @PathVariable("dia") String dia , HttpServletRequest request, HttpServletResponse response){
		File file = null;
		try {
			String data = dia + "/" + mes + "/" + ano;
			Map<String, Object> values = criarDadosCaixaDownload(data, request);
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
	
	private Map<String, Object> criarDadosCaixaDownload(String data, HttpServletRequest request) throws ParseException{
		ModelAndView mv = criarViewCaixa(Utils.dateFormat.parse(data), request);
		Map<String, Object> values = mv.getModel();
		Collection<TotaisPorFormaPagamentoVO> vos = (Collection<TotaisPorFormaPagamentoVO>)values.get("totais");
		Map<String, Object> result = new HashMap<>();
		for(TotaisPorFormaPagamentoVO vo : vos){
			result.put(vo.getFormaPagamento().name(), Utils.moneyFormat.format(vo.getTotal()));
		}
		result.put("total", Utils.moneyFormat.format((Double)values.get("totalPago")));
		result.put("data", data);
		return result;
	}
	
}
