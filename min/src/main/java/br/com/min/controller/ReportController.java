package br.com.min.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.controller.vo.ComissoesPorFuncionarioVO;
import br.com.min.entity.Comanda;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.Pagamento;
import br.com.min.entity.Pessoa;
import br.com.min.entity.Role;
import br.com.min.entity.TipoComissao;
import br.com.min.service.ComandaService;
import br.com.min.service.ComissaoService;
import br.com.min.service.PessoaService;
import br.com.min.utils.Utils;

@Controller()
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ComandaService comandaService;
	@Autowired
	private ComissaoService comissaoServico;
	@Autowired
	private PessoaService pessoaService;
	
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
			vo.getComissoes().add(comissao);
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
			Map<FormaPagamento, Double> totais = new HashMap<FormaPagamento, Double>();
			for(FormaPagamento fp : FormaPagamento.values()){
				totais.put(fp, 0d);
			}
			Double totalLancado = 0d;
			Double totalPago = 0d;
			for(Comanda comanda : comandas){
				totalLancado += comanda.getValorCobrado();
				for(Pagamento pagamento : comanda.getPagamentos()){
					Double total = totais.get(pagamento.getFormaPagamento());
					total += pagamento.getValor();
					totalPago += pagamento.getValor();
					totais.put(pagamento.getFormaPagamento(), total);
				}
			}
			
			for(FormaPagamento formaPagamento : totais.keySet()){
				mv.addObject("total" + formaPagamento.name(), totais.get(formaPagamento));
			}
			
			mv.addObject("totalLancado", totalLancado);
			mv.addObject("totalPago", totalPago);
			
			mv.addObject("data", data);
		}
		return mv;
	}
	
}
