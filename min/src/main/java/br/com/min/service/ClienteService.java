package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.ClienteDAO;
import br.com.min.entity.Cliente;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteDAO dao;
	
	@Transactional
	public void persist(Cliente cliente){
		dao.persist(cliente);
	}
	
	@Transactional
	public List<Cliente> findCliente(Cliente cliente){
		return dao.findCliente(cliente);
	}
	
	public List<Cliente> listAll(){
		return dao.findCliente(null);
	}
	
	public Cliente findById(Long id){
		Cliente cliente = new Cliente();
		cliente.setId(id);
		List<Cliente> result = dao.findCliente(cliente);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Cliente cliente = new Cliente();
		cliente.setId(id);
		dao.delete(cliente);
	}
	
}
