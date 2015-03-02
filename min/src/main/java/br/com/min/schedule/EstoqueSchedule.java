package br.com.min.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.min.entity.Pessoa;
import br.com.min.entity.Produto;
import br.com.min.entity.Role;
import br.com.min.entity.SituacaoEstoque;
import br.com.min.entity.Tarefa;
import br.com.min.entity.Usuario;
import br.com.min.service.PessoaService;
import br.com.min.service.ProdutoService;
import br.com.min.service.TarefaService;
import br.com.min.service.UsuarioService;

@Component
public class EstoqueSchedule {

	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private TarefaService tarefaService;
	@Autowired
	private PessoaService pessoaService;
	
	public void avisarEstoqueBaixo(){
		avisarEstoque(SituacaoEstoque.ALERTA);
		avisarEstoque(SituacaoEstoque.CRITICA);
	}
	
	private void avisarEstoque(SituacaoEstoque situacao){
		Produto entity = new Produto();
		entity.setSituacaoEstoque(situacao);
		List<Produto> produtos = produtoService.find(entity);
		List<Usuario> admins = usuarioService.findByRole(Role.ADMIN);
		if(produtos.size() >= 3){
			for(Usuario admin : admins){
				Tarefa tarefa = criarTarefa(admin);
				tarefa.setDescricao("Há " + produtos.size() + " produtos cujo " + situacao.getTexto().toLowerCase());
				tarefaService.persist(tarefa);
			}
		}else{
			for(Produto produto : produtos){
				for(Usuario admin : admins){
					Tarefa tarefa = criarTarefa(admin);
					tarefa.setDescricao(produto.getNome() + ": " + situacao.getTexto());
					tarefaService.persist(tarefa);
				}
			}
		}
	}
	
	private Tarefa criarTarefa(Usuario admin){

		Tarefa tarefa = new Tarefa();
		Pessoa criador = pessoaService.findById(1L);
		tarefa.setCriador(criador);
		tarefa.setDataAgendada(new Date());
		tarefa.setFuncionario(admin.getPessoa());
		tarefa.setParaTodos(false);
		
		return tarefa;
	}
	
}
