package br.com.min.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity
public class Pessoa implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7439432709290331921L;

	@Transient
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
	
	private Long id;
	private String nome;
	private String telefone;
	private Date aniversario;
	private String email;
	private String endereco;
	private String cep;
	private String cidade;
	private Funcao funcaoPrincipal;
	private Boolean funcionario;
	private String documento;
	private String cor;
	private Comissao comissao;
	private Imagem imagem;
	private Usuario usuario;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Date getAniversario() {
		return aniversario;
	}
	public void setAniversario(Date aniversario) {
		this.aniversario = aniversario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	@Transient
	public String getAniversarioStr(){
		if(aniversario == null){
			return null;
		}
		return sdf.format(aniversario);
	}
	public void setAniversarioStr(String data){
		try {
			if(data == null || data.isEmpty()){
				return;
			}
			aniversario = sdf.parse(data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	@Enumerated(EnumType.STRING)
	public Funcao getFuncaoPrincipal() {
		return funcaoPrincipal;
	}
	public void setFuncaoPrincipal(Funcao funcaoPrincipal) {
		this.funcaoPrincipal = funcaoPrincipal;
	}
	public Boolean getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	@OneToOne(optional=true, cascade=CascadeType.ALL)
	public Comissao getComissao() {
		return comissao;
	}
	public void setComissao(Comissao comissao) {
		this.comissao = comissao;
	}
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	public Imagem getImagem() {
		return imagem;
	}
	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}
	@OneToOne(mappedBy="pessoa", cascade=CascadeType.ALL, orphanRemoval=true)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
