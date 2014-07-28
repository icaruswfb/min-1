package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.ImagemDAO;
import br.com.min.dao.PessoaDAO;
import br.com.min.entity.Imagem;
import br.com.min.entity.Pessoa;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaDAO dao;
	@Autowired
	private GenericDAO genericDao;
	@Autowired
	private ImagemDAO imagemDao;
	
	@Transactional
	public void persist(Pessoa cliente){
		if(cliente.getImagem() != null){
			Imagem imagem = imagemDao.findById(cliente.getImagem().getId());
			cliente.setImagem(imagem);
		}
		genericDao.persist(cliente);
		if(cliente.getImagem() != null){
			imagemDao.deletarNaoUtilizados();
		}
	}
	
	@Transactional
	public List<Pessoa> findPessoa(Pessoa cliente){
		return dao.findPessoa(cliente);
	}
	
	public List<Pessoa> listarClientes(){
		Pessoa pessoa = new Pessoa();
		pessoa.setFuncionario(false);
		return dao.findPessoa(pessoa);
	}
	
	public List<Pessoa> listarFuncionarios(){
		Pessoa pessoa = new Pessoa();
		pessoa.setFuncionario(true);
		return dao.findPessoa(pessoa);
	}
	
	public Pessoa findById(Long id){
		Pessoa cliente = new Pessoa();
		cliente.setId(id);
		List<Pessoa> result = dao.findPessoa(cliente);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Pessoa cliente = new Pessoa();
		cliente.setId(id);
		genericDao.delete(cliente);
	}
	
}
