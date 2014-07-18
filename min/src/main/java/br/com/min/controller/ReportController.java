package br.com.min.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.min.entity.Comanda;
import br.com.min.entity.FormaPagamento;
import br.com.min.entity.Pagamento;
import br.com.min.service.ComandaService;
import br.com.min.utils.Utils;

@Controller()
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ComandaService comandaService;
	
	@RequestMapping(value="/fechamento", method=RequestMethod.GET)
	public ModelAndView listarFechamento(){
		return criarViewFechamento(new Date());
	}
	@RequestMapping(value="/fechamento/data", method=RequestMethod.POST)
	public ModelAndView listarFechamento(String data){
		try {
			return criarViewFechamento(Utils.dateFormat.parse(data));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	private ModelAndView criarViewFechamento(Date data){
		List<Comanda> comandas = comandaService.findFechamento(data);
		ModelAndView mv = new ModelAndView("fechamento");
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
		return mv;
	}
	
}
