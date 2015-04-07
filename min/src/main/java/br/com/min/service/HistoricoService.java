package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.HistoricoDAO;
import br.com.min.entity.Historico;
import br.com.min.entity.Pessoa;

@Service
public class HistoricoService extends BaseService<Historico, HistoricoDAO> {

	@Autowired
	private HistoricoDAO historicoDao;
	
	public List<Historico> findByClienteId(Long clienteId){
		Historico historico = new Historico();
		historico.setCliente(new Pessoa());
		historico.getCliente().setId(clienteId);
		return cleanResult(historicoDao.findHistorico(historico));
	}
	
	public List<Historico> findByFuncionarioId(Long funcionarioId){
		Historico historico = new Historico();
		historico.setFuncionario(new Pessoa());
		historico.getFuncionario().setId(funcionarioId);
		return cleanResult(historicoDao.findHistorico(historico));
	}
}
