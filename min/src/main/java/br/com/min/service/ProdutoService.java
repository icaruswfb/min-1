package br.com.min.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.min.dao.GenericDAO;
import br.com.min.dao.ProdutoDAO;
import br.com.min.entity.CategoriaProduto;
import br.com.min.entity.LancamentoEstoque;
import br.com.min.entity.Produto;
import br.com.min.entity.SituacaoEstoque;
import br.com.min.entity.TipoLancamentoEstoque;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoDAO dao;
	@Autowired
	private GenericDAO genericDao;
	
	@Transactional
	public Produto persist(Produto entity){
		return (Produto)genericDao.persist(entity);
	}
	
	@Transactional
	public List<Produto> find(Produto entity){
		return dao.find(entity);
	}
	
	public List<Produto> listar(){
		Produto entity = new Produto();
		return dao.find(entity);
	}
	
	public Produto findById(Long id){
		Produto entity = new Produto();
		entity.setId(id);
		List<Produto> result = dao.find(entity);
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}

	public void delete(Long id) {
		Produto entity = new Produto();
		entity.setId(id);
		dao.delete(entity);
	}
	
	public LancamentoEstoque addEstoque(Long produtoId, Long quantidade, TipoLancamentoEstoque tipo){
		LancamentoEstoque estoque = new LancamentoEstoque();
		estoque.setDataCriacao(new Date());
		if(quantidade < 0){
			tipo = TipoLancamentoEstoque.SAIDA;
		}
		estoque.setTipo(tipo);
		estoque.setQuantidade(quantidade);
		Produto produto = findById(produtoId);
		estoque.setProduto(produto);
		LancamentoEstoque result = (LancamentoEstoque) genericDao.persist(estoque);
		atualizarSituacaoEstoque(produto);
		return result;
	}
	
	public List<LancamentoEstoque> findLancamentosEstoquePorProduto(Long produtoId){
		return dao.findLancamentoEstoquePorProduto(produtoId);
	}

	public void atualizarSituacaoEstoque(Produto produto){
		List<LancamentoEstoque> estoques = dao.findLancamentoEstoquePorProduto(produto.getId());
		this.atualizarSituacaoEstoque(produto, estoques);
	}
	
	public void atualizarSituacaoEstoque(Produto produto, List<LancamentoEstoque> estoques){
		Long quantidade = 0L;
		for(LancamentoEstoque lancamento : estoques){
			if(TipoLancamentoEstoque.ENTRADA.equals(lancamento.getTipo())){
				quantidade += lancamento.getQuantidade();
			}else if(TipoLancamentoEstoque.SAIDA.equals(lancamento.getTipo())){
				quantidade -= lancamento.getQuantidade();
			}
		}
		if(produto.getQuantidadeAlerta() != null){
			if(quantidade > produto.getQuantidadeAlerta()){
				produto.setSituacaoEstoque(SituacaoEstoque.BOA);
			}else if(quantidade <= produto.getQuantidadeAlerta()){
				if(quantidade <= (produto.getQuantidadeAlerta() / 2)){
					produto.setSituacaoEstoque(SituacaoEstoque.CRITICA);
				}else{
					produto.setSituacaoEstoque(SituacaoEstoque.ALERTA);
				}
			}
		}else{
			produto.setSituacaoEstoque(SituacaoEstoque.ALERTA);
		}
		genericDao.persist(produto);
	}
	
	public void atualizarSituacaoEstoque(Long produtoId){
		List<LancamentoEstoque> estoques = dao.findLancamentoEstoquePorProduto(produtoId);
		Produto produto = this.findById(produtoId);
		this.atualizarSituacaoEstoque(produto, estoques);
	}

	public List<Produto> search(String pesquisa,
			CategoriaProduto categoriaProduto) {
		Produto entity = new Produto();
		entity.setCategoria(categoriaProduto);
		entity.setNome(pesquisa);
		return dao.find(entity);
	}
}
