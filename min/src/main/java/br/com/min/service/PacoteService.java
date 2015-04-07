package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.min.dao.PacoteDAO;
import br.com.min.entity.Comanda;
import br.com.min.entity.LancamentoServico;
import br.com.min.entity.Pacote;
import br.com.min.entity.Pessoa;

@Service
public class PacoteService extends BaseService<Pacote, PacoteDAO>{

	@Autowired
	private ComandaService comandaService;
	
	public List<Pacote> findPacotesAtivos(Long clienteId){
		return getDAO().findByClienteId(clienteId);
	}

	public Pacote descontarDoPacote(Long id, Long funcionarioId, Long assistenteId, Pessoa criador) {
		Pacote pacote = getDAO().findById(id);
		Comanda comanda = comandaService.abrirComanda(pacote.getCliente().getId(), criador);
		LancamentoServico lancamentoServico = comandaService.criarLancamentoServico(pacote.getServico().getId(), funcionarioId, assistenteId, pacote.getCliente().getId());
		lancamentoServico.setPacote(pacote);
		comanda = comandaService.addServico(lancamentoServico, comanda, criador);
		Long quantidadeAtual = getDAO().countQuantidade(id);
		pacote.setQuantidade(quantidadeAtual);
		getDAO().persist(pacote);
		return pacote;
	}
	
}
