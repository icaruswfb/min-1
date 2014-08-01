package br.com.min.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.ComissaoDAO;
import br.com.min.entity.LancamentoComissao;
import br.com.min.entity.Pessoa;

@Service
public class ComissaoService {

	@Autowired
	private ComissaoDAO dao;
	
	public List<LancamentoComissao> findByPeriodo(Date inicio, Date fim){
		return dao.findByPeriodo(inicio, fim, null);
	}
	
	public List<LancamentoComissao> findByPeriodo(Date inicio, Date fim, Pessoa funcionario){
		return dao.findByPeriodo(inicio, fim, funcionario);
	}
	
	public List<LancamentoComissao> findByMes(Date date){
		return dao.findLancamentosComissaoVendaDoMes(null, date, null);
	}
	
}
