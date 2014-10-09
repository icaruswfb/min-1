package br.com.min.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.HistoricoDAO;
import br.com.min.dao.ImagemDAO;
import br.com.min.dao.PessoaDAO;
import br.com.min.entity.Funcao;
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
	public Pessoa persist(Pessoa pessoa){
		if(pessoa.getImagem() != null){
			Imagem imagem = imagemDao.findById(pessoa.getImagem().getId());
			pessoa.setImagem(imagem);
		}
		return (Pessoa) genericDao.persist(pessoa);
//		if(pessoa.getImagem() != null){
//			imagemDao.deletarNaoUtilizados();
//		}
	}
	
	@Transactional
	public List<Pessoa> findPessoa(Pessoa pessoa, Funcao... funcoesExcluidas){
		return dao.findPessoa(pessoa, funcoesExcluidas);
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
		Pessoa pessoa = new Pessoa();
		pessoa.setId(id);
		List<Pessoa> result = dao.findPessoa(pessoa);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(id);
		dao.delete(pessoa);
	}
	
}
