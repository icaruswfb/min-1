package br.com.min.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.PagamentoDAO;
import br.com.min.entity.FluxoPagamento;
import br.com.min.entity.Pagamento;

@Service
public class PagamentoService {

	@Autowired
	private PagamentoDAO dao;
	@Autowired
	private GenericDAO genericDao;
	
	public Pagamento persist(Pagamento pagamento){
		pagamento.setData(new Date());
		return (Pagamento) genericDao.persist(pagamento);
	}
	
	public List<Pagamento> find(Date inicio, Date fim){
		return dao.findPagamentos(inicio, fim, null);
	}

	public List<Pagamento> find(Date inicio, Date fim, FluxoPagamento fluxo){
		return dao.findPagamentos(inicio, fim, fluxo);
	}
}
