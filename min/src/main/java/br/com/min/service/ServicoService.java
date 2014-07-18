package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.ServicoDAO;
import br.com.min.entity.Servico;

@Service
public class ServicoService {
	
	@Autowired
	private ServicoDAO dao;
	@Autowired
	private GenericDAO genericDao;
	
	@Transactional
	public void persist(Servico servico){
		genericDao.persist(servico);
	}
	
	@Transactional
	public List<Servico> findServico(Servico servico){
		return dao.findServico(servico);
	}
	
	public List<Servico> listar(){
		Servico servico = new Servico();
		return dao.findServico(servico);
	}
	
	public Servico findById(Long id){
		Servico servico = new Servico();
		servico.setId(id);
		List<Servico> result = dao.findServico(servico);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Servico cliente = new Servico();
		cliente.setId(id);
		genericDao.delete(cliente);
	}
	
}
